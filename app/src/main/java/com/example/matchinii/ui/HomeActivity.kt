package com.example.matchinii.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.matchinii.R
import com.example.matchinii.models.User
import com.example.matchinii.viewmodels.ApiInterface
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


lateinit var login : TextView
lateinit var pwd : TextView
private var appbar : BottomAppBar? = null
private var loginIntent: String? = null
private var imageIntent: String? = null
private var ageIntent: String? = null
lateinit var  myUserList : ArrayList<User>
var currentPosition : Int? = 0 ;
private var firstName : String? = ""
private var id1 : String? = ""
private var id2 : String? = ""
private  var idFinal: String? =""
private var Age :Int ? =0
private var image :String = ""
var apiservice= ApiInterface.create()
private lateinit var mSharedPref: SharedPreferences
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        appbar = findViewById(R.id.bottomAppBar2)
        loginIntent = intent.getStringExtra("login")
        Log.e("ooooooooo", loginIntent.toString())
        firstName = intent.getStringExtra("firstName")
        id1 = getIntent().getStringExtra("id1intent")
        id2 = intent.getStringExtra("id2intent")
        val s = intent.getStringExtra("value")
        ageIntent = intent.getStringExtra("age")
        imageIntent = intent.getStringExtra("image")

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) { if(currentPosition!! < position) {
                        // handle swipe LEFT
                    } else if( currentPosition!! > position){
                        val myAdapter = MyAdapter(this@HomeActivity, myUserList)
                        myAdapter.removeItem(currentPosition!! -1);
                        viewPager.adapter = myAdapter;
                    }
                    currentPosition = position;
                }
            override fun onPageSelected(position: Int) {}
            override fun onPageScrollStateChanged(state: Int) {
                when (state) {
                    ViewPager.SCROLL_STATE_DRAGGING -> {}
                    ViewPager.SCROLL_STATE_IDLE -> {
                    }
                    ViewPager.SCROLL_INDICATOR_LEFT-> {Log.e("---------", currentPosition.toString())

                        var previousPosition = currentPosition!! - 1
                        if (previousPosition < 0) {
                            previousPosition = 0
                        }
                       }
                    ViewPager.SCROLL_STATE_SETTLING -> {}
                }
            }
        })
        val map: HashMap<String, String> = HashMap()
        map["login"] = loginIntent.toString()
        Log.e("%%%%%%%%", loginIntent.toString())
        apiservice.getUser(map).enqueue(object : Callback<ArrayList<User>> {
            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) { val user = response.body()

               for( i in user!!.indices ) {
                    firstName = user?.get(i)?.FirstName
                    Age = user.get(i)?.Age!!
                    image = user?.get(i)?.Image.toString()
                    myUserList = ArrayList()
                   myUserList.addAll(user)
                   val myAdapter = MyAdapter(
                        this@HomeActivity, myUserList)
                        viewPager.adapter = myAdapter
                }
                if (user != null) {

                } else {
                }
            }
            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Toast.makeText(this@HomeActivity, "Connexion error!", Toast.LENGTH_SHORT).show()
            }
        })
        appbar!!.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {
                    startActivity(Intent(this@HomeActivity, ProfileActivity::class.java).apply {
                        putExtra("login" , loginIntent)
                            putExtra("age" , ageIntent)
                            putExtra("image" , imageIntent)
                               })
                    true
                }
                R.id.messages -> {
                    startActivity(Intent(this@HomeActivity, ChatActivity::class.java).apply {
                       putExtra("login" , loginIntent)
                      /*   putExtra("id final", idFinal.toString())
                        Log.e("id1intent",id1.toString())
                        Log.e("id2intent",id2.toString())*/

                    })
                    true
                }
                R.id.search -> {
                    Toast.makeText(this,"search",Toast.LENGTH_LONG).show()
                    true
                }
                R.id.home -> {
                    startActivity(Intent(this@HomeActivity, MapActivity ::class.java).apply {
                        putExtra("login" , loginIntent)
                    })
                    true
                }
                else -> false
            }
        }

    }
    private fun getItem(i : Int ): Int {
        return viewPager.currentItem +i ;
    }
    abstract class DoubleClickListener : View.OnClickListener {
        var lastClickTime: Long = 0
        override fun onClick(v: View?) {
            val clickTime = System.currentTimeMillis()
            if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
                onDoubleClick(v)
            }
            lastClickTime = clickTime
        }
        abstract fun onDoubleClick(v: View?)

        companion object {
            private const val DOUBLE_CLICK_TIME_DELTA: Long = 300 //milliseconds
        }
    }


}