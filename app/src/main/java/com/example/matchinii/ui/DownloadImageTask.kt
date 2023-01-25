package com.example.matchinii.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import java.io.InputStream
import java.net.URL

 class DownloadImageTask(bmImage: ImageView) :
    AsyncTask<String?, Void?, Bitmap?>() {
    var bmImage: ImageView

    init {
        this.bmImage = bmImage
    }

    override fun onPostExecute(result: Bitmap?) {
        bmImage.setImageBitmap(result)
    }

     override fun doInBackground(vararg params: String?): Bitmap? {
         val urldisplay = params[0]
         var mIcon11: Bitmap? = null
         try {
             val `in`: InputStream = URL(urldisplay).openStream()
             mIcon11 = BitmapFactory.decodeStream(`in`)
         } catch (e: Exception) {
             Log.e("Error", e.message.toString())
             e.printStackTrace()
         }
         return mIcon11
     }
 }