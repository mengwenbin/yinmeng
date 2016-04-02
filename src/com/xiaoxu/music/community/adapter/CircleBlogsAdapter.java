package com.xiaoxu.music.community.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.activity.CircleBlogDetailActivity;
import com.xiaoxu.music.community.activity.CircleBlogsActivity;
import com.xiaoxu.music.community.activity.MusicUserDetailActivity;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.BlogAuthorEntity;
import com.xiaoxu.music.community.entity.BlogsInfoEntity;
import com.xiaoxu.music.community.entity.CategorysInfoEntity;
import com.xiaoxu.music.community.entity.ImageEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.StringUtil;
import com.xiaoxu.music.community.util.TimeUtil;
import com.xiaoxu.music.community.view.custom_shape_imageview.CircleImageView;

import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CircleBlogsAdapter extends BaseAdapter{
	
	public static final int VIEWHOLDER_CATEGORY = 0;
	public static final int VIEWHOLDER_POSTS = 1;
	
	private CircleBlogsActivity Activity;
	private CategorysInfoEntity category;
	private List<BlogsInfoEntity> list;
	private BitmapUtils bitUtil,bitUtils2;
	private int top_count = 0;
	
	public CircleBlogsAdapter(CircleBlogsActivity Activity,CategorysInfoEntity category) {
		super();
		this.Activity = Activity;
		this.category = category;
		
		this.bitUtil = new BitmapUtils(Activity, Constant.DISK_CACHE_PATH,Constant.MAX_MEMORY_SIZE);
		this.bitUtil.configDefaultBitmapConfig(Config.RGB_565);//设置图片压缩类型
		this.bitUtil.configDefaultLoadingImage(R.drawable.test_default_head_bg_blue);// 默认背景图片
		this.bitUtil.configDefaultLoadFailedImage(R.drawable.test_default_head_bg_blue);// 加载失败图片
		
		this.bitUtils2 = new BitmapUtils(Activity, Constant.DISK_CACHE_PATH,Constant.MAX_MEMORY_SIZE);
		this.bitUtils2.configDefaultBitmapConfig(Config.RGB_565);//设置图片压缩类型
		this.bitUtils2.configDefaultLoadingImage(R.drawable.test_default_wait_img);// 默认背景图片
		this.bitUtils2.configDefaultLoadFailedImage(R.drawable.test_default_wait_img);// 加载失败图片
	}
	
	public int getTop_count() {
		return top_count;
	}

	public void setTop_count(int top_count) {
		this.top_count = top_count;
	}



	public CategorysInfoEntity getCategory() {
		return category;
	}

	public void setCategory(CategorysInfoEntity category) {
		this.category = category;
	}

	public List<BlogsInfoEntity> getList() {
		return list;
	}

	public void setList(List<BlogsInfoEntity> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list != null ? list.size() : 1;
	}
	
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		if (position == 0) {
			return VIEWHOLDER_CATEGORY;
		} else {
			return VIEWHOLDER_POSTS;
		}
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolderCategory holderCategory = null;
		ViewHolderPost holderPost = null;
		int type = getItemViewType(position);
		if (convertView == null) {
			switch (type) {
			case VIEWHOLDER_CATEGORY:
				convertView = LayoutInflater.from(Activity).inflate(R.layout.adapter_circle_blogs_category, null);
				holderCategory = new ViewHolderCategory(convertView);
				convertView.setTag(holderCategory);
				break;
			case VIEWHOLDER_POSTS:
				convertView = LayoutInflater.from(Activity).inflate(R.layout.adapter_circle_blogs_item, null);
				holderPost = new ViewHolderPost(convertView);
				convertView.setTag(holderPost);
				break;
			default: break;
			}
		} else {
			switch (type) {
				case VIEWHOLDER_CATEGORY: holderCategory = (ViewHolderCategory) convertView.getTag(); break;
				case VIEWHOLDER_POSTS: holderPost = (ViewHolderPost) convertView.getTag(); break;
				default: break;
			}
		}
		switch (type) {
		case VIEWHOLDER_CATEGORY:// 头部类别
			bitUtils2.display(holderCategory.image,StringUtil.replaceImagePath(category.getCategory_img2(),100));
			holderCategory.tv_tag.setText(category.getCategory_name()+"");
			holderCategory.tv_body.setText("简介："+ category.getCategory_summary());
			break;
		case VIEWHOLDER_POSTS:// 帖子列表
			BlogAuthorEntity user = list.get(position).getAuthor_info();
			bitUtil.display(holderPost.image_author,StringUtil.replaceImagePath(user.getUser_img(),100));
			holderPost.tv_author_name.setText(user.getUser_nick());
			int sex = Integer.parseInt(user.getUser_sex());
			switch (sex) {
				case 0: holderPost.image_sex.setImageResource(R.drawable.sex_martian); break;
				case 1: holderPost.image_sex.setImageResource(R.drawable.sex_man); break;
				case 2: holderPost.image_sex.setImageResource(R.drawable.sex_woman); break;
				default: break;
			}
			String str_digest = list.get(position).getDigest();
			int digest = Integer.parseInt(str_digest!=null?str_digest:"0");
			holderPost.image_digest.setVisibility(View.GONE);
			if(digest>0){
				holderPost.image_digest.setVisibility(View.VISIBLE);
			}
			holderPost.image_top.setVisibility(View.GONE);
			if(position<=top_count){
				holderPost.image_top.setVisibility(View.VISIBLE);
			}
			
			String release_time = TimeUtil.getStandardDate(list.get(position).getTime_create());
			holderPost.tv_release_time.setText(release_time);
			holderPost.tv_post_body.setText(list.get(position).getBlog_message());
			
			List<ImageEntity> list_images = list.get(position).getImg_attachment();
			int image_size = list_images != null ? list_images.size() : 0;
			
			holderPost.image_count.setVisibility(View.INVISIBLE);
			if(image_size>3){
				holderPost.image_count.setVisibility(View.VISIBLE);
				holderPost.image_count.setText(image_size+"P");
			}
			
			holderPost.layout_image.setVisibility(View.VISIBLE);
			holderPost.layout_image2.setVisibility(View.GONE);
			holderPost.layout_image3.setVisibility(View.GONE);
			holderPost.image_post.setVisibility(View.GONE);
			image_size = image_size >= 3 ? 3 : image_size;
			switch (image_size) {
			case 0:
				holderPost.layout_image.setVisibility(View.GONE);
				break;
			case 1:
				holderPost.image_post.setVisibility(View.VISIBLE);
				bitUtils2.display(holderPost.image_post,StringUtil.replaceImagePath(list_images.get(0).getAttachment(),600));
				break;
			case 2:
				holderPost.layout_image2.setVisibility(View.VISIBLE);
				bitUtils2.display(holderPost.image_post_1,StringUtil.replaceImagePath(list_images.get(0).getAttachment(),300));
				bitUtils2.display(holderPost.image_post_2,StringUtil.replaceImagePath(list_images.get(1).getAttachment(),300));
				break;
			case 3:
				holderPost.layout_image3.setVisibility(View.VISIBLE);
				bitUtils2.display(holderPost.image_post1,StringUtil.replaceImagePath(list_images.get(0).getAttachment(), 300));
				bitUtils2.display(holderPost.image_post2,StringUtil.replaceImagePath(list_images.get(1).getAttachment(), 300));
				bitUtils2.display(holderPost.image_post3,StringUtil.replaceImagePath(list_images.get(2).getAttachment(), 300));
				break;
			default: break;
			}
			holderPost.tv_praise_num.setText(""+ list.get(position).getHeats() + "");
			holderPost.tv_commend_num.setText(""+ list.get(position).getReplies() + "");
			
			holderPost.tv_post_body.setOnClickListener(new Click(position,2));
			holderPost.btn_praise.setOnClickListener(new Click(position,holderPost.tv_praise_num, holderPost.btn_praise, 1));
			holderPost.tv_release_time.setOnClickListener(new Click(position,2));
			holderPost.btn_commend.setOnClickListener(new Click(position,2));
			holderPost.layout_image.setOnClickListener(new Click(position,2));
			
			holderPost.image_author.setOnClickListener(new Click(position, 3));
			holderPost.tv_author_name.setOnClickListener(new Click(position, 3));
			holderPost.image_sex.setOnClickListener(new Click(position, 3));
			
			break;
		default:
			break;
		}
		return convertView;
	}
	
	private class ViewHolderCategory {
		ImageView image;
		TextView tv_tag, tv_body;
		public ViewHolderCategory(View convertView) {
			super();
			image = (ImageView) convertView.findViewById(R.id.circle_post_image);
			tv_tag = (TextView) convertView.findViewById(R.id.circle_post_category_name);
			tv_body = (TextView) convertView.findViewById(R.id.circle_post_category_body);
		}
	}

	private class ViewHolderPost {
		CircleImageView image_author;
		TextView tv_author_name, tv_release_time, tv_post_body, tv_praise_num, tv_commend_num, image_count;
		ImageView image_sex, image_digest,image_top;
		ImageButton btn_praise, btn_commend;
		//image
		FrameLayout layout_image;
		LinearLayout layout_image2, layout_image3;
		ImageView image_post;
		ImageView image_post_1, image_post_2;
		ImageView image_post1, image_post2, image_post3;
		public ViewHolderPost(View convertView) {
			super();
			image_author = (CircleImageView) convertView.findViewById(R.id.post_author_head);
			tv_author_name = (TextView) convertView.findViewById(R.id.post_author_name);
			image_sex = (ImageView) convertView.findViewById(R.id.post_author_sex);
			image_digest = (ImageView) convertView.findViewById(R.id.image_digest);
			image_top = (ImageView) convertView.findViewById(R.id.image_top);
			tv_release_time = (TextView) convertView.findViewById(R.id.post_release_time);
			tv_post_body = (TextView) convertView.findViewById(R.id.post_body);
			btn_praise = (ImageButton) convertView.findViewById(R.id.imagebutton_post_praise);
			btn_commend = (ImageButton) convertView.findViewById(R.id.imagebutton_post_commend);
			tv_praise_num = (TextView) convertView.findViewById(R.id.tv_post_praise_num);
			tv_commend_num = (TextView) convertView.findViewById(R.id.tv_post_commend_num);
			image_count = (TextView) convertView.findViewById(R.id.image_count);
			
			//无图和一张图片
			layout_image = (FrameLayout) convertView.findViewById(R.id.layout_content_images);
			image_post = (ImageView) convertView.findViewById(R.id.post_image);
			//两张图片
			layout_image2 = (LinearLayout) convertView.findViewById(R.id.layout_images_2);
			image_post_1 = (ImageView) convertView.findViewById(R.id.post_image_1);
			image_post_2 = (ImageView) convertView.findViewById(R.id.post_image_2);
			//三张图片
			layout_image3 = (LinearLayout) convertView.findViewById(R.id.layout_images_3);
			image_post1 = (ImageView) convertView.findViewById(R.id.post_image1);
			image_post2 = (ImageView) convertView.findViewById(R.id.post_image2);
			image_post3 = (ImageView) convertView.findViewById(R.id.post_image3);
		}
	}
	
	public class Click implements OnClickListener {
		private int position;
		private TextView tv_num;
		private ImageButton btn;
		private int type;// 1.赞2.评论
		
		public Click(int position, int type){
			this.position = position;
			this.type = type;
		}
		
		public Click(int position, TextView tv_num, ImageButton btn, int type) {
			super();
			this.position = position;
			this.tv_num = tv_num;
			this.btn = btn;
			this.type = type;
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(ActivityUtil.isFastDoubleClick(v.getId()))
				return;
			if (type == 1) { // 1.赞
				Intent in = new Intent(Activity, CircleBlogDetailActivity.class);
				in.putExtra("BlogsInfoEntity", list.get(position));
				in.putExtra("position", position);
				Activity.startActivityForResult(in, 0x1);
			} else if(type == 2){ // 2.评论
				Intent in = new Intent(Activity, CircleBlogDetailActivity.class);
				in.putExtra("BlogsInfoEntity", list.get(position));
				in.putExtra("position", position);
				Activity.startActivityForResult(in, 0x1);
			} else if(type == 3){
				Intent intent = new Intent(Activity, MusicUserDetailActivity.class);
				intent.putExtra("user_id", list.get(position).getUser_id());
				Activity.startActivity(intent);
			}
		}
	}

	public class PraiseCallBack extends BaseRequestCallBack {
		private int position;
		private TextView tv_num;
		private ImageButton btn;
		public PraiseCallBack(int position, TextView tv_num, ImageButton btn) {
			super();
			this.position = position;
			this.tv_num = tv_num;
			this.btn = btn;
		}
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			// TODO Auto-generated method stub
			if (code!=Constant.SUCCESS_REQUEST) {
				ActivityUtil.showShortToast(Activity, alert);
				return;
			}
			int num = Integer.parseInt(list.get(position).getHeats())+1;
			list.get(position).setHeats(String.valueOf(num));
			list.get(position).setPraise(String.valueOf(1));
			tv_num.setText(num+"");
			btn.setBackgroundResource(R.drawable.btn_praise_pressed);
		}
		@Override
		public void onFailure(HttpException arg0, String arg1) {
		}
		
	}

}
