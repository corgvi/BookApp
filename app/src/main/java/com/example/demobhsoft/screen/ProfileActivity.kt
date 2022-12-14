package com.example.demobhsoft.screen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.demobhsoft.R
import com.example.demobhsoft.datalocal.MySharedPreferences
import com.example.demobhsoft.model.UserModel
import com.example.demobhsoft.utils.Constant
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import de.hdodenhof.circleimageview.CircleImageView

class ProfileActivity : AppCompatActivity() {

    private lateinit var tvFullname: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvFirebseId: TextView
    private lateinit var tvUserId: TextView
    private lateinit var imgUser: CircleImageView
    private lateinit var imgBgUser: ImageView
    private lateinit var mySharedPreferences: MySharedPreferences
    private lateinit var toolbar: Toolbar
    private val storageRef = Firebase.storage.reference;
    private val db = Firebase.firestore
    private lateinit var url: String
    private val TAG = "ProfileActivity"
    private lateinit var user: UserModel;
    private lateinit var btnLogout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        tvEmail = findViewById(R.id.tv_email)
        tvFullname = findViewById(R.id.tv_fullname)
        tvFirebseId = findViewById(R.id.tv_firebaseId)
        tvUserId = findViewById(R.id.tv_userId)
        imgUser = findViewById(R.id.img_profile)
        btnLogout = findViewById(R.id.btn_logout)
        imgBgUser = findViewById(R.id.img_bg)
        mySharedPreferences = MySharedPreferences()
        user = mySharedPreferences.getModel(this)!!

        Glide.with(this)
            .load(user?.avatar)
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_file_download_off_24)
            .into(imgUser);
        Glide.with(this)
            .load(user?.imgBackground)
            .centerCrop()
            .placeholder(R.drawable.bg_default)
            .into(imgBgUser);
        tvFirebseId.text = user?.fireBaseId
        tvEmail.text = user?.email
        tvUserId.text = user?.userId
        tvFullname.text = user?.fullName

        imgUser.setOnClickListener{
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/*"
            imageAvartarActivityResult.launch(galleryIntent)
        }

        imgBgUser.setOnClickListener{
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/*"
            imageBgAvartarActivityResult.launch(galleryIntent)
        }

        btnLogout.setOnClickListener{
            logOut()
        }
    }

    private fun logOut() {
        FirebaseAuth.getInstance().signOut()
        val mySharedPreferences = MySharedPreferences()
        mySharedPreferences.clearModel(this)
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private var imageAvartarActivityResult: ActivityResultLauncher<Intent> =
    // lambda expression to receive a result back, here we
        // receive single item(photo) on selection
        registerForActivityResult( ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null) {
                // getting URI of selected Image
                val imageUri: Uri? = result.data?.data

                // val fileName = imageUri?.pathSegments?.last()

                // extract the file name with extension
                val sd = getFileName(applicationContext, imageUri!!)

                // Upload Task with upload to directory 'file'
                // and name of the file remains same
                val uploadTask = storageRef.child("file/$sd").putFile(imageUri)

                // On success, download the file URL and display it
                uploadTask.addOnSuccessListener {
                    // using glide library to display the image
                    storageRef.child("file/$sd").downloadUrl.addOnSuccessListener {
                        Glide.with(this)
                            .load(it)
                            .into(imgUser)
                        updateAvatar(it.toString())
                        Log.e("Firebase", "download passed")
                    }.addOnFailureListener {
                        Log.e("Firebase", "Failed in downloading")
                    }
                }.addOnFailureListener {
                    Log.e("Firebase", "Image Upload fail")
                }
            }
        }

    private var imageBgAvartarActivityResult: ActivityResultLauncher<Intent> =
    // lambda expression to receive a result back, here we
        // receive single item(photo) on selection
        registerForActivityResult( ActivityResultContracts.StartActivityForResult()) { result ->
            if (result != null) {
                // getting URI of selected Image
                val imageUri: Uri? = result.data?.data

                // val fileName = imageUri?.pathSegments?.last()

                // extract the file name with extension
                val sd = getFileName(applicationContext, imageUri!!)

                // Upload Task with upload to directory 'file'
                // and name of the file remains same
                val uploadTask = storageRef.child("file/$sd").putFile(imageUri)

                // On success, download the file URL and display it
                uploadTask.addOnSuccessListener {
                    // using glide library to display the image
                    storageRef.child("file/$sd").downloadUrl.addOnSuccessListener {
                        Glide.with(this)
                            .load(it)
                            .into(imgBgUser)
                        updateImgBackground(it.toString())
                        Log.e("Firebase", "download passed")
                    }.addOnFailureListener {
                        Log.e("Firebase", "Failed in downloading")
                    }
                }.addOnFailureListener {
                    Log.e("Firebase", "Image Upload fail")
                }
            }
        }

    @SuppressLint("Range")
    private fun getFileName(context: Context, uri: Uri): String? {
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor.use {
                if (cursor != null) {
                    if(cursor.moveToFirst()) {
                        return cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                    }
                }
            }
        }
        return uri.path?.lastIndexOf('/')?.let { uri.path?.substring(it) }
    }

    private fun updateAvatar(url: String){
        db.collection(Constant.USER.TB_USER)
            .document(user.userId)
            .update("avatar", url)
            .addOnFailureListener {
                Toast.makeText(this, "Update avatar successful", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Update avatar failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateImgBackground(url: String){
        db.collection(Constant.USER.TB_USER)
            .document(user.userId)
            .update("imgBackground", url)
            .addOnFailureListener {
                Toast.makeText(this, "Update avatar successful", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Update avatar failed", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}