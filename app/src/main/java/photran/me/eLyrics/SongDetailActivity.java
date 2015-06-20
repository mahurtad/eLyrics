package photran.me.eLyrics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import photran.me.h2viewpagerstate.H2ViewPagerState;
import photran.me.lrcview.DefaultLrcBuilder;
import photran.me.lrcview.ILrcBuilder;
import photran.me.lrcview.ILrcView;
import photran.me.lrcview.LrcRow;
import photran.me.lrcview.LrcView;
import photran.me.models.NetworkUtils;
import photran.me.models.SettingUI;
import photran.me.models.SongInforMp3;
import photran.me.visualizer.VisualizerView;

public class SongDetailActivity extends Activity {
    private ImageView imgSingle;
    private TextView txtSongName;
    private TextView txtSingle;
    private ImageButton btnCopy;
    private ImageButton btnPlay;
    private ImageButton btnStop;
    private ImageButton btnReset;
    private ViewPager viewPagerOutput;

    public LrcView lrcView;
    public VisualizerView mVisualizerView;
    private Visualizer mVisualizer;
    private H2ViewPagerState h2Viewpagerstate;

    //Song
    private SongInforMp3 songInforMp3 = null;
    private String mLyrics = null;

    // media
    MediaPlayer mPlayer;
    private int mPalyTimerDuration = 1000;
    private Timer mTimer;
    private TimerTask mTask;

    private ViewPager.OnPageChangeListener onPageChange = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            h2Viewpagerstate.setState(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private View.OnClickListener onCopy = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mLyrics != null) {
                copyTextLyrics(lrcView.getContentLyrics());
                Toast.makeText(v.getContext(),v.getContext().getString(R.string.copy_lyrics),Toast.LENGTH_LONG).show();
            }
        }
    };
    private View.OnClickListener onPlayCliked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                btnPlay.setImageResource(R.drawable.bg_selector_btn_play);
            } else {
                mPlayer.start();
                btnPlay.setImageResource(R.drawable.bg_selector_btn_pauce);
            }
        }
    };
    private View.OnClickListener onStopClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                btnPlay.setImageResource(R.drawable.bg_selector_btn_play);
            }
        }
    };
    private View.OnClickListener onResetCliked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private MediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mp, int percent) {

        }
    };

    private class ViewOutputAdapter extends PagerAdapter {

        public Object instantiateItem(View collection, int position) {
            View v = null;
            switch (position) {
                case 1:
                    v = lrcView;
                    break;
                case 0:

                    v = mVisualizerView;
                    break;
            }

            ((ViewPager) collection).addView(v, 0);

            return v;
        }

        @Override
        public int getCount() {
            return 2;
        }


        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == ((View) arg1);
        }

        @Override
        public Parcelable saveState() {
            return null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_song_detail);
        //actionbar
//        android.app.ActionBar actionBar = getActionBar();
//        actionBar.setDisplayShowTitleEnabled(true);
        initViews();
        //receive data
        receiveData();
    }

    private void receiveData() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String gsonSongString = bundle.getString(SongInforMp3.KEY);
                songInforMp3 = SongInforMp3.getSongFromJson(gsonSongString);
                setDataDishplayOnUI(songInforMp3);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //for LRC
        if (songInforMp3 != null) {
            beginLrcPlay();
            settingVisualizer();

            LyricLoader lyricLoader = new LyricLoader();
            lyricLoader.execute(songInforMp3.getLyrics_file());
        }
    }
    @Override
    protected void onPause() {
        super.onPause();

        if (isFinishing() && mPlayer != null) {
            mVisualizer.release();
            mPlayer.release();
            mPlayer.release();
            mPlayer = null;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupVisualizerFxAndUI() {
        mVisualizer = new Visualizer(mPlayer.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mVisualizer.setDataCaptureListener(new Visualizer.OnDataCaptureListener() {
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
                                              int samplingRate) {
                mVisualizerView.updateVisualizer(bytes);
            }

            public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {}
        }, Visualizer.getMaxCaptureRate() / 2, true, false);
    }

    private void settingVisualizer() {
        setupVisualizerFxAndUI();

        mVisualizer.setEnabled(true);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                mVisualizer.setEnabled(false);
            }
        });
    }
    private void settingLRC(){
//        String lrc = PMHelper.getFromAssets(this, "test.lrc");
        ILrcBuilder builder = new DefaultLrcBuilder();
        List<LrcRow> rows = builder.getLrcRows(mLyrics);

        lrcView.setLrc(rows);
        lrcView.setListener(new ILrcView.LrcViewListener() {

            public void onLrcSeeked(int newPosition, LrcRow row) {
                int time = (int)row.time;
                if (mPlayer != null && mPlayer.getCurrentPosition() > time) {
                    mPlayer.seekTo((int)row.time);
                }
            }
        });
    }
    public void beginLrcPlay(){
        killMediaPlayer();
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(songInforMp3.getSource_128());
            mPlayer.setLooping(true);
            mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mp) {

                    mp.start();
                    if (mTimer == null) {
                        mTimer = new Timer();
                        mTask = new LrcTask();
                        mTimer.scheduleAtFixedRate(mTask, 0, mPalyTimerDuration);
                    }
                }
            });
            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                public void onCompletion(MediaPlayer mp) {
                    stopLrcPlay();
//                    btnPlay.setImageResource(R.drawable.bg_selector_btn_play);
                }
            });
            mPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);

            mPlayer.prepare();
            mPlayer.start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void stopLrcPlay(){
        if(mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
    }
    class LrcTask extends TimerTask{

        long beginTime = -1;

        @Override
        public void run() {
            if(beginTime == -1) {
                beginTime = System.currentTimeMillis();
            }

            final long timePassed = mPlayer.getCurrentPosition();
            SongDetailActivity.this.runOnUiThread(new Runnable() {

                public void run() {
                    lrcView.seekLrcToTime(timePassed);
                }
            });

        }
    };

    //view
    public void initViews() {
        imgSingle = (ImageView) findViewById(R.id.imgSingle);
        txtSongName = (TextView) findViewById(R.id.txtSongName);
        txtSingle = (TextView) findViewById(R.id.txtSingle);

        btnCopy = (ImageButton) findViewById(R.id.btnCopy);
        btnCopy.setOnClickListener(onCopy);

        btnPlay = (ImageButton) findViewById(R.id.btnStatePlay);
        btnPlay.setOnClickListener(onPlayCliked);

        btnStop = (ImageButton) findViewById(R.id.btnStop);
        btnStop.setOnClickListener(onStopClicked);

        btnReset = (ImageButton) findViewById(R.id.btnReplay);
        btnReset.setOnClickListener(onResetCliked);

        viewPagerOutput = (ViewPager) findViewById(R.id.pagerOutput);
        viewPagerOutput.setOnPageChangeListener(onPageChange);
        viewPagerOutput.setAdapter(new ViewOutputAdapter());

        h2Viewpagerstate = (H2ViewPagerState) findViewById(R.id.hViewpagerstate1);
        h2Viewpagerstate.setType(H2ViewPagerState.RECT);

        lrcView = new LrcView(this, null);
        mVisualizerView = new VisualizerView(this);
    }

    private void setDataDishplayOnUI(SongInforMp3 songInforMp3){
        if (songInforMp3 == null) {
            return ;
        }
        Picasso.with(this).load(SettingUI.getImagePreView(songInforMp3.getThumbnail())).into(imgSingle);
        txtSongName.setText(songInforMp3.getTitle());
        txtSingle.setText(songInforMp3.getArtist());
        //play default
        btnPlay.setImageResource(R.drawable.bg_selector_btn_pauce);
    }

    private void copyTextLyrics(String lyrics) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Lyrics",lyrics);
        clipboard.setPrimaryClip(clip);
    }
    private void killMediaPlayer() {
        if(mPlayer != null) {
            try {
                mPlayer.release();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        if (mTimer != null) {
            mTask.cancel();
            mTimer.cancel();
            mTimer.purge();
        }

    }

    private class LyricLoader extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String mContent = NetworkUtils.getContentFormURL(url);
            return mContent;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mLyrics = s;
            if (mLyrics != null) {
                settingLRC();
            }
        }
    }
}
