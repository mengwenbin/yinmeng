package com.xiaoxu.music.community.view;

import java.util.HashMap;
import java.util.Map;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import com.xiaoxu.music.community.adapter.YasiteAdapter;

public class MyHorizontalScrollView extends HorizontalScrollView implements
		OnClickListener {

	//HorizontalListView中的LinearLayout
	private LinearLayout mContainer;
	// 数据适配器
	private YasiteAdapter mAdapter;
	private int MAX_SCROLL_HEIGHT = 150;
	private OnItemClickListener mOnClickListener;

	/**
	 * 保存View与位置的键值对
	 */
	private Map<View, Integer> mViewPos = new HashMap<View, Integer>();

	public MyHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {

		int newScrollX = scrollX + deltaX;

		int newScrollY = scrollY + deltaY;

		// Clamp values if at the limits and record
		final int left = -maxOverScrollX - MAX_SCROLL_HEIGHT;
		final int right = maxOverScrollX + scrollRangeX + MAX_SCROLL_HEIGHT;
		final int top = -maxOverScrollY;
		final int bottom = maxOverScrollY + scrollRangeY;

		boolean clampedX = false;
		if (newScrollX > right) {
			newScrollX = right;
			clampedX = true;
		} else if (newScrollX < left) {
			newScrollX = left;
			clampedX = true;
		}

		boolean clampedY = false;
		if (newScrollY > bottom) {
			newScrollY = bottom;
			clampedY = true;
		} else if (newScrollY < top) {
			newScrollY = top;
			clampedY = true;
		}

		onOverScrolled(newScrollX, newScrollY, clampedX, clampedY);

		return clampedX || clampedY;
	}


	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mContainer = (LinearLayout) getChildAt(0);
	}

	/**
	 * 初始化数据，设置数据适配器
	 */
	public void initDatas(YasiteAdapter mAdapter) {
		this.mAdapter = mAdapter;
		mContainer = (LinearLayout) getChildAt(0);
		mContainer = (LinearLayout) getChildAt(0);
		mContainer.removeAllViews();
		mViewPos.clear();

		for (int i = 0; i < mAdapter.getCount(); i++) {
			View view = mAdapter.getView(i, null, mContainer);
			view.setOnClickListener(this);
			mContainer.addView(view);
			mViewPos.put(view, i);
		}
	}

	@Override
	public void onClick(View v) {
		if (mOnClickListener != null) {
			mOnClickListener.onClick(v, mViewPos.get(v));
		}
	}

	public void setOnItemClickListener(OnItemClickListener mOnClickListener) {
		this.mOnClickListener = mOnClickListener;
	}

	/**
	 * 条目点击时的回调
	 */
	public interface OnItemClickListener {
		void onClick(View view, int pos);
	}

}
