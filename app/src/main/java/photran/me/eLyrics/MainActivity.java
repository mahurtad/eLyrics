package photran.me.eLyrics;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;

import com.parse.Parse;

import java.util.ArrayList;

import photran.me.fragments.ListSongsFragment;
import photran.me.fragments.YoutubeVideo;
import photran.me.navigationdrawer.NavigationDrawerFragment;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private DrawerLayout mDrawerLayout;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_below);

        mTitle = getTitle();

        // Set the ActionBar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        // Setup the menu drawer
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, mDrawerLayout, mToolbar);

        settingParser();
    }

    private void settingParser() {
        String appID = "E8FDGxCEMeJwm58KFK7Vp6luSGoVYzou5ceQMnTN";
        String clientID = "mnUji5Pak7cWFAOeoK5boGOpVhiWolY6C5lTCPf0";
        Parse.initialize(this, appID, clientID);
    }
    private ArrayList<Fragment>listFragments = new ArrayList<>();

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;
        switch (position){
            case 0:
                if (this.listFragments.size() == 0) {
                    fragment =  new ListSongsFragment();
                    this.listFragments.add(fragment);
                }
                break;
            case 1:
                if (this.listFragments.size() == 1) {
                    fragment =  new YoutubeVideo();
                    this.listFragments.add(fragment);
                }

                break;
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, listFragments.get(position))
                .commit();

        onSectionAttached(position);
        setTitle(mTitle);
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.title_section1);
                break;
            case 1:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            restoreActionBar();
            return false;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(Gravity.START)){
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }
}
