package com.ua.bellkross.android.carsharingclient.rest

import com.bellkross.carsharingserver.entity.Car
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header


interface CarSharingApi {
    @GET("/api/client/authorization")
    fun authorization(
            @Header(value = "Authorization") authorization: String
    ): Observable<Boolean>


    @GET("/api/client/getAvailableCars")
    fun getCarsForClient(
            @Header(value = "Authorization") authorization: String
    ): Observable<List<Car>>

    /**
     * Factory class for convenient creation of the Api Service interface
     */
    companion object Factory {
        /** Write here ur server ip */
        private const val serverIp = "192.168.1.189"
        /** Write here ur server port */
        private const val serverPort = "8887"

        fun create(): CarSharingApi {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://$serverIp:$serverPort/")
                    .build()

            return retrofit.create<CarSharingApi>(CarSharingApi::class.java)
        }
    }
}