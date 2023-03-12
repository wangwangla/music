package com.example.learnandroid.adapter;

import android.content.Context;
import android.os.Handler;

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
 * @Auther jian xian si qi
 * @Date 16:24
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private final Context mContext;
    private Handler handler;
    @StringRes
    private static final int[] TAB_TITLES
            = new int[]{
                    R.string.song,
                    R.string.play_order,
                    R.string.albm,
                    R.string.songer,

    };

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }


    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment;
        if (position==0){
            fragment = new SongFragment(handler);
        }else if (position == 1){
            fragment = new PlayerOrderFragment(handler);
//        }else if (position == 2){
//            fragment = new DirFragment();
        }else if (position == 2){
            fragment = new AlbmFragment();
        }else if (position == 3){
            fragment = new SongerFragment();
        }else {
            fragment = new SongFragment(handler);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES.length;
    }
}