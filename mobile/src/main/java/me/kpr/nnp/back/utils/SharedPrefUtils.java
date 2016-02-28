package me.kpr.nnp.back.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Artyom Dorosh
 * @since 2/25/16.
 */
public class SharedPrefUtils {

    // instance
    private static SharedPrefUtils instance;

    public static final String APP_SHRD_PREFS = "prefs";

    public static final String SHRD_PREF_LOGINNED = "shared_preference_loginned";
    public static final String SHRD_PREF_NAME = "shared_preference_name";
    public static final String SHRD_PREF_IMAGE_LING = "shared_preference_image_link";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private SharedPrefUtils(Context context) {
        this.preferences = context.getSharedPreferences(APP_SHRD_PREFS, Context.MODE_PRIVATE);
        this.editor = preferences.edit();
    }

    public static SharedPrefUtils getInstance(Context context) {
        if (instance == null)
            instance = new SharedPrefUtils(context);

        return instance;
    }

    public static SharedPrefUtils getInstance() {
        return instance;
    }

    public static void init(Context context) {
        instance = new SharedPrefUtils(context);
    }

    public boolean isLoginned() {
        return preferences.getBoolean(SHRD_PREF_LOGINNED, false);
    }

    public void setLoginned(boolean bool) {
        editor.putBoolean(SHRD_PREF_LOGINNED, bool).commit();
    }

    public String getName() {
        return preferences.getString(SHRD_PREF_NAME, "");
    }

    public void setName(String name) {
        editor.putString(SHRD_PREF_NAME, name);
    }

    public String getPhoto() {
        return preferences.getString(SHRD_PREF_IMAGE_LING, "");
    }

    public void setPhoto(String url) {
        editor.putString(SHRD_PREF_IMAGE_LING, url);
    }
}
