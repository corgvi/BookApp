package com.example.demobhsoft.screen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.demobhsoft.R
import com.example.demobhsoft.datalocal.MySharedPreferences
import com.example.demobhsoft.firebase.UserDAO
import com.example.demobhsoft.model.UserModel
import com.google.firebase.firestore.auth.User
import com.google.gson.Gson
import java.util.*

class LoginActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var edUsername: EditText
    private lateinit var edPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvRegister: TextView
    private lateinit var userDAO: UserDAO
    private fun randomUUID() = UUID.randomUUID().toString()
    private lateinit var mySharedPreferences: MySharedPreferences
    private var listUser = ArrayList<UserModel>()
    private val TAG = "loginactivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        edUsername = findViewById(R.id.ed_username)
        edPassword = findViewById(R.id.ed_password)
        btnLogin = findViewById(R.id.btn_login)
        userDAO = UserDAO()
        listUser = userDAO.getListUser()
        Log.d(TAG, "onCreate: ListUser $listUser")
        tvRegister = findViewById(R.id.tv_register)
        mySharedPreferences = MySharedPreferences()
        btnLogin.setOnClickListener {
            if(edPassword.text.isEmpty() || edUsername.text.isEmpty()){
                Toast.makeText(this, "Please do not leave the data field blank", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            userDAO.loginUser(edUsername.text.toString(), edPassword.text.toString(), listUser, this)
        }
        tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}