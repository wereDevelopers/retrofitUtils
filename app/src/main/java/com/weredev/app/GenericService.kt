package com.weredev.app

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

internal interface GenericService {

    @GET
    fun getJson(@Url endpoint: String): Call<ResponseBody>

}