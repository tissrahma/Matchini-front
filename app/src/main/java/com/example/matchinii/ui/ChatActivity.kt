package com.example.matchinii.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.matchinii.R
import com.example.matchinii.models.User
import com.example.matchinii.models.data
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.card_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
private var firstName :String = ""
private var image :String = ""
lateinit var  UserList : ArrayList<User>
private var laoutmanager : RecyclerView.LayoutManager?=null
private  var adapterrecycler : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>?=null
class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val map: HashMap<String, String> = HashMap()
        var loginIntent= intent.getStringExtra("login" )
        laoutmanager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false)
        map["login"] = loginIntent.toString()
        Log.e("login chat",loginIntent.toString())
        apiservice.getId(map).enqueue(object :Callback<data>{
            override fun onResponse(
                call: Call<data>,
                response: Response<data>
            ) {
                Log.e("id",response.body()!!.value)
                apiservice.amie(response.body()!!.value).enqueue(object : Callback<ArrayList<User>> {
                    override fun onResponse(
                        call: Call<ArrayList<User>>,
                        response: Response<ArrayList<User>>
                    ) {
                        val user = response.body()
                        for( i in user!!.indices ) {
                            firstName = user?.get(i)?.FirstName.toString()
                            image = user?.get(i)?.Image.toString()
                            UserList = ArrayList()
                            UserList.addAll(user)
                            Log.e("iiiiiiiii", response.body().toString())
                            recyclerView1.layoutManager = laoutmanager
                            adapterrecycler = RecyclerAdapter(UserList , Intent(this@ChatActivity, RecyclerAdapter::class.java).putExtra("login" , loginIntent))
                            recyclerView1.adapter= adapterrecycler
                    }}
                    override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                        println("non")
                    }

                })
            }

            override fun onFailure(call: Call<data>, t: Throwable) {
                println("leee")
            }

        })


      /*  apiservice.getUser(map).enqueue(object : Callback<ArrayList<User>> {
            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) { val user = response.body()
                for( i in user!!.indices ) {
                    firstName = user?.get(i)?.FirstName.toString()
                    image = user?.get(i)?.Image.toString()
                  UserList = ArrayList()
                    UserList.addAll(user)
                    Log.e("iiiiiiiii", firstName)
                    recyclerView1.layoutManager = laoutmanager
                    adapterrecycler = RecyclerAdapter(UserList)
                    recyclerView1.adapter= adapterrecycler
                    }
                if (user != null) {
                } else {
                }
            }
            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Toast.makeText(this@ChatActivity, "Connexion error!", Toast.LENGTH_SHORT).show()
            }
        })*/
    }
}