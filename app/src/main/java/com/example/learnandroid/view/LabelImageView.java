package com.example.learnandroid.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/18 11:38
 */
public class LabelImageView extends AppCompatImageView {
    private static final int LABEL_LENGTH = 100;
    private static final int LABEL_HYPOTENUSE_LENGTH = 100;
    private Paint textPaint;
    private Paint backgroundPaint;
    private Path pathText;
    private Path pathBackground;
    public LabelImageView(@NonNull Context context) {
        super(context);
        init();
    }

    public LabelImageView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LabelImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //计算路径
        calculatePath(getMeasuredWidth(), getMeasuredHeight());
        canvas.drawPath(pathBackground, backgroundPaint);
        canvas.drawTextOnPath("Hot", pathText, 100, -20, textPaint);
    }


    private void init() {
        pathText = new Path();
        pathBackground = new Path();

        textPaint = new Paint();
        textPaint.setTextSize(50);
        textPaint.setFakeBoldText(true);
        textPaint.setColor(Color.WHITE);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.RED);
        backgroundPaint.setStyle(Paint.Style.FILL);
    }

    private void calculatePath(int measuredWidth, int measuredHeight) {

        int top = 185;
        int right = measuredWidth;

        float x1 = right - LABEL_LENGTH - LABEL_HYPOTENUSE_LENGTH;
        float x2 = right - LABEL_HYPOTENUSE_LENGTH;
        float y1 = top - LABEL_LENGTH;
        float y2 = top + LABEL_LENGTH + LABEL_HYPOTENUSE_LENGTH;

        pathText.reset();
        pathText.moveTo(x1, top);
        pathText.lineTo(right, y2);
        pathText.close();

        pathBackground.reset();
        pathBackground.moveTo(x1, 0);
        pathBackground.lineTo(x2, 0);
        pathBackground.lineTo(right, y1);
        pathBackground.lineTo(right, y2-200);
        pathBackground.close();
    }

}
