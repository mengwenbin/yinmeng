package com.xiaoxu.music.community;

import com.lidroid.xutils.BitmapUtils;
import com.umeng.analytics.MobclickAgent;
import com.xiaoxu.music.community.FragmentPageSetting;
import com.xiaoxu.music.community.constant.Constant;
import com.xiaoxu.music.community.service.MediaPlayerService;
import com.xiaoxu.music.community.util.NetUtils;
import com.xiaoxu.music.community.view.RotateLoading;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class BaseNewFragment extends Fragment implements FragmentPageSetting{
	
	public Context context;
	public Activity activity;
	public BitmapUtils bitmapUtils;
	public View contentView;
	
	public RotateLoading rotateLoading;
	public ImageButton no_network;
	
	public YmApplication app;
	public AnimationDrawable ad;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		context = getActivity();
		activity = getActivity();
		app = YmApplication.getInstance();
		// SDK在统计Fragment时，需要关闭Activity自带的页面统计，来禁止默认的Activity页面统计方式。避免重复统计。
		MobclickAgent.openActivityDurationTrack(false);
		bitmapUtils = new BitmapUtils(context, Constant.DISK_CACHE_PATH,Constant.MAX_MEMORY_SIZE);
		contentView = inflater.inflate(setLayoutId(), container, false);
		setupView(contentView);
		setModel();
		return contentView;
	}
	
	public void initAnimation(View iv) {
		ad = (AnimationDrawable) iv.getBackground();
		on_off_anim();
	}
	
	public void on_off_anim() {
		if(ad==null)
			return;
		if(MediaPlayerService.isPlaying){
			ad.start();
		}else{
			ad.stop();
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    on_off_anim();
	}
	
	public View getView(View view,int id){
		return view.findViewById(id);
	}
	
	//Button
	protected Button getButton(View view,int id) {
		return (Button) view.findViewById(id);
	}
	
	//ImageButton
	protected ImageButton getImageButton(View view,int id) {
		return (ImageButton) view.findViewById(id);
	}
	
	//EditText
	protected EditText getEditText(View view,int id) {
		return (EditText) view.findViewById(id);
	}
	
	//ImageView
	protected ImageView getImageView(View view,int id) {
		return (ImageView) view.findViewById(id);
	}
	
	//ListView
	protected ListView getListView(View view,int id) {
		return (ListView) view.findViewById(id);
	}
	
	//GridView
	protected GridView getGridView(View view,int id) {
		return (GridView) view.findViewById(id);
	}
	
	//ViewPager
	protected ViewPager getViewPager(View view,int id) {
		return (ViewPager) view.findViewById(id);
	}
	
	//CheckBox
	protected CheckBox getCheckBox(View view,int id) {
		return (CheckBox) view.findViewById(id);
	}
	
	//TextView
	protected TextView getTextView(View view,int id) {
		return (TextView) view.findViewById(id);
	}
	
	//RelativeLayout
	protected RelativeLayout getRelativeLayout(View view,int id) {
		return (RelativeLayout) view.findViewById(id);
	}
	
	//LinearLayout
	protected LinearLayout getLinearLayout(View view,int id) {
		return (LinearLayout) view.findViewById(id);
	}
	
	protected ProgressBar getProgressBar(View view,int id){
		return (ProgressBar) view.findViewById(id);
	}
	
	protected RotateLoading getRotateLoading(View view, int id){
		return (RotateLoading) view.findViewById(id);
	}
	
	
	/**
	 * 显示 无网络图标
	 * @param l
	 */
	public void showNoNet(OnClickListener l){
		if(no_network != null){
			no_network.setOnClickListener(l);
			no_network.setVisibility(View.VISIBLE);
		}
	}
	/**
	 * 隐藏 无网络图标
	 */
	public void hintNoNet(){
		if(no_network != null){
			no_network.setVisibility(View.GONE);
		}
	}
	
	/**
	 * Loading 开始
	 * @param loading
	 */
	public void loadingStart(RotateLoading loading){
		rotateLoading = loading;
		if(rotateLoading != null)
			rotateLoading.start();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				loadingCancle(rotateLoading);
			}
		}, 5000);
	}
	
	/**
	 * Loading 关闭
	 * @param loading
	 */
	public void loadingCancle(RotateLoading rotateLoading){
		if(rotateLoading!=null&&rotateLoading.isStart()){
			rotateLoading.stop();
		}
	}
	
	/**
	 * 判断网络状态
	 * @return
	 */
	public boolean haveNetwork(Context context){
		if (NetUtils.getAPNType(context) == -1) { //判断网络状态
			return false;
		}else{
			hintNoNet();
			return true;
		}
	}
	
	/**
	 * 隐藏软键盘
	 */
	public void exitSoft() {
		View view = getActivity().getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
	
}
