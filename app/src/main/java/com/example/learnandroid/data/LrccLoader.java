//package com.example.learnandroid.data;
//
//import android.util.Log;
//
//import com.example.learnandroid.constant.MusicManager;
//import com.example.learnandroid.utils.TimeUtils;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//
///**
// * @Auther jian xian si qi
// * @Date 2023/4/15 0:09
// */
//public class LrccLoader {
//    public ArrayList loadLrc(String path){
//
//        ArrayList<LrcContent> lrclists = new ArrayList<>();
////        StringBuffer stringBuffer = new StringBuffer();
//        // 得到歌词文件路径
//        // c://user//music//song.mp3
//        // c://user//music//song.lrc
//        String lrcPathString = path.substring(0, path.lastIndexOf(".")) + ".lrc";
//        int index = lrcPathString.lastIndexOf("/");
//        String parentPath = null;
//        String lrcName = null;
//         if(index!=-1){
//            parentPath = lrcPathString.substring(0, index);
//            lrcName = lrcPathString.substring(index);
//         }
//        File file = new File(lrcPathString);
////
//        // 匹配SweetMusicPlayer/Lyrics
////        if (!file.exists()) {
////            file = new File(OnlineLrcUtil.getInstance().getLrcPath(
////                    song.getTitle(), song.getArtist()));
////        }
////        Log.i("Path", file.getAbsolutePath().toString());
//
//        // c://user//music//song.mp3
//        // c://user//Lyrics//song.lrc
//        // 匹配Lyrics
//        if (!file.exists()) {
//            file = new File(parentPath + "/../" + "Lyrics/" + lrcName);
//        }
//        Log.i("Path", file.getAbsolutePath().toString());
//
//        // c://user//music//song.mp3
//        // c://user//lyric//song.lrc
//        // 匹配lyric
//        if (!file.exists()) {
//            file = new File(parentPath + "/../" + "lyric/" + lrcName);
//        }
//        Log.i("Path", file.getAbsolutePath().toString());
//
//        // c://user//music//song.mp3
//        // c://user//Lyrics//song.lrc
//        // 匹配Lyric
//        if (!file.exists()) {
//            file = new File(parentPath + "/../" + "Lyric/" + lrcName);
//        }
//
//        Log.i("Path", file.getAbsolutePath().toString());
//
//
//        // c://user//music//song.mp3
//        // c://user//lyrics//song.lrc
//        // 匹配lyrics
//        if (!file.exists()) {
//            file = new File(parentPath + "/../" + "lyrics/" + lrcName);
//        }
//        Log.i("Path", file.getAbsolutePath().toString());
//
//
////        if (!file.exists()) {
////            stringBuffer.append(MusicManager.OperateState.READLRCFILE_FAIL);
////            return stringBuffer.toString();
////        }
//
//        try {
//            FileInputStream fin = new FileInputStream(file);
//            InputStreamReader isr = new InputStreamReader(fin, "utf-8");
//            BufferedReader br = new BufferedReader(isr);
//            String s;
//            boolean isLrc = false;
//            while ((s = br.readLine()) != null) {
//                // if(isLrc){
//                s = s.replace("[", ""); // 去掉左边括号
//                String lrcData[] = s.split("]");
//                // 这句是歌词
//                if (lrcData[0].matches("^\\d{2}:\\d{2}.\\d+$")) {
//                    int len = lrcData.length;
//                    int end = lrcData[len - 1].matches("^\\d{2}:\\d{2}.\\d+$") ? len : len - 1;
//
//                    for (int i = 0; i < end; i++) {
//                        LrcContent lrcContent = new LrcContent();
//                        int lrcTime = TimeUtils.getLrcMillTime(lrcData[i]);
//                        lrcContent.setLrcTime(lrcTime);
//                        if (lrcData.length == end)
//                            lrcContent.setLrcStr(""); // 空白行
//                        else
//                            lrcContent.setLrcStr(lrcData[len - 1]);
//
//                        lrclists.add(lrcContent);
//                    }
//
//                }
//
//            }
//            // 按时间排序
//            Collections.sort(lrclists, new Comparator<LrcContent>() {
//                @Override
//                public int compare(LrcContent o1, LrcContent o2) {
//                    return o1.getLrcTime() - o2.getLrcTime();
//                }
//            });
//
//            if (lrclists.size() == 0) {
////                stringBuffer.append(MusicManager.OperateState.READLRC_LISTNULL);
//            } else {
////                stringBuffer.append(MusicManager.OperateState.READLRC_SUCCESS);
//            }
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            // stringBuffer.append("未找到歌词文件");
//        } catch (UnsupportedEncodingException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            // stringBuffer.append("不支持的编码");
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            // stringBuffer.append("IO错误");
//        }
//        return lrclists;
//    }
//}
