package com.example.learnandroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnandroid.R;
import com.example.learnandroid.bean.Album;

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
//        holder.albumBg.setImageBitmap();
        holder.albumName.setText(album.artistName);
        holder.songNum.setText(album.songCount);
    }

    @Override
    public int getItemCount() {
        return albums.size();
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
        }
    }
}
