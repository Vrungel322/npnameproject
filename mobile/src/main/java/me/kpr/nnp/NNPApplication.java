package me.kpr.nnp;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

/**
 * @author Artyom Dorosh
 * @since 2/27/16.
 */
public class NNPApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
