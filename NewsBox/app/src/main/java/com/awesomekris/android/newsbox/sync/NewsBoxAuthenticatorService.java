package com.awesomekris.android.newsbox.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by kris on 16/10/3.
 */
public class NewsBoxAuthenticatorService extends Service {

    private NewsBoxAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new NewsBoxAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
