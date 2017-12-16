package com.awesomekris.android.newsbox;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.awesomekris.android.newsbox.data.NewsContract;
import com.awesomekris.android.newsbox.sync.NewsBoxSyncAdapter;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

public class NewsList extends AppCompatActivity {

    private final static String POSITION = "POSITION";

    TabLayout mTabLayout;
    ArrayList<String> mTabArray = new ArrayList<String>();
    ViewPager mViewPager;
    NewsListFragmentPagerAdapter mPagerAdapter;
    //    boolean isConnected;

    String[] defaultTabTitle;
//            new String[]{"artanddesign", "australia-news", "better-business", "books", "business", "cardiff", "childrens-books-site"
//            , "cities", "commentisfree", "community", "crosswords", "culture", "culture-network", "culture-professionals-network", "edinburgh", "education"
//            , "enterprise-network", "environment", "extra", "fashion", "film", "football", "global-development", "global-development-professionals-network", "government-computing-network"
//            , "guardian-professional", "healthcare-network", "help", "higher-education-network", "housing-network", "info", "jobsadvice"
//            , "katine", "law", "leeds", "lifeandstyle", "local", "local-government-network", "media", "media-network", "membership"
//            , "money", "music", "news", "politics", "public-leaders-network", "science", "search", "small-business-network",
//            "social-care-network", "social-enterprise-network", "society", "society-professionals", "sport", "stage", "teacher-network"
//            , "technology", "theguardian", "theobserver", "travel", "travel/offers", "tv-and-radio", "uk-news", "us-news", "voluntary-sector-network", "weather", "women-in-leadership", "world"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NewsBoxSyncAdapter.initializeSyncAdapter(this);
        setContentView(R.layout.activity_news_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ((MyApplication)getApplication()).startTracking();

        boolean isConnected = Utility.isNetworkAvailable(this);
        if(isConnected){
            NewsBoxSyncAdapter.syncImmediately(this);
        }else {
            Toast.makeText(this,"Network unavailable!",Toast.LENGTH_SHORT).show();
        }

        Cursor sectionCursor = getContentResolver().query(NewsContract.SectionEntry.CONTENT_URI, null, null, null, null);
        if (sectionCursor.getCount() != 0) {
            mTabArray.clear();
            while (sectionCursor.moveToNext()) {
                int sectionIndex = sectionCursor.getColumnIndex(NewsContract.SectionEntry.COLUMN_SECTION_ID);
                String sectionName = sectionCursor.getString(sectionIndex);
                mTabArray.add(sectionName);
            }
        } else {
            Resources res = this.getResources();
            defaultTabTitle = res.getStringArray(R.array.default_tab_titles);
            for (String title : defaultTabTitle) {
                mTabArray.add(title);
            }
        }
        sectionCursor.close();

        mTabLayout = (TabLayout) findViewById(R.id.section_tab);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mPagerAdapter = new NewsListFragmentPagerAdapter(getSupportFragmentManager(), this, mTabArray);


        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        
    }

    @Override
    protected void onStart() {
        super.onStart();

        Tracker tracker = ((MyApplication)getApplication()).getTracker();
        tracker.setScreenName("News List Screen");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, mTabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mViewPager.setCurrentItem(savedInstanceState.getInt(POSITION));
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
    public Intent getParentActivityIntent() {
        return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
}
