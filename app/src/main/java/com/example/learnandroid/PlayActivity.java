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
import com.example.learnandroid.utils.ThemeUtils;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        LinearLayout playLinearView = findViewById(R.id.play_linear_view);
        replace(new PlayFragment());

        ThemeUtils.updateSystemBarContent(this,false);
        ThemeUtils.hideTopBg(this);
    }

    public Bitmap blurBitmap(Context context, Bitmap bitmap) {
        // Let's create an empty bitmap with the same size of the bitmap we want
        // to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);

        // Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(context);//RenderScript是Android在API 11之后增加的

        // Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // Create the Allocations (in/out) with the Renderscript and the in/out
        // bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        // Set the radius of the blur
        blurScript.setRadius(25.f);

        // Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        // Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        // recycle the original bitmap
        bitmap.recycle();

        // After finishing everything, we destroy the Renderscript.
        rs.destroy();
        return outBitmap;
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