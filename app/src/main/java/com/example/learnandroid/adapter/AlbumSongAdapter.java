package com.example.learnandroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnandroid.R;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.constant.MusicManager;
import com.example.learnandroid.utils.TimeUtils;

import java.util.ArrayList;

/**
 * @Auther jian xian si qi
 * @Date 2023/4/12 8:15
 */
public class AlbumSongAdapter extends RecyclerView.Adapter<AlbumSongAdapter.AlbumSongHolder> {
    private ArrayList<MusicBean> musicBeans;
    public AlbumSongAdapter(ArrayList<MusicBean> songsForAlbum) {
        this.musicBeans = songsForAlbum;
    }

    @NonNull
    @Override
    public AlbumSongAdapter.AlbumSongHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlbumSongHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_detail_item,null));
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumSongHolder holder, int position) {
        MusicBean musicBean = musicBeans.get(position);
        holder.albumDetailSongName.setText(musicBean.getTitle());
        holder.albumDetailSongTime.setText(TimeUtils.longToTime(musicBean.getDuration()));
    }

    @Override
    public int getItemCount() {
        return musicBeans.size();
    }

    class AlbumSongHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView albumSongPic;
        TextView albumDetailSongName;
        TextView albumDetailSongTime;
        ImageView albumDetailMore;

        public AlbumSongHolder(@NonNull View itemView) {
            super(itemView);
            albumSongPic           = itemView.findViewById(R.id.album_song_pic);
            albumDetailSongName    = itemView.findViewById(R.id.album_detail_song_name);
            albumDetailSongTime    = itemView.findViewById(R.id.album_detail_song_time);
            albumDetailMore        = itemView.findViewById(R.id.album_detail_more);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            MusicBean musicBean = musicBeans.get(getPosition());
            boolean b = MusicManager.setData(musicBean.getId());
            if (b){
                MusicManager.play();
            }else {

            }
        }
    }
}
