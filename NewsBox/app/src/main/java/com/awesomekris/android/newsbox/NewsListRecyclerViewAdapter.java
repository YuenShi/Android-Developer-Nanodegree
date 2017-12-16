package com.awesomekris.android.newsbox;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.awesomekris.android.newsbox.data.NewsContract;
import com.awesomekris.android.newsbox.library.RecyclerViewCursorAdapter;
import com.squareup.picasso.Picasso;

/**
 * Created by kris on 16/10/4.
 */
public class NewsListRecyclerViewAdapter  extends RecyclerViewCursorAdapter<RecyclerView.ViewHolder> {

    private static final String LOG_TAG = NewsListRecyclerViewAdapter.class.getSimpleName();

//    private Cursor mCursor;
    private Context mContext;

    ActivityOptions mOptions;

    private String mContentId;
    private String mHeadline;
    private String mPublicationDate;
    private String mTrailText;
    private String mThumbnail;
    private String mBodyTextSummary;
    private String mShortUrl;

    public NewsListRecyclerViewAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, Cursor cursor) {

        mContentId = cursor.getString(NewsContract.ContentEntry.COLUMN_INDEX_ID);
        mHeadline = cursor.getString(NewsContract.ContentEntry.COLUMN_INDEX_HEADLINE);
        mPublicationDate = cursor.getString(NewsContract.ContentEntry.COLUMN_INDEX_WEB_PUBLICATION_DATE);
        mTrailText = cursor.getString(NewsContract.ContentEntry.COLUMN_INDEX_TRAIL_TEXT);
        mThumbnail = cursor.getString(NewsContract.ContentEntry.COLUMN_INDEX_THUMBNAIL);
        mBodyTextSummary = cursor.getString(NewsContract.ContentEntry.COLUMN_INDEX_BODY_TEXT_SUMMARY);
        mShortUrl = cursor.getString(NewsContract.ContentEntry.COLUMN_INDEX_SHORT_URL);

        ((ViewHolder)holder).titleView.setText(mHeadline);
        ((ViewHolder)holder).dateView.setText(mPublicationDate);
        ((ViewHolder)holder).trailView.setText(mTrailText);
        Picasso.with(mContext).load(mThumbnail).into(((ViewHolder)holder).thumbnailView);

    }

    @Override
    protected void onContentChanged() {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnailView;
        public TextView titleView;
        public TextView trailView;
        public TextView dateView;

        public ViewHolder(View view) {
            super(view);
            thumbnailView = (ImageView) view.findViewById(R.id.thumbnail);
            titleView = (TextView) view.findViewById(R.id.news_title);
            trailView = (TextView) view.findViewById(R.id.news_trail);
            dateView = (TextView) view.findViewById(R.id.news_date);

        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        int layoutId = R.layout.list_item_news;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: go to detail fragment
//                    view.setTransitionName(mContext.getString(R.string.photo_transition));

                String[] detailNews = new String[]{mHeadline,mPublicationDate,mTrailText,mThumbnail,mBodyTextSummary,mShortUrl};
//                Bundle detailNews = new Bundle();
//                detailNews.putString(mHeadline, NewsDetailActivity.HEADLINE);
//                detailNews.putString(mPublicationDate, NewsDetailActivity.PUBLICATION_DATE);
//                detailNews.putString(mTrailText, NewsDetailActivity.TRAIL_TEXT);
//                detailNews.putString(mThumbnail, NewsDetailActivity.THUMBNAIL);
//                detailNews.putString(mBodyTextSummary, NewsDetailActivity.BODAY_TEXT_SUMMARY);

                Activity activity = (Activity) mContext;
                ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity,
                        vh.thumbnailView,
                        mContext.getString(R.string.photo_transition));

                Intent intent = new Intent(activity, NewsDetailActivity.class);
                intent.putExtra(NewsDetailActivityFragment.NEWS_DETAIL,detailNews);
//                mContext.startActivity(intent);
                ActivityCompat.startActivity(activity, intent, activityOptions.toBundle());
            }
        });
        return vh;
    }

}
