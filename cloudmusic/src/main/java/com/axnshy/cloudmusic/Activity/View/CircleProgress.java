package com.axnshy.cloudmusic.Activity.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.axnshy.cloudmusic.R;

/**
 * Created by axnshy on 16/8/18.
 */
public class CircleProgress extends View {
    private Paint textPaint;
    private Paint innerCirclePaint;
    private Paint outCirclePaint;

    private int innerCircleRadius;
    private int outCircleWidth;
    private int textSize;
    private float progressMax;
    private float progressCur;
    private float progressDegree;
    private float progressTarget;
    private int textColor;
    private int innerColor;
    private int outterColor;
    private String innerText;


    public CircleProgress(Context context) {
        super(context);
        init(context);
    }

    public CircleProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    void init(Context context) {
        textSize = 50;
        innerColor = context.getResources().getColor(R.color.colorAccent);
        outterColor = context.getResources().getColor(R.color.black);
        textColor = context.getResources().getColor(R.color.white);
        innerCircleRadius = 400;
        outCircleWidth = 10;
        progressMax = 100;
        progressCur = 0;
        progressTarget = 0;
        progressDegree = 0;
        textPaint = new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);
        innerCirclePaint = new Paint();
        innerCirclePaint.setStyle(Paint.Style.FILL);
        innerCirclePaint.setColor(innerColor);
        innerCirclePaint.setStrokeWidth(innerCircleRadius);
        outCirclePaint=new Paint();
        outCirclePaint.setColor(outterColor);
        outCirclePaint.setStrokeWidth(outCircleWidth);
        outCirclePaint.setStyle(Paint.Style.STROKE);
        outCirclePaint.setAntiAlias(true);
        innerText = "Progerss";

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension((int) innerCircleRadius + outCircleWidth, innerCircleRadius + outCircleWidth);
            //如果宽度为wrap_content  高度为match_parent或者精确数值的时候
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            //宽度设置为max，高度为父容器高度
            setMeasuredDimension((int) innerCircleRadius + outCircleWidth, heightSpecSize);
            //如果宽度为match_parent或者精确数值的时候，高度为wrap_content
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            //宽度设置为父容器的宽度，高度为每条柱状图的宽度加上间距再乘以柱状图条数再加上开始Y值后得到的值
            setMeasuredDimension(widthSpecSize, innerCircleRadius + outCircleWidth);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rect = new RectF();
        float mCurrentAngle=0f;
        rect.left = 100f;
        rect.top = 100f;
        rect.right = 900f;
        rect.bottom = 900f;
        canvas.drawCircle(500, 500, innerCircleRadius, innerCirclePaint);
        canvas.drawText(innerText, 400, 350, textPaint);
        if (progressCur < progressMax) {
            if (progressCur<progressTarget) {
                //当前百分比+1
                progressCur+=1;
                //当前角度+360
                mCurrentAngle+=3.6;
                //每10ms重画一次
                postInvalidateDelayed(10);
            }
            canvas.drawArc(rect, -90f ,(progressCur/progressMax)*360-0.1f,false, outCirclePaint);
//            canvas.drawArc(rect, -90, , false, outCirclePaint);
        }
    }

    public CircleProgress setPosition(float position){
        this.progressTarget=position;
        invalidate();
        return this;
    }
}
