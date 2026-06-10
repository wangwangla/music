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
import com.example.learnandroid.application.utils.BitmapUtils;
import com.example.learnandroid.application.utils.ShareUtils;
import com.example.learnandroid.application.utils.TimeUtils;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.constant.MusicManager;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
	private final ArrayList<MusicBean> searchResults = new ArrayList<>();

	@NonNull
	@Override
	public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.songlist_view_layout, parent, false);
		return new SearchViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
		MusicBean musicBean = searchResults.get(position);
		holder.songName.setText(musicBean.getTitle());
		holder.songName.setSelected(true);
		holder.songSonger.setText(musicBean.getArtistName());
		holder.songTime.setText(" | " + TimeUtils.longToTime(musicBean.getDuration()));

		Uri albumArtUri = BitmapUtils.getAlbumArtUri(musicBean.getAlbumId());
		Bitmap bitmap = BitmapUtils.decodeUri(holder.itemView.getContext(), albumArtUri, 300, 300);
		if (bitmap != null) {
			holder.songPic.setImageBitmap(bitmap);
		} else {
			holder.songPic.setImageResource(R.mipmap.default_image2);
		}

		holder.itemView.setOnClickListener(v -> playSong(musicBean));
		holder.more.setOnClickListener(v -> showPopupMenu(v, musicBean));
	}

	@Override
	public int getItemCount() {
		return searchResults.size();
	}

	public void updateSearchResults(List<MusicBean> results) {
		searchResults.clear();
		if (results != null) {
			searchResults.addAll(results);
		}
		notifyDataSetChanged();
	}

	private void playSong(MusicBean musicBean) {
		if (musicBean.getId() == MusicManager.getId()) {
			if (!MusicManager.isPlaying()) {
				MusicManager.continuePlay();
			}
			return;
		}
		MusicManager.setDataAndplay(musicBean.getId());
	}

	private void showPopupMenu(View anchor, MusicBean musicBean) {
		MusicApplication.getMusicContent().setTheme(R.style.Theme_LearnAndroid);
		PopupMenu popupMenu = new PopupMenu(anchor.getContext(), anchor);
		popupMenu.inflate(R.menu.popup_song);
		popupMenu.setOnMenuItemClickListener(item -> handlePopupAction(item, musicBean, anchor));
		popupMenu.show();
	}

	private boolean handlePopupAction(MenuItem item, MusicBean musicBean, View anchor) {
		if (item.getItemId() == R.id.play) {
			playSong(musicBean);
			return true;
		}
		if (item.getItemId() == R.id.play_next) {
			ShareUtils.share(anchor.getContext(), musicBean.getId());
			return true;
		}
		return false;
	}

	static class SearchViewHolder extends RecyclerView.ViewHolder {
		private final ImageView songPic;
		private final TextView songName;
		private final TextView songSonger;
		private final TextView songTime;
		private final ImageView more;

		public SearchViewHolder(@NonNull View itemView) {
			super(itemView);
			songPic = itemView.findViewById(R.id.song_pic);
			songName = itemView.findViewById(R.id.song_name);
			songSonger = itemView.findViewById(R.id.song_songer);
			songTime = itemView.findViewById(R.id.song_duration);
			more = itemView.findViewById(R.id.song_more);
		}
	}
}
