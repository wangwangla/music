package com.example.learnandroid.file;

import androidx.recyclerview.widget.SortedList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.SortedSet;

/**
 * @Auther jian xian si qi
 * @Date 2023/3/12 10:21
 */
public class SavePlayList {
    public static void save(){
        FileUtils u = new FileUtils("playlist");
//        String s = u.readString(null);
        u.writeString("xx",true,null);
        String s = u.readString(null);
        String[] split = s.split("\n");

        for (String s1 : split) {

        }

    }


}
