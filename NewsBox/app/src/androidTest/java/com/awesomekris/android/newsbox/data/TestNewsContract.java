package com.awesomekris.android.newsbox.data;

import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by kris on 16/10/3.
 */
public class TestNewsContract extends AndroidTestCase {

    private static final long TEST_SECTION_ID = 1;
    private static final long TEST_CONTENT_ID = 2;
    private static final String Test_SECTION_IS_SHOWN = "1";

    public void testBuildSection() {

        Uri sectionUri = NewsContract.SectionEntry.buildSectionItemUriFromId(TEST_SECTION_ID);
        assertNotNull("Error: Null Uri returned. You must fill-in buildSectionUriFromId in " + "SectionEntry." + sectionUri);
        assertEquals("Error: Section ID not properly appended to the end of the Uri", TEST_SECTION_ID, Long.parseLong(sectionUri.getLastPathSegment()));
        assertEquals("Error: SectionId Uri doesn't match our expected result", sectionUri.toString(),"content://com.awesomekris.android.newsbox/section/1");

        Uri sectionByIsShownUri = NewsContract.SectionEntry.buildSectionSearchByIsShownUri(Test_SECTION_IS_SHOWN);
        assertNotNull("Error: Null Uri returned. You must fill-in buildContentUriFromId in " + "ContentEntry." + sectionByIsShownUri);
        assertEquals("Error: Section by Section ID not properly appended to the end of the Uri", Test_SECTION_IS_SHOWN, sectionByIsShownUri.getQueryParameters(NewsContract.SectionEntry.COLUMN_IS_SHOWN).get(0));
        assertEquals("Error: Section by Section ID Uri doesn't match our expected result", sectionByIsShownUri.toString(),"content://com.awesomekris.android.newsbox/section/search?isShown=1");



    }

    public void testBuildContent() {

        Uri contentUri = NewsContract.ContentEntry.buildContentItemUriFromId(TEST_CONTENT_ID);
        assertNotNull("Error: Null Uri returned. You must fill-in buildVideoUri in " + "VideoEntry." + contentUri);
        assertEquals("Error: Video ID not properly appended to the end of the Uri", TEST_CONTENT_ID, Long.parseLong(contentUri.getLastPathSegment()));
        assertEquals("Error: VideoId Uri doesn't match our expected result", contentUri.toString(),"content://com.awesomekris.android.newsbox/content/2");

        Uri contentBySectionIdUri = NewsContract.ContentEntry.buildContentSearchBySectionIdUri("1");
        assertNotNull("Error: Null Uri returned. You must fill-in buildReviewUri in " + "ContentEntry." + contentBySectionIdUri);
        assertEquals("Error: Content by Section ID not properly appended to the end of the Uri", Long.toString(TEST_SECTION_ID), contentBySectionIdUri.getQueryParameters(NewsContract.ContentEntry.COLUMN_SECTION_ID).get(0));
        assertEquals("Error: Content by Section ID Uri doesn't match our expected result", contentBySectionIdUri.toString(),"content://com.awesomekris.android.newsbox/content/search?section_id=1");


    }

}

