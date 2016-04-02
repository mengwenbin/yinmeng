package com.xiaoxu.music.community.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.lidroid.xutils.exception.HttpException;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.BaseNewActivity;
import com.xiaoxu.music.community.R;
import com.xiaoxu.music.community.adapter.SearchHistoryAdapter;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.entity.SearchTagEntity;
import com.xiaoxu.music.community.fragment.SearchMusicFragment;
import com.xiaoxu.music.community.fragment.SearchUserFragment;
import com.xiaoxu.music.community.model.impl.BaseRequestCallBack;
import com.xiaoxu.music.community.model.task.SearchTagTask;
import com.xiaoxu.music.community.util.ActivityUtil;
import com.xiaoxu.music.community.util.SharedPreUtils;
import com.xiaoxu.music.community.view.CustomViewPager;
import com.xiaoxu.music.community.view.FlowLayout;
import com.xiaoxu.music.community.view.SearchEditText;
import com.xiaoxu.music.community.view.SearchEditText.TextChangedListener;

public class SearchActivity extends BaseNewActivity implements OnClickListener, OnItemClickListener{
	
	private SearchEditText searchText;
	private String key = "";
	private View line_music, line_user;
	private TextView tv_music, tv_user;
	
	private RelativeLayout layout_tag,layout_content;
	private FlowLayout flowLayout;//流式布局 标签
	
	//历史记录
	private ListView list_view;
	private SearchHistoryAdapter adapter_history;
	private List<String> list_history = new ArrayList<String>();
	
	private CustomViewPager viewpager;
	private ViewPagerAdapter adapter;
	private SearchMusicFragment frag_music;
	private SearchUserFragment frag_user;
	
	private final String mPageName = "SearchActivity";
	public String Login_Tag = "com.xiaoxu.music.community.activity.SearchActivity";
	
//	String[] tag = new String[]{"原创","翻唱","V家","古风","剑三","N站唱见","国人唱见","中文翻唱","男声","灵魂歌手","鬼畜翻唱",
//			"开口跪","治愈向","燃向","神曲","忘记原曲","耳机福利","纯音乐","抖腿向","东方project","作业用","听歌向","三次元","欧美音乐",
//			"史诗金属","日系","电音","高音质","怀旧向","动画音乐","萌系","核爆神曲","同人","魔性","古剑","仙剑","天下3","盗墓笔记",
//			"说唱","18X","纯乐","人声"};
	
	@Override
	public void setContent() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_search);
		// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。
		MobclickAgent.updateOnlineConfig(this);
		// SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。
		AnalyticsConfig.enableEncrypt(true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(mPageName);// 友盟统计
		MobclickAgent.onResume(this);// 友盟统计
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(mPageName);
		MobclickAgent.onPause(this);
		StringBuffer buffer = new StringBuffer();
		for (String str : list_history) {
			buffer.append(str + ",");
		}
		if (buffer.length() > 0) {
			buffer = buffer.deleteCharAt(buffer.length() - 1);
		}
		SharedPreUtils.setSearchHistory(context, buffer.toString());
	}

	@Override
	public void setupView() {
		// TODO Auto-generated method stub
		//控制显示 热门标签  or 搜索内容
		getTextView(R.id.title_right_btn).setOnClickListener(this);
		
		layout_tag = getRelativeLayout(R.id.layout_tag);
		layout_content = getRelativeLayout(R.id.layout_content);
		flowLayout = (FlowLayout) findViewById(R.id.flow_layout);
		
		list_view = getListView(R.id.list_history);
		list_view.setOnItemClickListener(this);
		adapter_history = new SearchHistoryAdapter(this, list_history);
		list_view.setAdapter(adapter_history);
		
		viewpager = (CustomViewPager) getViewPager(R.id.viewpager_search);
		viewpager.setOnPageChangeListener(listener);
		viewpager.setScanScroll(true);
		
		line_music = findViewById(R.id.line_music);
		line_user = findViewById(R.id.line_user);
		tv_music = getTextView(R.id.select_music);
		tv_user = getTextView(R.id.select_user);
		
		searchText = (SearchEditText) findViewById(R.id.search_view);
		searchText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(searchText.getText().length()==0){
					layout_tag.setVisibility(View.VISIBLE);
				}
			}
		});
		searchText.setTextChangedListener(new TextChangedListener() {
			@Override
			public void onTextChanged(CharSequence s, int start, int count, int after) {
				if(s.length()==0)
					layout_tag.setVisibility(View.VISIBLE);
			}
		});
		searchText.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					exitSoft();
					layout_tag.setVisibility(View.INVISIBLE);
					key = v.getText().toString().trim();
					frag_music.search(key);
					frag_user.search(key);
					addHistory(key);
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public void setModel() {
		// TODO Auto-generated method stub
		taskTag();
		frag_music = new SearchMusicFragment();
		frag_user = new SearchUserFragment();
		adapter = new ViewPagerAdapter(getSupportFragmentManager());
		viewpager.setAdapter(adapter);
		String str_his = SharedPreUtils.getSearchHistory(context);
		if(!TextUtils.isEmpty(str_his)){
			String[] his = str_his.split(",");
			for(String h : his)
				list_history.add(h);
			setHistory(list_history);
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		exitSoft();
		key = list_history.get(position);
		addHistory(key);
		searchText.setText(key);
		layout_tag.setVisibility(View.INVISIBLE);
		frag_music.search(key);
		frag_user.search(key);
	}
	
	public void setHistory(List<String> history){
		list_history = history;
		adapter_history.setList(list_history);
		adapter_history.notifyDataSetChanged();
	}
	
	public void addHistory(String history){
		list_history.add(0, history);
		removeHistory();
		setHistory(list_history);
	}
	
	public void removeHistory(){
		if(list_history.size()>10){
			list_history.remove(list_history.size()-1);
			removeHistory();
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.title_right_btn){
//			if(layout_tag.getVisibility()==View.VISIBLE){
//				exitSoft();
//				layout_tag.setVisibility(View.INVISIBLE);
//			}else{
				finish();
//			}
		}
	}
	
	public void login(){
		Intent i = new Intent(context, StartActivity.class);
		i.putExtra(StartActivity.LOGIN_KEY, Login_Tag);
		startActivity(i);
	}
	
	public void taskTag(){
		SearchTagTask task = new SearchTagTask(context, "search_char", new BaseRequestCallBack(SearchTagEntity.class) {
			@Override
			public void onResult(String result, int code, String alert, Object obj) {
				if(code!=Constant.SUCCESS_REQUEST){
					return;
				}
				SearchTagEntity entity = (SearchTagEntity) obj;
				updateTagUI(entity.getV().split(","));
			}
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
			}
		});
		task.excute();
	}
	
	public void updateTagUI(String[] tags){
		if(null==tags||tags.length==0)
			return;
		MarginLayoutParams lp = new MarginLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 10;
        lp.rightMargin = 10;
        lp.topMargin = 10;
        lp.bottomMargin = 10;
        for(int i = 0; i < tags.length; i ++){
            TextView view = new TextView(this);
            view.setTextSize(16);
            view.setText(tags[i]);
            view.setPadding(5, 3, 5, 3);
            view.setBackgroundResource(R.drawable.bg_shape_tag);
            view.setTag(tags[i]);
            view.setOnClickListener(l_tag);
            flowLayout.addView(view,lp);
        }
	}
	
	/**
	 * 搜索
	 * @param k
	 */
	public void Search(String k){
		key = k;
		addHistory(key);
		searchText.setText(key);
		layout_tag.setVisibility(View.INVISIBLE);
		frag_music.search(key);
		frag_user.search(key);
	}
	
	OnClickListener l_tag = new OnClickListener() {
		@Override
		public void onClick(View v) {
			exitSoft();
			Search((String)v.getTag());
		}
	};
	
	OnPageChangeListener listener = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			if(arg0 == 0){
				line_music.setBackgroundColor(getResources().getColor(R.color.tab_text_select));
				line_user.setBackgroundColor(getResources().getColor(R.color.white));
				tv_music.setTextColor(getResources().getColor(R.color.tab_text_select));
				tv_user.setTextColor(getResources().getColor(R.color.tab_text_normal));
			} else {
				line_music.setBackgroundColor(getResources().getColor(R.color.white));
				line_user.setBackgroundColor(getResources().getColor(R.color.tab_text_select));
				tv_music.setTextColor(getResources().getColor(R.color.tab_text_normal));
				tv_user.setTextColor(getResources().getColor(R.color.tab_text_select));
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
		}
	};
	
	public class ViewPagerAdapter extends FragmentStatePagerAdapter {
		
		private Fragment fragment;
		
		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			switch (arg0) {
			case 0:
				fragment = frag_music;
				break;
			case 1:
				fragment = frag_user;
				break;
			default:
				break;
			}
			return fragment;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 2;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			super.destroyItem(container, position, object);
		}
	}
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if(layout_tag.getVisibility()==View.VISIBLE){
//				layout_tag.setVisibility(View.INVISIBLE);
//			}else{
//				finish();
//			}
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
	
}
