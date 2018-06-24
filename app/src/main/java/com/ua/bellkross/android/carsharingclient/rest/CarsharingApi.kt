package com.ua.bellkross.android.carsharingclient.rest

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header


interface CarsharingApi {
    @GET("/api/clients/authorization")
    fun authorization(
            @Header(value = "Authorization") authorization: String
    ): Observable<Boolean>

    /**
     * Factory class for convenient creation of the Api Service interface
     */
    companion object Factory {
        /** Write here ur server ip */
        private const val serverIp = "192.168.1.189"
        /** Write here ur server port */
        private const val serverPort = "8887"

        fun create(): CarsharingApi {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://$serverIp:$serverPort/")
                    .build()

            return retrofit.create<CarsharingApi>(CarsharingApi::class.java)
        }
    }
}