package com.awesomekris.android.newsbox;

import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awesomekris.android.newsbox.data.NewsContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class NewsListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String NEWS_LIST_PAGE = "NEWS_LIST_PAGE";
    private static final int CONTENT_LOADER_ID = 0;
    private int mPage;
//    public static final String TAG_ARRAY = "tag_array";

    RecyclerView mRecyclerView;
    LinearLayoutManager mLinearLayoutManager;
    NewsListRecyclerViewAdapter mNewsListRecyclerViewAdapter;
//    ArrayList<String> mTabTitles = new ArrayList<String>();
    String mTabTitle;

    String[] defaultTabTitle;
//            new String[]{"artanddesign","australia-news","better-business","books","business","cardiff","childrens-books-site"
//    ,"cities","commentisfree","community","crosswords","culture","culture-network","culture-professionals-network","edinburgh","education"
//    ,"enterprise-network","environment","extra","fashion","film","football","global-development","global-development-professionals-network","government-computing-network"
//    ,"guardian-professional","healthcare-network","help","higher-education-network","housing-network","info","jobsadvice"
//            ,"katine","law","leeds","lifeandstyle","local","local-government-network","media","media-network","membership"
//           , "money","music","news","politics","public-leaders-network","science","search","small-business-network",
//            "social-care-network","social-enterprise-network","society","society-professionals","sport","stage","teacher-network"
//    ,"technology","theguardian","theobserver","travel","travel/offers","tv-and-radio","uk-news","us-news","voluntary-sector-network","weather","women-in-leadership","world"};

    TextView mEmptyView;



    public static NewsListFragment newInstance(int page) {

        Bundle args = new Bundle();
        args.putInt(NEWS_LIST_PAGE, page);
        NewsListFragment newsListFragment = new NewsListFragment();
        newsListFragment.setArguments(args);
        return newsListFragment;
    }

    public NewsListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Resources res = getResources();
        defaultTabTitle = res.getStringArray(R.array.default_tab_titles);
        mPage = getArguments().getInt(NEWS_LIST_PAGE);
        View rootView = inflater.inflate(R.layout.fragment_news_list, container, false);

        mEmptyView = (TextView) rootView.findViewById(R.id.empty_view);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        Cursor sectionCursor = getActivity().getContentResolver().query(NewsContract.SectionEntry.CONTENT_URI,null,null,null,null);
        sectionCursor.moveToPosition(mPage);
        if(sectionCursor.getCount()!=0) {
            mTabTitle = sectionCursor.getString(sectionCursor.getColumnIndex(NewsContract.SectionEntry.COLUMN_SECTION_ID));

        }else{
            mTabTitle = defaultTabTitle[mPage];
        }
        sectionCursor.close();

        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLinearLayoutManager.setAutoMeasureEnabled(true);
        mNewsListRecyclerViewAdapter = new NewsListRecyclerViewAdapter(getActivity(), null, 1);


        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mNewsListRecyclerViewAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(CONTENT_LOADER_ID,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String selection = NewsContract.ContentEntry.COLUMN_SECTION_ID + " = ?";
        String[] selectionArgs = {mTabTitle};
        String sortOrder = NewsContract.ContentEntry.COLUMN_WEB_PUBLICATION_DATE + " DESC ";
        return new CursorLoader(getActivity(), NewsContract.ContentEntry.CONTENT_URI,null,selection,selectionArgs, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mNewsListRecyclerViewAdapter.swapCursor(cursor);

        if(cursor.getCount() != 0){
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }else {
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNewsListRecyclerViewAdapter.swapCursor(null);
    }


}
