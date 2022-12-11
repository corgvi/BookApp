package com.example.demobhsoft.screen.CartActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demobhsoft.R
import com.example.demobhsoft.datalocal.MySharedPreferences
import com.example.demobhsoft.firebase.GioHangDAO
import com.example.demobhsoft.model.GioHang
import com.example.demobhsoft.model.SachModel
import com.example.demobhsoft.model.UserModel
import com.example.demobhsoft.screen.CartActivity.adapter.CartUnpaidAdapter
import com.example.demobhsoft.utils.Constant
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlin.math.log

class CartActivity : AppCompatActivity() {

    private lateinit var cartUnpaidAdapter: CartUnpaidAdapter
    private lateinit var btnCheckOrder: RelativeLayout
    private lateinit var tvTotal: TextView
    private lateinit var rcvCart: RecyclerView
    private lateinit var donHangDAO: GioHangDAO
    private val TAG = "CartActivity"
    private var list = ArrayList<GioHang>()
    private val db = Firebase.firestore
    private val mySharedPreferences = MySharedPreferences()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        btnCheckOrder = findViewById(R.id.btn_check_order)
        tvTotal = findViewById(R.id.tv_total)
        rcvCart = findViewById(R.id.rcv_cart)
        btnCheckOrder.setOnClickListener {
            Toast.makeText(this, "Check Order", Toast.LENGTH_SHORT).show()
        }
        getListDonHangByUserId()
        rcvCart.layoutManager = LinearLayoutManager(this)
        cartUnpaidAdapter = CartUnpaidAdapter(list, this) { gioHang: GioHang, amount: Int ->
            gioHang.amount = amount
            db.collection(Constant.GIOHANG.TB_GIOHANG).document(gioHang.id)
                .set(gioHang)
                .addOnCanceledListener {
                    Log.d(TAG, "updated")
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "update failed: ${e.message}",)
                }
            getListDonHangByUserId()
        }
        rcvCart.adapter = cartUnpaidAdapter
    }

    private fun getListDonHangByUserId(){
        var total = 0
        val user: UserModel = mySharedPreferences.getModel(this)!!
        db.collection(Constant.GIOHANG.TB_GIOHANG)
            .get()
            .addOnSuccessListener { task ->
                list.clear()
                for(document in task){
                    val donHang: GioHang = document.toObject(GioHang::class.java)
                    if(user.userId.equals(donHang.userId)){
                        list.add(donHang)
                        db.collection(Constant.SACH.TB_SACH).document(donHang.sachId).get()
                            .addOnSuccessListener { task ->
                                val sach: SachModel? = task.toObject(SachModel::class.java)
                                total += sach!!.price*donHang.amount
                                tvTotal.text = "$total VND"
                            }
                    }
                }
                cartUnpaidAdapter.setList(list)
            }
            .addOnFailureListener{
                Log.e(TAG, "getDonHang: ${it.message}", )
            }
    }
}