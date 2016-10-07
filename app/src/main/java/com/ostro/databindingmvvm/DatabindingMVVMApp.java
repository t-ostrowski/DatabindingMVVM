package com.ostro.databindingmvvm;

import android.app.Application;
import android.support.annotation.Nullable;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * Created by Thomas Ostrowski
 * ostrowski.thomas@gmail.com
 */

public class DatabindingMVVMApp extends Application {

    private static DatabindingMVVMApp INSTANCE;

    public static DatabindingMVVMApp get() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        initializeLogger();
    }

    private void initializeLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Fabric.with(this, new Crashlytics());
            Timber.plant(new CrashlyticsTree());
        }
    }

    public class CrashlyticsTree extends Timber.Tree {
        private static final String CRASHLYTICS_KEY_PRIORITY = "priority";
        private static final String CRASHLYTICS_KEY_TAG = "tag";
        private static final String CRASHLYTICS_KEY_MESSAGE = "message";

        @Override
        protected void log(int priority, @Nullable String tag, @Nullable String message,
                           @Nullable Throwable exception) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO
                    || priority == Log.WARN) {
                return;
            }

            Crashlytics.setInt(CRASHLYTICS_KEY_PRIORITY, priority);
            Crashlytics.setString(CRASHLYTICS_KEY_TAG, tag);
            Crashlytics.setString(CRASHLYTICS_KEY_MESSAGE, message);

            if (exception == null) {
                Crashlytics.logException(new Exception(message));
            } else {
                Crashlytics.logException(exception);
            }
        }
    }
}
