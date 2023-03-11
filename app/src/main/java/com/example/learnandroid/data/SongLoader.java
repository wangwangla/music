package com.example.learnandroid.data;

import com.example.learnandroid.application.MyApplication;
import com.example.learnandroid.bean.MusicBean;

import java.util.ArrayList;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/11 16:28
 */
public class SongLoader {
    private static int loadType = 0;
    private static SaoMiaoMusicInterface instance;

    public static void setLoadType(int _loadType) {
        loadType = _loadType;
    }

    public static ArrayList<MusicBean> loadAllSongList() {
        getSaoMiaoMusicInstance().findMusic();
        return getSaoMiaoMusicInstance().getMusicBeans();
    }

    public static SaoMiaoMusicInterface getSaoMiaoMusicInstance(){
        if (instance == null) {
            if (loadType == 0) {
                instance = new ContentResolverFindMusic();
            }
            if (instance == null) {
                instance = new ContentResolverFindMusic();
            }
        }
        return instance;
    }

    public static void destory(){
        instance = null;
    }
}
