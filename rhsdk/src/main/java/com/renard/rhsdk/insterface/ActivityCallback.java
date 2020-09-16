package com.renard.rhsdk.insterface;

import android.content.Intent;
import android.content.res.Configuration;

/**
 * Created by Riven_rabbit on 2020/9/15
 *
 * @author suyanan
 */
public interface ActivityCallback {
    public void onActivityResult(int requestCode, int resultCode, Intent data);
    public void onCreate();
    public void onStart();
    public void onPause();
    public void onResume();
    public void onNewIntent(Intent newIntent);
    public void onStop();
    public void onDestroy();
    public void onRestart();
    public void onBackPressed();
    public void onConfigurationChanged(Configuration newConfig);
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults);
}
