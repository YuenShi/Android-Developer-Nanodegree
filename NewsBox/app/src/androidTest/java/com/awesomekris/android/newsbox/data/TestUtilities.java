package com.awesomekris.android.newsbox.data;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.awesomekris.android.newsbox.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/**
 * Created by kris on 16/10/3.
 */
public class TestUtilities extends AndroidTestCase {

    static public final int BULK_INSERT_RECORDS_TO_INSERT = 10;


    static void validateCursor (String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    //checks if all expectedValues are in the the cursor current position. Not the other way around
    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {

        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for(Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }

    }

    static ContentValues createDummySectionValue() {
        ContentValues row = new ContentValues();
        row.put(NewsContract.SectionEntry.COLUMN_SECTION_ID, "100");
        row.put(NewsContract.SectionEntry.COLUMN_WEB_TITLE, "great great great");
        row.put(NewsContract.SectionEntry.COLUMN_WEB_URL, "test web url");
        row.put(NewsContract.SectionEntry.COLUMN_API_URL, "test api url");
        row.put(NewsContract.SectionEntry.COLUMN_IS_SHOWN, "1");
        return row;
    }

    static ContentValues createDummyContentValue() {
        ContentValues row = new ContentValues();
        row.put(NewsContract.ContentEntry.COLUMN_CONTENT_ID, "2");
        row.put(NewsContract.ContentEntry.COLUMN_SECTION_ID, "1");
        row.put(NewsContract.ContentEntry.COLUMN_WEB_PUBLICATION_DATE, "2016-10-03");
        row.put(NewsContract.ContentEntry.COLUMN_HEADLINE, "headline");
        row.put(NewsContract.ContentEntry.COLUMN_TRAIL_TEXT, "trail");
        row.put(NewsContract.ContentEntry.COLUMN_SHORT_URL, "http://test.com");
        row.put(NewsContract.ContentEntry.COLUMN_THUMBNAIL, "thumbnail");
        row.put(NewsContract.ContentEntry.COLUMN_BODY_TEXT_SUMMARY, "body test summary");
        return row;
    }

    static ContentValues[] createBulkInsertSelectionValues() {
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++) {
            ContentValues row = new ContentValues();

            row.put(NewsContract.SectionEntry.COLUMN_SECTION_ID, "id " + i);
            row.put(NewsContract.SectionEntry.COLUMN_WEB_TITLE, "great great great");
            row.put(NewsContract.SectionEntry.COLUMN_WEB_URL, "test");
            row.put(NewsContract.SectionEntry.COLUMN_API_URL, "/test1.com");
            row.put(NewsContract.SectionEntry.COLUMN_IS_SHOWN, "1");
            returnContentValues[i] = row;
        }
        return returnContentValues;
    }

    static ContentValues[] createBulkInsertContentValues() {
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++) {
            ContentValues row = new ContentValues();
            row.put(NewsContract.ContentEntry.COLUMN_CONTENT_ID, "2" + i);
            row.put(NewsContract.ContentEntry.COLUMN_SECTION_ID, "1" + i);
            row.put(NewsContract.ContentEntry.COLUMN_WEB_PUBLICATION_DATE, "2016-10-03");
            row.put(NewsContract.ContentEntry.COLUMN_HEADLINE, "headline");
            row.put(NewsContract.ContentEntry.COLUMN_TRAIL_TEXT, "trail");
            row.put(NewsContract.ContentEntry.COLUMN_SHORT_URL, "http://test.com");
            row.put(NewsContract.ContentEntry.COLUMN_THUMBNAIL, "thumbnail");
            row.put(NewsContract.ContentEntry.COLUMN_BODY_TEXT_SUMMARY, "body test summary");
            returnContentValues[i] = row;
        }
        return returnContentValues;
    }

    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }


}

