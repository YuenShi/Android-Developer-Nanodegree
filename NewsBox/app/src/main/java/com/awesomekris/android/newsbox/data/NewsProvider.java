package com.awesomekris.android.newsbox.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by kris on 16/10/3.
 */
public class NewsProvider extends ContentProvider {

    static final int SECTION = 100;
    static final int SECTION_ITEM = 101;
    static final int SECTION_SEARCH_BY_SECTION_ID= 102;
    static final int SECTION_SEARCH_BY_IS_SHOWN = 103;

    final static int CONTENT = 200;
    final static int CONTENT_ITEM = 201;
    final static int CONTENT_SEARCH_BY_CONTENT_ID = 202;
    final static int CONTENT_SEARCH_BY_SECTION_ID = 203;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private NewsDbHelper mNewsHelper;

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = NewsContract.CONTENT_AUTHORITY;

        // .../section
        matcher.addURI(authority, NewsContract.PATH_SECTION, SECTION);
        // .../section/id
        matcher.addURI(authority, NewsContract.PATH_SECTION + "/#", SECTION_ITEM);
        // .../section/search?isShown=?
        matcher.addURI(authority, NewsContract.PATH_SECTION + "/" + NewsContract.SectionEntry.PATH_SEARCH, SECTION_SEARCH_BY_IS_SHOWN);


        // .../content
        matcher.addURI(authority, NewsContract.PATH_CONTENT, CONTENT);
        // .../content/id
        matcher.addURI(authority, NewsContract.PATH_CONTENT + "/#", CONTENT_ITEM);
        // .../content/search?section_id=?
        matcher.addURI(authority, NewsContract.PATH_CONTENT + "/" + NewsContract.ContentEntry.PATH_SEARCH, CONTENT_SEARCH_BY_SECTION_ID);

//        // .../movie/detail/movie_id
//        matcher.addURI(authority,
//                MovieContract.PATH_MOVIE + "/" + MovieContract.MovieEntry.PATH_DETAIL + "/#",
//                MOVIE_DETAIL);
        return matcher;

    }

    @Override
    public boolean onCreate() {
        mNewsHelper = new NewsDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case SECTION:
                return NewsContract.SectionEntry.CONTENT_TYPE;
            case SECTION_ITEM:
                return NewsContract.SectionEntry.CONTENT_ITEM_TYPE;
            case SECTION_SEARCH_BY_IS_SHOWN:
                return NewsContract.SectionEntry.CONTENT_TYPE;
            case SECTION_SEARCH_BY_SECTION_ID:
                return NewsContract.SectionEntry.CONTENT_TYPE;
            case CONTENT:
                return NewsContract.ContentEntry.CONTENT_TYPE;
            case CONTENT_ITEM:
                return NewsContract.ContentEntry.CONTENT_ITEM_TYPE;
            case CONTENT_SEARCH_BY_SECTION_ID:
                return NewsContract.ContentEntry.CONTENT_TYPE;
            case CONTENT_SEARCH_BY_CONTENT_ID:
                return NewsContract.ContentEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

    //section._ID = ?
    private static final String sSectionByIdSelection =
            NewsContract.SectionEntry.TABLE_NAME + "."
                    + NewsContract.SectionEntry._ID + " = ? ";

    //section.section_id = ?
    private static final String sSectionBySectionIdSelection =
            NewsContract.SectionEntry.TABLE_NAME + "."
                    + NewsContract.SectionEntry.COLUMN_SECTION_ID + " = ? ";

    //section.isShown = ?
    private static final String sSectionByIsShownSelection =
            NewsContract.SectionEntry.TABLE_NAME + "."
                    + NewsContract.SectionEntry.COLUMN_IS_SHOWN + " = ? ";

    //content._ID = ?
    private static final String sContentByIdSelection =
            NewsContract.ContentEntry.TABLE_NAME + "."
                    + NewsContract.ContentEntry._ID + " = ? ";

    //content.section_id = ?
    private static final String sContentBySectionIdSelection =
            NewsContract.ContentEntry.TABLE_NAME + "."
                    + NewsContract.ContentEntry.COLUMN_SECTION_ID + " = ? ";

    //content.content_id = ?
    private static final String sContentByContentIdSelection =
            NewsContract.ContentEntry.TABLE_NAME + "."
                    + NewsContract.ContentEntry.COLUMN_CONTENT_ID + " = ? ";


    //content sort by date desc order
    private static final String dateSortOrder =
            NewsContract.ContentEntry.COLUMN_WEB_PUBLICATION_DATE + " DESC";

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;
        SQLiteDatabase db = mNewsHelper.getReadableDatabase();

        switch (sUriMatcher.match(uri)){
            case SECTION: {
                retCursor = db.query(
                        NewsContract.SectionEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case SECTION_ITEM:
            {
                Long _id = NewsContract.SectionEntry.getIdFromUri(uri);
                String sectionItemSelection = sSectionByIdSelection;
                String [] sectionItenSelectionArgs = new String[]{Long.toString(_id)};
                retCursor = db.query(
                        NewsContract.SectionEntry.TABLE_NAME,
                        projection,
                        sectionItemSelection,
                        sectionItenSelectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case SECTION_SEARCH_BY_IS_SHOWN:
            {
                String isShownSelection = sSectionByIsShownSelection;
                String[] isShownSelectionArgs = new String[]{"1"};
                retCursor = db.query(
                        NewsContract.SectionEntry.TABLE_NAME,
                        projection,
                        isShownSelection,
                        isShownSelectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case SECTION_SEARCH_BY_SECTION_ID:
                String  sectionId = NewsContract.SectionEntry.getSectionIdFromUri(uri);
                String sectionSelection = sSectionBySectionIdSelection;
                String[] sectionSelectionArgs = new String[]{sectionId};
                retCursor = db.query(
                        NewsContract.SectionEntry.TABLE_NAME,
                        projection,
                        sectionSelection,
                        sectionSelectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case CONTENT:
                retCursor = db.query(
                        NewsContract.ContentEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        dateSortOrder
                );
                break;
            case CONTENT_ITEM:
                Long id = NewsContract.ContentEntry.getIdFromUri(uri);
                String contentSelection = sContentByIdSelection;
                String[] contentSelectionArgs = new String[]{Long.toString(id)};
                retCursor = db.query(
                        NewsContract.ContentEntry.TABLE_NAME,
                        projection,
                        contentSelection,
                        contentSelectionArgs,
                        null,
                        null,
                        dateSortOrder
                );
                break;
            case CONTENT_SEARCH_BY_SECTION_ID:
                String searchSectionId = NewsContract.ContentEntry.getSectionIdFromUri(uri);
                String searchSectionSelection = sContentBySectionIdSelection;
                String[] searchSectionSelectionArgs = new String[]{searchSectionId};
                retCursor = db.query(
                        NewsContract.ContentEntry.TABLE_NAME,
                        projection,
                        searchSectionSelection,
                        searchSectionSelectionArgs,
                        null,
                        null,
                        dateSortOrder
                );
                break;
            case CONTENT_SEARCH_BY_CONTENT_ID:
                String searchContentId = NewsContract.ContentEntry.getContentIdFromUri(uri);
                String searchContentSelection = sContentByContentIdSelection;
                String[] searchContentSelectionArgs = new String[]{searchContentId};
                retCursor = db.query(
                        NewsContract.ContentEntry.TABLE_NAME,
                        projection,
                        searchContentSelection,
                        searchContentSelectionArgs,
                        null,
                        null,
                        dateSortOrder
                );
                break;
//            case MOVIE_DETAIL:
//                Long movie_id = NewsContract.SectionEntry.getIdFromUri(uri);
//                Cursor cursorDetail = query(NewsContract.SectionEntry.buildMovieSearchByMovieIdUri(movie_id), NewsContract.SectionEntry.MOVIE_COLUMNS, null, null, null);
//                Cursor cursorTrailer = query(NewsContract.ContentEntry.buildTrailerSearchByfMovieIdUri(movie_id), NewsContract.ContentEntry.TRAILER_COLUMNS, null, null, null);
//                Cursor cursorReview = query(NewsContract.ReviewEntry.buildReviewSearchByMovieId(movie_id), NewsContract.ReviewEntry.REVIEW_COLUMNS, null, null, null);
//                Cursor[] cursors = new Cursor[]{cursorDetail, cursorTrailer, cursorReview};
//                retCursor = new MergeCursor(cursors);
//                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mNewsHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri retUri;

        switch (match) {
            case SECTION: {
                long _id = db.insert(NewsContract.SectionEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    retUri = NewsContract.SectionEntry.buildSectionItemUriFromId(_id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            }
            case CONTENT: {
                long _id = db.insert(NewsContract.ContentEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    retUri = NewsContract.ContentEntry.buildContentItemUriFromId(_id);
                else
                    throw new SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return retUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mNewsHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);

        int rowsDeleted = 0;

        if (null == selection) selection = "1";

        switch (match) {
            case SECTION:
                rowsDeleted = db.delete(NewsContract.SectionEntry.TABLE_NAME,selection,selectionArgs);
                break;
            case CONTENT:
                rowsDeleted = db.delete(NewsContract.ContentEntry.TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknow Uri: " + uri);
        }

        if (rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mNewsHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int rowsUpdated = 0;

        switch (match){
            case SECTION:
                rowsUpdated = db.update(NewsContract.SectionEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            case CONTENT:
                rowsUpdated = db.update(NewsContract.ContentEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknow Uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mNewsHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case SECTION:
                db.beginTransaction();
                int sectionReturnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(NewsContract.SectionEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            sectionReturnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return sectionReturnCount;
            case CONTENT:
                db.beginTransaction();
                int contentReturnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(NewsContract.ContentEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            contentReturnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return contentReturnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mNewsHelper.close();
        super.shutdown();
    }



}
