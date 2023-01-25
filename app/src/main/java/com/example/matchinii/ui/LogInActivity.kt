package com.example.matchinii.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns

import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.matchinii.R
import com.example.matchinii.models.User
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import com.example.matchinii.viewmodels.ApiInterface
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LogInActivity : AppCompatActivity() {
    lateinit var txtLoginin: TextInputEditText
    lateinit var txtLayoutLoginin: TextInputLayout
    var services = ApiInterface.create()
    lateinit var txtPasswordin: TextInputEditText
    lateinit var txtLayoutPasswordin: TextInputLayout
    lateinit var btnLogin: Button
    lateinit var toSignUp : TextView
    lateinit var forgetPassWord : TextView
    lateinit var remember : CheckBox
    lateinit var mSharedPref: SharedPreferences
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        toSignUp =findViewById(R.id.textView4)
        forgetPassWord = findViewById(R.id.textView)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("1061364574934-4kj8ater6oaqi5i0q396rjoemkb3cj20.apps.googleusercontent.com")
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        val googleLoginButton = findViewById<ImageView>(R.id.google_login_btn)
        googleLoginButton.setOnClickListener {
            signIn()
        }
        forgetPassWord.setOnClickListener(){
            startActivity(Intent(this , EmailVerifcationActivity::class.java))
        }
        txtLoginin = findViewById(R.id.ancientPasswordEditText)
        mSharedPref = getSharedPreferences("PREF_NAME", MODE_PRIVATE);
          txtLayoutLoginin = findViewById(R.id.ancientPasswordLayout)
        txtPasswordin = findViewById(R.id.newPasswordEditText)
        remember = findViewById(R.id.checkBox)
        txtLayoutPasswordin = findViewById(R.id.newPasswordLayout)
        if (mSharedPref.getBoolean("IS_REMEMBRED", false) ){
                Log.e("hhhhhhhhhh",mSharedPref.getBoolean("IS_REMEMBRED", false).toString())
            startActivity(Intent(this , HomeActivity::class.java).apply {
                putExtra("login", txtLoginin.text.toString())
            })
        }
        toSignUp.setOnClickListener(){
            startActivity(Intent(this@LogInActivity, SecondActivity::class.java))
        }
        fun validate(): Boolean {
            txtLayoutLoginin!!.error = null

            txtLayoutPasswordin!!.error = null
            if (!Patterns.EMAIL_ADDRESS.matcher(txtLoginin.text.toString()).matches()){
                txtLayoutLoginin!!.error = getString(R.string.checkYourEmail)
                return false
            }
             if (txtPasswordin?.text!!.isEmpty()) {
                txtLayoutPasswordin!!.error = getString(R.string.mustNotBeEmpty)
                return false
            }
            return true
        }
        btnLogin = findViewById(R.id.mailverifbtn)
        btnLogin.setOnClickListener {
            if (validate()) {
                val map: HashMap<String, String> = HashMap()
                map["login"] = txtLoginin.text.toString()
                map["password"] = txtPasswordin.text.toString()
                Log.e("user", map.toString())
                services.login(map).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        val user = response.body()
                        if (remember.isChecked) {
                            mSharedPref.edit().apply {
                                putBoolean("IS_REMEMBRED", true)
                                putString("login", txtLoginin.text.toString())
                                putString("password", txtPasswordin.text.toString())

                            }.apply()
                        } else {
                            mSharedPref.edit().clear().apply()

                        }

                        if (user != null) {
                            Toast.makeText(this@LogInActivity, "Login Success", Toast.LENGTH_SHORT)
                                .show()
                            startActivity(Intent(this@LogInActivity, HomeActivity::class.java).apply {  putExtra("login" ,txtLoginin.text.toString() ) })
                        } else {
                            Toast.makeText(this@LogInActivity, "User not found", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(this@LogInActivity, "Connexion error!", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            }

        }
            }
    private fun signIn() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(
            signInIntent, RC_SIGN_IN
        )
    }

// ...
    companion object {
        const val RC_SIGN_IN = 9001
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //Log.e("hamma","tahan")
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {

            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {

            val account = completedTask.getResult(
                ApiException::class.java

            )
            Log.e("test","test1")
            // Signed in successfully
            println(account)

            val map: HashMap<String, String> = HashMap()
            map["googleID"] =  account.id.toString()
            Log.e("google:id",account.id.toString())
            Log.e("google:id",account.givenName.toString())
            Log.e("google:id",account.familyName.toString())
            Log.e("google:id",account.email.toString())

            services.googleVerifier(map)

                .enqueue(object : Callback<User> {


                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        Log.e("test","test1")

                        val user = response.body()
                        println(user)
                        if (user != null) {
                            Log.e("test","user")
                            services.googleSignIn(map)
                                .enqueue(object : Callback<User> {
                                    override fun onResponse(
                                        call: Call<User>,
                                        response: Response<User>
                                    ) {
                                        println(user)
                                        val intent = Intent(this@LogInActivity, HomeActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    }

                                    override fun onFailure(call: Call<User>, t: Throwable) {
                                        TODO("Not yet implemented")
                                        Log.e("probleme","ok")
                                    }

                                })

                        }
                        else {

                            val map1: HashMap<String, String> = HashMap()
                            map1["googleID"] =  account.id.toString()
                            map1["login"] =  account.givenName.toString()
                            map1["lastname"] =  account.familyName.toString()
                            map1["email"] =  account.email.toString()
                            services.googleSignUp(map1)
                                .enqueue(object : Callback<User> {
                                    override fun onResponse(
                                        call: Call<User>,
                                        response: Response<User>
                                    ) {
                                        Log.e("amlneh el compte","cree")
                                    }

                                    override fun onFailure(call: Call<User>, t: Throwable) {
                                        Log.e("faama mochkla","non cree")
                                    }

                                })
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.e("failed verifier","failed to connect to google verifier")
                    }


                })
//            val googleId = account?.id ?: ""}
//            Log.i("Google ID",googleId)
//            val googleFirstName = account?.givenName ?: ""
//            Log.i("Google First Name", googleFirstName)
//            val googleLastName = account?.familyName ?: ""
//            Log.i("Google Last Name", googleLastName)
//            val googleEmail = account?.email ?: ""
//            Log.i("Google Email", googleEmail)
//            val googleProfilePicURL = account?.photoUrl.toString()
//            Log.i("Google Profile Pic URL", googleProfilePicURL)
//            val googleIdToken = account?.idToken ?: ""
//            Log.i("Google ID Token", googleIdToken)
        } catch (e: ApiException) {
            // Sign in was unsuccessful
            Log.e(
                "failed code=", e.statusCode.toString()
            )
        }
    }

}






