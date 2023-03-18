package com.example.learnandroid;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.constant.MusicManager;
import com.example.learnandroid.main.GeciFragment;
import com.example.learnandroid.main.PlayFragment;
import com.example.learnandroid.utils.MusicUtils;

public class PlayActivity extends AppCompatActivity {
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        LinearLayout playLinearView = findViewById(R.id.play_linear_view);


        View song = findViewById(R.id.song);
        replace(new PlayFragment(new Runnable() {
            @Override
            public void run() {
                MusicBean musicBean = MusicManager.getMusicBean();
                if (musicBean==null)return;
                Uri albumArtUri = MusicUtils.getAlbumArtUri(musicBean.getAlbumId());
                Bitmap bitmap = MusicUtils.decodeUri(getBaseContext(),albumArtUri);
                if (bitmap!=null) {
                    Bitmap bitmap1 = blurBitmap(getBaseContext(), bitmap,25f);
                    playLinearView.setBackground(new BitmapDrawable(bitmap1));
                }

            }
        }));
//        song.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                replace(new PlayFragmentI);
//            }
//        });
    }

    public static Bitmap blurBitmap(Context context, Bitmap bitmap) {
        //用需要创建高斯模糊bitmap创建一个空的bitmap
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        // 初始化Renderscript，该类提供了RenderScript context，创建其他RS类之前必须先创建这个类，其控制RenderScript的初始化，资源管理及释放
        RenderScript rs = RenderScript.create(context);
        // 创建高斯模糊对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        // 创建Allocations，此类是将数据传递给RenderScript内核的主要方 法，并制定一个后备类型存储给定类型
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
        //设定模糊度(注：Radius最大只能设置25.f)
        blurScript.setRadius(5.f);
        // Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);
        // Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);
        // recycle the original bitmap
        // bitmap.recycle();
        // After finishing everything, we destroy the Renderscript.
        rs.destroy();
        return outBitmap;
    }
    private final float BITMAP_SCALE = 1f;

    public Bitmap blurBitmap(Context context, Bitmap image,float blurRadius) {
        // 计算图片缩小后的长宽
        int width =2;
        int height = 2;

        // 将缩小后的图片做为预渲染的图片
        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        // 创建一张渲染后的输出图片
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        // 创建RenderScript内核对象
        RenderScript rs = RenderScript.create(context);
        // 创建一个模糊效果的RenderScript的工具对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        // 设置渲染的模糊程度, 25f是最大模糊度
        blurScript.setRadius(blurRadius);
        // 设置blurScript对象的输入内存
        blurScript.setInput(tmpIn);
        // 将输出数据保存到输出内存中
        blurScript.forEach(tmpOut);

        // 将数据填充到Allocation中
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }


    public void replace(Fragment fragment){
        //调用getSupportFragmentManager()方法获取fragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager调用beginTransaction()开启一个事务
        FragmentTransaction transition = fragmentManager.beginTransaction();
        //向容器添加或者Fragment
        transition.replace(R.id.play_view, fragment);
        //调用commit()方法进行事务提交
        transition.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}