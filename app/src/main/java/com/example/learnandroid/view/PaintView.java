package com.example.learnandroid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/18 12:01
 */
public class PaintView extends View {
    private Paint mPaint;
    private Shader shader1;
    private Shader shader2;
    private Shader shader3;

    public PaintView(Context context) {
        super(context);
        init();
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init(){
        mPaint = new Paint();
        //线性渐变
        shader1 = new LinearGradient(0, 0, 1000, 100, Color.RED, Color.BLUE, Shader.TileMode.CLAMP);
        shader2 = new LinearGradient(0, 600, 200, 600, Color.RED, Color.BLUE, Shader.TileMode.MIRROR);
        shader3 = new LinearGradient(0, 1100, 200, 1100, Color.RED, Color.BLUE, Shader.TileMode.REPEAT);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        mPaint.setShader(shader1);
        canvas.drawRect(0, 100, 1000, 500, mPaint);

    }
}
