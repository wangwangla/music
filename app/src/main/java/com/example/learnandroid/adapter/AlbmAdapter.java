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
import com.example.learnandroid.application.MyApplication;
import com.example.learnandroid.bean.Album;
import com.example.learnandroid.utils.MusicUtils;

import java.util.List;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/12 19:11
 */
public class AlbmAdapter extends RecyclerView.Adapter{

    private List<Album> arraylist;

    public AlbmAdapter(List<Album> arraylist) {
        this.arraylist = arraylist;
    }

    @NonNull
    @Override
    public AlbmAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_artist, null);
        AlbmAdapter.ItemHolder ml = new AlbmAdapter.ItemHolder(v);
        return ml;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Album album = arraylist.get(position);
        ItemHolder holder1 = (ItemHolder) (holder);
        holder1.albums.setText(album.artistName);

        Uri albumArtUri = MusicUtils.getAlbumArtUri(album.artistId);
        Bitmap bitmap = MusicUtils.decodeUri(MyApplication.getMusicContent(),albumArtUri,300,300);
        if (bitmap!=null) {
            holder1.artistImage.setImageBitmap(bitmap);
        }


        holder1.name.setText(album.title);
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        protected TextView name, albums;
        protected ImageView artistImage;
        protected View footer;

        public ItemHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.artist_name);
            this.albums = (TextView) view.findViewById(R.id.album_song_count);
            this.artistImage = (ImageView) view.findViewById(R.id.artistImage);
            this.footer = view.findViewById(R.id.footer);
        }
    }
}
