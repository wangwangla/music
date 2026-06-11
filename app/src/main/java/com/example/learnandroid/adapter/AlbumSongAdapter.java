  package com.example.learnandroid.adapter;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnandroid.R;
import com.example.learnandroid.application.MusicApplication;
import com.example.learnandroid.application.utils.AlbumArtLoader;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.constant.MusicManager;
import com.example.learnandroid.application.utils.ShareUtils;
import com.example.learnandroid.application.utils.TimeUtils;

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
                .inflate(R.layout.album_detail_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumSongHolder holder, int position) {
        MusicBean musicBean = musicBeans.get(position);
        holder.albumDetailSongName.setText(musicBean.getTitle());
        holder.albumDetailSongTime.setText(TimeUtils.longToTime(musicBean.getDuration()));
        AlbumArtLoader.load(holder.albumSongPic, musicBean.getAlbumId(), 300, 300, R.mipmap.default_image2);
//        从 Android Gradle Plugin 8.x 开始： R.java → 不再生成 final 常量
        holder.albumDetailMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAdapterPosition();
                if (adapterPosition == RecyclerView.NO_POSITION) {
                    return;
                }
                MusicBean currentMusicBean = musicBeans.get(adapterPosition);
                MusicApplication.getMusicContent().setTheme(R.style.Theme_LearnAndroid);
                final PopupMenu popupMenu = new PopupMenu(MusicApplication.getMusicContent(), v);
                popupMenu.inflate(R.menu.popup_song);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (R.id.play == item.getItemId()){
                            if (currentMusicBean.getId() == MusicManager.getId()){
                                if (!MusicManager.isPlaying()){
                                    MusicManager.continuePlay();
                                }
                            }else {
                                //播放
                                syncAlbumQueue();
                                MusicManager.setDataAndplay(adapterPosition);
                            }
                        }else if (item.getItemId() == R.id.play_next){
                            ShareUtils.share(v.getContext(), currentMusicBean.getId());
                        }
                        return false;
                    }
                });
                popupMenu.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return musicBeans.size();
    }

    @Override
    public void onViewRecycled(@NonNull AlbumSongHolder holder) {
        super.onViewRecycled(holder);
        AlbumArtLoader.clear(holder.albumSongPic, R.mipmap.default_image2);
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
            int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) {
                return;
            }
            syncAlbumQueue();
            MusicManager.setDataAndplay(position);
        }
    }

    private void syncAlbumQueue() {
        MusicManager.setSongList(new ArrayList<>(musicBeans));
    }
}
