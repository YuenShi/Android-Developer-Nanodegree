package com.awesomekris.android.newsbox.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by kris on 16/10/3.
 */
public class NewsContract {

    /*
    * content://com.awesomekris.android.newsbox/section
    *
    * TABLE_NAME:
    *               section
    *
    * COLUMN_NAME:
    *               _ID
    *               section_id
    *               webTitle
    *               webUrl
    *               apiUrl
    *               isShown
    * */

    //Authority is the package name for the app
    public static final String CONTENT_AUTHORITY = "com.awesomekris.android.newsbox";
    //Use CONTENT_AUTHORITY to create the base of all URIs which can be used to contact the content provider
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    //Looking for section, content
    public static final String PATH_SECTION = "section";
    public static final String PATH_CONTENT = "content";

    private static final String LOG_TAG = NewsContract.class.getSimpleName();

    public static final class SectionEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SECTION).build();
        //return dir for section
        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +PATH_SECTION;
        //return item for section
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +PATH_SECTION;
        //table name
        public static final String TABLE_NAME = "section";
        //Column Name
        public static final String COLUMN_SECTION_ID = "section_id";
        public static final String COLUMN_WEB_TITLE = "webTitle";
        public static final String COLUMN_WEB_URL = "webUrl";
        public static final String COLUMN_API_URL = "apiUrl";
        public static final String COLUMN_IS_SHOWN = "isShown";

        public static final String[] SECTION_COLUMNS = {
                _ID,
                COLUMN_SECTION_ID,
                COLUMN_WEB_TITLE,
                COLUMN_WEB_URL,
                COLUMN_API_URL,
                COLUMN_IS_SHOWN,
        };

        public static final int COLUMN_INDEX_ID = 0;
        public static final int COLUMN_INDEX_SECTION_ID = 1;
        public static final int COLUMN_INDEX_WEB_TITLE = 2;
        public static final int COLUMN_INDEX_WEB_URL = 3;
        public static final int COLUMN_INDEX_API_URL = 4;
        public static final int COLUMN_INDEX_IS_SHOWN = 5;

        /*
        dir
        .../section
        .../section/search?isShown = 1


        item
        .../section/id
        .../section/search?section_id = section_id
         */

//        public static final String PATH_SECTION = "section";
        public static final String PATH_SEARCH = "search";

        // .../section/id
        public static Uri buildSectionItemUriFromId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
        //.../section/search?section_id = section_id
        public static Uri buildSectionItemUriFromSectionId(long id) {
            Uri searchUriPath = CONTENT_URI.buildUpon().appendPath(PATH_SEARCH).build();
            return ContentUris.withAppendedId(searchUriPath, id);
        }
        // .../section
        public static Uri buildSectionUri() {
            return NewsContract.SectionEntry.CONTENT_URI.buildUpon().build();
        }

        // .../section/search?isShown = 1
        public static Uri buildSectionSearchByIsShownUri(String isShown) {
            return NewsContract.SectionEntry.CONTENT_URI.buildUpon()
                    .appendPath(PATH_SEARCH).appendQueryParameter(COLUMN_IS_SHOWN,isShown).build();
        }

//        public static Uri buildSortMovieUri(String sortKey) {
//            switch (sortKey) {
//                case PATH_POPULAR:
//                    return buildPopularMoviesUri();
//                case PATH_TOP_RATED:
//                    return buildTopRatedMoviesUri();
//                case PATH_FAVORITE:
//                    return buildFavoriteMoviesUri();
//                default:
//                    Log.e(LOG_TAG, "building sort movie uri, unsupported sort key received");
//                    return null;
//            }
//        }

        //parse  .../section/id
        public static long getIdFromUri(Uri uri){
            return Long.parseLong(uri.getLastPathSegment());
        }

        //parse  .../section/search?isShown = 1
        public static String getIsShownFromUri(Uri uri) {
            System.out.print(uri.getQueryParameter(COLUMN_IS_SHOWN));
            return uri.getQueryParameter(COLUMN_IS_SHOWN);
        }

        //parse  .../section/search?section_id = ?
        public static String getSectionIdFromUri(Uri uri) {
            System.out.print(uri.getQueryParameter(COLUMN_SECTION_ID));
            return uri.getQueryParameter(COLUMN_SECTION_ID);
        }

    }

    /*
    * content://om.awesomekris.android.newsbox/content
    *
    * TABLE_NAME:
    *               content
    *
    * COLUMN_NAME:
    *               _ID
    *               content_id
    *               section_id
    *               web_publication_date
    *               (fields)
    *               headline
    *               trailText
    *               shortUrl
    *               thumbnail
    *               (blocks)
    *               bodyTextSummary
    *
    * */

    public static final class ContentEntry implements BaseColumns {


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONTENT).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTENT;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTENT;

        public static final String PATH_SEARCH = "search";

        public static final String TABLE_NAME = "content";

        public static final String COLUMN_CONTENT_ID = "content_id";
        public static final String COLUMN_SECTION_ID = "section_id";
        public static final String COLUMN_WEB_PUBLICATION_DATE = "web_publication_date";
        public static final String COLUMN_HEADLINE = "headline";
        public static final String COLUMN_TRAIL_TEXT = "trailText";
        public static final String COLUMN_SHORT_URL = "shortUrl";
        public static final String COLUMN_THUMBNAIL = "thumbnail";
        public static final String COLUMN_BODY_TEXT_SUMMARY = "bodyTextSummary";

        public static final String[] CONTENT_COLUMNS = {
                _ID,
                COLUMN_CONTENT_ID,
                COLUMN_SECTION_ID,
                COLUMN_WEB_PUBLICATION_DATE,
                COLUMN_HEADLINE,
                COLUMN_TRAIL_TEXT,
                COLUMN_SHORT_URL,
                COLUMN_THUMBNAIL,
                COLUMN_BODY_TEXT_SUMMARY
        };

        public static final int COLUMN_INDEX_ID = 0;
        public static final int COLUMN_INDEX_CONTENT_ID = 1;
        public static final int COLUMN_INDEX_SECTION_ID = 2;
        public static final int COLUMN_INDEX_WEB_PUBLICATION_DATE = 3;
        public static final int COLUMN_INDEX_HEADLINE = 4;
        public static final int COLUMN_INDEX_TRAIL_TEXT = 5;
        public static final int COLUMN_INDEX_SHORT_URL = 6;
        public static final int COLUMN_INDEX_THUMBNAIL = 7;
        public static final int COLUMN_INDEX_BODY_TEXT_SUMMARY = 8;

        /*
        dir
        .../content
        .../content/search?section_id = section_id
        .../content/search?content_id = content_id

        item
        .../content/id

         */

        // .../content
        public static Uri buildContentUri() {
            return NewsContract.ContentEntry.CONTENT_URI.buildUpon().build();
        }

        // .../content/id
        public static Uri buildContentItemUriFromId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
        // .../content/search?content_id = content_id
        public static Uri buildContentSearchByContentIdUri(String contentId) {
            return CONTENT_URI.buildUpon()
                    .appendPath(PATH_SEARCH)
                    .appendQueryParameter(COLUMN_CONTENT_ID, contentId).build();
        }

        // .../content/search?section_id = section_id
        public static Uri buildContentSearchBySectionIdUri(String contentId) {
            return CONTENT_URI.buildUpon()
                    .appendPath(PATH_SEARCH)
                    .appendQueryParameter(COLUMN_SECTION_ID, contentId).build();
        }

        //parse  .../content/id
        public static long getIdFromUri(Uri uri) {
            return Long.parseLong(uri.getLastPathSegment());
        }
        //parse  .../content/search?content_id = content_id
        public static String getContentIdFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_CONTENT_ID);
        }
        //parse  .../content/search?section_id = section_id
        public static String getSectionIdFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_SECTION_ID);
        }

    }

}
