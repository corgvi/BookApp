package com.example.demobhsoft.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.demobhsoft.R
import com.example.demobhsoft.model.SachModel
import java.text.DecimalFormat
import java.text.NumberFormat

class OrderActivity : AppCompatActivity() {

    private val TAG = "OrderActivity"
    private lateinit var tvNameBook: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvAuthor: TextView
    private lateinit var tvRateNumber: TextView
    private lateinit var tvDesc: TextView
    private lateinit var tvNXB: TextView
    private lateinit var imgBook: ImageView
    private lateinit var btnAdd: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)

        val sach: SachModel = intent.getSerializableExtra("sach") as SachModel
        Log.d(TAG, "onCreate: ${sach.toString()}")
        tvNameBook = findViewById(R.id.tv_name_book)
        tvAuthor = findViewById(R.id.tv_author)
        tvDesc = findViewById(R.id.tv_desc)
        tvPrice = findViewById(R.id.tv_price)
        tvNXB = findViewById(R.id.tv_nxb_name)
        tvRateNumber = findViewById(R.id.tv_rate_number)
        imgBook = findViewById(R.id.img_book)
        btnAdd = findViewById(R.id.btn_add)
        val formatter: NumberFormat = DecimalFormat("#,###")
        tvNameBook.text = sach.name
        tvDesc.text = sach.description
        tvPrice.text = "${formatter.format(sach.price)} VND"
        tvAuthor.text = "by ${sach.author}"
        tvNXB.text = sach.publisher
        tvRateNumber.text = sach.rate.toString()
        Glide.with(this)
            .load(sach?.thumbnail)
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_file_download_off_24)
            .into(imgBook);

        btnAdd.setOnClickListener{
            Toast.makeText(this, "Add", Toast.LENGTH_SHORT).show()
        }
    }
}