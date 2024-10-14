package com.mascherpa.proyectojubbler.data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL="https://dolarapi.com/v1/";

    public static Retrofit getRetrofitInstance(){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL) 
                    .addConverterFactory(GsonConverterFactory.create()) //json - response
                    .build();
        }
        return retrofit;
    }
}
