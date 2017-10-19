package com.pluscubed.logcat;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.pluscubed.logcat.helper.LogcatHelper;
import com.pluscubed.logcat.util.UtilLogger;

import io.fabric.sdk.android.Fabric;

public class App extends Application {

    private static App INSTANCE = new App();
    private SharedPreferences preferences;

    public App() {
        INSTANCE = this;
    }

    public static App ctx() {
        return INSTANCE;
    }

    public static Context getContext() {
        return ctx();
    }


    @Override
    public void onCreate() {
        super.onCreate();
        hackVmPolicy();
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();
        Fabric.with(this, crashlyticsKit);

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

    }

    public SharedPreferences getPreferences() {
        if (preferences == null) {
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
        }
        return preferences;
    }

    /**
     * Disable file uri exposed check on Android N
     * @see android.os.FileUriExposedException
     * starting with Android N, we need to replace
     * all file:/// uri with the content:// uri
     * and use custom FileProvider to manage user files.
     *
     * This method replaces stock VmPolicy, and disable
     * default exposed check.
     * @see StrictMode.VmPolicy.Builder#detectFileUriExposure()
     *
     * This is a temporary hack, and will be removed later.
     *
     * //TODO: Remove this code after release [15.10.2017]
     **/
    private void hackVmPolicy(){
        if (Build.VERSION.SDK_INT >= 24) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        } else {
            new UtilLogger(this.getClass()).i("Lower android version: %d", Build.VERSION.SDK_INT);
        }
    }
}
