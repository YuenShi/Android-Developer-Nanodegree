package com.awesomekris.android.newsbox.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * Created by kris on 16/10/3.
 */
public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    public void deleteAllRecordsFromProvider() {

        mContext.getContentResolver().delete(
                NewsContract.SectionEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                NewsContract.ContentEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = mContext.getContentResolver().query(
                NewsContract.SectionEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from section table during delete", 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                NewsContract.ContentEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from content table during delete", 0, cursor.getCount());
        cursor.close();
    }

    public void deleteAllRecordsFromDB() {
        NewsDbHelper dbHelper = new NewsDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(NewsContract.ContentEntry.TABLE_NAME, null, null);
        db.delete(NewsContract.SectionEntry.TABLE_NAME, null, null);
        db.close();
    }

    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }

    public void testProviderRegistry(){

        PackageManager pm = mContext.getPackageManager();

        ComponentName componentName = new ComponentName(mContext.getPackageName(), NewsProvider.class.getName());

        try {
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            assertEquals("Error: NewsProvider registered with authority:" + providerInfo.authority +
                    " instead of authority: " + NewsContract.CONTENT_AUTHORITY, providerInfo.authority, NewsContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            assertTrue("Error: NewsProvider not registered at " + mContext.getPackageName(), false);
        }

    }

    public void testGetType() {

        String type;
        // .../section
        type = mContext.getContentResolver().getType(NewsContract.SectionEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.awesomekris.android.app.newsbox/section
        assertEquals("Error: the SectionEntry CONTENT_URI should return SectionEntry.CONTENT_DIR_TYPE",
                NewsContract.SectionEntry.CONTENT_TYPE, type);

        // .../section/1
        long _id = 1;
        type = mContext.getContentResolver().getType(
                NewsContract.SectionEntry.buildSectionItemUriFromId(_id));
        assertEquals("Error: the SectionEntry CONTENT_URI with _id should return SectionEntry.CONTENT_ITEM_TYPE",
                NewsContract.SectionEntry.CONTENT_ITEM_TYPE, type);

        // .../section/search?section_id=1
//        type = mContext.getContentResolver().getType(
//                NewsContract.SectionEntry.buildSectionItemUriFromSectionId(1));
//        assertEquals("Error: the SectionEntry CONTENT_URI with section_id path should return SectionEntry.CONTENT_ITEM_TYPE",
//                NewsContract.SectionEntry.CONTENT_ITEM_TYPE, type);
//
//        // .../section/search?isShown=1
//        type = mContext.getContentResolver().getType(
//                NewsContract.SectionEntry.buildSectionSearchByIsShownUri("1"));
//        assertEquals("Error: the SectionEntry CONTENT_URI with section_id path should return SectionEntry.CONTENT_DIR_TYPE",
//                NewsContract.SectionEntry.CONTENT_TYPE, type);


        // .../content
        type = mContext.getContentResolver().getType(NewsContract.ContentEntry.CONTENT_URI);
        assertEquals("Error: the ContentEntry CONTENT_URI should return ContentrEntry.CONTENT_DIR_TYPE",
                NewsContract.ContentEntry.CONTENT_TYPE, type);

        // .../content/2
        String content_id = "2";
        type = mContext.getContentResolver().getType(
                NewsContract.ContentEntry.buildContentItemUriFromId(2));
        assertEquals("Error: the ContentEntry CONTENT_URI with trailer_id should return ContentEntry.CONTENT_ITEM_TYPE",
                NewsContract.ContentEntry.CONTENT_ITEM_TYPE, type);

    }

    public void testBasicSectionQuery() {
        // insert our test records into the database
        NewsDbHelper dbHelper = new NewsDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues dummySectionValue = TestUtilities.createDummySectionValue();

        long sectionId = db.insert(NewsContract.SectionEntry.TABLE_NAME, null, dummySectionValue);
        assertTrue("Unable to Insert SectionEntry into the Database", sectionId != -1);

        db.close();

        // Test the basic content provider query
        Cursor movieCursor = mContext.getContentResolver().query(
                NewsContract.SectionEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        // Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicSectionQuery", movieCursor, dummySectionValue);
    }


    public void testBasicContentQueries() {
        // insert our test records into the database
        NewsDbHelper dbHelper = new NewsDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues dummySectionValue = TestUtilities.createDummySectionValue();
        long sectionId = db.insert(NewsContract.SectionEntry.TABLE_NAME, null, dummySectionValue);
        assertTrue("Unable to Insert ContentEntry into the Database", sectionId != -1);

        ContentValues dummyContentValue = TestUtilities.createDummyContentValue();
        long contentId = db.insert(NewsContract.ContentEntry.TABLE_NAME, null, dummyContentValue);
        assertTrue("Unable to Insert TrailerEntry into the Database", contentId != -1);

        // Test the basic content provider query
        Cursor trailerCursor = mContext.getContentResolver().query(
                NewsContract.ContentEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

//         Make sure we get the correct cursor out of the database
        TestUtilities.validateCursor("testBasicContentQueries", trailerCursor, dummyContentValue);
    }

    public void testInsertReadProvider() {
        ContentValues testValues = TestUtilities.createDummySectionValue();

        // Register a content observer for our insert.  This time, directly with the content resolver
        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(NewsContract.SectionEntry.CONTENT_URI, true, tco);
        Uri sectionUri = mContext.getContentResolver().insert(NewsContract.SectionEntry.CONTENT_URI, testValues);

        // Did our content observer get called?  Students:  If this fails, your insert location
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        long sectionRowId = ContentUris.parseId(sectionUri);

        // Verify we got a row back.
        assertTrue(sectionRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                NewsContract.SectionEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating sectionEntry.",
                cursor, testValues);


        // Fantastic.  Now that we have a movie, add some trailer!
        ContentValues contentValues = TestUtilities.createDummyContentValue();
        // The TestContentObserver is a one-shot class
        tco = TestUtilities.getTestContentObserver();

        mContext.getContentResolver().registerContentObserver(NewsContract.ContentEntry.CONTENT_URI, true, tco);

        Uri contentInsertUri = mContext.getContentResolver()
                .insert(NewsContract.ContentEntry.CONTENT_URI, contentValues);
        assertTrue("insert content row unsuccessfully", contentInsertUri != null);

        // Did our content observer get called?  Students:  If this fails, your insert weather
        // in your ContentProvider isn't calling
        // getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();
        mContext.getContentResolver().unregisterContentObserver(tco);

        // A cursor is your primary interface to the query results.
        Cursor contentCursor = mContext.getContentResolver().query(
                NewsContract.ContentEntry.CONTENT_URI,  // Table to Query
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // columns to group by
        );

        TestUtilities.validateCursor("testInsertReadProvider. Error validating ContentEntry insert.",
                contentCursor, contentValues);


    }




    public void testUpdateMovie() {
        // Create a new map of values, where column names are the keys
        ContentValues values = TestUtilities.createDummySectionValue();

        Uri sectionUri = mContext.getContentResolver().
                insert(NewsContract.SectionEntry.CONTENT_URI, values);
        long sectionRowId = ContentUris.parseId(sectionUri);

        // Verify we got a row back.
        assertTrue(sectionRowId != -1);
        Log.d(LOG_TAG, "New row id: " + sectionRowId);

        ContentValues updatedValues = new ContentValues(values);
        updatedValues.put(NewsContract.SectionEntry._ID, sectionRowId);
        updatedValues.put(NewsContract.SectionEntry.COLUMN_WEB_TITLE, "Zootopia");

        // Create a cursor with observer to make sure that the content provider is notifying
        // the observers as expected
        Cursor sectionCursor = mContext.getContentResolver().query(NewsContract.SectionEntry.CONTENT_URI, null, null, null, null);

        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
        sectionCursor.registerContentObserver(tco);

        int count = mContext.getContentResolver().update(
                NewsContract.SectionEntry.CONTENT_URI, updatedValues, NewsContract.SectionEntry._ID + "= ?",
                new String[]{Long.toString(sectionRowId)});
        assertEquals(count, 1);

        // Test to make sure our observer is called.  If not, we throw an assertion.
        //
        // Students: If your code is failing here, it means that your content provider
        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
        tco.waitForNotificationOrFail();

        sectionCursor.unregisterContentObserver(tco);
        sectionCursor.close();

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                NewsContract.SectionEntry.CONTENT_URI,
                null,   // projection
                NewsContract.SectionEntry._ID + " = " + sectionRowId,
                null,   // Values for the "where" clause
                null    // sort order
        );

        TestUtilities.validateCursor("testUpdateSection.  Error validating section entry update.",
                cursor, updatedValues);

        cursor.close();
    }

    public void testDeleteRecords() {
        testInsertReadProvider();

        // Register a content observer for our location delete.
        TestUtilities.TestContentObserver sectionObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(NewsContract.SectionEntry.CONTENT_URI, true, sectionObserver);

        // Register a content observer for our weather delete.
        TestUtilities.TestContentObserver contentObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(NewsContract.ContentEntry.CONTENT_URI, true, contentObserver);



        deleteAllRecordsFromProvider();

        // Students: If either of these fail, you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in the ContentProvider
        // delete.  (only if the insertReadProvider is succeeding)
        sectionObserver.waitForNotificationOrFail();
        contentObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(sectionObserver);
        mContext.getContentResolver().unregisterContentObserver(contentObserver);

    }

    public void testBulkInsert() {
        // first, let's create a location value
        ContentValues dummySectionValue = TestUtilities.createDummySectionValue();
        Uri sectionUri = mContext.getContentResolver().insert(NewsContract.SectionEntry.CONTENT_URI, dummySectionValue);
        long sectionId = ContentUris.parseId(sectionUri);

        // Verify we got a row back.
        //assertTrue(movieId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // A cursor is your primary interface to the query results.
        Cursor cursor = mContext.getContentResolver().query(
                NewsContract.SectionEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null  // sort order
        );


        TestUtilities.validateCursor("testBulkInsert. Error validating SectionEntry.",
                cursor, dummySectionValue);

        // Now we can bulkInsert some movies, trailer and reviews.

        ContentValues[] sectionContentValues = TestUtilities.createBulkInsertSelectionValues();
        ContentValues[] contentContentValues = TestUtilities.createBulkInsertContentValues();

        // Register a content observer for our bulk insert.
        // Register a content observer for our location delete.
        TestUtilities.TestContentObserver sectionObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(NewsContract.SectionEntry.CONTENT_URI, true, sectionObserver);

        // Register a content observer for our weather delete.
        TestUtilities.TestContentObserver contentObserver = TestUtilities.getTestContentObserver();
        mContext.getContentResolver().registerContentObserver(NewsContract.ContentEntry.CONTENT_URI, true, contentObserver);


        int insertSectionCount = mContext.getContentResolver().bulkInsert(NewsContract.SectionEntry.CONTENT_URI, sectionContentValues);
        int insertContentCount = mContext.getContentResolver().bulkInsert(NewsContract.ContentEntry.CONTENT_URI, contentContentValues);

        // Students:  If this fails, it means that you most-likely are not calling the
        // getContext().getContentResolver().notifyChange(uri, null); in your BulkInsert
        // ContentProvider method.
        sectionObserver.waitForNotificationOrFail();
        contentObserver.waitForNotificationOrFail();

        mContext.getContentResolver().unregisterContentObserver(sectionObserver);
        mContext.getContentResolver().unregisterContentObserver(contentObserver);

        assertEquals(insertSectionCount, TestUtilities.BULK_INSERT_RECORDS_TO_INSERT);
        assertEquals(insertContentCount, TestUtilities.BULK_INSERT_RECORDS_TO_INSERT);

        // A cursor is your primary interface to the query results.
        Cursor sectionCursor = mContext.getContentResolver().query(
                NewsContract.SectionEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // sort order
        );

        // A cursor is your primary interface to the query results.
        Cursor contentCursor = mContext.getContentResolver().query(
                NewsContract.ContentEntry.CONTENT_URI,
                null, // leaving "columns" null just returns all the columns.
                null, // cols for "where" clause
                null, // values for "where" clause
                null // sort order
        );


        // we should have as many records in the database as we've inserted + the first we inserted
//        assertEquals(sectionCursor.getCount(), TestUtilities.BULK_INSERT_RECORDS_TO_INSERT + 1);
//        assertEquals(contentCursor.getCount(), TestUtilities.BULK_INSERT_RECORDS_TO_INSERT);

        // and let's make sure they match the ones we created
        sectionCursor.moveToFirst();
        TestUtilities.validateCurrentRecord("testBulkInsert. Error validating SectionEntry[0]",
                sectionCursor, dummySectionValue);
        sectionCursor.moveToNext();

        for (int i = 0; i < TestUtilities.BULK_INSERT_RECORDS_TO_INSERT; i++, sectionCursor.moveToNext()) {
            TestUtilities.validateCurrentRecord("testBulkInsert.  Error validating MovieEntry " + (i + 1),
                    sectionCursor, sectionContentValues[i]);
        }
        sectionCursor.close();

        contentCursor.moveToFirst();
        for (int i = 0; i < TestUtilities.BULK_INSERT_RECORDS_TO_INSERT; i++, contentCursor.moveToNext()) {
            TestUtilities.validateCurrentRecord("testBulkInsert.  Error validating trailerEntry " + (i + 1),
                    contentCursor, contentContentValues[i]);
        }
        contentCursor.close();

    }

}


