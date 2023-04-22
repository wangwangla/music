package com.example.learnandroid.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.learnandroid.R;
import com.example.learnandroid.main.AlbmFragment;
import com.example.learnandroid.main.SongFragment;
import com.example.learnandroid.main.ArtistFragment;

/**
 * @Auther jian xian si qi
 * @Date 16:24
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private final Context mContext;

    @StringRes
    private static final int[] TAB_TITLES
            = new int[]{
                    R.string.song,
//                    R.string.play_order,
                    R.string.albm,
                    R.string.songer,

    };

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        if (position==0){
            fragment = new SongFragment();
//        }else if (position == 1){
//            fragment = new PlayerOrderFragment();
        }else if (position == 1){
            fragment = new AlbmFragment();
        }else if (position == 2){
            fragment = new ArtistFragment();
        }else {
            fragment = new SongFragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES.length;
    }

    //设置标题
    @Override
    public CharSequence getPageTitle(int position) {
        String string = mContext.getResources().getString(TAB_TITLES[position]);
        SpannableString spannableString = new SpannableString(string);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.BLACK);
        spannableString.setSpan(colorSpan, 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

}