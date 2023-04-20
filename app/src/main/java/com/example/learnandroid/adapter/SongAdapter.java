package com.example.learnandroid.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.learnandroid.R;
import com.example.learnandroid.application.MyApplication;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.constant.MusicManager;
import com.example.learnandroid.utils.BitmapUtils;
import com.example.learnandroid.utils.TimeUtils;

import java.util.List;

public class SongAdapter extends ArrayAdapter<MusicBean> {
    public SongAdapter(@NonNull Context context, int resource, @NonNull List<MusicBean> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        MusicBean musicBean=getItem(position);//得到当前项的 Fruit 实例
        View view;
        if (convertView == null){
            //为每一个子项加载设定的布局
            view= LayoutInflater.from(getContext()).inflate(R.layout.songlist_view_layout,parent,false);
            //分别获取 image view 和 textview 的实例
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
            // 设置要显示的图片和文字
         }else {
            view=convertView;
            viewHolder= (ViewHolder) view.getTag();//重新获取 viewHolder
        }

        Uri albumArtUri = BitmapUtils.getAlbumArtUri(musicBean.getAlbumId());
        Bitmap bitmap = BitmapUtils.decodeUri(getContext(),albumArtUri,300,300);
        if (bitmap!=null) {
            viewHolder.songPic.setImageBitmap(bitmap);
        }
        viewHolder.songTime.setText(" | "+TimeUtils.longToTime(musicBean.getDuration()));
        viewHolder.songName.setText(musicBean.getTitle());
        viewHolder.songSonger.setText(musicBean.getArtistName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (musicBean.getId() == MusicManager.getId()){
                    if (!MusicManager.isPlaying()){
                        MusicManager.continuePlay();
                    }
                    return;
                }
                //播放
                MusicManager.setData(position);
                MusicManager.setDataAndplay();
            }
        });
        viewHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //没太明白，为什么需要这么写  因为整个应用都是呀的是这个Theme
                MyApplication.getMusicContent().setTheme(R.style.Theme_LearnAndroid);
                final PopupMenu popupMenu = new PopupMenu(MyApplication.getMusicContent(), v);
                popupMenu.inflate(R.menu.popup_song);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.play:{
                                break;
                            }
                            case R.id.play_next:{
                                break;
                            }
                            case R.id.add_to_playlist:{
                                break;
                            }
                            case R.id.add_to_album:{
                                break;
                            }
                            case R.id.add_to_aritist:{
                                break;
                            }
                            case R.id.delete_from_device:{
                                break;
                            }
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        return view;
    }

    private class ViewHolder {
        private ImageView songPic;
        private TextView songName;
        private TextView songSonger;
        private TextView songTime;
        private ImageView more;

        public ViewHolder(View view) {
            this.songPic = view.findViewById(R.id.song_pic);
            this.songName = view.findViewById(R.id.song_name);
            this.songSonger = view.findViewById(R.id.song_songer);
            this.songTime = view.findViewById(R.id.song_duration);
            this.more = view.findViewById(R.id.song_more);
        }
    }
}
