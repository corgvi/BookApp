package com.example.demobhsoft.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.demobhsoft.model.GioHang
import com.example.demobhsoft.model.SachModel
import com.example.demobhsoft.model.UserModel
import com.example.demobhsoft.utils.Constant
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class GioHangDAO {

    private val db = Firebase.firestore
    private val TAG = "GioHangDAO"

    fun addToCart(sach: SachModel, user: UserModel, donHang: GioHang, activity: Activity){
        donHang.id = "${sach.sachId}-${user.userId}"
        db.collection(Constant.GIOHANG.TB_GIOHANG)
            .document("${sach.sachId}-${user.userId}")
            .set(donHang)
            .addOnSuccessListener {
                Toast.makeText(activity, "Add to cart successful", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(activity, "Add to cart failed", Toast.LENGTH_SHORT).show()
            }
    }

    fun getGioHang(): ArrayList<GioHang>{
        var list = ArrayList<GioHang>()

        db.collection(Constant.GIOHANG.TB_GIOHANG)
            .get()
            .addOnSuccessListener { task ->
                list.clear()
                for(document in task){
                    val donHang: GioHang = document.toObject(GioHang::class.java)
                    list.add(donHang)
                }
            }
            .addOnFailureListener{
                Log.e(TAG, "getDonHang: ${it.message}", )
            }
        Log.d(TAG, "getDonHang: ${list.size}")
        return list
    }
}