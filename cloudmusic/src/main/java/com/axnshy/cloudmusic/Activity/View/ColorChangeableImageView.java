package com.axnshy.cloudmusic.Activity.View;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by axnshy on 16/8/14.
 */
public class ColorChangeableImageView extends View {

    private int[] colorRGBValue;

    public ColorChangeableImageView(Context context) {
        super(context);
    }

    public ColorChangeableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorChangeableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ColorChangeableImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setColorRGBValue(String rgbValue) {
        colorRGBValue[0] = Integer.parseInt(rgbValue.substring(1, 3));
        colorRGBValue[1] = Integer.parseInt(rgbValue.substring(3, 5));
        colorRGBValue[2] = Integer.parseInt(rgbValue.substring(5, 7));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        if ((colorRGBValue[0] != 0) && (colorRGBValue[1] != 0) && (colorRGBValue[2] != 0)) {

        }
        super.onDraw(canvas);
    }
}
