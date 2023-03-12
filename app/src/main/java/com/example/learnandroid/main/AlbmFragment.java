package com.example.learnandroid.main;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.learnandroid.R;
import com.example.learnandroid.adapter.AlbmAdapter;
import com.example.learnandroid.adapter.ArtistAdapter;
import com.example.learnandroid.bean.Album;
import com.example.learnandroid.bean.Artist;
import com.example.learnandroid.data.AlbmLoader;
import com.example.learnandroid.data.ArtistLoader;
import com.example.learnandroid.view.BaseRecyclerView;

import java.util.ArrayList;

public class AlbmFragment extends Fragment {
    private BaseRecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private AlbmAdapter mAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate( R.layout.fragment_albm, container,false);
        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.albm_recycle);
//        recyclerView.setEmptyView(getActivity(), view.findViewById(R.id.list_empty), "No media found");

        setLayoutManager();


        if (getActivity() != null)
            new loadArtists().execute("");
    }


    private void setLayoutManager() {
        layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private class loadArtists extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            ArrayList<Album> artists = AlbmLoader.getArttist();

            if (getActivity() != null)
                mAdapter = new AlbmAdapter(artists);
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            if (mAdapter != null) {
                recyclerView.setAdapter(mAdapter);
            }
//            if (getActivity() != null) {
//                setItemDecoration();
//            }
        }

        @Override
        protected void onPreExecute() {
        }
    }


}
