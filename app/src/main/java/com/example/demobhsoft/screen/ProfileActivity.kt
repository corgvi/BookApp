package com.example.demobhsoft.screen

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.demobhsoft.R
import com.example.demobhsoft.datalocal.MySharedPreferences
import com.example.demobhsoft.model.UserModel
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
    private val TAG = "ProfileActivity"

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
        imgBgUser = findViewById(R.id.img_bg)
        mySharedPreferences = MySharedPreferences()
        val user: UserModel? = mySharedPreferences.getModel(this)

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
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}