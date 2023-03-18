package com.example.learnandroid.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.learnandroid.R;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/17 11:51
 */
public class CirBitMap extends View {
    private  BitmapShader bitmapShader = null;
    private Bitmap bitmap = null;
    private Paint paint = null;
    private float viewWidth;
    private float viewHeight;

    public CirBitMap(Context context) {
        super(context);
        initView();
    }

    public CirBitMap(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CirBitMap(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public CirBitMap(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private void initView() {
        //得到图像
        bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.app_widget_big)).getBitmap();
        //构造渲染器BitmapShader
        bitmapShader = new BitmapShader(bitmap,Shader.TileMode.MIRROR,Shader.TileMode.REPEAT);
//        BitmapShader bitmapShader=new BitmapShader(bitmap, Shader.TileMode.MIRROR,Shader.TileMode.MIRROR);
        paint = new Paint();
        viewWidth = 10;
        viewHeight = 10;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setShader(bitmapShader);
        //画矩形图
        canvas.drawRect(new RectF(0,0,viewWidth,viewHeight),paint);
    }
}