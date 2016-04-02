package com.xiaoxu.music.community.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

public class ImageUtils {

	/**  
     * 缩放图片  
     * @param bitmap  
     * @param width  
     * @param height  
     * @return */  
   public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {  
       int w = bitmap.getWidth();  
       int h = bitmap.getHeight();  
       Matrix matrix = new Matrix();  
       matrix.postScale((float) width / w, (float) height / h);  
       return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);  
   }  
 
   /**  
     * 将Drawable转化为Bitmap  
     * @param drawable  
     * @return  
     */  
   public static Bitmap drawableToBitmap(Drawable drawable) {  
       int width = drawable.getIntrinsicWidth();  
       int height = drawable.getIntrinsicHeight();  
       Bitmap bitmap = Bitmap.createBitmap(width, height, drawable  
               .getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
               : Bitmap.Config.RGB_565);  
       Canvas canvas = new Canvas(bitmap);  
       drawable.setBounds(0, 0, width, height);  
       drawable.draw(canvas);  
       return bitmap;  
   }  
 
   /**  
     * 获得圆角图片  
     * @param bitmap  
     * @param roundPx  
     * @return  
     */  
   public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,float roundx) {  
       Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);  
       Canvas canvas = new Canvas(output);  
       Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());  
       RectF rectF = new RectF(rect); 
       Paint paint = new Paint();
       paint.setAntiAlias(true);   // 防止边缘的锯齿  
       paint.setFilterBitmap(true);  // 对位图进行滤波处理  
       canvas.drawRoundRect(rectF, roundx, roundx, paint);
       paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));//设置两张图片相交时的显示模式为 SRC_IN 
       canvas.drawBitmap(bitmap, rect, rect, paint);  
       return output;
   } 

	/**  
     * 获得带倒影的图片  
     * @param bitmap  
     * @return  
     */  
   public static Bitmap getReflectionImageWithOrigin(Bitmap bitmap) {  
         
       // 原始图片和反射图片中间的间距  
       final int reflectionGap = 4;  
       int width = bitmap.getWidth();  
       int height = bitmap.getHeight();  
         
       // 反转    
       Matrix matrix = new Matrix();  
         
       // 第一个参数为1表示x方向上以原比例为准保持不变，正数表示方向不变。     
       // 第二个参数为-1表示y方向上以原比例为准保持不变，负数表示方向取反。  
       matrix.preScale(1, -1);  
       Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 3,  
               width, height / 3, matrix, false);  
       Bitmap bitmapWithReflection = Bitmap.createBitmap(width,  
               (height + height / 3), Config.ARGB_8888);  
       Canvas canvas = new Canvas(bitmapWithReflection);  
       canvas.drawBitmap(bitmap, 0, 0, null);  
       Paint defaultPaint = new Paint();  
       canvas.drawRect(0, height, width, height + reflectionGap, defaultPaint);  
       canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);  
       Paint paint = new Paint();  
       LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,  
               bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,  
               0x00ffffff, TileMode.CLAMP);  
       paint.setShader(shader);  
       paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));  
       canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()  
               + reflectionGap, paint);  
       return bitmapWithReflection;  
   }  
   
      
   /**
    * 
    * TODO<图片模糊化处理> 
    * @throw 
    * @return Bitmap 
    * @param bitmap 源图片
    * @param radius The radius of the blur Supported range 0 < radius <= 25
    * @param context 上下文
    */ 
   @SuppressLint("NewApi")
   public static Bitmap blurBitmap(Bitmap bitmap,float radius,Context context){   
          
       //Let's create an empty bitmap with the same size of the bitmap we want to blur   
       Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);   
            
       //Instantiate a new Renderscript   
       RenderScript rs = RenderScript.create(context);   
            
       //Create an Intrinsic Blur Script using the Renderscript   
       ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));   
            
       //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps   
       Allocation allIn = Allocation.createFromBitmap(rs, bitmap);   
       Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);   
            
       //Set the radius of the blur   
       if(radius > 25){ 
           radius = 25.0f; 
       }else if (radius <= 0){ 
           radius = 1.0f; 
       } 
       blurScript.setRadius(radius);   
            
       //Perform the Renderscript   
       blurScript.setInput(allIn);   
       blurScript.forEach(allOut);   
            
       //Copy the final bitmap created by the out Allocation to the outBitmap   
       allOut.copyTo(outBitmap);   
            
       //recycle the original bitmap   
       bitmap.recycle();   
       bitmap = null; 
       //After finishing everything, we destroy the Renderscript.   
       rs.destroy();   
            
       return outBitmap;   
            
            
   } 
      
      
   /** 
    * TODO<给图片添加指定颜色的边框> 
    * @param srcBitmap 原图片 
    * @param borderWidth 边框宽度
    * @param color 边框的颜色值 
    * @return 
    */   
   public static Bitmap addFrameBitmap(Bitmap srcBitmap,int borderWidth,int color)   
   {   
       if (srcBitmap == null){ 
           return null; 
       } 
          
       int newWidth = srcBitmap.getWidth() + borderWidth ; 
       int newHeight = srcBitmap.getHeight() + borderWidth ; 
          
       Bitmap outBitmap = Bitmap.createBitmap(newWidth, newHeight, Config.ARGB_8888);  
          
       Canvas canvas = new Canvas(outBitmap); 
          
       Rect rec = canvas.getClipBounds();   
       rec.bottom--;   
       rec.right--;   
       Paint paint = new Paint();   
       //设置边框颜色   
       paint.setColor(color);   
       paint.setStyle(Paint.Style.STROKE);   
       //设置边框宽度   
       paint.setStrokeWidth(borderWidth);   
       canvas.drawRect(rec, paint); 
          
       canvas.drawBitmap(srcBitmap, borderWidth/2, borderWidth/2, null); 
       canvas.save(Canvas.ALL_SAVE_FLAG); 
       canvas.restore(); 
       if (srcBitmap !=null && !srcBitmap.isRecycled()){ 
           srcBitmap.recycle(); 
           srcBitmap = null; 
       } 
          
       return outBitmap; 
   }  
}
