package com.example.learnandroid.application.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore;

/**
 * @Auther jian xian si qi
 * @Date 2023/4/22 15:55
 */
public class ShareUtils {
    public static void share(Context context,long id){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("audio/*");
        share.putExtra(Intent.EXTRA_STREAM, getSongUri(context, id));
        context.startActivity(Intent.createChooser(share, "Share"));
    }

    public static Uri getSongUri(Context context, long id) {
        final String[] projection = new String[]{
                BaseColumns._ID, MediaStore.MediaColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM_ID
        };
        final StringBuilder selection = new StringBuilder();
        selection.append(BaseColumns._ID + " IN (");
        selection.append(id);
        selection.append(")");
        final Cursor c = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection.toString(),
                null, null);

        if (c == null) {
            return null;
        }
        c.moveToFirst();


        try {

            Uri uri = Uri.parse(c.getString(1));
            c.close();

            return uri;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
