package com.awesomekris.android.newsbox.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.awesomekris.android.newsbox.BuildConfig;
import com.awesomekris.android.newsbox.R;
import com.awesomekris.android.newsbox.Utility;
import com.awesomekris.android.newsbox.data.NewsContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by kris on 16/10/3.
 */
public class NewsBoxSyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String LOG_TAG = NewsBoxSyncAdapter.class.getSimpleName();
    public static final String ACTION_DATA_UPDATED = "com.awesomekris.android.newsbox.ACTION_DATA_UPDATED";
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;

    private ArrayList<String> mSectionIdList = new ArrayList<String>();

    private String[] defaultTabTitle;

//            new String[]{"artanddesign","australia-news","better-business","books","business","cardiff","childrens-books-site"
//            ,"cities","commentisfree","community","crosswords","culture","culture-network","culture-professionals-network","edinburgh","education"
//            ,"enterprise-network","environment","extra","fashion","film","football","global-development","global-development-professionals-network","government-computing-network"
//            ,"guardian-professional","healthcare-network","help","higher-education-network","housing-network","info","jobsadvice"
//            ,"katine","law","leeds","lifeandstyle","local","local-government-network","media","media-network","membership"
//            , "money","music","news","politics","public-leaders-network","science","search","small-business-network",
//            "social-care-network","social-enterprise-network","society","society-professionals","sport","stage","teacher-network"
//            ,"technology","theguardian","theobserver","travel","travel/offers","tv-and-radio","uk-news","us-news","voluntary-sector-network","weather","women-in-leadership","world"};


    public NewsBoxSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        defaultTabTitle = context.getResources().getStringArray(R.array.default_tab_titles);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        Log.d(LOG_TAG, "Starting sync");

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String responseJsonStr = null;
        String BASE_URL = "http://content.guardianapis.com/";
        String PATH_SECTION = "sections";
        String PATH_SEARCH = "search";

        String PARAM_API_KEY = "api-key";
        String PARAM_SECTION = "section";
        String PARAM_FORMAT = "format";
        String PARAM_FROM_DATE = "from-date";
        String PARAM_SHOW_FIELDS = "show-fields";
        String PARAM_SHOW_BLOCKS = "show-blocks";

        String FORMAT = "json";
        String FROM_DATE = Utility.getStartDate(System.currentTimeMillis());
        String SHOW_FIELDS = "starRating,headline,thumbnail,trailText,short-url";
        String SHOW_BLOCKS = "body";

        //Get Section
        try {
            if (mSectionIdList.size() != 0) {
                mSectionIdList.clear();
            }

            Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                    .appendPath(PATH_SECTION)
                    .appendQueryParameter(PARAM_API_KEY, BuildConfig.THE_GUARDIAN_API_KEY).build();

            Log.v(LOG_TAG, "Build Uri:" + buildUri);

            URL url = new URL(buildUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return;
            }

            responseJsonStr = buffer.toString();
            getSectionFromJsonStr(responseJsonStr);

        } catch (IOException e) {

            Log.e(LOG_TAG, "Error ", e);

        } catch (JSONException e) {

            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();

        } finally {

            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        if (mSectionIdList.size() == 0) {
            for (String title : defaultTabTitle) {
                mSectionIdList.add(title);
            }
        }

        //Get Content
        for (String sectionId : mSectionIdList) {

            try {

                Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                        .appendPath(PATH_SEARCH)
                        .appendQueryParameter(PARAM_SECTION, sectionId)
                        .appendQueryParameter(PARAM_FORMAT, FORMAT)
                        .appendQueryParameter(PARAM_FROM_DATE, FROM_DATE)
                        .appendQueryParameter(PARAM_SHOW_FIELDS, SHOW_FIELDS)
                        .appendQueryParameter(PARAM_SHOW_BLOCKS, SHOW_BLOCKS)
                        .appendQueryParameter(PARAM_API_KEY, BuildConfig.THE_GUARDIAN_API_KEY).build();

                Log.v(LOG_TAG, "Build Uri:" + buildUri);

                URL url = new URL(buildUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");

                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return;
                }

                responseJsonStr = buffer.toString();
                getContentFromJsonStr(responseJsonStr);


            } catch (IOException e) {

                Log.e(LOG_TAG, "Error ", e);

            } catch (JSONException e) {

                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();

            } finally {

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

        }

        return;
    }

    private void getSectionFromJsonStr(String responseJsonStr) throws JSONException {

        final String RESPONSE = "response";
        final String RESULTS = "results";

        final String ID = "id";
        final String WEB_TITLE = "webTitle";
        final String WEB_URL = "webUrl";
        final String API_URL = "apiUrl";

        try {
            JSONObject resultObject = new JSONObject(responseJsonStr);
            JSONObject responseJsonObject = resultObject.getJSONObject(RESPONSE);
            JSONArray resultsArray = responseJsonObject.getJSONArray(RESULTS);
            int resultNumber = resultsArray.length();
            Vector<ContentValues> cVVector = new Vector<ContentValues>(resultNumber);

            for (int i = 0; i < resultNumber; i++) {
                String id;
                String web_title;
                String web_url;
                String api_url;
                //isShown

                JSONObject jsonObject = resultsArray.getJSONObject(i);

                id = jsonObject.getString(ID);
                web_title = jsonObject.getString(WEB_TITLE);
                web_url = jsonObject.getString(WEB_URL);
                api_url = jsonObject.getString(API_URL);

                ContentValues sectionValues = new ContentValues();

                sectionValues.put(NewsContract.SectionEntry.COLUMN_SECTION_ID, id);
                sectionValues.put(NewsContract.SectionEntry.COLUMN_WEB_TITLE, web_title);
                sectionValues.put(NewsContract.SectionEntry.COLUMN_WEB_URL, web_url);
                sectionValues.put(NewsContract.SectionEntry.COLUMN_API_URL, api_url);

                cVVector.add(sectionValues);
            }

            bulkInsertAndUpdateSectionTable(cVVector);

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

    }

    private void getContentFromJsonStr(String responseJsonStr) throws JSONException {

        final String RESPONSE = "response";
        final String RESULTS = "results";

        final String ID = "id"; //content id
        final String SECTION_ID = "sectionId";
        final String WEB_PUBLICATION_DATE = "webPublicationDate";

        final String FIELDS = "fields";
        final String HEADLINE = "headline";
        final String TRAIL_TEXT = "trailText";
        final String SHORT_URL = "shortUrl";
        final String THUMBNAIL = "thumbnail";

        final String BLOCKS = "blocks";
        final String BODY = "body";
        final String BODY_TEXT_SUMMARY = "bodyTextSummary";

        try {
            JSONObject resultObject = new JSONObject(responseJsonStr);
            JSONObject responseJsonObject = resultObject.getJSONObject(RESPONSE);
            JSONArray resultsArray = responseJsonObject.getJSONArray(RESULTS);
            int resultNumber = resultsArray.length();
            Vector<ContentValues> cVVector = new Vector<ContentValues>(resultNumber);

            for (int i = 0; i < resultNumber; i++) {
                String content_id;
                String section_id;
                String web_publication_date;

                JSONObject fields;
                String headline;
                String trail_text;
                String short_url;
                String thumbnail;

                JSONObject block;
                JSONArray body;
                JSONObject bodyObject;
                String body_text_summary;


                JSONObject jsonObject = resultsArray.getJSONObject(i);

                content_id = jsonObject.getString(ID);
                section_id = jsonObject.getString(SECTION_ID);
                web_publication_date = jsonObject.getString(WEB_PUBLICATION_DATE);

                fields = jsonObject.getJSONObject(FIELDS);
                headline = fields.getString(HEADLINE);
                trail_text = fields.getString(TRAIL_TEXT);
                short_url = fields.getString(SHORT_URL);
                thumbnail = fields.getString(THUMBNAIL);

                block = jsonObject.getJSONObject(BLOCKS);
                body = block.getJSONArray(BODY);
                bodyObject = body.getJSONObject(0);
                body_text_summary = bodyObject.getString(BODY_TEXT_SUMMARY);

                ContentValues sectionValues = new ContentValues();

                sectionValues.put(NewsContract.ContentEntry.COLUMN_CONTENT_ID, content_id);
                sectionValues.put(NewsContract.ContentEntry.COLUMN_SECTION_ID, section_id);
                sectionValues.put(NewsContract.ContentEntry.COLUMN_WEB_PUBLICATION_DATE, web_publication_date);
                sectionValues.put(NewsContract.ContentEntry.COLUMN_HEADLINE, headline);
                sectionValues.put(NewsContract.ContentEntry.COLUMN_TRAIL_TEXT, trail_text);
                sectionValues.put(NewsContract.ContentEntry.COLUMN_SHORT_URL, short_url);
                sectionValues.put(NewsContract.ContentEntry.COLUMN_THUMBNAIL, thumbnail);
                sectionValues.put(NewsContract.ContentEntry.COLUMN_BODY_TEXT_SUMMARY, body_text_summary);

                cVVector.add(sectionValues);
            }

            bulkInsertAndUpdateContentTable(cVVector);

        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

    }

    private void bulkInsertAndUpdateSectionTable(Vector<ContentValues> cVVector) {

        int inserted = 0;

        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);

            List<ContentValues> toInsertSectionContentValues = new ArrayList<>();

            int updatedrow = 0;
            String selection = NewsContract.SectionEntry.COLUMN_SECTION_ID + " = ? ";

            for (ContentValues contentValues : cvArray) {

                String sectionId = contentValues.getAsString(NewsContract.SectionEntry.COLUMN_SECTION_ID);
                mSectionIdList.add(sectionId);

                String[] selectionArgs = {sectionId};
                Cursor cursor = getContext().getContentResolver().query(NewsContract.SectionEntry.CONTENT_URI, null, selection, selectionArgs, null);
                if (cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    getContext().getContentResolver().update(NewsContract.SectionEntry.CONTENT_URI, contentValues, selection, selectionArgs);
                    updatedrow++;
                } else {
                    toInsertSectionContentValues.add(contentValues);
                    Log.d(LOG_TAG, "UpdateSectionTask Complete. " + updatedrow + " Updated");
                }

                cursor.close();
            }
            inserted = getContext().getContentResolver().bulkInsert(NewsContract.SectionEntry.CONTENT_URI,
                    toInsertSectionContentValues.toArray(new ContentValues[toInsertSectionContentValues.size()]));
        }
        Log.d(LOG_TAG, "FetchSectionTask Complete. " + inserted + " Inserted");
    }

    private void bulkInsertAndUpdateContentTable(Vector<ContentValues> cVVector) {

        int inserted = 0;

        if (cVVector.size() > 0) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);

            List<ContentValues> toInsertContentContentValues = new ArrayList<>();

            int updatedrow = 0;
            String selection = NewsContract.ContentEntry.COLUMN_CONTENT_ID + " = ? ";

            for (ContentValues contentValues : cvArray) {

                String contentId = contentValues.getAsString(NewsContract.ContentEntry.COLUMN_CONTENT_ID);

                String[] selectionArgs = {contentId};
                Cursor cursor = getContext().getContentResolver().query(NewsContract.ContentEntry.CONTENT_URI, null, selection, selectionArgs, null);
                if (cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    getContext().getContentResolver().update(NewsContract.ContentEntry.CONTENT_URI, contentValues, selection, selectionArgs);
                    updatedrow++;
                } else {
                    toInsertContentContentValues.add(contentValues);
                    Log.d(LOG_TAG, "UpdateContentTask Complete. " + updatedrow + " Updated");
                }

                cursor.close();
            }
            inserted = getContext().getContentResolver().bulkInsert(NewsContract.ContentEntry.CONTENT_URI,
                    toInsertContentContentValues.toArray(new ContentValues[toInsertContentContentValues.size()]));
            updateWidgets();
        }
        Log.d(LOG_TAG, "FetchContentTask Complete. " + inserted + " Inserted");
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }

    public static Account getSyncAccount(Context context) {
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Create the account type and default account
        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        // If the password doesn't exist, the account doesn't exist
        if (null == accountManager.getPassword(newAccount)) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account newAccount, Context context) {
                /*
         * Since we've created an account
         */
        NewsBoxSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

                        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

                       /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account, authority, new Bundle(), syncInterval);
        }
    }

    private void updateWidgets() {
        Context context = getContext();
        // Setting the package ensures that only components in our app will receive the broadcast
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED)
                .setPackage(context.getPackageName());
        context.sendBroadcast(dataUpdatedIntent);
    }

}
