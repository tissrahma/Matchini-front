package com.example.matchinii.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.matchinii.R
import de.hdodenhof.circleimageview.CircleImageView

class matchViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
    val userPic: CircleImageView

    init {
        userPic = itemView.findViewById(R.id.post)

    }
}