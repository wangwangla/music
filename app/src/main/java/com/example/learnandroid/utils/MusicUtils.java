package com.example.learnandroid.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Log;

import com.example.learnandroid.R;

import java.io.IOException;
import java.io.InputStream;

public class MusicUtils {

    public static Uri getAlbumArtUri(long albumId) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), albumId);
    }

    /**********获取歌曲专辑图片*************/

        /**
         * 读取一个缩放后的图片，限定图片大小，避免OOM
         *
         * @param uri       图片uri，支持“file://”、“content://”
         * @param maxWidth  最大允许宽度
         * @param maxHeight 最大允许高度
         * @return 返回一个缩放后的Bitmap，失败则返回null
         */
        public static Bitmap decodeUri(Context context, Uri uri, int maxWidth, int maxHeight) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true; //只读取图片尺寸
            resolveUri(context, uri, options);

//            //计算实际缩放比例
//            int scale = 1;
//            for (int i = 0; i < Integer.MAX_VALUE; i++) {
//                if ((options.outWidth / scale > maxWidth &&
//                        options.outWidth / scale > maxWidth * 1.4) ||
//                        (options.outHeight / scale > maxHeight &&
//                                options.outHeight / scale > maxHeight * 1.4)) {
//                    scale++;
//                } else {
//                    break;
//                }
//            }

//            options.inSampleSize = scale;
            options.inJustDecodeBounds = false;//读取图片内容
            options.inPreferredConfig = Bitmap.Config.RGB_565; //根据情况进行修改
            Bitmap bitmap = null;
            try {
                bitmap = resolveUriForBitmap(context, uri, options);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        public static void resolveUri(Context context, Uri uri, BitmapFactory.Options options) {
            if (uri == null) {
                return;
            }
            String scheme = uri.getScheme();
            if (ContentResolver.SCHEME_CONTENT.equals(scheme) ||
                    ContentResolver.SCHEME_FILE.equals(scheme)) {
                InputStream stream = null;
                try {
                    stream = context.getContentResolver().openInputStream(uri);
                    BitmapFactory.decodeStream(stream, null, options);
                } catch (Exception e) {
                    Log.w("resolveUri", "Unable to open content: " + uri, e);
                } finally {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            Log.w("resolveUri", "Unable to close content: " + uri, e);
                        }
                    }
                }
            } else if (ContentResolver.SCHEME_ANDROID_RESOURCE.equals(scheme)) {
                Log.w("resolveUri", "Unable to close content: " + uri);
            } else {
                Log.w("resolveUri", "Unable to close content: " + uri);
            }
        }

        private static Bitmap resolveUriForBitmap(Context context, Uri uri, BitmapFactory.Options options) {
            if (uri == null) {
                return null;
            }

            Bitmap bitmap = null;
            String scheme = uri.getScheme();
            if (ContentResolver.SCHEME_CONTENT.equals(scheme) ||
                    ContentResolver.SCHEME_FILE.equals(scheme)) {
                InputStream stream = null;
                try {
                    stream = context.getContentResolver().openInputStream(uri);
                    bitmap = BitmapFactory.decodeStream(stream, null, options);
                } catch (Exception e) {
                    Bitmap albumPicture = BitmapFactory.decodeResource(context.getResources(), R.drawable.img);
                    //music1是从歌曲文件读取不出来专辑图片时用来代替的默认专辑图片
                    int width = albumPicture.getWidth();
                    int height = albumPicture.getHeight();
                    //Log.w("DisplayActivity","width = "+width+" height = "+height);
                    // 创建操作图片用的Matrix对象
                    Matrix matrix = new Matrix();
                    // 计算缩放比例
                    float sx = ((float) 120 / width);
                    float sy = ((float) 120 / height);
                    // 设置缩放比例
                    matrix.postScale(sx, sy);
                    // 建立新的bitmap，其内容是对原bitmap的缩放后的图
                    bitmap = Bitmap.createBitmap(albumPicture, 0, 0, width, height, matrix, false);
                    Log.w("resolveUriForBitmap", "Unable to open content: " + uri, e);
                } finally {
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            Log.w("resolveUriForBitmap", "Unable to close content: " + uri, e);
                        }
                    }
                }
            } else if (ContentResolver.SCHEME_ANDROID_RESOURCE.equals(scheme)) {
                Log.w("resolveUriForBitmap", "Unable to close content: " + uri);
            } else {
                Log.w("resolveUriForBitmap", "Unable to close content: " + uri);
            }

            return bitmap;
        }
    }