package com.example.learnandroid.navutil;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.util.Pair;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.learnandroid.R;
import com.example.learnandroid.main.AlbumDetailFragment;
import com.example.learnandroid.main.ArtistDetailFragment;
import com.example.learnandroid.main.GeciFragment;

/**
 * @Auther jian xian si qi
 * @Date 2023/4/20 21:58
 */
public class NavigationUtils {

    public static void navigateToAlbum(Activity context, long albumID) {
        FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        Fragment fragment;
        fragment = AlbumDetailFragment.newInstance(context,albumID, false);
        transaction.add(R.id.frame_content, fragment);
        transaction.addToBackStack(null).commit();
    }

    public static void navigateToArtist(Activity context, long artistID) {
        FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        Fragment fragment;
        fragment = ArtistDetailFragment.newInstance(context, artistID, false);
        transaction.replace(R.id.frame_content, fragment);
        transaction.addToBackStack(null).commit();
    }


    public static void navigateToGeci(Activity context, long musicId) {
        FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
        Fragment fragment;
        fragment = GeciFragment.newInstance(context, musicId, false);
        transaction.replace(R.id.lrcy_view, fragment);
        transaction.addToBackStack(null).commit();
    }
}