package com.example.learnandroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnandroid.R;

/**
 * @Auther jian xian si qi
 * @Date 2023/4/12 8:15
 */
public class AlbumSongAdapter extends RecyclerView.Adapter {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlbumSongHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.album_detail_item,null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class AlbumSongHolder extends RecyclerView.ViewHolder{

        public AlbumSongHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
