package com.awesomekris.android.newsbox.widget;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.awesomekris.android.newsbox.NewsDetailActivityFragment;
import com.awesomekris.android.newsbox.R;
import com.awesomekris.android.newsbox.data.NewsContract;

/**
 * Created by kris on 16/10/4.
 */
public class NewsWidgetRemoteViewService extends RemoteViewsService {

    private String mContentId;
    private String mHeadline;
    private String mPublicationDate;
    private String mTrailText;
    private String mThumbnail;
    private String mBodyTextSummary;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            private Cursor data = null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }

                final long identityToken = Binder.clearCallingIdentity();
//                String date = Utility.getStartDate(System.currentTimeMillis());
//                String section = "news";
//                String selection = NewsContract.ContentEntry.COLUMN_SECTION_ID + " = ?";
//                String[] selectionArgs = new String[]{section};
                String sortOrder = NewsContract.ContentEntry.COLUMN_WEB_PUBLICATION_DATE + " DESC ";
                data = getContentResolver().query(NewsContract.ContentEntry.CONTENT_URI, null,null,
                        null, sortOrder);
                Binder.restoreCallingIdentity(identityToken);

            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_today_list_item);

                mContentId = data.getString(NewsContract.ContentEntry.COLUMN_INDEX_ID);
                mHeadline = data.getString(NewsContract.ContentEntry.COLUMN_INDEX_HEADLINE);
                mPublicationDate = data.getString(NewsContract.ContentEntry.COLUMN_INDEX_WEB_PUBLICATION_DATE);
                mTrailText = data.getString(NewsContract.ContentEntry.COLUMN_INDEX_TRAIL_TEXT);
                mThumbnail = data.getString(NewsContract.ContentEntry.COLUMN_INDEX_THUMBNAIL);
                mBodyTextSummary = data.getString(NewsContract.ContentEntry.COLUMN_INDEX_BODY_TEXT_SUMMARY);

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
//                    setRemoteContentDescription(views, description);
//                }
                views.setTextViewText(R.id.widget_headline, mHeadline);
                views.setTextViewText(R.id.widget_trail_text, mTrailText);

                String[] detailNews = new String[]{mHeadline,mPublicationDate,mTrailText,mThumbnail,mBodyTextSummary};

                final Intent fillInIntent = new Intent();
                fillInIntent.putExtra(NewsDetailActivityFragment.NEWS_DETAIL,detailNews);

                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_today_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }


}
