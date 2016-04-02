package com.xiaoxu.music.community.view;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.exception.HttpException;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.adapter.AddSongMenuAdapter;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.SongMenuInfoEntity;
import com.xiaoxu.music.community.entity.SongMenuListEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.MineSongTask;
import com.xiaoxu.music.community.model.task.NewSongMenuTask;
import com.xiaoxu.music.community.model.task.SongMenuAddSongTask;
import com.xiaoxu.music.community.parser.ParseUtil;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.NetUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;

public class AddSongMenuPop extends PopupWindow implements OnItemClickListener{

	private Activity activity;
	private View menuView;
	private int pageNum = 1;
	private MineSongTask task;
	private ListView listView;
	private AddSongMenuAdapter adapter;
	private List<SongMenuInfoEntity> list;
	private String music_id;
	private String musiclist_id;
	
	public AddSongMenuPop(Activity context,String music_id,String musiclist_id) {
		super(context);
		
		this.activity = context;
		this.music_id = music_id;
		this.musiclist_id = musiclist_id;
		list = new ArrayList<SongMenuInfoEntity>();
		list.add(0, new SongMenuInfoEntity("-1", "新建歌单"));
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		menuView = inflater.inflate(R.layout.dialog_songmenu, null);
		listView = (ListView) menuView.findViewById(R.id.list_view);
		adapter = new AddSongMenuAdapter(context, list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		task();
		this.setContentView(menuView);
		this.setWidth(LayoutParams.FILL_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setAnimationStyle(R.style.AnimBottom);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		this.setBackgroundDrawable(dw);
		backgroundAlpha(0.5f);
	    this.setOnDismissListener(new poponDismissListener());  //添加pop窗口关闭事件  
		menuView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				int height = menuView.findViewById(R.id.pop_layout).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}
				return true;
			}
		});
		
	}
	
	/** 
     * 设置添加屏幕的背景透明度 
     * @param bgAlpha 
     */  
    public void backgroundAlpha(float bgAlpha) {  
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();  
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);  
    }
    
    /** 
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来 
     */  
    class poponDismissListener implements PopupWindow.OnDismissListener{  
        @Override  
        public void onDismiss() {  
            // TODO Auto-generated method stub  
            backgroundAlpha(1f);  
        }  
    } 
	
	public void task() {
		if (haveNetwork()) {
			CustomProgressDialog.show(activity, true, null).setCanceledOnTouchOutside(false);
			task = new MineSongTask(activity, pageNum,callback);
			task.excute();
		} else {
			if(adapter != null){
				adapter.setList(null);
				adapter.notifyDataSetChanged();
			}
		}
	}
	
	public boolean haveNetwork(){
		if (NetUtils.getAPNType(activity) == -1) { //判断网络状态
			return false;
		}else{
			return true;
		}
	}
	
	BaseRequestCallBack callback = new BaseRequestCallBack() {
		
		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			CustomProgressDialog.disMiss();
			if (code != Constant.SUCCESS_REQUEST) {
				ActivityUtil.showShortToast(activity, alert);
				return;
			}
			if(code!=Constant.SUCCESS_REQUEST)
				return;
			obj = ParseUtil.parseResultObj(result, SongMenuListEntity.class);
			SongMenuListEntity entity = (SongMenuListEntity) obj;
			if(Integer.valueOf(entity.getList_size()) > 0 && entity.getList_musiclist() != null ){
				list = entity.getList_musiclist();
				list.add(0, new SongMenuInfoEntity("-1", "新建歌单"));
				adapter.setList(list); 
				adapter.notifyDataSetChanged();
			}
			
		}

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			CustomProgressDialog.disMiss();
		}
	};

	View view;
	EditText edit_name;
	Button btn_ok,btn_cancel;
	Dialog dialog;
	public void showAddMenu(){
		view = activity.getLayoutInflater().inflate(R.layout.dialog_new_song_menu, null);
		edit_name = (EditText) view.findViewById(R.id.edit_name);
		btn_ok = (Button) view.findViewById(R.id.ok);
		btn_cancel =  (Button) view.findViewById(R.id.cancel);
		btn_ok.setOnClickListener(dialogClick);
		btn_cancel.setOnClickListener(dialogClick);
		dialog = new Dialog(activity, R.style.Custom_Dialog);
		dialog = new Dialog(activity, R.style.Custom_Dialog);
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(true);
		dialog.setCancelable(true);// 按返回键是否取消
		// 设置居中
		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.CENTER);
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.dimAmount = 0.5f;// 设置背景层透明度
		dialog.show();
	}
	
	/*
	 * 创建歌单dialog监听
	 */
	OnClickListener dialogClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.ok:
					String name = edit_name.getText().toString();
					if(!TextUtils.isEmpty(name))
						newSongMenu(name);
					dialog.dismiss();
					break;
				case R.id.cancel: 
					dialog.dismiss(); 
					break;
			}
		}
	};
	
	/*
	 * 新建歌单
	 */
		public void newSongMenu(String name){
			
			NewSongMenuTask task = new NewSongMenuTask(activity, name, new BaseRequestCallBack(SongMenuListEntity.class) {
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					CustomProgressDialog.disMiss();
					}
				@Override
				public void onResult(String result, int code, String alert, Object obj) {
					CustomProgressDialog.disMiss();
					if(code!=Constant.SUCCESS_REQUEST)
						return;
					SongMenuListEntity entity = (SongMenuListEntity) obj;
					if(Integer.parseInt(entity.getList_size())>0){
						list = entity.getList_musiclist();
						list.add(0, new SongMenuInfoEntity("-1", "新建歌单"));
						adapter.setList(list);
						adapter.notifyDataSetChanged();
					}
				}
			});
			task.excute();
			CustomProgressDialog.show(activity, true, null).setCanceledOnTouchOutside(false);
		}
		
		//添加歌曲到歌单
		public void addSongMenu(int position, String songmenu_id){
			SongMenuAddSongTask task = new SongMenuAddSongTask(activity, music_id, songmenu_id, new BaseRequestCallBack() {
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					CustomProgressDialog.disMiss();
					}
				@Override
				public void onResult(String result, int code, String alert, Object obj) {
					CustomProgressDialog.disMiss();
					ActivityUtil.showShortToast(activity, alert);
					if(code!=Constant.SUCCESS_REQUEST)
						return;
					obj = ParseUtil.parseResultObj(result, SongMenuListEntity.class);
					SongMenuListEntity entity = (SongMenuListEntity) obj;
					if(Integer.parseInt(entity.getList_size())>0){
						list = entity.getList_musiclist();
						list.add(0, new SongMenuInfoEntity("-1", "新建歌单"));
					}
					adapter.setList(list);
					adapter.notifyDataSetChanged();
					
					Intent intent = new Intent("xxx");
					activity.sendBroadcast(intent);
					dismiss();
				}
			});
			task.excute();
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			if(position==0){
				showAddMenu();
				return;
			}
			if(musiclist_id.equals(list.get(position).getMusiclist_id())){
				ActivityUtil.showShortToast(activity, "歌单重复");
				dismiss();
			}else{
				addSongMenu(position,list.get(position).getMusiclist_id());
			}
		}

}
