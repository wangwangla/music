package com.example.learnandroid.adapter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnandroid.R;
import com.example.learnandroid.application.MusicApplication;
import com.example.learnandroid.bean.Album;
import com.example.learnandroid.sign.SignListener;
import com.example.learnandroid.utils.BitmapUtils;

import java.util.ArrayList;

/**
 * @Auther jian xian si qi
 * @Date 2023/4/18 23:03
 */
public class ArtistAlbumAdpater extends RecyclerView.Adapter<ArtistAlbumAdpater.ArtistAlbumHolder> {

    private ArrayList<Album> albums;

    public ArtistAlbumAdpater(ArrayList<Album> albums){
        this.albums = albums;
    }

    @NonNull
    @Override
    public ArtistAlbumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_album_view, null);
        ArtistAlbumHolder ml = new ArtistAlbumHolder(v);
        return ml;
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistAlbumHolder holder, int position) {
        Album album = albums.get(position);
        Uri albumArtUri = BitmapUtils.getAlbumArtUri(album.id);
        Bitmap bitmap = BitmapUtils.decodeUri(MusicApplication.getMusicContent(),albumArtUri,300,300);
        if (bitmap!=null) {
            holder.albumBg.setImageBitmap(bitmap);
        }
        holder.albumName.setText(album.title);
        holder.albumName.setSelected(true);
        holder.songNum.setText(album.songCount+"首音乐");

    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    private SignListener runnable;

    public void setCallBackListener(SignListener runnable) {
        this.runnable = runnable;
    }

    class ArtistAlbumHolder extends RecyclerView.ViewHolder {
        public ImageView albumBg;
        public TextView albumName;
        public TextView songNum;
        public ArtistAlbumHolder(@NonNull View itemView) {
            super(itemView);
            albumBg = itemView.findViewById(R.id.album_bg);
            albumName = itemView.findViewById(R.id.album_name);
            songNum = itemView.findViewById(R.id.song_num);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    runnable.sign(albums.get(getPosition()).id);
                }
            });
        }
    }
}
