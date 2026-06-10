package com.example.learnandroid.application.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.example.learnandroid.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class AlbumArtLoader {
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(2);
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());
    private static final int DEFAULT_SIZE = 300;
    private static final Object REQUEST_LOCK = new Object();
    private static final Map<String, List<RequestTarget>> IN_FLIGHT_REQUESTS = new HashMap<>();

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
        String requestKey = buildRequestKey(albumArtUri, width, height);
        imageView.setTag(R.id.album_art_request_tag, requestKey);
        imageView.setImageResource(placeholderRes);

        Bitmap cachedBitmap = BitmapUtils.getCachedBitmap(albumArtUri, width, height);
        if (cachedBitmap != null) {
            imageView.setImageBitmap(cachedBitmap);
            return;
        }

        RequestTarget requestTarget = new RequestTarget(imageView, placeholderRes, requestKey);
        if (enqueueIfRunning(requestKey, requestTarget)) {
            return;
        }

        Context context = imageView.getContext().getApplicationContext();
        EXECUTOR.execute(() -> {
            Bitmap bitmap = BitmapUtils.decodeUri(context, albumArtUri, width, height);
            dispatchResult(requestKey, bitmap);
        });
    }

    private static boolean enqueueIfRunning(String requestKey, RequestTarget requestTarget) {
        synchronized (REQUEST_LOCK) {
            List<RequestTarget> requestTargets = IN_FLIGHT_REQUESTS.get(requestKey);
            if (requestTargets != null) {
                requestTargets.add(requestTarget);
                return true;
            }
            requestTargets = new ArrayList<>();
            requestTargets.add(requestTarget);
            IN_FLIGHT_REQUESTS.put(requestKey, requestTargets);
            return false;
        }
    }

    private static void dispatchResult(String requestKey, Bitmap bitmap) {
        List<RequestTarget> requestTargets;
        synchronized (REQUEST_LOCK) {
            requestTargets = IN_FLIGHT_REQUESTS.remove(requestKey);
        }
        if (requestTargets == null || requestTargets.isEmpty()) {
            return;
        }
        MAIN_HANDLER.post(() -> {
            Iterator<RequestTarget> iterator = requestTargets.iterator();
            while (iterator.hasNext()) {
                RequestTarget requestTarget = iterator.next();
                ImageView imageView = requestTarget.imageViewRef.get();
                if (imageView == null) {
                    continue;
                }
                Object currentTag = imageView.getTag(R.id.album_art_request_tag);
                if (!(currentTag instanceof String) || !requestTarget.requestKey.equals(currentTag)) {
                    continue;
                }
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    imageView.setImageResource(requestTarget.placeholderRes);
                }
            }
        });
    }

    private static String buildRequestKey(Uri albumArtUri, int width, int height) {
        return albumArtUri + "_" + width + "x" + height;
    }

    public static void clear(ImageView imageView, int placeholderRes) {
        if (imageView == null) {
            return;
        }
        imageView.setTag(R.id.album_art_request_tag, null);
        imageView.setImageResource(placeholderRes);
    }

    private static class RequestTarget {
        private final WeakReference<ImageView> imageViewRef;
        private final int placeholderRes;
        private final String requestKey;

        private RequestTarget(ImageView imageView, int placeholderRes, String requestKey) {
            this.imageViewRef = new WeakReference<>(imageView);
            this.placeholderRes = placeholderRes;
            this.requestKey = requestKey;
        }
    }
}

