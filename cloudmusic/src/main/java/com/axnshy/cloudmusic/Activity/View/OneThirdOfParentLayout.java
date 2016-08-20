package com.axnshy.cloudmusic.Activity.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by axnshy on 16/8/9.
 */
public class OneThirdOfParentLayout extends ViewGroup {
    public OneThirdOfParentLayout(Context context) {
        super(context);
    }

    public OneThirdOfParentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OneThirdOfParentLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public OneThirdOfParentLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
