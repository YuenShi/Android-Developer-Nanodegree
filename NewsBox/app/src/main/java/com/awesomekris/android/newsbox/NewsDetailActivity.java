package com.awesomekris.android.newsbox;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.squareup.picasso.Picasso;

public class NewsDetailActivity extends AppCompatActivity {

//    public static final String HEADLINE = "HEADLINE";
//    public static final String PUBLICATION_DATE = "PUBLICATION_DATE";
//    public static final String TRAIL_TEXT = "TRAIL_TEXT";
//    public static final String THUMBNAIL = "THUMBNAIL";
//    public static final String BODAY_TEXT_SUMMARY = "BODY_TEXT_SUMMARY";
    private ImageView mThumbnailView;
    private String mHeadline;
    private String mShortUrl;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        //implement shared element transition
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        }
        setContentView(R.layout.activity_news_detail);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);

        if(mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        // Hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Show status bar
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);


        mThumbnailView = (ImageView) findViewById(R.id.detail_thumbnail);
        String[] detailNews = getIntent().getStringArrayExtra(NewsDetailActivityFragment.NEWS_DETAIL);
        mHeadline = detailNews[0];
        String mThumbnail = detailNews[3];
        Picasso.with(this).load(mThumbnail).into(mThumbnailView);
        mShortUrl = detailNews[5];
//        Uri uri = Uri.parse(mShortUrl).buildUpon().build();
//        getSupportActionBar().setTitle(mHeadline);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mHeadline + "#" + mShortUrl)
                .getIntent();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Intent.createChooser(shareIntent,Intent.ACTION_SEND));
                Snackbar.make(view, "Share news with friends!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Tracker tracker = ((MyApplication)getApplication()).getTracker();
        tracker.setScreenName("News Detail Screen");
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public Intent getParentActivityIntent() {
        return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }
}
