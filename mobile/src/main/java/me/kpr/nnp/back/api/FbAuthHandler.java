package me.kpr.nnp.back.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.util.Arrays;

import me.kpr.nnp.back.utils.SharedPrefUtils;
import me.kpr.nnp.front.activity.FirstLaunchActivity;
import timber.log.Timber;

import static me.kpr.nnp.back.api.FbAuthHandler.FbFields.AGE_RANGE;
import static me.kpr.nnp.back.api.FbAuthHandler.FbFields.BIRTHDAY;
import static me.kpr.nnp.back.api.FbAuthHandler.FbFields.EMAIL;
import static me.kpr.nnp.back.api.FbAuthHandler.FbFields.FIRST_NAME;
import static me.kpr.nnp.back.api.FbAuthHandler.FbFields.GENDER;
import static me.kpr.nnp.back.api.FbAuthHandler.FbFields.ID;
import static me.kpr.nnp.back.api.FbAuthHandler.FbFields.LAST_NAME;
import static me.kpr.nnp.back.api.FbAuthHandler.FbFields.LINK;
import static me.kpr.nnp.back.api.FbAuthHandler.FbFields.LOCALE;
import static me.kpr.nnp.back.api.FbAuthHandler.FbFields.PICTURE;
import static me.kpr.nnp.back.api.FbAuthHandler.FbFields.VERIFIED;

/**
 * @author Artyom Dorosh
 * @since 11/3/15
 */
public class FbAuthHandler {

    private Activity activity;

    // Constant fields
    private static final String FACEBOOK_LINK = "https://graph.facebook.com/";
    private static final String PICTURE_LINK = "/picture?type=large";
    private static final String FIELDS = "fields";
    // Fb data to request profile data
    private static final String[] sFbData = {
            ID, FIRST_NAME, LAST_NAME, GENDER, LOCALE,
            LINK, AGE_RANGE, EMAIL, VERIFIED, PICTURE, BIRTHDAY
    };
    // Fb scopes to login process access
    private static final String[] sFbScope = new String[]
            {"public_profile", "email", "user_birthday"};
    // Singleton private instance
    private static FbAuthHandler instance;
    // Facebook callback manager to handle onActivityResult
    private CallbackManager mCallbackManager;


    /**
     * Private constructor for singleton definition
     */
    private FbAuthHandler() {
        mCallbackManager = CallbackManager.Factory.create();

        FacebookCallback<LoginResult> mLoginCallback = new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                // Setting current API access token
                AccessToken.setCurrentAccessToken(loginResult.getAccessToken());
                // Requesting data to generate user social profile
                requestUserData();
            }

            @Override
            public void onCancel() {
                // TODO : send error event to activity
            }

            @Override
            public void onError(FacebookException e) {
                Timber.e(e, "New Facebook error while login process.");
                // TODO : send error event to activity
            }
        };
        LoginManager.getInstance().registerCallback(mCallbackManager, mLoginCallback);
    }

    /**
     * Standard singleton definition method
     *
     * @return current singleton global instance
     */
    public static FbAuthHandler getInstance() {
        FbAuthHandler fbAuthHandler = instance;
        if (instance == null)
            synchronized (FbAuthHandler.class) {
                fbAuthHandler = instance;
                if (fbAuthHandler == null)
                    instance = fbAuthHandler = new FbAuthHandler();

            }

        return fbAuthHandler;
    }

    /**
     * Initialize FacebookSdk from Application class to get it
     * instance from every endpoint of application.
     *
     * @param applicationContext context of application
     */
    public static void initialize(Context applicationContext) {
        FacebookSdk.sdkInitialize(applicationContext);
    }

    /**
     * Generating data request string for Facebook API
     *
     * @return string to path Fb request
     */
    private String generateDataRequestString() {
        StringBuilder fbRequestBuilder = new StringBuilder();
        for (int i = 0; i < sFbData.length; i++) {
            fbRequestBuilder.append(sFbData[i]);
            if (i < sFbData.length - 1)
                fbRequestBuilder.append(",");
        }
        return fbRequestBuilder.toString();
    }

    /**
     * Start login process to Facebook
     *
     * @param activity activity to start login view.
     */
    public void login(Activity activity) {
        this.activity = activity;
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList(sFbScope));
    }

    /**
     * Start logout process from Facebook
     */
    public void logout() {
        LoginManager.getInstance().logOut();
    }

    /**
     * Request for user data  to Facebook API
     */
    private void requestUserData() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        if (object != null) {
                            // Logging gotten JSON
                            Timber.v(object.toString());
                            // Creating new Social profile
                            createSocialProfileFromResponse(object);
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString(FIELDS, generateDataRequestString());
        request.setParameters(parameters);
        request.executeAsync();
    }

    /**
     * Create social profile from FB SDK response
     *
     * @param object JSON object from FB API response
     */
    private void createSocialProfileFromResponse(JSONObject object) {

        try {
            // Set profile base name
            SharedPrefUtils.getInstance().setName(object.optString(FIRST_NAME)
                    + " "
                    + object.optString(LAST_NAME));

            // Adding info to dataList
            SharedPrefUtils.getInstance().setPhoto(FACEBOOK_LINK + object.optString(ID) +
                    PICTURE_LINK);

            SharedPrefUtils.getInstance().setLoginned(true);

            if (activity instanceof FirstLaunchActivity)
                ((FirstLaunchActivity) activity).finishAuth();

        } catch (Exception exception) {
            Timber.e(exception, "Exception while parsing JSON in requestUserData");
        }
    }

    /**
     * Call this method in activity in your activity  in
     * {@link android.support.v7.app.AppCompatActivity#onActivityResult(int, int, Intent)}.
     *
     * @param requestCode not useful for our method
     * @param resultCode  not useful for our method
     * @param data        not useful for our method
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressWarnings("unused")
    public interface FbFields {
        String ID = "id";
        String NAME = "name";
        String FIRST_NAME = "first_name";
        String MIDDLE_NAME = "middle_name";
        String LAST_NAME = "last_name";
        String GENDER = "gender";
        String LOCALE = "locale";
        String LANGUAGE = "languages";
        String LINK = "link";
        String AGE_RANGE = "age_range";
        String THIRD_PARTY_ID = "third_party_id";
        String INSTALLED = "installed";
        String TIMEZONE = "timezone";
        String UPDATED_TIME = "updated_time";
        String VERIFIED = "verified";
        String BIO = "bio";
        String BIRTHDAY = "birthday";
        String COVER = "cover";
        String CURRENCY = "currency";
        String DEVICES = "devices";
        String EDUCATION = "education";
        String EMAIL = "email";
        String HOMETOWN = "hometown";
        String INTERESTED_IN = "interested_in";
        String LOCATION = "location";
        String POLITICAL = "political";
        String PAYMENT_PRICEPOINTS = "payment_pricepoints";
        String PAYMENT_MOBILE_PRICEPOINTS = "payment_mobile_pricepoints";
        String FAVORITE_ATHLETES = "favorite_athletes";
        String FAVORITE_TEAMS = "favorite_teams";
        String PICTURE = "picture";
        String QUOTES = "quotes";
        String RELATIONSHIP_STATUS = "relationship_status";
        String RELIGION = "religion";
        String SECURITY_SETTINGS = "security_settings";
        String SINGNIFICANT_OTHER = "significant_other";
        String VIDEO_UPLOAD_LIMITS = "video_upload_limits";
        String WEBSITE = "website";
        String WORK = "work";
    }

}