package kw.learn.mylibrary.toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ToolBarUtils {

    public ToolBarUtils(AppCompatActivity activity,int id) {
        Toolbar toolbar = activity.findViewById(id);
        toolbar.setTitle("");
        activity.setSupportActionBar(toolbar);
    }
}
