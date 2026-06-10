package com.example.learnandroid;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learnandroid.adapter.SearchAdapter;
import com.example.learnandroid.bean.MusicBean;
import com.example.learnandroid.constant.MusicManager;
import com.example.learnandroid.data.SongLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Auther jian xian si qi
 * @Date 2023/4/8 8:24
 */
public class SearchActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, View.OnTouchListener {
    private final Executor mSearchExecutor = Executors.newSingleThreadExecutor();
    @Nullable
    private AsyncTask<String, Void, ArrayList<MusicBean>> mSearchTask = null;
    private SearchView mSearchView;
    private InputMethodManager mImm;
    private String queryString;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private final ArrayList<MusicBean> allSongs = new ArrayList<>();
    private final ArrayList<MusicBean> searchResults = new ArrayList<>();
    private SearchAdapter searchAdapter;
    private Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Toolbar toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = findViewById(R.id.search_tip_list);
        emptyView = findViewById(R.id.search_empty_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setOnTouchListener(this);
        searchAdapter = new SearchAdapter();
        recyclerView.setAdapter(searchAdapter);
        updateSearchResults(new ArrayList<>());
        if(savedInstanceState != null && savedInstanceState.containsKey("QUERY_STRING")){
            bundle = savedInstanceState;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (queryString != null){
            outState.putString("QUERY_STRING", queryString);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);

        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));

        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint(getString(R.string.search_library));

        mSearchView.setIconifiedByDefault(false);
        mSearchView.setIconified(false);

        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.menu_search), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                finish();
                return false;
            }
        });

        menu.findItem(R.id.menu_search).expandActionView();
        if(bundle != null && bundle.containsKey("QUERY_STRING")){
            mSearchView.setQuery(bundle.getString("QUERY_STRING"), true);
        }
        return true;
    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(final String query) {
        onQueryTextChange(query);
        hideInputManager();

        return true;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {

        if (newText.equals(queryString)) {
            return true;
        }
        if (mSearchTask != null) {
            mSearchTask.cancel(false);
            mSearchTask = null;
        }
        queryString = newText;
        if (queryString.trim().equals("")) {
            updateSearchResults(new ArrayList<>());
        } else {
            mSearchTask = new SearchTask().executeOnExecutor(mSearchExecutor, queryString);
            Log.d("AAAABBBBBB", "TaskCanelled? " + (mSearchTask.isCancelled()));
        }

        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        hideInputManager();
        return false;
    }

    @Override
    protected void onDestroy() {
        if (mSearchTask != null && mSearchTask.getStatus() != AsyncTask.Status.FINISHED) {
            mSearchTask.cancel(false);
        }
        super.onDestroy();
    }

    public void hideInputManager() {
        if (mSearchView != null) {
            if (mImm != null) {
                mImm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
            }
            mSearchView.clearFocus();
        }
    }

    private void updateSearchResults(List<MusicBean> results) {
        searchResults.clear();
        if (results != null) {
            searchResults.addAll(results);
        }
        searchAdapter.updateSearchResults(searchResults);
        if (queryString == null || queryString.trim().isEmpty()) {
            emptyView.setText(R.string.search_empty_hint);
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else if (searchResults.isEmpty()) {
            emptyView.setText(R.string.search_no_result);
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private ArrayList<MusicBean> loadAllSongsIfNeeded() {
        if (allSongs.isEmpty()) {
            ArrayList<MusicBean> musicBeans = SongLoader.loadAllSongList();
            allSongs.clear();
            if (musicBeans != null) {
                allSongs.addAll(musicBeans);
                MusicManager.setSongList(musicBeans);
            }
        }
        return new ArrayList<>(allSongs);
    }

    private boolean matchSong(MusicBean musicBean, String keyword) {
        return containsIgnoreCase(musicBean.getTitle(), keyword)
                || containsIgnoreCase(musicBean.getArtistName(), keyword)
                || containsIgnoreCase(musicBean.getAlbumName(), keyword);
    }

    private boolean containsIgnoreCase(String source, String keyword) {
        return source != null && source.toLowerCase(Locale.getDefault()).contains(keyword);
    }

    private class SearchTask extends AsyncTask<String,Void,ArrayList<MusicBean>> {

        @Override
        protected ArrayList<MusicBean> doInBackground(String... params) {
            String keyword = params[0].trim().toLowerCase(Locale.getDefault());
            ArrayList<MusicBean> songs = loadAllSongsIfNeeded();
            ArrayList<MusicBean> results = new ArrayList<>();
            for (MusicBean musicBean : songs) {
                if (isCancelled()) {
                    return results;
                }
                if (matchSong(musicBean, keyword)) {
                    results.add(musicBean);
                }
            }
            return results;
        }

        @Override
        protected void onPostExecute(ArrayList<MusicBean> objects) {
            super.onPostExecute(objects);
            mSearchTask = null;
            updateSearchResults(objects);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            mSearchTask = null;
        }
    }
}
