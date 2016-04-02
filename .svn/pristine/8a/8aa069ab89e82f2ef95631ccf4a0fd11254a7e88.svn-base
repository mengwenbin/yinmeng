package com.xiaoxu.music.community.util;

import android.view.animation.Interpolator;

public class DecelerateAccelerateInterpolator implements Interpolator {

    //input��0��1������ֵҲ��0��1.����ֵ�����߱����ٶȼӼ�����
    @Override
    public float getInterpolation(float input) {
        return (float) (Math.tan((input * 2 - 1) / 4 * Math.PI)) / 2.0f + 0.5f;
    }
}
