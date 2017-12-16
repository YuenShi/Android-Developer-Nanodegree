package com.awesomekris.android.newsbox.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kris on 16/10/3.
 */
public class NewsDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "news.db";

    private static final String LOG_TAG = NewsDbHelper.class.getSimpleName();

    public NewsDbHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_SECTION_TABLE = "CREATE TABLE " + NewsContract.SectionEntry.TABLE_NAME
                + " (" +
                NewsContract.SectionEntry._ID + " INTEGER PRIMARY KEY NOT NULL, " +
                NewsContract.SectionEntry.COLUMN_SECTION_ID + " TEXT UNIQUE NOT NULL, " +
                NewsContract.SectionEntry.COLUMN_WEB_TITLE + " TEXT  NOT NULL, " +
                NewsContract.SectionEntry.COLUMN_WEB_URL + " TEXT NOT NULL, " +
                NewsContract.SectionEntry.COLUMN_API_URL + " TEXT NOT NULL, " +
                NewsContract.SectionEntry.COLUMN_IS_SHOWN + " TEXT " +
                ");";

        final String SQL_CREATE_CONTENT_TABLE = "CREATE TABLE " + NewsContract.ContentEntry.TABLE_NAME + " (" +
                NewsContract.ContentEntry._ID + " INTEGER PRIMARY KEY NOT NULL," +
                NewsContract.ContentEntry.COLUMN_CONTENT_ID + " TEXT UNIQUE NOT NULL, " +
                NewsContract.ContentEntry.COLUMN_SECTION_ID + " TEXT NOT NULL, " +
                NewsContract.ContentEntry.COLUMN_WEB_PUBLICATION_DATE + " TEXT NOT NULL, " +
                NewsContract.ContentEntry.COLUMN_HEADLINE + " TEXT NOT NULL, " +
                NewsContract.ContentEntry.COLUMN_TRAIL_TEXT + " TEXT NOT NULL, " +
                NewsContract.ContentEntry.COLUMN_SHORT_URL + " TEXT NOT NULL, " +
                NewsContract.ContentEntry.COLUMN_THUMBNAIL + " TEXT NOT NULL, " +
                NewsContract.ContentEntry.COLUMN_BODY_TEXT_SUMMARY + " TEXT NOT NULL "
                + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_SECTION_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CONTENT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NewsContract.SectionEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NewsContract.ContentEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
