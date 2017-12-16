package com.example.android.sunshine.app;

/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(DetailFragment.DETAIL_URI, getIntent().getData());
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.weather_detail_container, fragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
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
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

//    /**
//     * A placeholder fragment containing a simple view.
//     */
//    public static class PlaceholderFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
//
//        private final static String LOG_TAG = PlaceholderFragment.class.getSimpleName();
//        private final static String FORECAST_SHARE_HASHTAG = " #SunshineApp";
//        private final static int DETAIL_LOADER = 0;
//        private ShareActionProvider mShareActionProvider;
//
//        private String mForecast;
//
//        private String[] FORECAST_COLUMNS = {
//                WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
//                WeatherContract.WeatherEntry.COLUMN_DATE,
//                WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
//                WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
//                WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,};
//
//        private static final int COL_WEATHER_ID = 0;
//        private static final int COL_WEATHER_DATE = 1;
//        private static final int COL_WEATHER_DESC = 2;
//        private static final int COL_WEATHER_MAX_TEMP = 3;
//        private static final int COL_WEATHER_MIN_TEMP = 4;
//
//        public PlaceholderFragment() {
//            setHasOptionsMenu(true);
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//
////            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
////            Intent intent = getActivity().getIntent();
////            if (intent != null ) {
////                mForecast = intent.getDataString();
////            }
////            if (null != mForecast) {
////                ((TextView) rootView.findViewById(R.id.detail_text)).setText(mForecast);
////            }
//
//            return inflater.inflate(R.layout.fragment_detail, container, false);
//        }
//
//        @Override
//        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//            super.onCreateOptionsMenu(menu, inflater);
//
//            inflater.inflate(R.menu.detailfragment, menu);
//
//            MenuItem menuItem = menu.findItem(R.id.action_share);
//
//            mShareActionProvider = (ShareActionProvider)MenuItemCompat.getActionProvider(menuItem);
//
//            if(mShareActionProvider != null) {
//                mShareActionProvider.setShareIntent(createShareForecastIntent());
//            } else {
//                Log.d(LOG_TAG, "Share Action Provider is null?");
//            }
//
//
//        }
//
//        @Override
//        public void onActivityCreated(Bundle savedInstanceState) {
//            getLoaderManager().initLoader(DETAIL_LOADER, null, this);
//            super.onActivityCreated(savedInstanceState);
//        }
//
//        @Override
//        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
//            Log.v(LOG_TAG,"In onCreateLoader");
//            Intent intent = getActivity().getIntent();
//            if (intent == null) {
//                return null;
//            }
//
//            return new CursorLoader(getActivity(),intent.getData(),FORECAST_COLUMNS,null,null,null);
//        }
//
//        @Override
//        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//
//            Log.v(LOG_TAG, "In onLoadFinished");
//            if( !data.moveToFirst()){
//                return;
//            }
//
//            String dateString = Utility.formatDate(data.getLong(COL_WEATHER_DATE));
//            String weatherDescription = data.getString(COL_WEATHER_DESC);
//            boolean isMetric = Utility.isMetric(getActivity());
//            String high = Utility.formatTemperature(getActivity() ,data.getDouble(COL_WEATHER_MAX_TEMP),isMetric);
//            String low = Utility.formatTemperature(getActivity(), data.getDouble(COL_WEATHER_MIN_TEMP),isMetric);
//
//            mForecast = String.format("%s - %s - %s/%s",dateString,weatherDescription,high,low);
//
//            TextView detailTextView = (TextView)getView().findViewById(R.id.detail_text);
//            detailTextView.setText(mForecast);
//
//            if(mShareActionProvider != null) {
//                mShareActionProvider.setShareIntent(createShareForecastIntent());
//            }
//
//        }
//
//        @Override
//        public void onLoaderReset(Loader<Cursor> loader) {
//
//        }
//
//        private Intent createShareForecastIntent(){
//            Intent shareIntent = new Intent(Intent.ACTION_SEND);
//            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
//            shareIntent.setType("text/plain");
//            shareIntent.putExtra(Intent.EXTRA_TEXT,mForecast + FORECAST_SHARE_HASHTAG);
//
//            return shareIntent;
//        }
//
//    }
//}
//
///*
//public class DetailActivity extends ActionBarActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_detail);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//    }
//
//}
//*/