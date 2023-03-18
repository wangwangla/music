package com.example.learnandroid.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.learnandroid.R;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/15 19:14
 */
public class CirView extends View {
    private int defaultSize;
    public CirView(Context context) {
        super(context);
    }

    public CirView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //获取属性集合
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CirImageView);
        //获取default_size属性
        defaultSize = typedArray.getDimensionPixelSize(R.styleable.CirImageView_default_size_type,100);
        //将TypedArray回收
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMySize(defaultSize,widthMeasureSpec);
        int height = getMySize(defaultSize,heightMeasureSpec);

        if (width<height){
            height = width;
        }else {
            width = height;
        }
        setMeasuredDimension(width,height);
    }

    private int getMySize(int defaultSize, int measureSpec) {
        int mSize = defaultSize;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        switch (mode){
            case MeasureSpec.UNSPECIFIED:
                mSize = defaultSize;
                break;
            case MeasureSpec.AT_MOST:
                mSize = size;
                break;
            case MeasureSpec.EXACTLY:
                mSize = size;
                break;
        }
        return mSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //调用父类的onDraw函数，因为View这个类实现了一些基本的绘制功能，比如绘制背景颜色和背景图片
        super.onDraw(canvas);
        //半径
        int r = getMeasuredWidth()/2;
        //以圆心的横坐标为当前View的左起始位置+半径
        int centerX = getLeft() + r;
        //以圆心的横坐标为当前View的顶部起始位置+半径
        int centerY = getTop() + r;
        Paint paint = new Paint();
        paint.setColor(Color.YELLOW);
        canvas.drawCircle(centerX,centerY,r,paint);
    }
}
