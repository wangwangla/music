package com.example.learnandroid.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.toolbox.ImageLoader;
import com.example.learnandroid.PlayActivity;
import com.example.learnandroid.R;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.constant.MusicManager;
import com.example.learnandroid.utils.MusicUtils;

import java.util.List;

public class SongAdapter extends ArrayAdapter<MusicBean> {
    public SongAdapter(@NonNull Context context, int resource, @NonNull List<MusicBean> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MusicBean musicBean=getItem(position);//得到当前项的 Fruit 实例
        //为每一个子项加载设定的布局
        View view= LayoutInflater.from(getContext()).inflate(R.layout.songlist_view_layout,parent,false);
        //分别获取 image view 和 textview 的实例
        ImageView fruitimage =view.findViewById(R.id.song_pic);
        TextView fruitname =view.findViewById(R.id.song_name);
        TextView fruitprice=view.findViewById(R.id.song_songer);
        // 设置要显示的图片和文字
        Uri albumArtUri = MusicUtils.getAlbumArtUri(musicBean.getAlbumId());
        Bitmap bitmap = MusicUtils.decodeUri(getContext(),albumArtUri,300,300);
        if (bitmap!=null) {
            fruitimage.setImageBitmap(bitmap);
        }
        fruitname.setText(musicBean.getTitle());
        fruitprice.setText(musicBean.getArtistName());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(context, PlayActivity.class);
//                context.startActivity(intent);
                //click common ;
                if (musicBean.getId() == MusicManager.getId()){
                    return;
                }
                //播放
                MusicManager.setData(position);
                MusicManager.play();
            }
        });
        return view;
    }
}
