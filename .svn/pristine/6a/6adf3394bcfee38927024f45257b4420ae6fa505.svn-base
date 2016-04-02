package com.xiaoxu.music.community.activity;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.adapter.DownLoadAdapter;
import com.xiaoxu.music.community.dao.DownLoadDB;
import com.xiaoxu.music.community.entity.SimpleUserEntity;
import com.xiaoxu.music.community.entity.SongInfoEntity;
import com.xiaoxu.music.community.service.MediaPlayerService;
import com.xiaoxu.music.community.view.DelPopWindow;

public class MineDownloadActivity extends BaseNewActivity implements OnClickListener {

	private ListView listView;
	private ImageView del_image;
	private TextView songs_num;
	private TextView title_center;
	private TextView all_check;
	private TextView complete;
	private ImageView left_btn;
	private ImageButton right_btn;
	private ImageView edit_music;
	private ImageView play_all;
	private RelativeLayout del_layout;
	private DownLoadDB downLoaddb;
	private List<SongInfoEntity> list;
	private List<SongInfoEntity> Item;
	private DownLoadAdapter adapter;
	private DelPopWindow delpop;
	private RelativeLayout layout_play_all;
	private boolean isShow = false , flag = false;
	
	private final String mPageName = "MineDownloadActivity";
	@Override
	public void setupView() {

		left_btn = (ImageView) findViewById(R.id.left_btn);
		right_btn = (ImageButton) findViewById(R.id.right_btn);
		initAnimation(right_btn);
		listView = (ListView) findViewById(R.id.list_view);
		play_all = (ImageView) findViewById(R.id.playall);
		del_layout = (RelativeLayout) findViewById(R.id.layout_del);
		songs_num = (TextView) findViewById(R.id.songs_num);
		title_center = (TextView) findViewById(R.id.title_center_content);
		all_check = (TextView) findViewById(R.id.all_check);
		complete = (TextView) findViewById(R.id.complete);
		edit_music = (ImageView) findViewById(R.id.eidt_music);
		del_image = (ImageView) findViewById(R.id.del_image);
		layout_play_all = (RelativeLayout) findViewById(R.id.layout_play_all);
		title_center.setText("我的下载");
		
		initListener();
	}

	private void initListener() {

		left_btn.setOnClickListener(this);
		right_btn.setOnClickListener(this);
		play_all.setOnClickListener(this);
		edit_music.setOnClickListener(this);
		del_image.setOnClickListener(this);
		all_check.setOnClickListener(this);
		complete.setOnClickListener(this);
	}

	@Override
	public void setContent() {
		setContentView(R.layout.activity_minedownload);
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(this);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
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
		
		downLoaddb = new DownLoadDB(context);
		list = downLoaddb.getMusicListIsDownLoad();
		if(list != null && list.size() > 0){
			for (int i = 0; i < list.size(); i++) {
				SongInfoEntity songInfoEntity = list.get(i);
				SimpleUserEntity userEntity = new SimpleUserEntity(songInfoEntity.getUser_nick(),
				songInfoEntity.getUser_sex(), songInfoEntity.getUser_img(), songInfoEntity.getIs_attention()
				, songInfoEntity.getUser_summary());
				songInfoEntity.setUser(userEntity);
			}
			songs_num.setText("播放全部（共"+list.size()+"首）");
			adapter = new DownLoadAdapter(context,MineDownloadActivity.this,app,list,downLoaddb);
			listView.setAdapter(adapter);
			
		}else{
			songs_num.setText("播放全部（共"+0+"首）");
			edit_music.setEnabled(false);
		}
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.left_btn:
			finish();
			break;
		case R.id.right_btn:
			startActivity(new Intent(context, PlayActivity.class));
			break;
		case R.id.playall:
			
			List<SongInfoEntity> lists = new ArrayList<SongInfoEntity>();
			if(list != null && list.size() > 0){
				lists.addAll(adapter.getList());
			}
			app.setSongMenu(lists);
			app.playStart(0);
			break;
		case R.id.del_image:
			
			if(MediaPlayerService.isPlaying){
				adapter.current = -1;
			}
			delpop = new DelPopWindow(MineDownloadActivity.this,itemsOnClick);
			Button del_btn = delpop.getDel_btn();
			Item = adapter.getDeleteIDs();
			if(list !=null && Item.size() > 0){
				del_btn.setEnabled(true);
				del_btn.setText("删除"+Item.size()+"项");
			}
			delpop.showAtLocation(MineDownloadActivity.this.findViewById(R.id.rel_main),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			
			break;
		case R.id.eidt_music:
			
			layout_play_all.setVisibility(View.GONE);
			left_btn.setVisibility(View.GONE);
			right_btn.setVisibility(View.GONE);
			all_check.setVisibility(View.VISIBLE);
			complete.setVisibility(View.VISIBLE);
			del_layout.setVisibility(View.VISIBLE);
			title_center.setText("批量操作");
			if(!isShow){
				isShow = true;
				adapter.setState(1);
			}else{
				isShow = false;
				adapter.setState(0);
			}
			adapter.notifyDataSetChanged();
			break;
		case R.id.all_check:
			
			if(!flag){
				flag = true;
				adapter.setCheckAll();
			}else{
				flag = false;
				adapter.setCheckNO();
			}
			
			break;
		case R.id.complete:
			
			layout_play_all.setVisibility(View.VISIBLE);
			left_btn.setVisibility(View.VISIBLE);
			right_btn.setVisibility(View.VISIBLE);
			all_check.setVisibility(View.GONE);
			complete.setVisibility(View.GONE);
			play_all.setVisibility(View.VISIBLE);
			onResumeBinder();
			del_layout.setVisibility(View.GONE);
			title_center.setText("我的下载");
			adapter.setState(0);
			isShow = false;
			if(adapter.getList() != null && adapter.getList().size() > 0){
				adapter.setCheckNO();
			}
			if(adapter.getList().size() == 0){
				edit_music.setEnabled(false);
			}
			break;
		}
		
	}
	
	private OnClickListener itemsOnClick = new OnClickListener() {
		public void onClick(View v) {
			delpop.dismiss();
			switch (v.getId()) {
			case R.id.del_btn:
				downLoaddb.deleteAll(Item);
				List<SongInfoEntity> newList = downLoaddb.getMusicListIsDownLoad();
				adapter.setList(newList);
				songs_num.setText("播放全部（共"+newList.size()+"首）");
				adapter.setCheckNO();
				adapter.notifyDataSetChanged();
				break;
			}
		}
	};


}
