package com.example.learnandroid.data;

import android.content.Context;
import android.text.TextUtils;

import com.example.learnandroid.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Auther jian xian si qi
 * @Date 2023/4/18 9:19
 */
public class DefaultLrcParser {
    private static final DefaultLrcParser istance = new DefaultLrcParser();
    public static final DefaultLrcParser getIstance() {
        return istance;
    }
    private DefaultLrcParser() {}

    public void readFile(Context context){
        StringBuilder builder = new StringBuilder();
        try {
            InputStream lrcInputStream = context.getResources().openRawResource(R.raw.yyy);
            InputStreamReader lrcInputReader = new InputStreamReader(lrcInputStream, "utf-8");
            BufferedReader lrcReader = new BufferedReader(lrcInputReader);
            //获取字节码，一次读取一个字符
            String lineContent = "";
            while ((lineContent = lrcReader.readLine()) != null) {
                builder.append(lineContent);
                builder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /***
     * 将歌词文件里面的字符串 解析成一个List<LrcRow>
     */
    public List<LrcRow> getLrcRows(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        BufferedReader br = new BufferedReader(new StringReader(str));
        List<LrcRow> lrcRows = new ArrayList<>();
        String lrcLine;
        try {
            while ((lrcLine = br.readLine()) != null) {
                List<LrcRow> rows = LrcRow.createRows(lrcLine);
                if (rows != null && rows.size() > 0) {
                    lrcRows.addAll(rows);
                }
            }
            Collections.sort(lrcRows);
            int len = lrcRows.size();
            for (int i = 0; i < len - 1; i++) {
                lrcRows.get(i).setTotalTime(lrcRows.get(i + 1).getTime() - lrcRows.get(i).getTime());
            }
            lrcRows.get(len - 1).setTotalTime(5000);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return lrcRows;
    }
}