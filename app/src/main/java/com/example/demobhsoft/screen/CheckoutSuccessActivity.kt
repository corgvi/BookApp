package com.example.demobhsoft.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RelativeLayout
import com.example.demobhsoft.R
import com.example.demobhsoft.screen.MainActivity.MainActivity

class CheckoutSuccessActivity : AppCompatActivity() {

    private lateinit var btnCheckOut: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_success)

        btnCheckOut = findViewById(R.id.btn_check_out)

        btnCheckOut.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}