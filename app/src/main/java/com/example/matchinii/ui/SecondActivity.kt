package com.example.matchinii.ui

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentUris
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.matchinii.R
import com.example.matchinii.models.User
import com.example.matchinii.viewmodels.ApiInterface
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class SecondActivity : AppCompatActivity() {
    lateinit var txtLoginup: TextInputEditText
    lateinit var txtLayoutLoginup: TextInputLayout
    lateinit var txtPasswordup: TextInputEditText
    lateinit var txtLayoutPasswordup: TextInputLayout
    lateinit var textinputlayoutage:TextInputLayout
    private var permissionsToRequest: ArrayList<String>? = null
    private val permissionsRejected: ArrayList<String> = ArrayList()
    private val permissions: ArrayList<String> = ArrayList()
    private val ALL_PERMISSIONS_RESULT = 107

    lateinit var register_btn: Button
    lateinit var Profile: ImageView
    lateinit var age: TextInputEditText
    private var selectedImageUri: Uri? = null
    var services = ApiInterface.create()
    private val startForResultOpenGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedImageUri =  result.data!!.data
                Profile!!.setImageURI(result.data!!.data)
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        txtLoginup = findViewById(R.id.EditEmail)
        txtLayoutLoginup = findViewById(R.id.layoutEmail)
        txtPasswordup = findViewById(R.id.EditPassword)
        textinputlayoutage=findViewById(R.id.textinputlayoutage)
        txtLayoutPasswordup = findViewById(R.id.LayoutPassword)
        age=findViewById(R.id.edittextage)
        // checkPermissionWRITE_EXTERNAL_STORAGE(this)
        //txtPasswordRep = findViewById(R.id.EditRestPassword)
        // txtLayoutPasswordRep = findViewById(R.id.LayoutRestPassword)

        register_btn = findViewById(R.id.nextbtn)

        Profile = findViewById(R.id.profilepicsignup)
        askPermissions();
        Profile!!.setOnClickListener {
            //  if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //       ActivityCompat.requestPermissions(this,  arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1);
            //    }


            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "image/*"
            startForResultOpenGallery.launch(intent)
            Log.e("image", selectedImageUri.toString())

        }
        register_btn.setOnClickListener {

            val map: HashMap<String, String> = HashMap()
            map["login"] = txtLoginup.text.toString()
            map["password"] = txtPasswordup.text.toString()
            map["Image"] = selectedImageUri.toString()
            map["Age"] = age.text.toString()
            Log.e("map", map.toString());

            val mBitmap = BitmapFactory.decodeFile(
                getPathFromUri(
                    this.applicationContext,
                    selectedImageUri!!
                )
            )

            val file = File(getPathFromUri(this.applicationContext, selectedImageUri!!))
            val bos = ByteArrayOutputStream()
            mBitmap.compress(Bitmap.CompressFormat.PNG, 0, bos)
            val bitmapdata = bos.toByteArray()

            //  val out: OutputStream? = contentResolver.openOutputStream(file)
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            Log.e(
                "////////////////// ",
                getPathFromUri(this.applicationContext, selectedImageUri!!).toString()
            )
            // val mBitmap = BitmapFactory.decodeFile(getPathFromUri(this.applicationContext, selectedImageUri!!));
            if (validate()) {
                val reqfile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                val body = MultipartBody.Part.createFormData("Image", getName(getPathFromUri(this.applicationContext, selectedImageUri!!)), reqfile)
                val name = RequestBody.create("text/plain".toMediaTypeOrNull(), "Image")
                var loginbody =
                    MultipartBody.Part.createFormData("login", txtLoginup.text.toString())
                var passwordbody =
                    MultipartBody.Part.createFormData("password", txtPasswordup.text.toString())
                var Agebody = MultipartBody.Part.createFormData("Age", age.text.toString())

                Log.e("hhhhhhhh ",getName(selectedImageUri.toString())!! )
                services.register(loginbody, passwordbody, Agebody, body, name)
                    .enqueue(object : Callback<User> {
                        override fun onResponse(call: Call<User>, response: Response<User>) {
                            val user = response.body()
                            if (user != null) {
                                val intent = Intent(
                                    this@SecondActivity, EditProfileActivity::class.java
                                ).apply {
                                    putExtra("login", txtLoginup.text.toString()).apply {
                                        putExtra("age", age.text.toString())
                                        putExtra("image", selectedImageUri.toString())
                                    }
                                }
                                startActivity(intent)
                                finish()
                            } else {
                            }
                        }
                        override fun onFailure(call: Call<User>, t: Throwable) {
                            Log.e("-----------call", t.toString())
                            Toast.makeText(
                                this@SecondActivity,
                                "Connexion error!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }

        }
    }
    fun getName(filename: String?): String? {
        if (filename == null) {
            return null
        }
        val index = filename.lastIndexOf('/')
        return filename.substring(index + 1)
    }
    private fun validate(): Boolean {
        txtLayoutLoginup.error = null
        txtLayoutPasswordup.error = null
        textinputlayoutage.error=null

        if (!Patterns.EMAIL_ADDRESS.matcher(txtLoginup.text.toString()).matches()){
            txtLayoutLoginup.error = getString(R.string.mustNotBeEmpty)
            return false
        }
        if (txtPasswordup.length()<5){
            txtLayoutPasswordup.error =  ("Cette Champs est plus 5 caractères")
            return false
        }
        if (age.text!!.isEmpty()){
            textinputlayoutage.error = getString(R.string.mustNotBeEmpty)
            return false
        }
        return true
    }
    fun getPathFromUri(context: Context, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                )
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(
                    split[1]
                )
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {

            // Return the remote address
            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
                context,
                uri,
                null,
                null
            )
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

    fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            cursor = context.contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }
    val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123
    val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 23

    fun checkPermissionWRITE_EXTERNAL_STORAGE(
        context: Context?
    ): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        return if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE

                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (context as Activity?)!!,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    showDialog2(
                        "External storage", context,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE ,

                        )
                } else {
                    ActivityCompat
                        .requestPermissions(
                            (context as Activity?)!!,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                        )
                }
                false
            } else {
                true
            }
        } else {
            true
        }
    }
    fun checkPermissionREAD_EXTERNAL_STORAGE(
        context: Context?
    ): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        return if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE

                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (context as Activity?)!!,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    showDialog(
                        "External storage", context,
                        Manifest.permission.READ_EXTERNAL_STORAGE ,

                        )
                } else {
                    ActivityCompat
                        .requestPermissions(
                            (context as Activity?)!!,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                        )
                }
                false
            } else {
                true
            }
        } else {
            true
        }
    }
    fun showDialog(
        msg: String, context: Context?,
        permission: String
    ) {
        val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle("Permission necessary")
        alertBuilder.setMessage("$msg permission is necessary")
        alertBuilder.setPositiveButton(android.R.string.yes,
            DialogInterface.OnClickListener { dialog, which ->
                ActivityCompat.requestPermissions(
                    (context as Activity?)!!, arrayOf(permission),
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                )
            })
        val alert: AlertDialog = alertBuilder.create()
        alert.show()
    }
    fun showDialog2(
        msg: String, context: Context?,
        permission: String
    ) {
        val alertBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle("Permission necessary")
        alertBuilder.setMessage("$msg permission is necessary")
        alertBuilder.setPositiveButton(android.R.string.yes,
            DialogInterface.OnClickListener { dialog, which ->
                ActivityCompat.requestPermissions(
                    (context as Activity?)!!, arrayOf(permission),
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                )
            })
        val alert: AlertDialog = alertBuilder.create()
        alert.show()
    }
    private fun askPermissions() {

        permissions.add(WRITE_EXTERNAL_STORAGE)
        permissions.add(READ_EXTERNAL_STORAGE)
        permissionsToRequest = findUnAskedPermissions(permissions)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest!!.size > 0) requestPermissions(
                permissionsToRequest!!.toTypedArray(),
                ALL_PERMISSIONS_RESULT
            )
        }
    }
    private fun findUnAskedPermissions(wanted: ArrayList<String>): ArrayList<String>? {
        val result = ArrayList<String>()
        for (perm in wanted) {
            if (!hasPermission(perm)) {
                result.add(perm)
            }
        }
        return result
    }

    private fun hasPermission(permission: String): Boolean {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
            }
        }
        return true
    }

    private fun canMakeSmores(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }

}




