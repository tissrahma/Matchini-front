package com.example.matchinii.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.matchinii.R

class ViewHodler (itemView: View) : RecyclerView.ViewHolder(itemView) {
    var itemImage: ImageView
    var itemName: TextView

    init {
        itemImage = itemView.findViewById<ImageView>(R.id.userProfileImage)
        itemName = itemView.findViewById<TextView>(R.id.textView9)
    }
}