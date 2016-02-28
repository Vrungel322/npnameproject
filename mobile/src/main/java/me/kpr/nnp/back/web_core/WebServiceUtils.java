package me.kpr.nnp.back.web_core;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.widget.Toast;

import retrofit.RetrofitError;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by denys on 28.02.16.
 */
public class WebServiceUtils {

    public final static String ENDPOINT = "http://192.168.32.251:2021/";
    public final static int MAX_RETRIES = 2;

    public static void unsubscribeIfNotNull(Subscription subscription) {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    public static CompositeSubscription getNewCompositeSubIfUnsubscribed(CompositeSubscription subscription) {
        if (subscription == null || subscription.isUnsubscribed()) {
            return new CompositeSubscription();
        }

        return subscription;
    }

    public static void printError(Throwable t, Context context) {
        if (((RetrofitError) t).isNetworkError()) {
            if (!Connectivity.isConnected(context)) {
                Toast.makeText(context, /*context.getResources().getString(R.string.not_connected_error)*/ "No internet connection", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Connectivity.isConnectedFast(context)) {
                Toast.makeText(context, /*context.getResources().getString(R.string.slow_connected_error)*/ "Slow internet connection", Toast.LENGTH_SHORT).show();
                return;
            }
        } else {
//           Toast.makeText(context, context.getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
        }
    }
}
