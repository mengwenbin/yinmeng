package com.xiaoxu.music.community.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 自定义GridView 克服 scrollview中无法 自适应高度的问题
 * @author ice
 */
public class CustomGridView extends GridView {
	
	 public CustomGridView(Context context, AttributeSet attrs) {   
         super(context, attrs);   
     }   
    
     public CustomGridView(Context context) {
         super(context);   
     }   
    
     public CustomGridView(Context context, AttributeSet attrs, int defStyle) {   
         super(context, attrs, defStyle);   
     }   
    
     @Override   
     public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {   
         int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,   
                 MeasureSpec.AT_MOST);  
         super.onMeasure(widthMeasureSpec, expandSpec);   
     }

}
