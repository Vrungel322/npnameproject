package me.kpr.nnp.back.web_core.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import me.kpr.nnp.back.web_core.WebServiceUtils;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.converter.GsonConverter;


/**
 * Created by denys on 28.02.16.
 */
public class ProductService {



    private static ProductService instance;
    private static WebService webService;

    private ProductService() {
        RequestInterceptor requestInterceptor = (request) ->
                request.addHeader("Accept", "application/json");

        Gson gson = new GsonBuilder()
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(WebServiceUtils.ENDPOINT)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new AndroidLog("Product"))
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(requestInterceptor)
                .build();

        webService = restAdapter.create(WebService.class);
    }

    public static synchronized WebService getWebService() {
        if (instance == null) {
            instance = new ProductService();
        }
        if (webService == null)
            return null;
        return webService;
    }

}
