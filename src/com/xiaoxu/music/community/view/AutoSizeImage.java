package com.xiaoxu.music.community.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class AutoSizeImage extends ImageView {

	private Context context;
	private Bitmap mMaskBitmap;
	private Paint mPaint;
	
	/** 位图对象 */
	private Bitmap bitmap = null;
	private int bitWidth;
	private int bitHeight;
	
	/** 屏幕的分辨率 */
	private DisplayMetrics dm;
	private int screenWidth;
	private int screenHeight;
	
	public AutoSizeImage(Context context) {
		super(context);
		sharedConstructor(context);
	}

	public AutoSizeImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		sharedConstructor(context);
	}

	public AutoSizeImage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		sharedConstructor(context);
	}

	private void sharedConstructor(Context context) {
		context = context;
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}

	public void setupView() {
		Context context = getContext();
		// 获取屏幕分辨率,需要根据分辨率来使用图片居中
		dm = context.getResources().getDisplayMetrics();
		screenWidth = dm.widthPixels; 
		screenHeight = dm.heightPixels;
		// 根据MyImageView来获取bitmap对象
		BitmapDrawable bd = (BitmapDrawable) this.getDrawable();
		if (bd != null) {
			bitmap = bd.getBitmap();
			int width = bitmap.getWidth();// 获取真实宽高
			int height = bitmap.getHeight();
			LayoutParams lp = getLayoutParams();
			int layoutHeight = (height * screenWidth)/ width;// 调整高度
			lp.width = screenWidth;
			lp.height = layoutHeight;
			setLayoutParams(lp);
		}
		
		// 设置ScaleType为ScaleType.MATRIX，这一步很重要
				this.setScaleType(ScaleType.MATRIX);
				this.setImageBitmap(bitmap);
	}
}
