package com.example.matchinii.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.matchinii.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity3 : AppCompatActivity() {
        private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
        lateinit var  getpos : Button

        override fun onCreate(savedInstanceState: Bundle?) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_main3)
            getpos = findViewById(R.id.getpos)
getpos.setOnClickListener(){
    checkPermission()

}
        }

        private fun checkPermission () {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            }else {
                getLocation()
            }
        }
        @SuppressLint("MissingPermission")
        private fun getLocation ()
        {
            fusedLocationProviderClient.lastLocation?.addOnSuccessListener {
                if(it == null){
                    Toast.makeText(this , "sry cant get location " , Toast.LENGTH_SHORT).show()
                }      else it.apply{
                    val latitude   = it.latitude
                    val longitude = it.longitude
                    Log.e("$$$$$$$$$$$$$$$$" , longitude.toString())
                    Log.e("$$$$$$$$$$$$$$$$" , latitude.toString())
                }
            }
        }
        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            if(grantResults.isNotEmpty()&& grantResults[0] == PackageManager.PERMISSION_GRANTED )
                if (ContextCompat.checkSelfPermission(this , Manifest.permission.ACCESS_COARSE_LOCATION)==(PackageManager.PERMISSION_GRANTED)
                ){
                    Toast.makeText(this , "asba" , Toast.LENGTH_SHORT).show()
                    getLocation()
                }
        }
    }


