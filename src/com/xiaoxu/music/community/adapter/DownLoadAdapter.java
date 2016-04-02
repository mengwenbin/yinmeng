package com.xiaoxu.music.community.adapter;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.exception.HttpException;
import com.xiaoxu.music.community.BaseNewAdapter;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.YmApplication;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.dao.DownLoadDB;
import com.xiaoxu.music.community.entity.SimpleUserEntity;
import com.xiaoxu.music.community.entity.SongInfoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.CollectMusicTask;
import com.xiaoxu.music.community.util.AccountInfoUtils;
import com.xiaoxu.music.community.util.ActivityUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DownLoadAdapter extends BaseNewAdapter {

	private YmApplication app;
	private List<SongInfoEntity> list;
	private int state = 0;// 是否显示checkbox
	public int current = -1;
	private DownLoadDB downLoaddb;

	public DownLoadAdapter(Context context, Activity activity,
			YmApplication app, List<SongInfoEntity> list, DownLoadDB downLoaddb) {
		super(context, activity);
		this.app = app;
		this.list = list;
		this.downLoaddb = downLoaddb;
	}

	public int isState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public List<SongInfoEntity> getList() {
		return list;
	}

	public void setList(List<SongInfoEntity> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list != null ? list.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.adapter_minedownload_item, null);
			viewHolder.music_name = (TextView) convertView.findViewById(R.id.music_name);
			viewHolder.music_author = (TextView) convertView .findViewById(R.id.music_author);
			viewHolder.btn_more = (ImageView) convertView .findViewById(R.id.image_more);
			viewHolder.check = (CheckBox) convertView.findViewById(R.id.check_box);
			viewHolder.play_view = convertView.findViewById(R.id.view);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.music_name.setText(list.get(position).getMusic_name());
		SimpleUserEntity entity = list.get(position).getUser();
		if (entity != null) {
			String author_name = entity.getUser_nick();
			if (!author_name.equals("")) {
				viewHolder.music_author.setText(author_name);
			} else {
				viewHolder.music_author.setText("<unknown>");
			}
		} else {
			viewHolder.music_author.setText("<unknown>");
		}

		// checkbox
		if (state == 1) {
			viewHolder.check.setVisibility(View.VISIBLE);
			viewHolder.check.setTag(position);
			viewHolder.check.setChecked(list.get(position).isChecked());
			viewHolder.check.setOnCheckedChangeListener(check_listener);
		} else if (state == 0) {
			viewHolder.check.setVisibility(View.GONE);
		}
		viewHolder.music_name.setTextColor(context.getResources().getColor(R.color.music_list_song_name));
		viewHolder.music_author.setTextColor(context.getResources().getColor(R.color.music_list_song_author));
		viewHolder.play_view.setBackgroundColor(context.getResources().getColor(R.color.white));
		if (position == current) {
			viewHolder.play_view.setBackgroundColor(context.getResources().getColor(R.color.music_list_song_name_pressed));
			viewHolder.music_name.setTextColor(context.getResources().getColor(R.color.music_list_song_name_pressed));
			viewHolder.music_author.setTextColor(context.getResources().getColor(R.color.music_list_song_author_pressed));
		}
		viewHolder.btn_more.setOnClickListener(new ClickListener(1,position));
		convertView.setOnClickListener(new ClickListener(2,position));
		
		return convertView;
	}
	
	class ViewHolder {

		TextView music_name, music_author, musci_sum;
		RelativeLayout play_all;
		RelativeLayout play_one;
		ImageView btn_more;
		CheckBox check;
		View play_view;
	}

	OnCheckedChangeListener check_listener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			int position = (Integer) buttonView.getTag();
			if (position > list.size() - 1) {
				return;
			}
			list.get(position).setChecked(isChecked);
			notifyDataSetChanged();
		}
	};

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
			case 1:
				if (AccountInfoUtils.getInstance(context).isLogin()) {
					showMenu(position);
				} else {
					ActivityUtil.showShortToast(context,context.getString(R.string.please_login));
				}
				break;

			case 2:
				current = position;
				notifyDataSetInvalidated();
				app.addSingleMusic(list.get(current),list.get(position).getMusic_id());
				app.playStart(list.get(current));
				break;
			}
		}
	}

	class CallBack extends BaseRequestCallBack {

		private int position;
		private int type = 1;

		public CallBack(int position, int type) {
			super();
			this.position = position;
			this.type = type;
		}

		@Override
		public void onResult(String result, int code, String alert, Object obj) {

			if(code != Constant.SUCCESS_REQUEST){
				ActivityUtil.showShortToast(context, alert);
				return;
			}
			ActivityUtil.showCollectToast(context,alert);
			if (type == 1) {// 收藏歌曲
				btn_collect.setImageResource(R.drawable.btn_collect_pressed);
				SongInfoEntity infoEntity = list.get(position);
				infoEntity.setFavorite("1");
				downLoaddb.updateFavorite(infoEntity);
			}
			dialog.dismiss();
		}

		@Override
		public void onFailure(HttpException arg0, String arg1) {

		}
	}

	Dialog dialog;
	View menuView;
	TextView tv_name;
	ImageButton btn_collect, btn_share;

	public void showMenu(int position) {
		menuView = LayoutInflater.from(context).inflate(R.layout.dialog_popup_window_music_bottom, null);
		tv_name = (TextView) menuView.findViewById(R.id.music_name);
		btn_collect = (ImageButton) menuView.findViewById(R.id.btn_collect);
		btn_share = (ImageButton) menuView.findViewById(R.id.btn_share);
		btn_collect.setOnClickListener(new MenuClickListener());
		btn_share.setOnClickListener(new MenuClickListener());

		tv_name.setText(list.get(position).getMusic_name());
		if (list.get(position).getFavorite().equals("1")) {
			btn_collect.setImageResource(R.drawable.btn_collect_pressed);
			btn_collect.setTag(position);
		} else {
			btn_collect.setImageResource(R.drawable.btn_collect_normal);
			btn_collect.setTag(position);
		}
		if (Integer.parseInt(list.get(position).getFavorite()) == 1) {
			btn_collect.setImageResource(R.drawable.btn_collect_pressed);
		}
		btn_share.setTag(position);
		// dialog
		dialog = new Dialog(context, R.style.Custom_Progress);
		dialog.setContentView(menuView);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);// 按返回键是否取消
		// 设置居中
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM);
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = ActivityUtil.getScreenWidth(context);
		lp.dimAmount = 0.5f;// 设置背景层透明度
		dialog.show();
	}
	
	class MenuClickListener implements OnClickListener{
		@Override
		public void onClick(View v) {
			int position = (Integer) v.getTag();
			switch (v.getId()) {
			case R.id.btn_collect:
				if (haveNetwork()) {
					CollectMusicTask task = new CollectMusicTask(context, 
					list.get(position).getMusic_id(), new CallBack(position, 1));
					task.excute();
				} else {
					ActivityUtil.showShortToast(context, "请连接网络");
				}

				break;
			case R.id.btn_share:
				dialog.dismiss();
				shareMusic(list.get(position));
				break;
			}

		}
	};
	
	// 取消全选
		public void setCheckNO() {
			for (SongInfoEntity entity : list) {
				entity.setChecked(false);
			}
			notifyDataSetChanged();
		}

		// 全选
		public void setCheckAll() {
			for (SongInfoEntity entity : list) {
				entity.setChecked(true);
			}
			notifyDataSetChanged();
		}

		// 得到要删除音乐
		public List<SongInfoEntity> getDeleteIDs() {
			List<SongInfoEntity> deleteList = new ArrayList<SongInfoEntity>();
			for (SongInfoEntity entity : list) {
				if (entity.isChecked()) {
					deleteList.add(entity);
				}
			}
			return deleteList;
		}
}
