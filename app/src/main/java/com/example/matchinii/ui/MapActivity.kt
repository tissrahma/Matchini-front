package com.example.matchinii.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Criteria
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import com.example.matchinii.R
import com.mapbox.common.location.Location
import com.mapbox.geojson.Point
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager


var mapView: MapView? = null
var location : Location? = null
var locationManager : LocationManager? = null
var criteria : Criteria? = null
var bestProvider  :String?=null
private const val RUNTIME_PERMISSIONS = Manifest.permission.ACCESS_FINE_LOCATION
private const val REQUEST_CODE = 100
class MapActivity : AppCompatActivity() {
   // private lateinit var binding: MapboxActivityUserCurrentLocationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_map)
       locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager?
       criteria = Criteria()
       bestProvider = locationManager!!.getBestProvider(criteria!!, true).toString()
       if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
               location =  locationManager!!.getLastKnownLocation(bestProvider!!) as Location
               mapView = findViewById(R.id.mapView)
               mapView?.getMapboxMap()?.loadStyleUri(Style.MAPBOX_STREETS)
               mapView?.getMapboxMap()?.loadStyleUri(
                   Style.MAPBOX_STREETS,
                   object : Style.OnStyleLoaded {
                       override fun onStyleLoaded(style: Style) {
                           addAnnotationToMap()
                       }
                   }
               )
           }
    }
    private fun addAnnotationToMap() {
// Create an instance of the Annotation API and get the PointAnnotationManager.
        bitmapFromDrawableRes(
            this@MapActivity,
            R.drawable.red_marker
        )?.let {

            val annotationApi = mapView?.annotations
            val pointAnnotationManager = annotationApi?.createPointAnnotationManager(mapView!!)
// Set options for the resulting symbol layer.

            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
// Define a geographic coordinate.
                .withPoint(Point.fromLngLat(location!!.longitude, location!!.latitude))

// Specify the bitmap you assigned to the point annotation
// The bitmap will be added to map style automatically.
                .withIconImage(it)
// Add the resulting pointAnnotation to the map.
            pointAnnotationManager?.create(pointAnnotationOptions)
            Log.e("ayoooo" , location!!.longitude.toString())
        }
    }
    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int) =
        convertDrawableToBitmap(AppCompatResources.getDrawable(context, resourceId))

    private fun convertDrawableToBitmap(sourceDrawable: Drawable?): Bitmap? {
        if (sourceDrawable == null) {
            return null
        }
        return if (sourceDrawable is BitmapDrawable) {
            sourceDrawable.bitmap
        } else {
// copying drawable object to not manipulate on the same reference
            val constantState = sourceDrawable.constantState ?: return null
            val drawable = constantState.newDrawable().mutate()
            val bitmap: Bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth, drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }

    }
    private fun hasPermissions(context: Context, vararg permissions: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
        }
        return true
    }



    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }
}
