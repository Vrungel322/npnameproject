package me.kpr.nnp.front.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.facebook.appevents.AppEventsLogger;

import me.kpr.nnp.R;
import me.kpr.nnp.back.api.FbAuthHandler;

/**
 * Created by dorosh on 2/28/16.
 */
public class FirstLaunchActivity extends AppCompatActivity {

    private FbAuthHandler fbAuthHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fbAuthHandler = FbAuthHandler.getInstance();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        findViewById(R.id.fb).setOnClickListener((v) -> fbAuthHandler.login(this));
    }


    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbAuthHandler.onActivityResult(requestCode, resultCode, data);
    }

    public void finishAuth() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> {
            Intent intent = new Intent(this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}
