package com.example.matchinii.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.matchinii.R
import com.example.matchinii.models.data
import com.example.matchinii.viewmodels.ApiInterface
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
lateinit var button : Button
lateinit var email_input : TextInputEditText
lateinit var email_input_layout : TextInputLayout
lateinit var ancientPasswordLayout:TextInputLayout
var services = ApiInterface.create()
class EmailVerifcationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_verifcation)
        button = findViewById(R.id.mailverifbtn)
        email_input = findViewById(R.id.ancientPasswordEditText)
        ancientPasswordLayout=findViewById(R.id.ancientPasswordLayout)
        email_input_layout = findViewById(R.id.ancientPasswordLayout)
        val fragment2 = CodeFragment()
        button.setOnClickListener(){
            email_input_layout.error=null
            if (email_input.text!!.isEmpty()){
                email_input_layout.error = getString(R.string.mustNotBeEmpty)
            } else {
                val map: HashMap<String, String> = HashMap()
                map["login"] = email_input.text.toString()
                services.forgotPassword(map)
                    .enqueue(object : Callback<data> {
                        override fun onResponse(call: Call<data>, response: Response<data>) {
                            println("value")
                            println(response.body()?.value)
                            if (response.body()!= null)
                            {
                                val bundle = Bundle();
                                fragment2.arguments = bundle
                                bundle.putString("code",response.body()?.value )
                                bundle.putString("login" , email_input.text.toString())
                                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView2, fragment2).commit()
                                Toast.makeText(this@EmailVerifcationActivity, "Please check your email for the confirmation code!", Toast.LENGTH_SHORT)
                                    .show()
                            }else {Toast.makeText(this@EmailVerifcationActivity, "No account with this email exists!", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        override fun onFailure(call: Call<data>, t: Throwable) {
                            Toast.makeText(this@EmailVerifcationActivity, "Connection Error!", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }

    }
            }


