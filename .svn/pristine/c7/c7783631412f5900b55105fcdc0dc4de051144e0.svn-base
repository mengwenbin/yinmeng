package com.xiaoxu.music.community.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.adapter.EditSongAdapter;
import com.xiaoxu.music.community.adapter.SongDetailsAdapter;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.SongInfoEntity;
import com.xiaoxu.music.community.model.task.MineSongTask;
import com.xiaoxu.music.community.parser.ParseUtil;
import com.xiaoxu.music.community.service.MediaPlayerService;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.view.DelPopWindow;

/*
 * 移除歌单信息
 */
public class EditMineSongActivity extends BaseNewActivity implements OnClickListener {

	private Intent intent;
	private String musiclist_id;
	private ImageView del_image;
	private TextView tv_all_chak;
	private TextView tv_complete;
	private ListView listView;
	private List<SongInfoEntity> Item;
	private ArrayList<SongInfoEntity> list;
	
	private EditSongAdapter adapter;
	private DelPopWindow delpop;
	private MineSongTask task;
	
	private Activity activity;
	private boolean flag = false;
	private final String mPageName = "EditMineSongActivity";
	
	@Override
	public void setupView() {
		
		tv_all_chak = (TextView) findViewById(R.id.all_check);
		tv_complete = (TextView) findViewById(R.id.complete_tv);
		listView = (ListView) findViewById(R.id.list_view);
		del_image = (ImageView) findViewById(R.id.del_image);
		
		initListener();
	}

	private void initListener() {

		tv_all_chak.setOnClickListener(this);
		tv_complete.setOnClickListener(this);
		del_image.setOnClickListener(this);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setContent() {
		
		setContentView(R.layout.activity_edit_minesong);
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(this);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		activity = this;
		intent = getIntent();
		list = (ArrayList<SongInfoEntity>) intent.getSerializableExtra("list");
		musiclist_id = intent.getStringExtra("musiclist_id");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart( mPageName );//友盟统计
		MobclickAgent.onResume(this);//友盟统计
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(this);
	}

	@Override
	public void setModel() {

		adapter = new EditSongAdapter(context, list);
		listView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.all_check:
			if(!flag){
				adapter.setCheckAll();
				flag = true;
			}else{
				adapter.setCheckNO();
				flag = false;
			}
			break;
		case R.id.complete_tv:
			
			Intent intent = new Intent(context,SongMenuDetailsActivity.class);
			if(Item != null && Item.size() > 0){
				intent.putExtra("list_size", Item.size()+"");
			}
			setResult(1, intent);
			finish();
			break;
		case R.id.del_image:
			
			if(MediaPlayerService.isPlaying){
				SongDetailsAdapter.current = -1;
			}
			delpop = new DelPopWindow(activity, itemsOnClick);
			Button del_btn = delpop.getDel_btn();
			Item = adapter.getDeleteIDs();
			if (list != null && Item.size() > 0) {
				del_btn.setEnabled(true);
				del_btn.setText("删除" + Item.size() + "项");
			}
			delpop.showAtLocation(findViewById(R.id.main_ll),Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		}
	}
	
	/*
	 * popwindow删除监听
	 */
	private OnClickListener itemsOnClick = new OnClickListener(){
		
		public void onClick(View v) {
			delpop.dismiss();
			switch (v.getId()) {
			case R.id.del_btn:
				if (haveNetwork()) {
					
					String music_id = initMusic_id(Item);
					task = new MineSongTask(context, musiclist_id, music_id, delcallback);
					task.excute();
				}
				break;
			}
		}
	};
	
	private String initMusic_id(List<SongInfoEntity> list){
		
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i).getMusic_id()+",");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}
	
	/*
	 * 删除我的歌曲回调
	 */
	RequestCallBack<String> delcallback = new RequestCallBack<String>() {

		@Override
		public void onFailure(HttpException arg0, String arg1) {
		}

		@Override
		public void onSuccess(ResponseInfo<String> arg0) {

			String result = arg0.result;
			int code = ParseUtil.parseCode(result);
			if (code != Constant.SUCCESS_REQUEST) {
				return;
			}
			String alert = ParseUtil.parseAlert(result);
			ActivityUtil.showShortToast(context, alert);
			adapter.delete();
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(context,SongMenuDetailsActivity.class);
			if(Item != null && Item.size() > 0){
				intent.putExtra("list_size", Item.size()+"");
			}
			setResult(1, intent);
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
