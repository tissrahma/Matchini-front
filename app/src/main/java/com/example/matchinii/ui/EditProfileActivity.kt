package com.example.matchinii.ui
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.matchinii.R
import com.example.matchinii.models.User
import com.example.matchinii.viewmodels.ApiInterface
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
lateinit var LastName: TextInputEditText
lateinit var LastNameLayout: TextInputLayout
lateinit var School: TextInputEditText
lateinit var SchoolLayout: TextInputLayout
lateinit var Aboutyou: TextInputEditText
lateinit var AboutyouLayout: TextInputLayout
lateinit var Job: TextInputEditText
lateinit var jobLayout: TextInputLayout
lateinit var FirstName: TextInputEditText
lateinit var FirstNameLayout: TextInputLayout
lateinit var age: TextInputEditText
lateinit var ageLayout: TextInputLayout
lateinit var phone: TextInputEditText
lateinit var phoneLayout: TextInputLayout
lateinit var btnNext: Button
lateinit var Profile: ImageView
lateinit var btnMale: Button
lateinit var btnFemale: Button
private var loginIntent3 : String? = null

lateinit var sexe : String
private var selectedImageUri: Uri? = null

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        sexe = "female"
        LastName = findViewById(R.id.Elastname)
        LastNameLayout = findViewById(R.id.inpoutLastName)
        FirstName = findViewById(R.id.EFirstName)
        FirstNameLayout = findViewById(R.id.inpoutFirstName)
        School = findViewById(R.id.Eschool)
        SchoolLayout = findViewById(R.id.inpoutschool)
        Aboutyou = findViewById(R.id.EAbout)
        AboutyouLayout = findViewById(R.id.inpoutAbout)
        Job = findViewById(R.id.EJobTile)
        jobLayout = findViewById(R.id.inpoutJobTile)
        age = findViewById(R.id.EAge)
        ageLayout = findViewById(R.id.inpoutAge)
        phone = findViewById(R.id.EPhone)
        phoneLayout = findViewById(R.id.inpoutPhone)
        btnNext = findViewById(R.id.Save)

        btnMale = findViewById(R.id.woman_button)
        btnFemale = findViewById(R.id.man_button)

        btnMale.setOnClickListener() {
            sexe = "Woman"
            Toast.makeText(this, "Woman", Toast.LENGTH_LONG).show()
            btnMale.setBackgroundColor(Color.GRAY);
            btnFemale.setBackgroundColor(Color.TRANSPARENT);

        }
        btnFemale.setOnClickListener() {
            sexe = "man"
            Toast.makeText(this, "man", Toast.LENGTH_LONG).show()
            btnFemale.setBackgroundColor(Color.GRAY);
            btnMale.setBackgroundColor(Color.TRANSPARENT);

        }

        fun validate(): Boolean {
            ageLayout.error = null
            FirstNameLayout.error = null

            if (FirstName.text!!.isEmpty()) {
                FirstNameLayout.error = getString(R.string.mustNotBeEmpty)
                return false
            }
            if (age.text!!.isEmpty()) {
                ageLayout.error = getString(R.string.mustNotBeEmpty)
                return false
            }
            return true
        }
        loginIntent3 = intent!!.getStringExtra("login")
        var services = ApiInterface.create()
        btnNext.setOnClickListener() {
            if (validate()) {
                val map: HashMap<String, String> = HashMap()
                map["login"] = loginIntent3.toString()
                map["FirstName"] = FirstName.text.toString()
                map["LasteName"] = LastName.text.toString()
                map["Age"] = age.text.toString()
                map["Numero"] = phone.text.toString()
                map["AboutYou"] = Aboutyou.text.toString()
                map["School"] = School.text.toString()
                map["Job"] = Job.text.toString()
                map["Sexe"] = sexe
                map["Image"] = selectedImageUri.toString()
                Log.e("intent3", loginIntent3.toString())
                Log.e("lmap ", map.toString())
                services.modifier(map).enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {
                        val user = response.body()

                        Log.e("user is ", user.toString())

                        /*        if (remember.isChecked) {
                        mSharedPref.edit().apply {
                            putBoolean("IS_REMEMBRED", true)
                            putString("login", txtLoginin.text.toString())
                            putString("password", txtPasswordin.text.toString())
                        }.apply()
                    } else {
                        mSharedPref.edit().clear().apply()
                    }
                    startActivity(Intent(this@EditProfileActivity, HomeActivity::class.java))
                    Log.e("jjjjjjjj", response.body().toString())
 */
                        if (user != null) {
                            Toast.makeText(this@EditProfileActivity, " Success", Toast.LENGTH_SHORT)
                                .show()
                            startActivity(
                                Intent(
                                    this@EditProfileActivity,
                                    HomeActivity::class.java
                                ).apply {
                                    putExtra("login", loginIntent3.toString())
                                    putExtra("name", FirstName.text.toString())
                                    putExtra("age", age.text.toString())
                                    putExtra("image", selectedImageUri.toString())
                                })
                            //
                            //  val intent = Intent()
                            //  intent.putExtra("name", FirstName.text.toString())
                            //  setResult(Activity.RESULT_OK, intent)
                            //  finish()
                        } else {
                            Toast.makeText(
                                this@EditProfileActivity,
                                "User not found",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Toast.makeText(
                            this@EditProfileActivity,
                            "Connexion error!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                })
            }

        }
    }
}
