package com.example.learnandroid.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import androidx.annotation.Nullable;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/18 18:39
 */
public class CircleImage extends androidx.appcompat.widget.AppCompatImageView {
    private Matrix matrix;
    private Paint paint;
    private int mRound;//圆角度数
    private int mRadius;//圆的半径
    private int type;//控件类型
    private final int CIRCLE=0;//圆形
    private final int ROUND=1;//圆角
    public CircleImage(Context context) {
        super(context,null);
    }
    public CircleImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        matrix=new Matrix();
        paint=new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        setShader();
        canvas.drawCircle(getWidth()/2, getHeight()/2, mRadius, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mSize = Math.min(getMeasuredWidth(), getMeasuredHeight());
        mRadius = mSize / 2;
        setMeasuredDimension(mSize, mSize);
    }

    /**
     * 设置着色器
     */
    private void setShader() {
        //获取Drawable
        Drawable resources=getDrawable();
        float scale = 1.0f;//缩放比例
        if (resources instanceof BitmapDrawable) {
            //获取bitmap
            Bitmap bitmap = ((BitmapDrawable) resources).getBitmap();
            if (bitmap == null) return;
            //圆形
            if (type==CIRCLE){
                // 获取bitmap宽高中的小值
                int minBitMap = Math.min(bitmap.getWidth(), bitmap.getHeight());
                //取view宽高中的小值 尽量保证图片内容的显示
                int minValue = Math.min(getWidth(), getHeight());
                //设置半径
                mRadius = minValue / 2;
                //计算缩放比例  一定要*1.0f 因为int之间的计算结果会四舍五入0或1 效果就不美丽了
                scale = minValue * 1.0f / minBitMap;
            }else{
                //比较view和图片宽高比例大的 要让缩放后的图片大于view
                scale = Math.max(getWidth() * 1.0f / bitmap.getWidth(), getHeight()
                        * 1.0f / bitmap.getHeight());
            }
            //设置缩放比例
            matrix.setScale(scale, scale);
            /**
             * 创建着色器 设置着色模式
             * TileMode的取值有三种：
             *  CLAMP 拉伸  REPEAT 重复   MIRROR 镜像
             */
            BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            //设置矩阵
            shader.setLocalMatrix(matrix);
            //设置着色
            paint.setShader(shader);
        }
    }
}