package com.fynd.glamar_sdk_test;

import com.fynd.glamar_sdk_test.GlamAREssential.GlamConfigModel;
import com.fynd.glamar_sdk_test.GlamAREssential.SKU;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("sku")
    public Call<GlamConfigModel> getParticularSKU(@Query("id") String id, @Header("Authorization") String authorization,
                                                  @Header("Content-type") String contentType, @Header("source") String source);

    @GET("sku-list")
    public Call<List<SKU>> getAllSKUs(@Header("Authorization") String authorization,
                                      @Header("Content-type") String contentType, @Header("source") String source);
}
