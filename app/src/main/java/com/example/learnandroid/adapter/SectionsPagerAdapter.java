package com.example.learnandroid.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.learnandroid.R;
import com.example.learnandroid.main.AlbmFragment;
import com.example.learnandroid.main.DirFragment;
import com.example.learnandroid.main.PlayerOrderFragment;
import com.example.learnandroid.main.SongFragment;
import com.example.learnandroid.main.SongerFragment;


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES
            = new int[]{
                    R.string.song,
                    R.string.play_order,
                    R.string.dir,
                    R.string.albm,
                    R.string.songer,

    };
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment;
        if (position==0){
            fragment = new SongFragment();
        }else if (position == 1){
            fragment = new PlayerOrderFragment();
        }else if (position == 2){
            fragment = new DirFragment();
        }else if (position == 3){
            fragment = new AlbmFragment();
        }else if (position == 4){
            fragment = new SongerFragment();
        }else {
            fragment = new SongFragment();
        }
        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES.length;
    }
}