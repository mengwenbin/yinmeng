package com.xiaoxu.music.community.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ViewPager 能控制是否可 滑动
 * @author ice
 *
 */
public class CustomViewPager extends ViewPager {

	private boolean isCanScroll = false;
	
	public CustomViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 是否可以滑动
	 * @param isCanScroll
	 */
	public void setScanScroll(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}
	
	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if (isCanScroll) {
			return super.onTouchEvent(arg0);
		} else {
			return false;
		}
	}

	@Override
	public void setCurrentItem(int item, boolean smoothScroll) {
		// TODO Auto-generated method stub
		super.setCurrentItem(item, smoothScroll);
	}
	
	@Override
	public void setCurrentItem(int item) {
		// TODO Auto-generated method stub
		super.setCurrentItem(item);
	}
	
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		try {
			if (isCanScroll) {
				return super.onInterceptTouchEvent(ev);
			} else {
				return false;
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return false;
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
            return false;
        }
	}
	
}
