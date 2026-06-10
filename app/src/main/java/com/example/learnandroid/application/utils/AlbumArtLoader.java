package com.example.learnandroid.application.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.example.learnandroid.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class AlbumArtLoader {
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(2);
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());
    private static final int DEFAULT_SIZE = 300;

    private AlbumArtLoader() {
    }

    public static void load(ImageView imageView, long albumId) {
        load(imageView, albumId, DEFAULT_SIZE, DEFAULT_SIZE, R.mipmap.default_image2);
    }

    public static void load(ImageView imageView, long albumId, int width, int height, int placeholderRes) {
        if (imageView == null) {
            return;
        }
        Uri albumArtUri = BitmapUtils.getAlbumArtUri(albumId);
        if (albumArtUri == null) {
            imageView.setImageResource(placeholderRes);
            imageView.setTag(R.id.song_pic, null);
            return;
        }

        String requestKey = albumArtUri.toString() + "_" + width + "x" + height;
        imageView.setTag(R.id.song_pic, requestKey);
        imageView.setImageResource(placeholderRes);

        Bitmap cachedBitmap = BitmapUtils.getCachedBitmap(albumArtUri, width, height);
        if (cachedBitmap != null) {
            imageView.setImageBitmap(cachedBitmap);
            return;
        }

        Context context = imageView.getContext().getApplicationContext();
        EXECUTOR.execute(() -> {
            Bitmap bitmap = BitmapUtils.decodeUri(context, albumArtUri, width, height);
            MAIN_HANDLER.post(() -> {
                Object currentTag = imageView.getTag(R.id.song_pic);
                if (!(currentTag instanceof String) || !requestKey.equals(currentTag)) {
                    return;
                }
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    imageView.setImageResource(placeholderRes);
                }
            });
        });
    }
}

