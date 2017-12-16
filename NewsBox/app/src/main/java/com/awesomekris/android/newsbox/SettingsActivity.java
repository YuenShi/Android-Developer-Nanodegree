package com.awesomekris.android.newsbox;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by kris on 16/10/18.
 */
    public class SettingsActivity extends AppCompatActivity {
//    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
//        @Override
//        public boolean onPreferenceChange(Preference preference, Object newValue) {
//            String stringValue = newValue.toString();
//
//            if(preference instanceof CheckBoxPreference){
////                CheckBoxPreference checkBoxPreference = (CheckBoxPreference)preference;
//                boolean checked = Boolean.valueOf(newValue.toString());
//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
//
//                Log.d("Get Preference Manager","stored");
//
//            }else {
//                preference.setSummary(stringValue);
//            }
//
//            return true;
//        }
//    };
//
//    private static void bindPreferenceSummaryToValue(Preference preference) {
//        // Set the listener to watch for value changes.
//        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);
//
//        // Trigger the listener immediately with the preference's
//        // current value.
//        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
//                PreferenceManager
//                        .getDefaultSharedPreferences(preference.getContext())
//                        .getString(preference.getKey(), ""));
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettinsActivityFragment())
                .commit();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class SettinsActivityFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            //int id = item.getItemId();
            return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public Intent getParentActivityIntent() {
        return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

}
