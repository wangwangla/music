package com.example.learnandroid.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/18 12:28
 */
public class ColorFilterView extends androidx.appcompat.widget.AppCompatImageView {
    private Shader shader;
    private Paint paint;
    private ColorFilter filter;

    public ColorFilterView(@NonNull Context context) {
        super(context);
        this.init();
    }

    public ColorFilterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public ColorFilterView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    public void init(){
        Drawable drawable = getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint = new Paint();
        filter = new LightingColorFilter(Color.RED,Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        paint.setShader(shader);
        paint.setColorFilter(filter);
        canvas.drawCircle(250, 250, 250, paint);
    }
}
