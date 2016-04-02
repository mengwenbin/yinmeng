package com.xiaoxu.music.community.view.custom_shape_imageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;

/**
 * Created by Mostafa Gazar on 11/2/13.
 */
public class RectangleImageView extends BaseImageView {
	
	private boolean isRoundedCorner = false;//是否是圆角  @author mengwenbin
	private float roundPx = 8;//圆角的px（ 5-10）
	
	/**
	 * 设置是否是圆角图
	 * @param isRoundedCorner
	 */
	public void setRoundedCorner(boolean isRoundedCorner) {
		this.isRoundedCorner = isRoundedCorner;
	}
	
	/**
	 * 设置圆角的尺寸
	 * @param roundPx
	 * 				 5-10
	 */
	public void setRoundPx(float roundPx) {
		this.roundPx = roundPx;
	}
	

	public RectangleImageView(Context context) {
		super(context);
	}

	public RectangleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RectangleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public static Bitmap getBitmap(int width, int height) {
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.BLACK);
		canvas.drawRect(new RectF(0.0f, 0.0f, width, height), paint);
		return bitmap;
	}
	
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}
	
	@Override
	public Bitmap getBitmap() {
		if(isRoundedCorner){
			return getRoundedCornerBitmap(getBitmap(getWidth(), getHeight()),roundPx);
		}
		return getBitmap(getWidth(), getHeight());
	}
}
