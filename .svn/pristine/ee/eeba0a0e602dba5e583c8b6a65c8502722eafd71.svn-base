package com.xiaoxu.music.community.fragment;

import java.util.ArrayList;
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
import com.xiaoxu.music.community.activity.MusicDetailActivity;
import com.xiaoxu.music.community.adapter.SingleAdapter;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.FavoriteSingleEntity;
import com.xiaoxu.music.community.entity.SingleEntity;
import com.xiaoxu.music.community.entity.SongInfoEntity;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.MineSongMenuTask;
import com.xiaoxu.music.community.model.task.MusicUserSongsTask;
import com.xiaoxu.music.community.parser.ParseUtil;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.TimeUtil;
import com.xiaoxu.music.community.view.DelPopWindow;
import com.xiaoxu.music.community.view.xlistview.XListView;
import com.xiaoxu.music.community.view.xlistview.XListView.OnXListViewListener;

/*
 * 收藏的单曲
 */
public class SongFragment extends BaseNewFragment implements
		OnXListViewListener, OnItemClickListener {

	private TextView songs_num;
	private ImageView edit_iv;
	private ImageView play_all;
	private ImageView left_btn;
	private ImageView del_image;
	private ImageButton right_btn;
	private TextView all_check;
	private TextView complete;
	private TextView title_center;
	private XListView listview;
	private LinearLayout title_layout;
	private RelativeLayout del_layout;
	private RelativeLayout play_item;
	private SingleAdapter adapter;
	private List<SingleEntity> Item;
	private List<SingleEntity> list, oldlist;
	private String latelyRefreshTime;// 最近刷新时间

	private int pageNum = 1;
	private String list_size;
	private MusicUserSongsTask task;
	private MineSongMenuTask delSongtask;

	private DelPopWindow delpop;
	private boolean isShow = false, flag = false;
	
	private final  String mPageName = "SongFragment";

	@Override
	public int setLayoutId() {
		
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(activity);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
		return R.layout.fragment_minesong;
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
	
	/*
	 * 初始化控件
	 */

	@Override
	public void setupView(View view) {

		rotateLoading = getRotateLoading(view, R.id.loading);
		no_network = getImageButton(view, R.id.no_network);
		songs_num = (TextView) view.findViewById(R.id.songs_num);
		play_all = (ImageView) view.findViewById(R.id.playall);
		del_image = (ImageView) view.findViewById(R.id.del_image);
		edit_iv = (ImageView) view.findViewById(R.id.eidt_music);
		del_layout = (RelativeLayout) view.findViewById(R.id.rel_del);
		play_item = (RelativeLayout) view.findViewById(R.id.play_item);

		listview = (XListView) view.findViewById(R.id.listview);
		listview.setFooterReady(true);
		listview.setPullLoadEnable(XListView.FOOTER_HIDE);
		initListener();
	}

	/*
	 * 初始化监听事件
	 */
	private void initListener() {

		listview.setXListViewListener(this);
		listview.setOnItemClickListener(this);
		del_image.setOnClickListener(new ClickListener());
		play_all.setOnClickListener(new ClickListener());
		edit_iv.setOnClickListener(new ClickListener());
		
	}

	@Override
	public void setModel() {

		adapter = new SingleAdapter(context,list);
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
			play_item.setVisibility(View.VISIBLE);
			loadingStart(rotateLoading);
			task = new MusicUserSongsTask(context, pageNum, callback);
			task.excute();
		} else {
			if(adapter != null){
				adapter.setList(null);
				adapter.notifyDataSetChanged();
				play_item.setVisibility(View.GONE);
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
			FavoriteSingleEntity.class) {

		@Override
		public void onResult(String result, int code, String alert, Object obj) {
			loadingCancle(rotateLoading);
			listview.stopLoadMore();
			listview.stopRefresh();
			if (code != Constant.SUCCESS_REQUEST) {
				return;
			}
			FavoriteSingleEntity data = (FavoriteSingleEntity) obj;
			List<SingleEntity> lists = data.getList_favorite();
			if (lists != null && lists.size() > 0) {
				list = lists;
				list_size = data.getList_size();
				updateUI();
				songs_num.setText("播放全部（" + list_size + "首）");
				edit_iv.setClickable(true);
			}else{
				edit_iv.setClickable(false);
				songs_num.setText("播放全部（" + 0 + "首）");
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

	/*
	 * 刷新页面
	 */
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

	/*
	 * 下拉刷新
	 */
	@Override
	public void onLoadMore() {
		task();
	}

	/*
	 *listView点击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		oldlist = adapter.getList();
		SongInfoEntity song = oldlist.get(position - 1).getMusic_info();
		if(song == null){
			ActivityUtil.showShortToast(context, "该歌曲已经失效");
			return;
		}
		Intent intent = new Intent(context, MusicDetailActivity.class);
		intent.putExtra("SongInfoEntity", song);
		intent.putExtra("position", position - 1);
		startActivityForResult(intent, 0);
	}

	private class ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			
			case R.id.playall:

				List<SongInfoEntity> lists = new ArrayList<SongInfoEntity>();
				if(list != null && list.size() > 0){
					lists.addAll(initList(adapter.getList()));
				}
				app.setSongMenu(lists);
				app.playStart(0);

				break;
			case R.id.eidt_music:

				right_btn.setVisibility(View.GONE);
				left_btn.setVisibility(View.GONE);
				title_layout.setVisibility(View.GONE);
				title_center.setVisibility(View.VISIBLE);
				all_check.setVisibility(View.VISIBLE);
				complete.setVisibility(View.VISIBLE);
				play_item.setVisibility(View.GONE);
				del_layout.setVisibility(View.VISIBLE);
				if (!isShow) {
					adapter.setState(1);
					isShow = true;
				} else {
					adapter.setState(0);
					isShow = false;
				}
				adapter.notifyDataSetChanged();
				break;
			case R.id.del_image:
				delpop = new DelPopWindow(getActivity(), itemsOnClick);
				Button del_btn = delpop.getDel_btn();
				Item = adapter.getDeleteIDs();
				if (list != null && Item.size() > 0) {
					del_btn.setEnabled(true);
					del_btn.setText("删除" + Item.size() + "项");
				}
				delpop.showAtLocation(getActivity().findViewById(R.id.collect_main),Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
				break;
			case R.id.no_network:
				hintNoNet();
				task();
				break;
			}
		}

	}

	/*
	 * popwindow删除监听
	 */
	private OnClickListener itemsOnClick = new OnClickListener() {
		
		public void onClick(View v) {
			delpop.dismiss();
			switch (v.getId()) {
			case R.id.del_btn:
				String favorite_id = getfavorite_id(Item);
				String favorite_category = getfavorite_category(Item);
				if (haveNetwork(context)) {
					delSongtask = new MineSongMenuTask(context, pageNum,favorite_id, favorite_category, delcallback);
					delSongtask.excute();
					adapter.delete();
					songs_num.setText("播放全部（" + adapter.getCount()+ "首）");
					if(adapter.getCount() < 10){
						listview.setPullLoadEnable(XListView.FOOTER_HIDE);
					}
				}
				break;
			}
		}
	};

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
		}
	};

	/*
	 * 返回收藏id串
	 */
	private String getfavorite_id(List<SingleEntity> Item) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < Item.size(); i++) {
			sb.append(Item.get(i).getFavorite_id() + ",");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();

	}

	/*
	 * 返回收藏歌曲类型
	 */
	private String getfavorite_category(List<SingleEntity> Item) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < Item.size(); i++) {
			sb.append(Item.get(i).getFavorite_category() + ",");
		}
		sb.deleteCharAt(sb.length()-1);
		return sb.toString();
	}

	/*
	 * 播放全部音乐初始化List
	 */
	private List<SongInfoEntity> initList(List<SingleEntity> list) {

		List<SongInfoEntity> Item = new ArrayList<SongInfoEntity>();
		for (int i = 0; i < list.size(); i++) {
			Item.add(list.get(i).getMusic_info());
		}
		return Item;

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (task != null) {
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
		play_item.setVisibility(View.VISIBLE);
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
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
			
			if(resultCode == 1){
				if(data != null){
					int position = data.getIntExtra("position", 0);
					SongInfoEntity info = (SongInfoEntity) data.getSerializableExtra("info");
					List<SingleEntity> list = adapter.getList();
					SingleEntity singleEntity = list.get(position);
					singleEntity.setMusic_info(info);
					list.set(position,singleEntity);
					adapter.setList(list);
					adapter.notifyDataSetChanged();
				}
			}
		}

}
