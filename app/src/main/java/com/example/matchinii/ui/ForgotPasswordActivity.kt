package com.example.matchinii.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import com.example.matchinii.R
import com.example.matchinii.models.User
import com.example.matchinii.viewmodels.ApiInterface
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

lateinit var emailtext: TextInputEditText
lateinit var emaillayout: TextInputLayout
lateinit var passwordtext: TextInputEditText
lateinit var passwordlayout: TextInputLayout
lateinit var savebtn: Button

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        val login = intent.getStringExtra("login")
        savebtn = findViewById(R.id.mailverifbtn1)
        emailtext = findViewById(R.id.ancientPasswordEditText1)
        emaillayout=findViewById(R.id.ancientPasswordLayout)
        passwordlayout=findViewById(R.id.newPasswordLayout)
        passwordtext=findViewById(R.id.newPasswordEditText)

        var services = ApiInterface.create()
        fun validate(): Boolean {
            emaillayout!!.error = null
            passwordlayout!!.error = null
            if (emailtext?.text!!.isEmpty()){
                emaillayout!!.error = getString(R.string.mustNotBeEmpty)
                return false
            }
            if (passwordtext?.text!!.isEmpty()) {
                passwordlayout!!.error = getString(R.string.mustNotBeEmpty)
                return false
            }
            return true
        }

        savebtn.setOnClickListener(){
      if (validate()) {
    val map: HashMap<String, String> = HashMap()
     
         map["login"] = login.toString()
        map["password"] = emailtext.text.toString()
        services.restorPassword(map).enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>){
                    startActivity(Intent(this@ForgotPasswordActivity, LogInActivity::class.java).apply {

                    } )
                    finish()
                    }

            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.e("-----------call",t.toString())
                Toast.makeText(this@ForgotPasswordActivity, "Connexion error!", Toast.LENGTH_SHORT).show()
            }
        })
      }
}

}

    }


