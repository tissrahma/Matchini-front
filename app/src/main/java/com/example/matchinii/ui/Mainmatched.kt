package com.example.matchinii.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matchinii.R
import com.example.matchinii.models.User

lateinit var mRecyclerView: RecyclerView
lateinit var adapter: MyAdapter
var matchList: List<User> = ArrayList<User>()
class Mainmatched : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainmatched)
        mRecyclerView = findViewById(R.id.matche_recycler_view)
        //adapter = MyAdapter(matchList)
        val mLayoutManager1: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext)
        mRecyclerView.setLayoutManager(mLayoutManager1)
        mRecyclerView.setItemAnimator(DefaultItemAnimator())
      //  mRecyclerView.setAdapter(adapter)

    }
}


