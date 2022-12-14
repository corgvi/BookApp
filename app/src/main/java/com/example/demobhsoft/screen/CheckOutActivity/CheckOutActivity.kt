package com.example.demobhsoft.screen.CheckOutActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demobhsoft.R
import com.example.demobhsoft.datalocal.MySharedPreferences
import com.example.demobhsoft.model.DonHang
import com.example.demobhsoft.model.GioHang
import com.example.demobhsoft.model.SachModel
import com.example.demobhsoft.model.UserModel
import com.example.demobhsoft.screen.CheckoutSuccessActivity
import com.example.demobhsoft.utils.Constant
import com.example.demobhsoft.utils.ConvertToVND
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.UUID

class CheckOutActivity : AppCompatActivity() {

    private lateinit var tvTotal: TextView
    private lateinit var btnCheckOut: RelativeLayout
    private lateinit var rcvCheckOut: RecyclerView
    private lateinit var checkOutAdapter: CheckOutAdapter
    private lateinit var toolbar: Toolbar
    private var listCheckOut = ArrayList<GioHang>()
    private val db = Firebase.firestore
    private val mySharedPreferences = MySharedPreferences()
    private val TAG = "CheckOutActivity"
    private lateinit var user: UserModel
    private var total = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        user = mySharedPreferences.getModel(this)!!
        tvTotal = findViewById(R.id.tv_total)
        btnCheckOut = findViewById(R.id.btn_check_out)
        rcvCheckOut = findViewById(R.id.rcv_check_out)
        rcvCheckOut = findViewById(R.id.rcv_check_out)
        getListDonHangByUserId()
        rcvCheckOut.layoutManager = LinearLayoutManager(this)
        checkOutAdapter = CheckOutAdapter(listCheckOut, this)
        rcvCheckOut.adapter = checkOutAdapter
        btnCheckOut.setOnClickListener {
            deleteItemFromCartAndAddToBill()
        }

    }

    private fun getListDonHangByUserId(){
        db.collection(Constant.GIOHANG.TB_GIOHANG)
            .get()
            .addOnSuccessListener { task ->
                listCheckOut.clear()
                for(document in task){
                    val donHang: GioHang = document.toObject(GioHang::class.java)
                    if(user.userId.equals(donHang.userId)){
                        db.collection(Constant.SACH.TB_SACH).document(donHang.sachId).get()
                            .addOnSuccessListener { task ->
                                if(donHang.status){
                                    listCheckOut.add(donHang)
                                    val sach: SachModel? = task.toObject(SachModel::class.java)
                                    total += sach!!.price*donHang.amount
                                    tvTotal.text = ConvertToVND(total)
                                }
                                checkOutAdapter.setList(listCheckOut)
                            }
                    }
                }
            }
            .addOnFailureListener{
                Log.e(TAG, "getDonHang: ${it.message}", )
            }
    }

    private fun deleteItemFromCartAndAddToBill(){
        var listGioHang = ArrayList<GioHang>()
        db.collection(Constant.GIOHANG.TB_GIOHANG)
            .get()
            .addOnSuccessListener { task ->
                listCheckOut.clear()
                listGioHang.clear()
                for(document in task){
                    val gioHang: GioHang = document.toObject(GioHang::class.java)
                    if(gioHang.status && user.userId == gioHang.userId){
                        listGioHang.add(gioHang)
                        db.collection(Constant.GIOHANG.TB_GIOHANG)
                            .document(gioHang.id)
                            .delete()
                            .addOnSuccessListener {
                                startActivity(Intent(this, CheckoutSuccessActivity::class.java))
                                finish()
                            }
                            .addOnFailureListener{
                                Toast.makeText(this, "Check out failed", Toast.LENGTH_SHORT).show()
                                Log.e(TAG, "deleteItemFromCart: ${it.message}", )
                            }
                    }
                }
                val donHang: DonHang = DonHang(listGioHang, total, user.userId)
                addToBill(donHang)
            }
            .addOnFailureListener{
                Log.e(TAG, "deleteItemFromCart2: ${it.message}", )
            }
    }

    private fun  addToBill(donHang: DonHang){
        donHang.id = "${UUID.randomUUID().toString()} - ${user.userId}"
        db.collection(Constant.DonHang.TB_DONHANG)
            .document("${donHang.id}")
            .set(donHang)
            .addOnSuccessListener {
                Toast.makeText(this, "Add to bill successful", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Add to bill failed", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}