package com.example.matchinii.viewmodels

import com.example.matchinii.models.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface MessagesInterface {
    @POST("/Message/addmessage/{User1_param1}/{RommeName}")
    fun addmessage(@Path("User1_param1") User1_param1:String, @Path("RommeName") RommeName:String,@Body map: HashMap<String, String>): Call<Messages>
    @POST("/Message/showmessage")
    fun showmessage( @Body map: HashMap<String, String>): Call <ArrayList<data>>
    companion object {
        fun create() : MessagesInterface{
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://192.168.1.12:3000")
                .build()
            return retrofit.create(MessagesInterface::class.java)
        }
    }
}