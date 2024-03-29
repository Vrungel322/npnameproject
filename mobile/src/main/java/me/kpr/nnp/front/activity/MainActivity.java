package me.kpr.nnp.front.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.kpr.nnp.R;
import me.kpr.nnp.back.callback.OnMessageReceivedCallback;
import me.kpr.nnp.back.nfc.NFCHelper;
import me.kpr.nnp.back.utils.SharedPrefUtils;
import me.kpr.nnp.back.web_core.SyncManager;
import me.kpr.nnp.back.web_core.models.Product;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_BEACON_UUID = "beaconUUID";
    private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
    private NFCHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!SharedPrefUtils.getInstance(this).isLoginned()) {
            Intent intent = new Intent(this,FirstLaunchActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        test();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (SharedPrefUtils.getInstance(this).isLoginned()) {
            View view = navigationView.getHeaderView(0);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            TextView textView = (TextView) view.findViewById(R.id.name);

            Glide.with(this).load(SharedPrefUtils.getInstance(this).getPhoto())
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(imageView);
            textView.setText(SharedPrefUtils.getInstance(this).getName());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        PERMISSION_REQUEST_COARSE_LOCATION);
            }
        }

        // NFC stuff
        helper = new NFCHelper();
        helper.setMessage("LALALALLAL");
        helper.init(this);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            helper.setListener((a, i) -> {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    if (i == NfcAdapter.STATE_ON) {
                        // set smth
                    } else {
                        // set smth
                    }
                });
            });
        }
        helper.setCallback(new OnMessageReceivedCallback() {
            @Override
            public void onReceive(String message) {
                Timber.e("SUCCESS : " + message);
                Toast.makeText(getApplicationContext(), "Message: " + message,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure() {
                Toast.makeText(getApplicationContext(), "FAILURE", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void test() {
        SyncManager manager = SyncManager.getInstance(this);
        manager.setListener(new SyncManager.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(List<Product> products) {
                for (Product product : products) {
                    Timber.d(product.getName());
                }
            }

            @Override
            public void onLoadError() {
                Toast.makeText(MainActivity.this, "Load error", Toast.LENGTH_SHORT).show();
            }
        });
        manager.loadProducts(119041L, 10, 0);

    }

    @Override
    protected void onResume() {
        super.onResume();
        helper.onResume(getIntent());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_COARSE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Timber.e("coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage(getString(R.string.ungranted));
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(dialog -> {
                    });
                    builder.show();
                }
            }
        }
    }
}
