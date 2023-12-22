package com.cube.pruebatecnicajorgecubedo.network;

import com.cube.Entry;

import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitRequests {

    @POST
    void postContent(Entry entry);

    @GET
    void getContents();
}
