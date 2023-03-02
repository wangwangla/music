package kw.test.music;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import kw.test.music.databinding.ActivityMainBinding;
import kw.test.music.databinding.ViewItemNavBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private float mHeaderWidth;
    private ActivityMainBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewItemNavBinding navView = binding.navView;
        ImageView shadow = binding.shadow;
        shadow.setColorFilter(R.color.nullshadow);
        final ViewTreeObserver vto = navView.getRoot().getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mHeaderWidth = navView.getRoot().getMeasuredWidth();
                //在API16之后removeOnGlobalLayoutListener代替了removeGlobalOnLayoutListener
                if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    navView.getRoot().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    navView.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                navView.getRoot().setX(-mHeaderWidth);
                float[] mm = {-mHeaderWidth,0};
                Animator animator = ObjectAnimator.ofFloat(navView.getRoot(),"translationX",mm);
                animator.setDuration(1000);
                animator.start();
                binding.shadow.setEnabled(true);
                ObjectAnimator anim = ObjectAnimator.ofFloat(binding.shadow, "alpha", 0, 0.5f);
                anim.setDuration(1000);
                anim.start();

                binding.navView.getRoot().setEnabled(true);
            }
        });
        binding.shadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.shadow.setEnabled(false);
                float[] mm = {0,-mHeaderWidth};
                Animator animator = ObjectAnimator.ofFloat(navView.getRoot(),"translationX",mm);
                animator.setDuration(1000);
                animator.start();


                ObjectAnimator anim = ObjectAnimator.ofFloat(binding.shadow, "alpha", 0.5f, 0.0f);
                anim.setDuration(1000);
                anim.start();
            }
        });

        binding.actionBar.navItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator anim = ObjectAnimator.ofFloat(binding.shadow, "alpha", 0, 0.5f);
                anim.setDuration(1000);
                anim.start();
                float[] mm = {-mHeaderWidth,0};
                Animator animator = ObjectAnimator.ofFloat(navView.getRoot(),"translationX",mm);
                animator.setDuration(1000);
                animator.start();
                binding.shadow.setEnabled(true);
                binding.navView.getRoot().setEnabled(true);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}