package com.xiaoxu.music.community.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewFragment;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.activity.MineCollectActivity;
import com.xiaoxu.music.community.activity.MusicListActivity;
import com.xiaoxu.music.community.adapter.SongMenuAdapter;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.FavoriteSongMenuEntity;
import com.xiaoxu.music.community.entity.SongMenuEntity;
import com.xiaoxu.music.community.entity.SongMenuInfoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.MineSongMenuTask;
import com.xiaoxu.music.community.parser.ParseUtil;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.TimeUtil;
import com.xiaoxu.music.community.view.DelPopWindow;
import com.xiaoxu.music.community.view.xlistview.XListView;
import com.xiaoxu.music.community.view.xlistview.XListView.OnXListViewListener;

/*
 * 收藏的歌单
 */
public class SongMenuFragment extends BaseNewFragment implements
		OnXListViewListener, OnItemClickListener{

	private TextView songs_num;
	private ImageView left_btn;
	private ImageButton right_btn;
	private TextView all_check;
	private TextView complete;
	private TextView title_center;
	private ImageView del_image;
	private ImageView edit_iv;
	private RelativeLayout rel_collect;
	private RelativeLayout del_layout;
	private LinearLayout title_layout;
	
	private XListView listview;
	private SongMenuAdapter adapter;
	private List<SongMenuEntity> Item;
	private List<SongMenuEntity> list, oldlist;
	private String latelyRefreshTime;// 最近刷新时间

	private int pageNum = 1;
	private String list_size;
	private MineSongMenuTask task;
	private DelPopWindow delpop;
	
	private boolean isShow = false,flag = false;
	private final  String mPageName = "SongMenuFragment";

	@Override
	public int setLayoutId() {

		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(activity);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		return R.layout.fragment_songmenu;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart( mPageName );//友盟统计
		MobclickAgent.onResume(activity);//友盟统计
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		left_btn = (ImageView) getActivity().findViewById(R.id.left_btn);
		right_btn = (ImageButton) getActivity().findViewById(R.id.right_btn);
		all_check = (TextView) getActivity().findViewById(R.id.all_check_tv);
		complete = (TextView) getActivity().findViewById(R.id.complete_tv);
		title_center = (TextView) getActivity().findViewById(R.id.title_center_content);
		title_layout = (LinearLayout) getActivity().findViewById(R.id.rel_layout);
		
	}
	
	@Override
	public void setupView(View view) {

		no_network = getImageButton(view, R.id.no_network);
		rotateLoading = getRotateLoading(view, R.id.loading);
		songs_num = (TextView) view.findViewById(R.id.songs_num);
		edit_iv = (ImageView) view.findViewById(R.id.edit_iv);
		rel_collect = (RelativeLayout) view.findViewById(R.id.rel_collect);
		del_layout = (RelativeLayout) view.findViewById(R.id.layout_del);
		del_image = (ImageView) view.findViewById(R.id.del_image);
		listview = (XListView) view.findViewById(R.id.listview);
		listview.setFooterReady(true);
		listview.setPullLoadEnable(XListView.FOOTER_HIDE);
		
		initListener();
	}

	private void initListener() {
		
		edit_iv.setOnClickListener(new ClickListener());
		del_image.setOnClickListener(new ClickListener());
		listview.setXListViewListener(this);
		listview.setOnItemClickListener(this);
		
	}

	@Override
	public void setModel() {
		
		adapter = new SongMenuAdapter(context,list);
		listview.setAdapter(adapter);
		task();
	}

	public void loading() {
		if (adapter!=null&&adapter.getCount() == 0) {
			loadingStart(rotateLoading);
			onRefresh();
		}
	}

	public void task() {
		if (haveNetwork(context)) {
			rel_collect.setVisibility(View.VISIBLE);
			loadingStart(rotateLoading);
			task = new MineSongMenuTask(context,pageNum, callback);
			task.excute();
		} else {
			if(adapter != null){
				adapter.setList(null);
				adapter.notifyDataSetChanged();
				rel_collect.setVisibility(View.GONE);
				pageNum = 1;
				showNoNet(new ClickListener());
			}
			new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                	listview.setPullLoadEnable(XListView.FOOTER_HIDE);
        			listview.stopLoadMore();
        			listview.stopRefresh();
                }
            }, Constant.DELAYMILLIS);
		}
	}

	BaseRequestCallBack callback = new BaseRequestCallBack(
			FavoriteSongMenuEntity.class) {

		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			loadingCancle(rotateLoading);
			listview.stopLoadMore();
			listview.stopRefresh();
			if (code != Constant.SUCCESS_REQUEST) {
				return;
			}
			
			FavoriteSongMenuEntity data = (FavoriteSongMenuEntity) obj;
			List<SongMenuEntity> lists = data.getList_favorite();
			if (lists != null && lists.size() > 0) {
				list = lists;
				list_size = data.getList_size();
				updateUI();
				songs_num.setText("我收藏的歌单（共"+list_size+"首）");
				edit_iv.setClickable(true);
			}else{
				edit_iv.setClickable(false);
				songs_num.setText("我收藏的歌单（共"+0+"首）");
				listview.setPullLoadEnable(XListView.FOOTER_HIDE);
			}
		}

		@Override
		public void onFailure(HttpException arg0, String arg1) {
			loadingCancle(rotateLoading);
			listview.stopLoadMore();
			listview.stopRefresh();
		}
	};

	public void updateUI() {
		if (pageNum == 1) {
			adapter.setList(list);
			pageNum = 2;
		} else if (pageNum >= 2) {
			oldlist = adapter.getList();
			oldlist.addAll(list);
			adapter.setList(oldlist);
			pageNum++;
		}
		adapter.notifyDataSetChanged();
		latelyRefreshTime = TimeUtil.getSystemTimer(context);
		if (list.size() >= 10) {
			listview.setPullLoadEnable(XListView.FOOTER_WAIT);
		} else {
			listview.setPullLoadEnable(XListView.FOOTER_HIDE);
		}
	}

	@Override
	public void onRefresh() {
		listview.setPullLoadEnable(XListView.FOOTER_HIDE);
		listview.setRefreshTime(latelyRefreshTime);
		pageNum = 1;
		task();
	}

	@Override
	public void onLoadMore() {
		task();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		oldlist = adapter.getList();
		SongMenuInfoEntity songmenu = oldlist.get(position-1).getMusiclist_info();
		if(songmenu == null){
			ActivityUtil.showShortToast(context, "该歌单已经失效");
			return;
		}
		Intent intent = new Intent(context, MusicListActivity.class);
		intent.putExtra("musiclist_id", songmenu.getMusiclist_id());
		intent.putExtra("musiclist_name", songmenu.getMusiclist_name());
		startActivity(intent);
	}

	private OnClickListener itemsOnClick = new OnClickListener() {
		public void onClick(View v) {
			delpop.dismiss();
			switch (v.getId()) {
			
			case R.id.del_btn:
				String favorite_id = getfavorite_id(Item);
				String favorite_category = getfavorite_category(Item);
				if(haveNetwork(context)){
					task = new MineSongMenuTask(context, pageNum, favorite_id, favorite_category, delcallback);
					task.excute();
					adapter.delete();
					songs_num.setText("我收藏的歌单（共"+adapter.getList().size()+"首）");
					if(adapter.getCount() < 10){
						listview.setPullLoadEnable(XListView.FOOTER_HIDE);
					}
				}
				break;
			}
		}
	};
	
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
		}
	};
	
	private String getfavorite_id(List<SongMenuEntity> Item){
		
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < Item.size(); i++) {
			sb.append(Item.get(i).getFavorite_id()+",");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
		
	}
	
	private String getfavorite_category(List<SongMenuEntity> Item){
		
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < Item.size(); i++) {
			sb.append(Item.get(i).getFavorite_category()+",");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
		
	}
	
	private class ClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.edit_iv:
				
				right_btn.setVisibility(View.GONE);
				left_btn.setVisibility(View.GONE);
				title_layout.setVisibility(View.GONE);
				title_center.setVisibility(View.VISIBLE);
				all_check.setVisibility(View.VISIBLE);
				complete.setVisibility(View.VISIBLE);
				rel_collect.setVisibility(View.GONE);
				del_layout.setVisibility(View.VISIBLE);
				if(!isShow){
					adapter.setState(1);
					isShow = true;
				}else{
					adapter.setState(0);
					isShow = false;
				}
				adapter.notifyDataSetChanged();
				break;
			case R.id.del_image:
				delpop = new DelPopWindow(getActivity(),itemsOnClick);
				Button del_btn = delpop.getDel_btn();
				Item = adapter.getDeleteIDs();
				if(list !=null && Item.size() > 0){
					del_btn.setEnabled(true);
					del_btn.setText("删除"+Item.size()+"项");
				}
				delpop.showAtLocation(getActivity().findViewById(R.id.collect_main),Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
				break;
			case R.id.no_network:
				hintNoNet();
				task();
			}
		}
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(task != null){
			task.cancelTask();
		}
	}
	
	public void all_check(){
		
		if(adapter != null && adapter.getCount() > 0){
			
			if (!flag) {
				adapter.setCheckAll();
				flag = true;
			} else {
				
				adapter.setCheckNO();
				flag = false;
				
			}
			
		}
		
	}
	
	public void complete(){
		
		title_layout.setVisibility(View.VISIBLE);
		title_center.setVisibility(View.GONE);
		right_btn.setVisibility(View.VISIBLE);
		left_btn.setVisibility(View.VISIBLE);
		all_check.setVisibility(View.GONE);
		complete.setVisibility(View.GONE);
		rel_collect.setVisibility(View.VISIBLE);
		del_layout.setVisibility(View.GONE);
		if(adapter != null){
			
			isShow = false;
			adapter.setState(0);
			if(adapter.getList() != null && adapter.getList().size() > 0){
				adapter.setCheckNO();
			}
			((MineCollectActivity)activity).onResumeBinder();
			
		}
		
	}

}
