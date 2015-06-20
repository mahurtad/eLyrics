package photran.me.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.gc.materialdesign.widgets.SnackBar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import photran.me.adapters.YouTubeVideoAdapter;
import photran.me.eLyrics.R;
import photran.me.models.NetworkUtils;
import photran.me.models.PMHelper;
import photran.me.models.SettingUI;
import photran.me.models.VideoYoutube;
import photran.me.videoview.acvitity.OpenYouTubePlayerActivity;

public class YoutubeVideo extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AbsListView.OnScrollListener {
	private static final String DATA_BASE_NAME = "StudyEnglish";
	private ListView lstYoutubeVideo;
	private List<VideoYoutube> modelParses = new ArrayList<VideoYoutube>();
	YouTubeVideoAdapter youTubeVideoAdapter = null;
    private int pageSize = 10;
    private int skip = 0;
    private  SnackBar snackBar = null;
    private View.OnClickListener onTapClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loadListVideos();
        }
    };

    private SnackBar getSnackBar() {
        if (snackBar == null) {
            snackBar = SettingUI.getSnackBarDishPlayErrorNetworking(getActivity(), onTapClicked);
        }
        return  snackBar;
    }

    private boolean isDone = false;
    private boolean isLoadMore = false;
    private boolean isPullToRefesh = false;
    private View footerView;

    private ProgressBar googleProgress;

    private FindCallback<ParseObject> completion = new FindCallback<ParseObject>() {

		@Override
		public void done(List<ParseObject> arg0, ParseException arg1) {
            googleProgress.setVisibility(View.GONE);
            isDone = true;
			if (arg1 != null) {
				Log.v("ParseException", "" + arg1.toString());
                swipeRefreshLayout.setRefreshing(false);
                removeFooter();
                isDone = false;

			} else {
                if (arg0.size() > 0) {
                    for (ParseObject object : arg0) {
                        VideoYoutube modelParse = PMHelper
                                .getVideoYoutubeFormParseObject(object);
                        if (isPullToRefesh) {
                            modelParses.add(0, modelParse);
                        }else {
                            modelParses.add(modelParses.size(), modelParse);
                        }
                    }
                    skip = skip + pageSize;
                    initAdapter();
                    if (isPullToRefesh) {
                        swipeRefreshLayout.setRefreshing(false);
                    }else if (isLoadMore){
                        int index = modelParses.size() - arg0.size();
                        if (index == 0) {
                            index = 1;
                        }
                        changeStateFinishedLoafMore(index - 1);
                    }
                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    removeFooter();
                    isDone = false;
                }
			}
		}
	};
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			VideoYoutube modelParse = youTubeVideoAdapter.getItem(position);

			Intent videoIntent = new Intent(null, Uri.parse("ytv://"
					+ modelParse.getYoutubeId()), getActivity(),
					OpenYouTubePlayerActivity.class);
			startActivity(videoIntent);
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_youtube,
                container,
				false);
		lstYoutubeVideo = (ListView) rootView.findViewById(R.id.list);
        footerView = SettingUI.getFooter(this.getActivity());
        lstYoutubeVideo.addFooterView(footerView);
        lstYoutubeVideo.setOnScrollListener(this);

        googleProgress = (ProgressBar) rootView.findViewById(R.id.google_progress);
        SettingUI.settingProgressbar(googleProgress);

        settingSwipeDownToRefresh(rootView);
		return rootView;
	}
    private SwipeRefreshLayout swipeRefreshLayout;
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
    public void onRefresh() {
        isLoadMore = false;
        isPullToRefesh = true;
        loadListVideos();
    }

	protected void initAdapter() {
		youTubeVideoAdapter = new YouTubeVideoAdapter(modelParses,
				getActivity());
		lstYoutubeVideo.setAdapter(youTubeVideoAdapter);
		lstYoutubeVideo.setOnItemClickListener(onItemClickListener);
	}

    public void callParser(String strName) {
        isDone = false;
        ParseQuery<ParseObject> query = ParseQuery.getQuery(strName);
        query.setLimit(pageSize);
        query.setSkip(skip);
        query.findInBackground(completion);
    }
    private void loadListVideos(){
        if (NetworkUtils.hasConnection(getActivity())) {
            callParser(DATA_BASE_NAME);
           // getSnackBar().dismiss();
        }else {
            if (!getSnackBar().isShowing()) {
                getSnackBar().show();
            }
            googleProgress.setVisibility(View.GONE);

        }
    }

	@Override
	public void onResume() {
		super.onResume();
        loadListVideos();
	}

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisible, int visibleCount, int totalCount) {
        boolean loadMore = firstVisible + visibleCount >= totalCount;
        if (loadMore && isDone) {
            isLoadMore = true;
            isPullToRefesh = false;
            loadListVideos();
        }
    }
    void changeStateFinishedLoafMore(final int index) {
        youTubeVideoAdapter.notifyDataSetChanged();

        lstYoutubeVideo.post(new Runnable() {
            @Override
            public void run() {
                lstYoutubeVideo.smoothScrollToPosition(index);
            }
        });
    }

    void removeFooter() {
        lstYoutubeVideo.post(new Runnable() {
            @Override
            public void run() {
                lstYoutubeVideo.removeFooterView(footerView);
            }
        });
    }
}
