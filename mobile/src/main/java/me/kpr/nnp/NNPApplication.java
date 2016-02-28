package me.kpr.nnp;

import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.activeandroid.ActiveAndroid;

import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;
import org.altbeacon.beacon.startup.BootstrapNotifier;
import org.altbeacon.beacon.startup.RegionBootstrap;

import java.util.ArrayList;
import java.util.List;

import me.kpr.nnp.front.activity.MainActivity;
import timber.log.Timber;

/**
 * @author Artyom Dorosh
 * @since 2/27/16.
 */
public class NNPApplication extends Application implements BootstrapNotifier {

    public final static String[] ids = {
            "2f234454-cf6d-4a0f-adf2-f4911ba9ffa6",
            "2f234454-cf6d-4a0f-adf2-f5011ba9ffa6"
    };

    public RegionBootstrap regionBootstrap;

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);

        Timber.plant(new Timber.DebugTree());

        if (android.os.Build.VERSION.SDK_INT < 18) {
            Timber.e("requires Android 4.3");
        } else if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Timber.e("requires feature BLE");;
        } else {
            List<Region> identifiers = new ArrayList<>();
            for (String string : ids) {
                Region region = new Region("me.kpr.nnp." + string, Identifier.parse(string),
                        null, null);
                identifiers.add(region);
            }
            regionBootstrap = new RegionBootstrap(this, identifiers);
        }
    }

    @Override
    public void didEnterRegion(Region region) {
        Timber.e("Entering region : " + region.getUniqueId());
        Timber.e("REGION ID : " + region.getUniqueId());
        Timber.e("UUID1 : " + region.getId1().toHexString());
        Timber.e("UUID STRING : " + region.getId1().toString());

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(MainActivity.EXTRA_BEACON_UUID, region.getId1().toHexString());
        startActivity(intent);
    }

    @Override
    public void didExitRegion(Region region) {
        Timber.e("Exiting region : " + region.getUniqueId());
        Timber.e("REGION ID : " + region.getUniqueId());
        Timber.e("UUID1 : " + region.getId1().toHexString());
    }

    @Override
    public void didDetermineStateForRegion(int i, Region region) {

    }
}
