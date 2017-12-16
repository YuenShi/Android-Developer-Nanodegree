package com.sam_chordas.android.stockhawk.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by sam_chordas on 10/1/15.
 */
public class StockIntentService extends IntentService {

  public StockIntentService(){
    super(StockIntentService.class.getName());
  }

  public StockIntentService(String name) {
    super(name);
  }

  @Override protected void onHandleIntent(Intent intent) {
    Log.d(StockIntentService.class.getSimpleName(), "Stock Intent Service");
    StockTaskService stockTaskService = new StockTaskService(this);
    Intent sendIntent = new Intent();
    if (intent.getStringExtra(StockTaskService.SERVICE_TAG).equals("add")){
      sendIntent.putExtra(StockTaskService.SYMBOL_TAG, intent.getStringExtra(StockTaskService.SYMBOL_TAG));
    }
    sendIntent.putExtra(StockTaskService.SERVICE_TAG,intent.getStringExtra(StockTaskService.SERVICE_TAG));
    // We can call OnRunTask from the intent service to force it to run immediately instead of
    // scheduling a task.
    stockTaskService.onHandleIntent(sendIntent);
  }
}
