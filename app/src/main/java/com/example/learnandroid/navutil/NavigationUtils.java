package com.example.learnandroid.navutil;

import android.app.Activity;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.learnandroid.R;
import com.example.learnandroid.main.AlbumDetailActivity;
import com.example.learnandroid.main.ArtistDetailActivity;
import com.example.learnandroid.main.GeciFragment;

/**
 * @Auther jian xian si qi
 * @Date 2023/4/20 21:58
 */
public class NavigationUtils {

    public static void navigateToAlbum(Activity context, long albumID) {
        Intent intent = new Intent(context, AlbumDetailActivity.class);
        intent.putExtra(AlbumDetailActivity.EXTRA_ALBUM_ID, albumID);
        context.startActivity(intent);
    }

    public static void navigateToArtist(Activity context, long artistID) {
        Intent intent = new Intent(context, ArtistDetailActivity.class);
        intent.putExtra(ArtistDetailActivity.EXTRA_ARTIST_ID, artistID);
        context.startActivity(intent);
    }


    public static void navigateToGeci(Activity context, long musicId) {
        FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        Fragment fragment;
        fragment = GeciFragment.newInstance(context, musicId, false);
        transaction.replace(R.id.lrcy_view, fragment);
        transaction.addToBackStack(null).commit();
    }

    public static void repeatFragment(){

    }
}