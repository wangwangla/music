package com.example.learnandroid;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learnandroid.data.DefaultLrcParser;
import com.example.learnandroid.data.LrcMusic;
import com.example.learnandroid.data.LrcRow;
import com.example.learnandroid.utils.Utils;
import com.example.learnandroid.view.LrcView;
import com.example.learnandroid.view.ShowLrcView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther jian xian si qi
 * @Date 2023/4/17 8:02
 */
public class LrcActivity extends AppCompatActivity {
    private ShowLrcView view;
    private EditText editText;
    private Button btn;
    private ArrayList<LrcMusic> lrcs;
    private ArrayList<String> list;
    private ArrayList<Long> list1;
    private int lrc_index;
    private int time ;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1){
                view.seekTo(time,false,false);
                time += 1000;
//                if(lrc_index == list.size()){
//                    handler.removeMessages(1);
//                }
//                lrc_index++;
//                System.out.println("******"+lrc_index+"*******");
//                view.scrollToIndex(lrc_index);
                handler.sendEmptyMessageDelayed(1,1000);
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lyc_main);
        initViews();
        initEvents();

//        List<LrcRow> lrcRows = DefaultLrcParser.getIstance().getLrcRows(builder.toString());
//        view.setLrcRows(lrcRows);
    }

    private void initViews(){
        view = findViewById(R.id.view);
        editText = (EditText) findViewById(R.id.editText);
        btn = (Button) findViewById(R.id.button);
    }

    private void initEvents(){
//        InputStream is = getResources().openRawResource(R.raw.yyy);
//        // BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        list = new ArrayList<String>();
//        list1 = new ArrayList<>();
//        lrcs = Utils.redLrc(is);
//        for(int i = 0; i< lrcs.size(); i++){
//            list.add(lrcs.get(i).getLrc());
//            System.out.println(lrcs.get(i).getLrc()+"=====");
//            list1.add(0l);//lrcs.get(i).getTime()
//        }
////        view.setLyricText(list, list1);
//        view.postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                view.scrollToIndex(0);
//            }
//        },1000);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String text = editText.getText().toString();
//                int index = 0;
//                index = Integer.parseInt(text);
////                view.scrollToIndex(index);
//            }
//        });

//        view.setOnLyricScrollChangeListener(new LrcView.OnLyricScrollChangeListener() {
//            @Override
//            public void onLyricScrollChange(final int index, int oldindex) {
//                editText.setText(""+index);
//                lrc_index = index;
//                System.out.println("===="+index+"======");
//                //滚动handle不能放在这，因为，这是滚动监听事件，滚动到下一次，handle又会发送一次消息，出现意想不到的效果
//            }
//        });
        handler.sendEmptyMessageDelayed(1,4000);
//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                        handler.removeCallbacksAndMessages(null);
//
//                        System.out.println("取消了");
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        System.out.println("开始了");
//                        handler.sendEmptyMessageDelayed(1,2000);
//                        break;
//                    case MotionEvent.ACTION_CANCEL://时间别消耗了
//                        break;
//                }
//                return false;
//            }
//        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

}