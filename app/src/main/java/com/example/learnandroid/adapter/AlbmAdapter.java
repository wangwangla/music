package com.example.learnandroid.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnandroid.R;
import com.example.learnandroid.application.utils.AlbumArtLoader;
import com.example.learnandroid.bean.Album;
import com.example.learnandroid.navutil.NavigationUtils;

import java.util.List;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/12 19:11
 */
public class AlbmAdapter extends RecyclerView.Adapter{
    private Activity activity;
    private List<Album> arraylist;

    public AlbmAdapter(List<Album> arraylist, FragmentActivity activity) {
        this.arraylist = arraylist;
        this.activity = activity;
    }

    @NonNull
    @Override
    public AlbmAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_artist, viewGroup, false);
        AlbmAdapter.ItemHolder ml = new AlbmAdapter.ItemHolder(v);
        return ml;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Album album = arraylist.get(position);
        ItemHolder holder1 = (ItemHolder) (holder);
        holder1.albums.setText(album.title);
        AlbumArtLoader.load(holder1.artistImage, album.id, 300, 300, R.mipmap.default_image2);
        holder1.name.setText(activity.getString(R.string.album_detail_meta, album.artistName, album.songCount));
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        ItemHolder itemHolder = (ItemHolder) holder;
        AlbumArtLoader.clear(itemHolder.artistImage, R.mipmap.default_image2);
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView name, albums;
        protected ImageView artistImage;
        protected View footer;

        public ItemHolder(View view) {
            super(view);
            this.name = view.findViewById(R.id.artist_name);
            this.albums = view.findViewById(R.id.album_song_count);
            this.artistImage = view.findViewById(R.id.artistImage);
            this.footer = view.findViewById(R.id.footer);
            view.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position == RecyclerView.NO_POSITION) {
                return;
            }
            NavigationUtils.navigateToAlbum(activity, arraylist.get(position).id);
        }
    }
}
