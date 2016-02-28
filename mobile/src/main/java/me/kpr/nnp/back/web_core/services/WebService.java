package me.kpr.nnp.back.web_core.services;

import java.util.List;

import me.kpr.nnp.back.web_core.models.Product;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by denys on 28.02.16.
 */
public interface WebService {

    @GET(ROUTES.PRODUCTS)
    Observable<List<Product>> getProducts(@Query("id") Long id, @Query("limit") Integer limit, @Query("offset") Integer offset);

    interface ROUTES {
        String PRODUCTS = "/search/products";
    }
}
