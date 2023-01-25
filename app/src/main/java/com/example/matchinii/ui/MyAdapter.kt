package com.example.matchinii.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.matchinii.R
import com.example.matchinii.models.User
import com.example.matchinii.models.data
import com.example.matchinii.viewmodels.ApiInterface
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.card_item.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL


class MyAdapter(private val context: HomeActivity, private val myUserArray: ArrayList<User> ) :PagerAdapter() {
    val intent = Intent(context, HomeActivity::class.java)



    lateinit var userName1: String;
    override fun getCount(): Int {
        return myUserArray.size
         }
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
       }

    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.P)
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.card_item , container , false )
        val model = myUserArray[position]
        val image = model.Image
       val login = model.login
        val firstname = model.FirstName
        val age = model.Age
      //  val url = URL(image)
        val a:Int=0
        val like = view.imageView2
        view.textView3.text = age.toString();
        view.textView8.text = firstname
          Glide.with(context).load(image).into(view.cardimage)
        view.setOnClickListener(object : HomeActivity.DoubleClickListener() {
            override fun onDoubleClick(v: View?) {
                view.isClickable = false
              var loginIntent= context.intent.getStringExtra("login" )
                var apiservicTest = ApiInterface.create()
                like.visibility = View.VISIBLE
                Handler().postDelayed({like.visibility =View.INVISIBLE }, 1000)
                val map2: HashMap<String, String> = HashMap()
                map2["login"] = loginIntent.toString()
                 apiservicTest.getObjectId(map2, model.login ).enqueue(object : Callback<ArrayList<data>>{
                    override fun onResponse(
                        call: Call<ArrayList<data>>,
                        response: Response<ArrayList<data>>
                     ) {
                        val id = response.body()
                        if (id != null) {
                            val id1 = response.body()?.get(0)?.value
                            val id2 = response.body()?.get(1)?.value
//                            val intent = Intent(context, HomeActivity::class.java)
//                            intent.putExtra("id1intent",id1)
//                            intent.putExtra("id1intent",id2)
                            //      context.intent.putExtra("id1intent",id1)
                            // context.intent.putExtra("id2intent",id2)
//                            val intent = Intent(context, HomeActivity::class.java)
//                            intent.putExtra("id1intent", id1)
//                           startActivity(intent)

                            intent.apply {
                                putExtra("id1intent", id1)
                                putExtra("id2intent", id2)

                            }


                                Log.e("id1 =  ", id1.toString())
                                Log.e("id2 = ", id2.toString())
                            apiservicTest.matches(id1!! , id2!!).enqueue(object : Callback<User>{
                                override fun onResponse(

                                    call: Call<User>,
                                    response: Response<User>
                                ) {
                                    println("success")

                                }
                                override fun onFailure(call: Call<User>, t: Throwable) {
                                   println("fail")
                                }

                            })



                        }
                    }
                    override fun onFailure(call: Call<ArrayList<data>>, t: Throwable) {
                      println("ygtfghcvn")
                    }

                })


//                apiservicTest.IsMatched(loginIntent.toString(),map2)
//                    .enqueue(object : Callback<bool>{
//                        override fun onResponse(
//                            call: Call<bool>,
//                            response: Response<bool>
//                        ) {
//                            Log.e("+++++++++" , response.body()?.value.toString())
//                            if (response.body()?.value == true)
//                            {}
//                            view.foreground = context.getDrawable(R.drawable.brown_and_white__modern_travel_app_user_interface__2___1_)
//                            Handler().postDelayed({view.foreground =context.getDrawable(R.color.transparent)}, 2000)
//                            //context.intent.putExtra("value",response.body()?.value == true)
//                             /*  val intent = Intent(context, ChatRoomActivity::class.java)
//                                intent.putExtra("value", response.body()?.value.toString())
//                               view.context. startActivity(intent)*/
//                                val myIntent: Intent = Intent(
//                                   // view.context, ChatRoomActivity::class.java
//
//                                )
//                                 context.intent.putExtra("login",loginIntent)
//
//
//                                Log.e("login ad",loginIntent.toString())
//                               // view.context.startActivity(myIntent)
//
//
//
//
//                        }
//                        override fun onFailure(
//                            call: Call<bool>,
//                            t: Throwable
//                        ) {
//
//                        }
//                    })

            }


        })

        container.addView(view , position)
        return  view
    }




    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
       container.removeView(`object` as View)
    }

    fun removeItem(position : Int ) {
        if (position > -1 && position < myUserList.size) {
            myUserList.removeAt(position);
            notifyDataSetChanged();
        }

}

}


