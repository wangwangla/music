package com.example.learnandroid.adapter;

import android.graphics.Bitmap;
import android.net.Uri;
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
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.constant.MusicManager;
import com.example.learnandroid.utils.BitmapUtils;
import com.example.learnandroid.utils.ShareUtils;
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
        Uri albumArtUri = BitmapUtils.getAlbumArtUri(musicBean.getAlbumId());
        Bitmap bitmap = BitmapUtils.decodeUri(MusicApplication.getMusicContent(),albumArtUri,300,300);
        holder.albumSongPic.setImageBitmap(bitmap);

        holder.albumDetailMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicApplication.getMusicContent().setTheme(R.style.Theme_LearnAndroid);
                final PopupMenu popupMenu = new PopupMenu(MusicApplication.getMusicContent(), v);
                popupMenu.inflate(R.menu.popup_song);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.play:{
                                if (musicBean.getId() == MusicManager.getId()){
                                    if (!MusicManager.isPlaying()){
                                        MusicManager.continuePlay();
                                    }
                                }else {
                                    //播放
                                    MusicManager.setData(musicBean.getId());
                                    MusicManager.setDataAndplay();
                                }
                                break;
                            }
                            case R.id.play_next:{
                                ShareUtils.share(v.getContext(), musicBean.getId());
                                break;
                            }
//                            case R.id.add_to_playlist:{
//                                break;
//                            }
//                            case R.id.add_to_album:{
//                                break;
//                            }
//                            case R.id.add_to_aritist:{
//                                break;
//                            }
//                            case R.id.delete_from_device:{
//                                break;
//                            }
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
            MusicManager.setDataAndplay(musicBean.getId());
        }
    }
}
