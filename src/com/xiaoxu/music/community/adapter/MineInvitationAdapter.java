package com.xiaoxu.music.community.adapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.activity.CircleBlogDetailActivity;
import com.xiaoxu.music.community.activity.MusicUserDetailActivity;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.BlogAuthorEntity;
import com.xiaoxu.music.community.entity.BlogsInfoEntity;
import com.xiaoxu.music.community.entity.ImageEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.CircleBlogsTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.StringUtil;
import com.xiaoxu.music.community.util.TimeUtil;
import com.xiaoxu.music.community.view.custom_shape_imageview.CircleImageView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MineInvitationAdapter extends BaseAdapter {

	private Intent intent;
	private Activity activity; 
	private BitmapUtils bitmapUtils;
	private List<BlogsInfoEntity> list;

	private void setBitmapUtils() {
		bitmapUtils = new BitmapUtils(activity, Constant.DISK_CACHE_PATH,Constant.MAX_MEMORY_SIZE);
		bitmapUtils.configDefaultBitmapConfig(Config.RGB_565);//设置图片压缩类型
		bitmapUtils.configDefaultLoadingImage(R.drawable.test_default_wait_img);// 默认背景图片
		bitmapUtils.configDefaultLoadFailedImage(R.drawable.test_default_wait_img);// 加载失败图片
	}
	public MineInvitationAdapter(Activity activity, List<BlogsInfoEntity> list) {
		super();
		this.activity = activity;
		this.list = list;
		setBitmapUtils();
	}
	
	
	public List<BlogsInfoEntity> getList() {
		return list;
	}

	
	public void setList(List<BlogsInfoEntity> list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list != null ?list.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return (BlogsInfoEntity) list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		ViewHolder invitationHolder = null;
		
		if (convertView == null) {
			
			invitationHolder = new ViewHolder();
			convertView = LayoutInflater.from(activity).inflate(R.layout.adapter_invitation_item, null);
			invitationHolder.image_author = (CircleImageView) convertView.findViewById(R.id.post_author_head);
			invitationHolder.tv_author_name = (TextView) convertView.findViewById(R.id.post_author_name);
			invitationHolder.image_sex = (ImageView) convertView.findViewById(R.id.post_author_sex);
			invitationHolder.tv_release_time = (TextView) convertView.findViewById(R.id.post_release_time);

			invitationHolder.tv_post_body = (TextView) convertView.findViewById(R.id.post_body);

			invitationHolder.layout_image = (LinearLayout) convertView.findViewById(R.id.layout_content_images);
			invitationHolder.image_post1 = (ImageView) convertView.findViewById(R.id.post_image1);
			invitationHolder.image_post2 = (ImageView) convertView.findViewById(R.id.post_image2);
			invitationHolder.image_post3 = (ImageView) convertView.findViewById(R.id.post_image3);

			invitationHolder.btn_praise = (ImageButton) convertView.findViewById(R.id.imagebutton_post_praise);
			invitationHolder.btn_commend = (ImageButton) convertView.findViewById(R.id.imagebutton_post_commend);
			invitationHolder.tv_praise_num = (TextView) convertView.findViewById(R.id.tv_post_praise_num);
			invitationHolder.tv_commend_num = (TextView) convertView.findViewById(R.id.tv_post_commend_num);
			invitationHolder.del_invitation = (ImageView) convertView.findViewById(R.id.del_invitation);
			
			invitationHolder.layout_item = (RelativeLayout) convertView.findViewById(R.id.layout_all);
			invitationHolder.user_item = (RelativeLayout) convertView.findViewById(R.id.layout_content_top);

			convertView.setTag(invitationHolder);

		} else {
			invitationHolder = (ViewHolder) convertView.getTag();
		}

		BlogAuthorEntity user = list.get(position).getAuthor_info();
		String user_img  = StringUtil.replaceImagePath(user.getUser_img(), 100);
		bitmapUtils.display(invitationHolder.image_author, user_img);
		invitationHolder.tv_author_name.setText(user.getUser_nick());
		int sex = Integer.parseInt(user.getUser_sex());
		switch (sex) {
		case 0:
			invitationHolder.image_sex.setImageResource(R.drawable.sex_martian);
			break;
		case 1:
			invitationHolder.image_sex.setImageResource(R.drawable.sex_man);
			break;
		case 2:
			invitationHolder.image_sex.setImageResource(R.drawable.sex_woman);
			break;
		default:
			break;
		}
		String release_time = TimeUtil.getStandardDate(list.get(position).getTime_create());
		invitationHolder.tv_release_time.setText(release_time);
		invitationHolder.tv_post_body.setText(list.get(position).getBlog_message());
		List<ImageEntity> list_images = list.get(position).getImg_attachment();
		int image_size = list_images != null ? list_images.size() : 0;
		image_size = image_size >= 3 ? 3 : image_size;
		switch (image_size) {
		case 0:
			invitationHolder.layout_image.setVisibility(View.GONE);
			break;
		case 1:
			invitationHolder.layout_image.setVisibility(View.VISIBLE);
			String attachment = list_images.get(0).getAttachment();
			bitmapUtils.display(invitationHolder.image_post1,
					StringUtil.replaceImagePath(attachment, 300));
			invitationHolder.image_post2.setImageDrawable(null);
			invitationHolder.image_post3.setImageDrawable(null);
			break;
		case 2:
			invitationHolder.layout_image.setVisibility(View.VISIBLE);
			bitmapUtils.display(invitationHolder.image_post1, StringUtil.replaceImagePath(list_images.get(0).getAttachment(), 300));
			bitmapUtils.display(invitationHolder.image_post2, StringUtil.replaceImagePath(list_images.get(1).getAttachment(), 300));
			invitationHolder.image_post3.setImageDrawable(null);
			break;
		case 3:
			invitationHolder.layout_image.setVisibility(View.VISIBLE);
			bitmapUtils.display(invitationHolder.image_post1, StringUtil.replaceImagePath(list_images.get(0).getAttachment(), 300));
			bitmapUtils.display(invitationHolder.image_post2, StringUtil.replaceImagePath(list_images.get(1).getAttachment(), 300));
			bitmapUtils.display(invitationHolder.image_post3, StringUtil.replaceImagePath(list_images.get(2).getAttachment(), 300));
			break;
		}
		
		invitationHolder.tv_praise_num.setText(list.get(position).getHeats());
		invitationHolder.tv_commend_num.setText(list.get(position).getReplies());
		invitationHolder.btn_praise.setBackgroundResource(R.drawable.btn_praise_normal);
//		if (Integer.parseInt(list.get(position).getPraise()) == 1) {
//			invitationHolder.btn_praise.setBackgroundResource(R.drawable.btn_praise_pressed);
//		}

		invitationHolder.btn_praise.setOnClickListener(new ClickListener(2, position));
		invitationHolder.layout_item.setOnClickListener(new ClickListener(2, position));
		invitationHolder.btn_commend.setOnClickListener(new ClickListener(2, position));
		invitationHolder.del_invitation.setOnClickListener(new ClickListener(3, position));
		invitationHolder.user_item.setOnClickListener(new ClickListener(4, position));
		
		return convertView;
	}

	class ViewHolder {
		LinearLayout layout_image;
		CircleImageView image_author;
		ImageView image_sex, del_invitation;
		ImageButton btn_praise, btn_commend;
		TextView tv_author_name, tv_release_time, tv_post_body, tv_praise_num,tv_commend_num;
		ImageView image_post1, image_post2, image_post3;
		RelativeLayout layout_item,user_item;
	}

	public class ClickListener implements OnClickListener {
		
		private int type;
		private int position;
		
		public ClickListener(int type, int position) {
			super();
			this.type = type;
			this.position = position;
		}

		@Override
		public void onClick(View v) {
			switch (type) {
			case 2:
				
				intent = new Intent(activity, CircleBlogDetailActivity.class);
				intent.putExtra("position", position);
				intent.putExtra("BlogsInfoEntity", list.get(position));
				activity.startActivityForResult(intent, 0);
				
				break;
			case 3:
				
				showdialog(position);
				
				break;
			case 4:
				
				intent = new Intent(activity, MusicUserDetailActivity.class);
				intent.putExtra("user_id", list.get(position).getUser_id());
				activity.startActivity(intent);
				
				break;

			}
			
		}
	}

	
	View view;
	Dialog dialog;
	TextView msg;
	TextView title;
	Button btn_ok;
	Button btn_cancel;
	public void showdialog(int position){
		view = activity.getLayoutInflater().inflate(R.layout.del_invitation_dialog, null);
		title = (TextView) view.findViewById(R.id.title);
		title.setText("删除帖子");
		msg = (TextView) view.findViewById(R.id.msg);
		msg.setText("亲！你确定删除这条帖子？");
		btn_ok = (Button) view.findViewById(R.id.ok);
		btn_cancel =  (Button) view.findViewById(R.id.cancel);
		btn_ok.setOnClickListener(new dialogclick(position));
		btn_cancel.setOnClickListener(new dialogclick(position));
		dialog = new Dialog(activity, R.style.Custom_Dialog);
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);// 按返回键是否取消
		// 设置居中
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (int) (ActivityUtil.getScreenWidth(activity) * 0.8);
		lp.dimAmount = 0.5f;// 设置背景层透明度
		dialog.show();
	}
	
	 class dialogclick implements OnClickListener{
		 
		 private int position;
		 
		 public dialogclick(int position){
			 this.position = position;
		 }
		 @Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.ok:
						new CircleBlogsTask(activity, list.get(position).getTid(), callback).excute();
						list.remove(position);
						notifyDataSetChanged();
						dialog.dismiss();
						break;
					case R.id.cancel: 
						dialog.dismiss(); 
						break;
				}
			}
		 
	 }
	
	private BaseRequestCallBack callback = new BaseRequestCallBack() {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			
		}

		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			
			if(code != Constant.SUCCESS_REQUEST){
				ActivityUtil.showShortToast(activity, alert);
				return;
			}
			ActivityUtil.showAttentionToast(activity, "删除成功").show();
			
		}
		
	};
	
	
}
