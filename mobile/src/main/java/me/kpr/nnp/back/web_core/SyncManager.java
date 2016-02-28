package me.kpr.nnp.back.web_core;

import android.content.Context;

import java.util.List;

import me.kpr.nnp.back.web_core.models.Product;
import me.kpr.nnp.back.web_core.services.ProductService;
import me.kpr.nnp.back.web_core.services.WebService;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by denys on 28.02.16.
 */
public class SyncManager {

    private static SyncManager instance;
    private Context context;

    private CompositeSubscription subscription;

    private OnLoadCompleteListener listener;

    private SyncManager(Context context) {
        this.context = context;
        subscription = new CompositeSubscription();
    }

    @Override
    protected void finalize() {
        subscription.unsubscribe();
    }

    public static synchronized SyncManager getInstance(Context context) {
        if (instance == null)
            instance = new SyncManager(context);
        return instance;
    }

    public void loadProducts(final Long companyId, final Integer limit, final Integer offset) {
        if (subscription == null)
            subscription = new CompositeSubscription();
        WebService webService = ProductService.getWebService();

        subscription.add(webService.getProducts(companyId, limit, offset)
                .retry(WebServiceUtils.MAX_RETRIES)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Product>>() {
                    @Override
                    public void onCompleted() {
                        Timber.d("Load complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        WebServiceUtils.printError(e, context);
                        if (listener != null)
                            listener.onLoadError();
                    }

                    @Override
                    public void onNext(List<Product> products) {
                        if (listener != null)
                            listener.onLoadComplete(products);

                    }
                }));
    }

    public void setListener(OnLoadCompleteListener listener) {
        this.listener = listener;
    }

    public interface OnLoadCompleteListener {
        void onLoadComplete(List<Product> products);
        void onLoadError();
    }

}
