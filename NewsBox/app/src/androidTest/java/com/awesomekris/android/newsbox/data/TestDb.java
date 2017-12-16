package com.awesomekris.android.newsbox.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by kris on 16/10/3.
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    public void testCreateDb() throws Throwable {

        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(NewsContract.SectionEntry.TABLE_NAME);
        tableNameHashSet.add(NewsContract.ContentEntry.TABLE_NAME);

        mContext.deleteDatabase(NewsDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new NewsDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: This means that the database has not been created correctly", c.moveToFirst());

        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        assertTrue("Error: This means that we were unable to query the database for table informatioin.", tableNameHashSet.isEmpty());

        //MOVIE TABLE
        c = db.rawQuery("PRAGMA table_info(" + NewsContract.SectionEntry.TABLE_NAME + ")", null);
        assertTrue("Error: This means that we were unable to query the database for table information.", c.moveToFirst());

        final HashSet<String> sectionColumnHashSet = new HashSet<String>();
        sectionColumnHashSet.add(NewsContract.SectionEntry._ID);
        sectionColumnHashSet.add(NewsContract.SectionEntry.COLUMN_SECTION_ID);
        sectionColumnHashSet.add(NewsContract.SectionEntry.COLUMN_WEB_TITLE);
        sectionColumnHashSet.add(NewsContract.SectionEntry.COLUMN_WEB_URL);
        sectionColumnHashSet.add(NewsContract.SectionEntry.COLUMN_API_URL);
        sectionColumnHashSet.add(NewsContract.SectionEntry.COLUMN_IS_SHOWN);

        int sectionColumnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(sectionColumnNameIndex);
            sectionColumnHashSet.remove(columnName);
        }while(c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required location entry columns", sectionColumnHashSet.isEmpty());

        c = db.rawQuery("PRAGMA table_info(" + NewsContract.ContentEntry.TABLE_NAME + ")", null);
        assertTrue("Error: This means that we were unable to query the database for table information.", c.moveToFirst());

        final HashSet<String> contentColumnHashSet = new HashSet<String>();
        contentColumnHashSet.add(NewsContract.ContentEntry._ID);
        contentColumnHashSet.add(NewsContract.ContentEntry.COLUMN_CONTENT_ID);
        contentColumnHashSet.add(NewsContract.ContentEntry.COLUMN_SECTION_ID);
        contentColumnHashSet.add(NewsContract.ContentEntry.COLUMN_WEB_PUBLICATION_DATE);
        contentColumnHashSet.add(NewsContract.ContentEntry.COLUMN_HEADLINE);
        contentColumnHashSet.add(NewsContract.ContentEntry.COLUMN_TRAIL_TEXT);
        contentColumnHashSet.add(NewsContract.ContentEntry.COLUMN_THUMBNAIL);
        contentColumnHashSet.add(NewsContract.ContentEntry.COLUMN_BODY_TEXT_SUMMARY);

        int contentColumnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(contentColumnNameIndex);
            contentColumnHashSet.remove(columnName);
        }while(c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required section entry columns", contentColumnHashSet.isEmpty());

        c.close();
        db.close();

    }

    void deleteTheDatabase() {
        mContext.deleteDatabase(NewsDbHelper.DATABASE_NAME);
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        deleteTheDatabase();
    }



    public void testInsertMovieRow() {
        SQLiteDatabase db = new NewsDbHelper(mContext).getWritableDatabase();

        ContentValues dummySectionRow = TestUtilities.createDummySectionValue();
        long rowId = db.insert(NewsContract.SectionEntry.TABLE_NAME, null, dummySectionRow);
        assertTrue(rowId != -1);

        // Query the database and receive a Cursor back
        Cursor cursor = db.query(NewsContract.SectionEntry.TABLE_NAME,
                null, //all columns
                null, //all rows
                null, //
                null, //
                null, //
                null //
        );

        // Move the cursor to a valid database row
        assertTrue("Error: cursor is error or empty", cursor.moveToFirst());


        // Validate data in resulting Cursor with the original ContentValues
        TestUtilities.validateCurrentRecord("Error: Section Query Validation Fail", cursor, dummySectionRow);
        assertFalse("Error: The database has more than one record", cursor.moveToNext());

        cursor.close();
        db.close();
    }

    public void testInsertContentTable() {
        SQLiteDatabase db = new NewsDbHelper(mContext).getWritableDatabase();

        ContentValues dummySectionRow = TestUtilities.createDummySectionValue();
        long sectionId = db.insert(NewsContract.SectionEntry.TABLE_NAME, null, dummySectionRow);
        assertTrue(sectionId != -1);

        ContentValues dummyContentRow = TestUtilities.createDummyContentValue();
        long contentId = db.insert(NewsContract.ContentEntry.TABLE_NAME, null, dummyContentRow);
        assertTrue(contentId != -1);

        // Query the database and receive a Cursor back
        Cursor cursor = db.query(NewsContract.ContentEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        assertTrue(cursor.moveToFirst());

        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)
        TestUtilities.validateCurrentRecord("testInsertReadDb ContentEntry failed to validate",
                cursor, dummyContentRow);
        assertFalse(cursor.moveToNext());

        cursor.close();
        db.close();

    }


}

