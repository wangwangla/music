//package com.example.learnandroid.adapter;
//
//import android.app.Activity;
//import android.os.Handler;
//import android.view.LayoutInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.PopupMenu;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.learnandroid.SearchActivity;
//import com.example.learnandroid.bean.MusicBean;
//
//import java.util.Collections;
//import java.util.List;
//
///**
// * @Auther jian xian si qi
// * @Date 2023/4/8 13:48
// */
//public class SearchAdapter extends BaseSongAdapter<SearchAdapter.ItemHolder> {
//
//    private Activity mContext;
//    private List searchResults = Collections.emptyList();
//
//    public SearchAdapter(Activity context) {
//        this.mContext = context;
//
//    }
//
//    @Override
//    public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
////        switch (viewType) {
////            case 0:
////                View v0 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_song, null);
////                ItemHolder ml0 = new ItemHolder(v0);
////                return ml0;
////            case 1:
////                View v1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_album_search, null);
////                ItemHolder ml1 = new ItemHolder(v1);
////                return ml1;
////            case 2:
////                View v2 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_artist, null);
////                ItemHolder ml2 = new ItemHolder(v2);
////                return ml2;
////            case 10:
////                View v10 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_section_header, null);
////                ItemHolder ml10 = new ItemHolder(v10);
////                return ml10;
////            default:
////                View v3 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_song, null);
////                ItemHolder ml3 = new ItemHolder(v3);
////                return ml3;
////        }
//    }
//
//    @Override
//    public void onBindViewHolder(final ItemHolder itemHolder, int i) {
////        switch (getItemViewType(i)) {
////            case 0:
////                MusicBean song = (MusicBean) searchResults.get(i);
////                itemHolder.title.setText(song.title);
////                itemHolder.songartist.setText(song.albumName);
////                ImageLoader.getInstance().displayImage(TimberUtils.getAlbumArtUri(song.albumId).toString(), itemHolder.albumArt,
////                        new DisplayImageOptions.Builder().cacheInMemory(true)
////                                .cacheOnDisk(true)
////                                .showImageOnFail(R.drawable.ic_empty_music2)
////                                .resetViewBeforeLoading(true)
////                                .displayer(new FadeInBitmapDisplayer(400))
////                                .build());
////                setOnPopupMenuListener(itemHolder, i);
////                break;
////            case 1:
////                Album album = (Album) searchResults.get(i);
////                itemHolder.albumtitle.setText(album.title);
////                itemHolder.albumartist.setText(album.artistName);
////                ImageLoader.getInstance().displayImage(TimberUtils.getAlbumArtUri(album.id).toString(), itemHolder.albumArt,
////                        new DisplayImageOptions.Builder().cacheInMemory(true)
////                                .cacheOnDisk(true)
////                                .showImageOnFail(R.drawable.ic_empty_music2)
////                                .resetViewBeforeLoading(true)
////                                .displayer(new FadeInBitmapDisplayer(400))
////                                .build());
////                break;
////            case 2:
////                Artist artist = (Artist) searchResults.get(i);
////                itemHolder.artisttitle.setText(artist.name);
////                String albumNmber = TimberUtils.makeLabel(mContext, R.plurals.Nalbums, artist.albumCount);
////                String songCount = TimberUtils.makeLabel(mContext, R.plurals.Nsongs, artist.songCount);
////                itemHolder.albumsongcount.setText(TimberUtils.makeCombinedString(mContext, albumNmber, songCount));
////                break;
////            case 10:
////                itemHolder.sectionHeader.setText((String) searchResults.get(i));
////            case 3:
////                break;
////        }
//    }
//
//    @Override
//    public void onViewRecycled(ItemHolder itemHolder) {
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return searchResults.size();
//    }
//
//    private void setOnPopupMenuListener(ItemHolder itemHolder, final int position) {
//
//        itemHolder.menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return 3;
//    }
//
//    public void updateSearchResults(List searchResults) {
//        this.searchResults = searchResults;
//    }
//
//    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        protected TextView title, songartist, albumtitle, artisttitle, albumartist, albumsongcount, sectionHeader;
//        protected ImageView albumArt, artistImage, menu;
//
//        public ItemHolder(View view) {
//            super(view);
//            this.title = (TextView) view.findViewById(R.id.song_title);
//            this.songartist = (TextView) view.findViewById(R.id.song_artist);
//            this.albumsongcount = (TextView) view.findViewById(R.id.album_song_count);
//            this.artisttitle = (TextView) view.findViewById(R.id.artist_name);
//            this.albumtitle = (TextView) view.findViewById(R.id.album_title);
//            this.albumartist = (TextView) view.findViewById(R.id.album_artist);
//            this.albumArt = (ImageView) view.findViewById(R.id.albumArt);
//            this.artistImage = (ImageView) view.findViewById(R.id.artistImage);
//            this.menu = (ImageView) view.findViewById(R.id.popup_menu);
//
//            this.sectionHeader = (TextView) view.findViewById(R.id.section_header);
//
//
//            view.setOnClickListener(this);
//        }
//
//        @Override
//        public void onClick(View v) {
//        }
//    }
//}
//
//
//
//
//
