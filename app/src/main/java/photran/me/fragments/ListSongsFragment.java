package photran.me.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gc.materialdesign.widgets.SnackBar;
import com.google.gson.Gson;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import photran.me.adapters.SongsAdapter;
import photran.me.eLyrics.R;
import photran.me.eLyrics.SongDetailActivity;
import photran.me.models.NetworkUtils;
import photran.me.models.PMHelper;
import photran.me.models.SettingUI;
import photran.me.models.SongInforMp3;

/**
 * Created by ttpho on 25/01/2015.
 */
public class ListSongsFragment extends Fragment implements  SwipeRefreshLayout.OnRefreshListener,
        AdapterView.OnItemClickListener, android.support.v7.widget.SearchView.OnQueryTextListener  {

    private static final String DATA_BASE_NAME = "Songs";
    private GridView grvSongs;
    private SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar googleProgress;
    //data
    private List<SongInforMp3>listSongMp3s = new ArrayList<SongInforMp3>();
    private int pageSize = 10;
    private int skip = 0;
    private FindCallback<ParseObject> completion = new FindCallback<ParseObject>() {

        @Override
        public void done(List<ParseObject> arg0, ParseException arg1) {

            googleProgress.setVisibility(View.GONE);
            if (arg1 != null) {
                Log.v("ParseException", "" + arg1.toString());
            } else {
                for (ParseObject object : arg0) {
                    SongInforMp3 songInforMp3 = PMHelper
                            .getSongInforMp3FormParseObject(object);
                    listSongMp3s.add(0,songInforMp3);
                }
                skip = skip + pageSize;
                Log.v("completion","" + skip + "-" + listSongMp3s.size());
                initAdapter();
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    };
    private  SnackBar snackBar = null;
    private View.OnClickListener onTapClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loadListMusics();
        }
    };

    private SnackBar getSnackBar() {
        if (snackBar == null) {
            snackBar = SettingUI.getSnackBarDishPlayErrorNetworking(getActivity(), onTapClicked);
        }
        return  snackBar;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.global, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        if(searchItem != null) {
            android.support.v7.widget.SearchView
                    searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(searchItem);
            if (searchView != null) {
                searchView.setOnQueryTextListener(this);
            } else {
                Log.v("search", "null");
            }
        }

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list_song, container, false);
        grvSongs = (GridView) rootView.findViewById(R.id.gridViewSongs);
        grvSongs.setOnItemClickListener(this);

        googleProgress = (ProgressBar) rootView.findViewById(R.id.google_progress);
        SettingUI.settingProgressbar(googleProgress);

        settingSwipeDownToRefresh(rootView);

        return rootView;
    }
    private void settingSwipeDownToRefresh(View rootView){
        // for SwipeRefreshLayout don't change
        swipeRefreshLayout = (SwipeRefreshLayout) rootView
                .findViewById(R.id.lySwipeRefresh);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_red_dark);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadListMusics();
    }

    @Override
    public void onRefresh() {
        loadListMusics();
    }

    private void loadListMusics() {
        if (NetworkUtils.hasConnection(getActivity())) {
            callParser(DATA_BASE_NAME);
        } else {
            if (!getSnackBar().isShowing()) {
                getSnackBar().show();
            }
            googleProgress.setVisibility(View.GONE);
        }
    }

    private void callParser(String dataBaseName) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(dataBaseName);
        query.setLimit(pageSize);
        query.setSkip(skip);
        query.findInBackground(completion);
    }
    private void initAdapter() {
        SongsAdapter songsAdapter = new SongsAdapter(getActivity(),listSongMp3s);
        grvSongs.setAdapter(songsAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SongInforMp3 songInforMp3 = listSongMp3s.get(position);

        Intent intent = new Intent(this.getActivity(), SongDetailActivity.class);
        Bundle bundle = new Bundle();
        Gson gsonSong = new Gson();
        String gsonSongString = gsonSong.toJson(songInforMp3);
        bundle.putString(SongInforMp3.KEY, gsonSongString);
        intent.putExtras(bundle);

        startActivity(intent);

    }
    //TODO search implement
    @Override
    public boolean onQueryTextSubmit(String s) {
        Toast.makeText(this.getActivity(), "submit: " + s, Toast.LENGTH_SHORT).show();
        return false;
    }
    //TODO search implement
    @Override
    public boolean onQueryTextChange(String s) {
        Toast.makeText(this.getActivity(), "text: " + s, Toast.LENGTH_SHORT).show();
        return false;
    }
}
