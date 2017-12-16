package com.awesomekris.android.newsbox;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by kris on 16/10/3.
 */
public class NewsListFragmentPagerAdapter extends FragmentPagerAdapter{

    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[]{"tab1","tab2","tab3"};

    private Context mContext;
    private ArrayList<String> mTabTitle = new ArrayList<String >();
    private int mTotal;



    public NewsListFragmentPagerAdapter(FragmentManager fm, Context context, ArrayList<String> tabTitle) {
        super(fm);
        this.mContext = context;
        mTabTitle = tabTitle;
        mTotal = mTabTitle.size();
    }

    @Override
    public Fragment getItem(int position) {

        return NewsListFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        if(mTotal != 0){
            return mTotal;
        }
        else
            return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if(mTabTitle.size() != 0){
            return mTabTitle.get(position);
        }
        else
            return tabTitles[position];
    }
}
