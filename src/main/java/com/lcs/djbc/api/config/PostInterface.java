package com.lcs.djbc.api.config;

import com.lcs.djbc.api.models.Data;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PostInterface {

    @POST("interchange/SendDataLcs")
    public Call<Data> notify(@Body Data data, @Header("beacukai-api-key") String key);


}
