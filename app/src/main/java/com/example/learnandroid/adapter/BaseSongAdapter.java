package com.example.learnandroid.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.learnandroid.bean.MusicBean;
import com.google.android.gms.cast.framework.CastSession;

import java.util.List;

/**
 * @Auther jian xian si qi
 * @Date 2023/4/8 14:10
 */

public class BaseSongAdapter<V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {

    @Override
    public V onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(V holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        public ItemHolder(View view) {
            super(view);
        }

    }
    public void removeSongAt(int i){}

    public void updateDataSet(List<MusicBean> arraylist) {}

}
