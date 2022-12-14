package com.example.demobhsoft.screen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.demobhsoft.R
import com.example.demobhsoft.firebase.UserDAO
import com.example.demobhsoft.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var edUsername: EditText
    private lateinit var edPassword: EditText
    private lateinit var edFullName: EditText
    private lateinit var btnRegister: Button
    private lateinit var tvLogin: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var userDAO: UserDAO
    private val TAG = "registerUser"
    private fun randomUUID() = UUID.randomUUID().toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        userDAO = UserDAO()
        edUsername = findViewById(R.id.ed_username)
        edPassword = findViewById(R.id.ed_password)
        edFullName = findViewById(R.id.ed_fullname);
        btnRegister = findViewById(R.id.btn_register)
        tvLogin = findViewById(R.id.tv_login)
        btnRegister.setOnClickListener {
            Log.d(TAG, "onCreate: onclick btnRegister")
            if(edFullName.text.isEmpty() || edUsername.text.isNotEmpty() || edPassword.text.isEmpty()){
                Toast.makeText(this, "Please do not leave the data field blank", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            var user = UserModel(randomUUID(), edUsername.text.toString(), edPassword.text.toString(), edFullName.text.toString());
            userDAO.createUserWithEmail(user, this)
        }

        tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
        }
    }
}