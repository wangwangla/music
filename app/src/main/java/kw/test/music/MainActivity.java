package kw.test.music;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

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

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewItemNavBinding navView = binding.navView;


        final ViewTreeObserver vto = navView.getRoot().getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                float mHeaderWidth = navView.getRoot().getMeasuredWidth();
                float mHeaderHeight = navView.getRoot().getMeasuredHeight();
                //在API16之后removeOnGlobalLayoutListener代替了removeGlobalOnLayoutListener
                if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    navView.getRoot().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    navView.getRoot().getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                navView.getRoot().setX(-mHeaderWidth);
                float[] mm = {-mHeaderWidth,0};
                Animator animator = ObjectAnimator.ofFloat(navView.getRoot(),"translationX",mm);
                animator.start();
//                Animation animation = new TranslateAnimation(80,100,0,100);
//                animation.setDuration(4000);
//                navView.getRoot().setAnimation(animation);
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}