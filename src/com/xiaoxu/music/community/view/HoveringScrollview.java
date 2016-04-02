package com.xiaoxu.music.community.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 滑动到顶部悬浮功能条 ScrollView
 * @author ice
 */
public class HoveringScrollview extends ScrollView {
	
	private ScrollListener onScrollListener;
	private boolean isScrollViewMoved = true;
	private boolean canChange = false;
	//修改isScrollViewMoved
	public void setScrollViewMoved(boolean isScrollViewMoved) {
		if(canChange){
			this.isScrollViewMoved = isScrollViewMoved;
		}
	}
	
	//设置是否运行修改isScrollViewMoved
	public void setCanChangeMoved(boolean canChange) {
		this.canChange = canChange;
	}

	/**
	 * 主要是用在用户手指离开本view，本view还在继续滑动，我们用来保存Y的距离，然后做比较
	 */
	private int lastScrollY;

	public HoveringScrollview(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 设置滚动接口
	 * @param onScrollListener
	 */
	public void setScrollListener(ScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}

	/**
	 * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中
	 */
	private Handler handler = new Handler() {
		
		public void handleMessage(android.os.Message msg) {
			int scrollY = HoveringScrollview.this.getScrollY();
//			int height = getHeight();
//			if((scrollY+height)==getChildAt(0).getMeasuredHeight()&&canChange){//滚动到底部
//				isScrollViewMoved = false;
//            }
			// 此时的距离和记录下的距离不相等，在隔6毫秒给handler发送消息?
			if (lastScrollY != scrollY) {
				lastScrollY = scrollY;
				handler.sendMessageDelayed(handler.obtainMessage(), 6);
			}
			if (onScrollListener != null) {
				onScrollListener.onScroll(scrollY);
			}
		};
	};
	
	

	/**
	 * 重写onTouchEvent， 当用户的手在HoveringScrollview上面的时候，
	 * 直接将HoveringScrollview滑动的Y方向距离回调给onScroll方法中，当用户抬起手的时候，
	 * HoveringScrollview可能还在滑动，所以当用户抬起手我们隔6毫秒给handler发送消息，在handler处理
	 * HoveringScrollview滑动的距离
	 */
	public boolean onTouchEvent(MotionEvent ev) {
		if (onScrollListener != null) {
			onScrollListener.onScroll(lastScrollY = this.getScrollY());
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_UP:
			handler.sendMessageDelayed(handler.obtainMessage(), 20);
			break;
		}
		return super.onTouchEvent(ev);
	};
	
	/**
	 * 滚动的回调接口
	 */
	public interface ScrollListener {
		/**
		 * 回调方法， 返回本view滑动的Y方向距离
		 */
		public void onScroll(int scrollY);
	}
	
	
//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		// TODO Auto-generated method stub
//		return super.onInterceptTouchEvent(ev)&&isScrollViewMoved;
//	}
	
	
}
