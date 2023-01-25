package com.example.matchinii.models

import androidx.annotation.DrawableRes
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("login")
    var login: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("FirstName")
    var FirstName: String,
    @SerializedName("LasteName")
    var  LasteName: String,
    @SerializedName(" Age")
    var Age: Int,
    @SerializedName("Numero")
    var Numero: String,
    @SerializedName("Sexe")
    var Sexe: String,
    @SerializedName("Image")
    var Image: String,
    @SerializedName("Matches")
    var Matches: String,
)