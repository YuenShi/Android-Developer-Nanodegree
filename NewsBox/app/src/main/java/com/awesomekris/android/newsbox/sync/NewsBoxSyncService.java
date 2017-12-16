package com.awesomekris.android.newsbox.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by kris on 16/10/3.
 */
public class NewsBoxSyncService extends Service {

    private static final String LOG_TAG = NewsBoxSyncService.class.getSimpleName();
    private static final Object sSyncAdapterLock = new Object();
    private static NewsBoxSyncAdapter sNewsBoxSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "onCreate - NewsBoxSyncService");
        synchronized (sSyncAdapterLock) {
            if (sNewsBoxSyncAdapter == null) {
                sNewsBoxSyncAdapter = new NewsBoxSyncAdapter(getApplicationContext(),true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sNewsBoxSyncAdapter.getSyncAdapterBinder();
    }
}
