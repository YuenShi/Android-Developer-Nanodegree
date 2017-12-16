package com.udacity.gradle.builditbigger;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by kris on 16/9/16.
 */
public class TestJokeAsyncTask extends ActivityInstrumentationTestCase2<MainActivity> {

   public TestJokeAsyncTask(){
       super(MainActivity.class);
   }

    private MainActivity mMainActivity;
    private String mJoke;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMainActivity = getActivity();
    }

    public void testJokeAsyncTask(){

        try {
            new JokeAsyncTask(){
                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    mJoke = s;
                }
            }.execute();

            assertNotNull(mJoke);
            assertFalse(mJoke.length() == 0);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}