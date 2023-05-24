package com.example.learnandroid.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ThumbnailUtils;
import android.os.Environment;

import java.io.IOException;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/17 19:35
 */
public class BitMapUtils {

    /**
     * 旧时光特效
     *
     * @param bmp
     *            原图片
     * @return 旧时光特效图片
     */
    public static Bitmap oldTimeFilter(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int pixColor = 0;
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                pixColor = pixels[width * i + k];
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                newR = (int) (0.393 * pixR + 0.769 * pixG + 0.189 * pixB);
                newG = (int) (0.349 * pixR + 0.686 * pixG + 0.168 * pixB);
                newB = (int) (0.272 * pixR + 0.534 * pixG + 0.131 * pixB);
                int newColor = Color.argb(255, newR > 255 ? 255 : newR, newG > 255 ? 255 : newG, newB > 255 ? 255
                        : newB);
                pixels[width * i + k] = newColor;
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 暖意特效
     *
     * @param bmp
     *            原图片
     * @param centerX
     *            光源横坐标
     * @param centerY
     *            光源纵坐标
     * @return 暖意特效图片
     */
    public static Bitmap warmthFilter(Bitmap bmp, int centerX, int centerY) {
        final int width = bmp.getWidth();
        final int height = bmp.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;

        int pixColor = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;
        int radius = Math.min(centerX, centerY);

        final float strength = 150F; // 光照强度 100~150
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int pos = 0;
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                pos = i * width + k;
                pixColor = pixels[pos];

                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);

                newR = pixR;
                newG = pixG;
                newB = pixB;

                // 计算当前点到光照中心的距离，平面座标系中求两点之间的距离
                int distance = (int) (Math.pow((centerY - i), 2) + Math.pow(centerX - k, 2));
                if (distance < radius * radius) {
                    // 按照距离大小计算增加的光照值
                    int result = (int) (strength * (1.0 - Math.sqrt(distance) / radius));
                    newR = pixR + result;
                    newG = pixG + result;
                    newB = pixB + result;
                }

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));

                pixels[pos] = Color.argb(255, newR, newG, newB);
            }
        }

        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * 根据饱和度、色相、亮度调整图片
     *
     * @param bm
     *            原图片
     * @param saturation
     *            饱和度
     * @param hue
     *            色相
     * @param lum
     *            亮度
     * @return 处理后的图片
     */
    public static Bitmap handleImage(Bitmap bm, int saturation, int hue, int lum) {
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        ColorMatrix mLightnessMatrix = new ColorMatrix();
        ColorMatrix mSaturationMatrix = new ColorMatrix();
        ColorMatrix mHueMatrix = new ColorMatrix();
        ColorMatrix mAllMatrix = new ColorMatrix();
        float mSaturationValue = saturation * 1.0F / 127;
        float mHueValue = hue * 1.0F / 127;
        float mLumValue = (lum - 127) * 1.0F / 127 * 180;
        mHueMatrix.reset();
        mHueMatrix.setScale(mHueValue, mHueValue, mHueValue, 1);

        mSaturationMatrix.reset();
        mSaturationMatrix.setSaturation(mSaturationValue);
        mLightnessMatrix.reset();

        mLightnessMatrix.setRotate(0, mLumValue);
        mLightnessMatrix.setRotate(1, mLumValue);
        mLightnessMatrix.setRotate(2, mLumValue);

        mAllMatrix.reset();
        mAllMatrix.postConcat(mHueMatrix);
        mAllMatrix.postConcat(mSaturationMatrix);
        mAllMatrix.postConcat(mLightnessMatrix);

        paint.setColorFilter(new ColorMatrixColorFilter(mAllMatrix));
        canvas.drawBitmap(bm, 0, 0, paint);
        return bmp;
    }

    /**
     * 添加图片外边框
     *
     * @param context
     *            上下文
     * @param bm
     *            原图片
     * @param frameName
     *            边框名称
     * @return 带有边框的图片
     */
//    public static Bitmap combinateFrame(Context context, Bitmap bm, String frameName) {
//        // 原图片的宽高
//        int imageWidth = bm.getWidth();
//        int imageHeight = bm.getHeight();
//
//        // 边框
//        Bitmap leftUp = decodeBitmap(context, frameName, 0);
//        Bitmap leftDown = decodeBitmap(context, frameName, 2);
//        Bitmap rightDown = decodeBitmap(context, frameName, 4);
//        Bitmap rightUp = decodeBitmap(context, frameName, 6);
//        Bitmap top = decodeBitmap(context, frameName, 7);
//        Bitmap down = decodeBitmap(context, frameName, 3);
//        Bitmap left = decodeBitmap(context, frameName, 1);
//        Bitmap right = decodeBitmap(context, frameName, 5);
//
//        Bitmap newBitmap = null;
//        Canvas canvas = null;
//
//        // 判断大小图片的宽高
//        int judgeWidth = 0;
//        int judgeHeight = 0;
//        if ("frame7".equals(frameName)) {
//            judgeWidth = leftUp.getWidth() + rightUp.getWidth() + top.getWidth() * 5;
//            judgeHeight = leftUp.getHeight() + leftDown.getHeight() + left.getHeight() * 5;
//        } else if ("frame10".equals(frameName)) {
//            judgeWidth = leftUp.getWidth() + rightUp.getWidth() + top.getWidth() * 5;
//            judgeHeight = leftUp.getHeight() + leftDown.getHeight() + left.getHeight() * 10;
//        } else {
//            judgeWidth = leftUp.getWidth() + rightUp.getWidth() + top.getWidth();
//            judgeHeight = leftUp.getHeight() + leftDown.getHeight() + left.getHeight();
//        }
//        // 内边框
//        if (imageWidth > judgeWidth && imageHeight > judgeHeight) {
//            // 重新定义一个bitmap
//            newBitmap = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888);
//            canvas = new Canvas(newBitmap);
//            Paint paint = new Paint();
//            // 画原图
//            canvas.drawBitmap(bm, 0, 0, paint);
//            // 上空余宽度
//            int topWidth = imageWidth - leftUp.getWidth() - rightUp.getWidth();
//            // 上空余填充个数
//            int topCount = (int) Math.ceil(topWidth * 1.0f / top.getWidth());
//            for (int i = 0; i < topCount; i++) {
//                canvas.drawBitmap(top, leftUp.getWidth() + top.getWidth() * i, 0, paint);
//            }
//            // 下空余宽度
//            int downWidth = imageWidth - leftDown.getWidth() - rightDown.getWidth();
//            // 下空余填充个数
//            int downCount = (int) Math.ceil(downWidth * 1.0f / down.getWidth());
//            for (int i = 0; i < downCount; i++) {
//                canvas.drawBitmap(down, leftDown.getWidth() + down.getWidth() * i, imageHeight - down.getHeight(),
//                        paint);
//            }
//            // 左空余高度
//            int leftHeight = imageHeight - leftUp.getHeight() - leftDown.getHeight();
//            // 左空余填充个数
//            int leftCount = (int) Math.ceil(leftHeight * 1.0f / left.getHeight());
//            for (int i = 0; i < leftCount; i++) {
//                canvas.drawBitmap(left, 0, leftUp.getHeight() + left.getHeight() * i, paint);
//            }
//            // 右空余高度
//            int rightHeight = imageHeight - rightUp.getHeight() - rightDown.getHeight();
//            // 右空余填充个数
//            int rightCount = (int) Math.ceil(rightHeight * 1.0f / right.getHeight());
//            for (int i = 0; i < rightCount; i++) {
//                canvas.drawBitmap(right, imageWidth - right.getWidth(), rightUp.getHeight() + right.getHeight() * i,
//                        paint);
//            }
//            // 画左上角
//            canvas.drawBitmap(leftUp, 0, 0, paint);
//            // 画左下角
//            canvas.drawBitmap(leftDown, 0, imageHeight - leftDown.getHeight(), paint);
//            // 画右下角
//            canvas.drawBitmap(rightDown, imageWidth - rightDown.getWidth(), imageHeight - rightDown.getHeight(), paint);
//            // 画右上角
//            canvas.drawBitmap(rightUp, imageWidth - rightUp.getWidth(), 0, paint);
//
//        } else {
//            if ("frame7".equals(frameName)) {
//                imageWidth = leftUp.getWidth() + top.getWidth() * 5 + rightUp.getWidth();
//                imageHeight = leftUp.getHeight() + left.getHeight() * 5 + leftDown.getHeight();
//            } else if ("frame10".equals(frameName)) {
//                imageWidth = leftUp.getWidth() + top.getWidth() * 5 + rightUp.getWidth();
//                imageHeight = leftUp.getHeight() + left.getHeight() * 10 + leftDown.getHeight();
//            } else {
//                imageWidth = leftUp.getWidth() + top.getWidth() + rightUp.getWidth();
//                imageHeight = leftUp.getHeight() + left.getHeight() + leftDown.getHeight();
//            }
//            newBitmap = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888);
//            canvas = new Canvas(newBitmap);
//            Paint paint = new Paint();
//            int newImageWidth = imageWidth - left.getWidth() - right.getWidth() + 5;
//            int newImageHeight = imageHeight - top.getHeight() - down.getHeight() + 5;
//            bm = Bitmap.createScaledBitmap(bm, newImageWidth, newImageHeight, true);
//            canvas.drawBitmap(bm, left.getWidth(), top.getHeight(), paint);
//            if ("frame7".equals(frameName)) {
//
//                for (int i = 0; i < 5; i++) {
//                    canvas.drawBitmap(top, leftUp.getWidth() + top.getWidth() * i, 0, paint);
//                }
//
//                for (int i = 0; i < 5; i++) {
//                    canvas.drawBitmap(left, 0, leftUp.getHeight() + left.getHeight() * i, paint);
//                }
//
//                for (int i = 0; i < 5; i++) {
//                    canvas.drawBitmap(right, imageWidth - right.getWidth(),
//                            rightUp.getHeight() + right.getHeight() * i, paint);
//                }
//
//                for (int i = 0; i < 5; i++) {
//                    canvas.drawBitmap(down, leftDown.getWidth() + down.getWidth() * i, imageHeight - down.getHeight(),
//                            paint);
//                }
//                canvas.drawBitmap(leftUp, 0, 0, paint);
//                canvas.drawBitmap(rightUp, leftUp.getWidth() + top.getWidth() * 5, 0, paint);
//                canvas.drawBitmap(leftDown, 0, leftUp.getHeight() + left.getHeight() * 5, paint);
//                canvas.drawBitmap(rightDown, imageWidth - rightDown.getWidth(), rightUp.getHeight() + right.getHeight()
//                        * 5, paint);
//
//            } else if ("frame10".equals(frameName)) {
//                for (int i = 0; i < 5; i++) {
//                    canvas.drawBitmap(top, leftUp.getWidth() + top.getWidth() * i, 0, paint);
//                }
//
//                for (int i = 0; i < 10; i++) {
//                    canvas.drawBitmap(left, 0, leftUp.getHeight() + left.getHeight() * i, paint);
//                }
//
//                for (int i = 0; i < 10; i++) {
//                    canvas.drawBitmap(right, imageWidth - right.getWidth(),
//                            rightUp.getHeight() + right.getHeight() * i, paint);
//                }
//
//                for (int i = 0; i < 5; i++) {
//                    canvas.drawBitmap(down, leftDown.getWidth() + down.getWidth() * i, imageHeight - down.getHeight(),
//                            paint);
//                }
//                canvas.drawBitmap(leftUp, 0, 0, paint);
//                canvas.drawBitmap(rightUp, leftUp.getWidth() + top.getWidth() * 5, 0, paint);
//                canvas.drawBitmap(leftDown, 0, leftUp.getHeight() + left.getHeight() * 10, paint);
//                canvas.drawBitmap(rightDown, imageWidth - rightDown.getWidth(), rightUp.getHeight() + right.getHeight()
//                        * 10, paint);
//            } else {
//                canvas.drawBitmap(leftUp, 0, 0, paint);
//                canvas.drawBitmap(top, leftUp.getWidth(), 0, paint);
//                canvas.drawBitmap(rightUp, leftUp.getWidth() + top.getWidth(), 0, paint);
//                canvas.drawBitmap(left, 0, leftUp.getHeight(), paint);
//                canvas.drawBitmap(leftDown, 0, leftUp.getHeight() + left.getHeight(), paint);
//                canvas.drawBitmap(right, imageWidth - right.getWidth(), rightUp.getHeight(), paint);
//                canvas.drawBitmap(rightDown, imageWidth - rightDown.getWidth(),
//                        rightUp.getHeight() + right.getHeight(), paint);
//                canvas.drawBitmap(down, leftDown.getWidth(), imageHeight - down.getHeight(), paint);
//            }
//        }
//        // 回收
//        leftUp.recycle();
//        leftUp = null;
//        leftDown.recycle();
//        leftDown = null;
//        rightDown.recycle();
//        rightDown = null;
//        rightUp.recycle();
//        rightUp = null;
//        top.recycle();
//        top = null;
//        down.recycle();
//        down = null;
//        left.recycle();
//        left = null;
//        right.recycle();
//        right = null;
//        canvas.save(Canvas.ALL_SAVE_FLAG);
//        canvas.restore();
//        return newBitmap;
//    }

    /**
     * 获取边框图片
     *
     * @param context
     *            上下文
     * @param frameName
     *            边框名称
     * @param position
     *            边框的类型
     * @return 边框图片
     */
    private static Bitmap decodeBitmap(Context context, String frameName, int position) {
        try {
            switch (position) {
                case 0:
                    return BitmapFactory.decodeStream(context.getAssets().open("frames/" + frameName + "/leftup.png"));
                case 1:
                    return BitmapFactory.decodeStream(context.getAssets().open("frames/" + frameName + "/left.png"));
                case 2:
                    return BitmapFactory.decodeStream(context.getAssets().open("frames/" + frameName + "/leftdown.png"));
                case 3:
                    return BitmapFactory.decodeStream(context.getAssets().open("frames/" + frameName + "/down.png"));
                case 4:
                    return BitmapFactory.decodeStream(context.getAssets().open("frames/" + frameName + "/rightdown.png"));
                case 5:
                    return BitmapFactory.decodeStream(context.getAssets().open("frames/" + frameName + "/right.png"));
                case 6:
                    return BitmapFactory.decodeStream(context.getAssets().open("frames/" + frameName + "/rightup.png"));
                case 7:
                    return BitmapFactory.decodeStream(context.getAssets().open("frames/" + frameName + "/up.png"));
                default:
                    return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 添加内边框
     *
     * @param bm
     *            原图片
     * @param frame
     *            内边框图片
     * @return 带有边框的图片
     */


    /**
     * 创建一个缩放的图片
     *
     * @param path
     *            图片地址
     * @param w
     *            图片宽度
     * @param h
     *            图片高度
     * @return 缩放后的图片
     */
    public static Bitmap createBitmap(String path, int w, int h) {
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            // 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
            BitmapFactory.decodeFile(path, opts);
            int srcWidth = opts.outWidth;// 获取图片的原始宽度
            int srcHeight = opts.outHeight;// 获取图片原始高度
            int destWidth = 0;
            int destHeight = 0;
            // 缩放的比例
            double ratio = 0.0;
            if (srcWidth < w || srcHeight < h) {
                ratio = 0.0;
                destWidth = srcWidth;
                destHeight = srcHeight;
            } else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
                ratio = (double) srcWidth / w;
                destWidth = w;
                destHeight = (int) (srcHeight / ratio);
            } else {
                ratio = (double) srcHeight / h;
                destHeight = h;
                destWidth = (int) (srcWidth / ratio);
            }
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            // 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
            newOpts.inSampleSize = (int) ratio + 1;
            // inJustDecodeBounds设为false表示把图片读进内存中
            newOpts.inJustDecodeBounds = false;
            // 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
            newOpts.outHeight = destHeight;
            newOpts.outWidth = destWidth;
            // 获取缩放后图片
            return BitmapFactory.decodeFile(path, newOpts);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    public static Bitmap createBitmap2(Context context, int id, int w, int h) {
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            // 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
            BitmapFactory.decodeResource(context.getResources(), id, opts);
            int srcWidth = opts.outWidth;// 获取图片的原始宽度
            int srcHeight = opts.outHeight;// 获取图片原始高度
            int destWidth = 0;
            int destHeight = 0;
            // 缩放的比例
            double ratio = 0.0;
            if (srcWidth < w || srcHeight < h) {
                ratio = 0.0;
                destWidth = srcWidth;
                destHeight = srcHeight;
            } else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
                ratio = (double) srcWidth / w;
                destWidth = w;
                destHeight = (int) (srcHeight / ratio);
            } else {
                ratio = (double) srcHeight / h;
                destHeight = h;
                destWidth = (int) (srcWidth / ratio);
            }
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            // 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
            newOpts.inSampleSize = (int) ratio + 1;
            // inJustDecodeBounds设为false表示把图片读进内存中
            newOpts.inJustDecodeBounds = false;
            // 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
            newOpts.outHeight = destHeight;
            newOpts.outWidth = destWidth;
            // 获取缩放后图片
            // return BitmapFactory.decodeFile(path, newOpts);
            return BitmapFactory.decodeResource(context.getResources(), id, newOpts);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }
}
