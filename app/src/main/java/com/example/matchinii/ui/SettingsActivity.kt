package com.example.matchinii.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.matchinii.R
import kotlinx.android.synthetic.main.activity_settings.*
import android.widget.SeekBar
import androidx.cardview.widget.CardView
import com.example.matchinii.models.User
import com.example.matchinii.viewmodels.ApiInterface
import com.google.android.material.slider.RangeSlider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "SettingsActivity"

lateinit  var man :SwitchCompat
lateinit  var woman :SwitchCompat
lateinit  var distancetext :TextView
var max : Float? = null
var min : Float? = null
lateinit var Logout1: CardView
lateinit var rangeslider : com.google.android.material.slider.RangeSlider
lateinit var deleteAcc: CardView
lateinit var loginIntentsettting : String

//var rangeSeekBar: RangeSeekBar? = null

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        loginIntentsettting = intent!!.getStringExtra("login" ).toString()
        man = findViewById(R.id.switch_man)
        woman = findViewById(R.id.switch_woman)
        distancetext = findViewById(R.id.distance_text)

        rangeslider = findViewById(R.id.slider)
        rangeslider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: RangeSlider) {

                val values = slider.values
                //Those are the new updated values of sldier when user has finshed dragging
                Log.i("SliderNewValue From", values[0].toString())
                Log.i("SliderNewValue To", values[1].toString())


                distancetext.text = "From: ${values[0]},                                  To: ${values[1]}"

                max = values[0]
                min = values[1]



            }

            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: RangeSlider) {


            }
        })

        rangeslider.addOnChangeListener { rangeSlider, value, fromUser ->
            // Responds to when slider's value is changed
        }
        Logout1=findViewById(R.id.savebtn)
        deleteAcc=findViewById(R.id.btndelete)
        val map: HashMap<String, String> = HashMap()
        map["login"] = loginIntentsettting.toString()
        val map2: HashMap<String, String> = HashMap()
        map2["login"] = loginIntentsettting
        /*agepref.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                distance_text.text = "$progress Year"
                map2["AgePref"] = progress.toString()
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })*/
        Logout1.setOnClickListener() {
            map2["login"] = loginIntentsettting
            map2["AgeMin"] = max.toString()
            map2["AgeMax"] = min.toString()
            apiservice.addAgePref(map2).enqueue(object : Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    println("success")
                }
                override fun onFailure(call: Call<User>, t: Throwable) {
                    println("fail")
                }

            })
            startActivity(Intent(this@SettingsActivity , HomeActivity::class.java).putExtra("login" ,
                loginIntentsettting))
        }
        deleteAcc.setOnClickListener{
            apiservice.deleteAcc(loginIntentsettting).enqueue(object:Callback<User>{
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    Toast.makeText(
                        this@SettingsActivity,
                        "by by !",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(applicationContext, SecondActivity::class.java))
                    finish()
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(
                        this@SettingsActivity,
                        "fail  !",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
        }
        man.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val map: HashMap<String, String> = HashMap()
                map["Sexe"] = man.isChecked.toString()
                apiservice.showme(map).enqueue(object :Callback<User>{
                    override fun onResponse(call: Call<User>, response: Response<User>) {


                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {

                    }

                })

                man.isChecked = true
                woman.isChecked = false
            }
        })
        woman.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val map: HashMap<String, String> = HashMap()
                map["Sexe"] = woman.isChecked.toString()
                apiservice.showme(map).enqueue(object :Callback<User>{
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        Log.e("3asba", woman.isChecked.toString())

                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {

                    }

                })

                man.isChecked = false
                woman.isChecked = true
            }
        })

        /*   rangeSeekBar.setOnRangeSeekBarChangeListener(object : OnRangeSeekBarChangeListener() {
               fun onRangeSeekBarValuesChanged(bar: RangeSeekBar?, minValue: Any, maxValue: Any) {
                   age_rnge.setText("$minValue-$maxValue")
               }
           })
           back.setOnClickListener { onBackPressed() }


       }*/




    }
}