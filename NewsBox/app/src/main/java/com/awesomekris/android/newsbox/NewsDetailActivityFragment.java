package com.awesomekris.android.newsbox;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsDetailActivityFragment extends Fragment {

    public static final String NEWS_DETAIL = "NEWS_DETAIL";

    private String mHeadline;
    private String mPublicationDate;
    private String mTrailText;
    private String mThumbnail;
    private String mBodyTextSummary;

    private ImageView thumbnailView;
    private TextView headlineView;
    private TextView publicationDateView;
    private TextView trailTextView;
    private TextView bodyTextSummaryView;

    private AdView mAdView;

    public NewsDetailActivityFragment() {
    }

    public NewsDetailActivity getActivityCast() {
        return (NewsDetailActivity) getActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_news_detail, container, false);

        String[] detailNews = getActivity().getIntent().getStringArrayExtra(NEWS_DETAIL);
        mHeadline = detailNews[0];//bundle.getString(NewsDetailActivity.HEADLINE);
        mPublicationDate = detailNews[1];//bundle.getString(NewsDetailActivity.PUBLICATION_DATE);
        mTrailText = detailNews[2];//bundle.getString(NewsDetailActivity.TRAIL_TEXT);
        mThumbnail = detailNews[3];//bundle.getString(NewsDetailActivity.THUMBNAIL);
        mBodyTextSummary = detailNews[4];//bundle.getString(NewsDetailActivity.BODAY_TEXT_SUMMARY);

//        thumbnailView = (ImageView)rootView.findViewById(R.id.detail_thumbnail);
        headlineView = (TextView)rootView.findViewById(R.id.detail_headline);
        publicationDateView = (TextView)rootView.findViewById(R.id.detail_publication_date);
        trailTextView = (TextView)rootView.findViewById(R.id.detail_trail_text);
        bodyTextSummaryView = (TextView)rootView.findViewById(R.id.detail_body_text_summary);

//        Picasso.with(getContext()).load(mThumbnail).into(thumbnailView);
        headlineView.setText(mHeadline);
        publicationDateView.setText(mPublicationDate);
        trailTextView.setText(mTrailText);
        bodyTextSummaryView.setText(mBodyTextSummary);


        mAdView = (AdView)rootView.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        return rootView;
    }
}
