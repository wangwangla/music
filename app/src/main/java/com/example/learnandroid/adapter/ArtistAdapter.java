package com.example.learnandroid.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnandroid.R;
import com.example.learnandroid.bean.Artist;

import java.util.List;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/12 11:35
 */
public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ItemHolder>{

    private List<Artist> arraylist;

    public ArtistAdapter(List<Artist> arraylist) {
        this.arraylist = arraylist;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_artist, null);
        ItemHolder ml = new ItemHolder(v);
        return ml;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        Artist artist = arraylist.get(position);
        StringBuilder builder = new StringBuilder();
        builder.append(artist.albumCount);
        builder.append("张专辑");
        builder.append(artist.songCount);
        builder.append("首歌");

        holder.albums.setText(builder.toString());
        holder.artistImage.setImageResource(R.drawable.default_image2);




        holder.name.setText(artist.name);
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