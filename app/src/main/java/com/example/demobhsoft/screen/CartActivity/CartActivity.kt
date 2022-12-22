package com.example.demobhsoft.screen.CartActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demobhsoft.R
import com.example.demobhsoft.datalocal.MySharedPreferences
import com.example.demobhsoft.firebase.GioHangDAO
import com.example.demobhsoft.model.GioHang
import com.example.demobhsoft.model.SachModel
import com.example.demobhsoft.model.UserModel
import com.example.demobhsoft.screen.CartActivity.adapter.CartUnpaidAdapter
import com.example.demobhsoft.screen.CheckOutActivity.CheckOutActivity
import com.example.demobhsoft.utils.Constant
import com.example.demobhsoft.utils.ConvertToVND
import com.example.demobhsoft.viewmodel.CartViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlin.math.log

class CartActivity : AppCompatActivity() {

    private lateinit var cartUnpaidAdapter: CartUnpaidAdapter
    private lateinit var btnCheckOrder: RelativeLayout
    private lateinit var tvTotal: TextView
    private lateinit var rcvCart: RecyclerView
    private lateinit var toolbar: Toolbar
    private val TAG = "CartActivity"
    private var list = ArrayList<GioHang>()
    private val db = Firebase.firestore
    private val mySharedPreferences = MySharedPreferences()
    private val cartViewModel: CartViewModel by lazy {
        ViewModelProvider(
            this,
            CartViewModel.CartViewModelFactory(this.application, this)
        )[CartViewModel::class.java]
    }
    private lateinit var user: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
        btnCheckOrder = findViewById(R.id.btn_check_order)
        tvTotal = findViewById(R.id.tv_total)
        rcvCart = findViewById(R.id.rcv_cart)
        btnCheckOrder.setOnClickListener {
            startActivity(Intent(this, CheckOutActivity::class.java))
        }
        user = mySharedPreferences.getModel(this)!!
        getListDonHangByUserId()
        rcvCart.layoutManager = LinearLayoutManager(this)
        cartUnpaidAdapter = CartUnpaidAdapter(list, this) { gioHang: GioHang, amount: Int, isCheck: Boolean ->
            Log.d(TAG, "onCreate: isCheck = $isCheck")
            gioHang.status = isCheck
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
//            initViewModel()
        }
        rcvCart.adapter = cartUnpaidAdapter
//        initViewModel()
    }

    private fun initViewModel() {
        var listGioHang = ArrayList<GioHang>()

        cartViewModel.getListGioHang()
        cartViewModel.getListSach()
        cartViewModel.mListGioHangLiveData.observe(this, Observer{ list ->
            listGioHang.clear()
            for (gioHang in list){
                if(user.userId.equals(gioHang.userId)){
                    listGioHang.add(gioHang)
                    cartUnpaidAdapter.setList(listGioHang)
                    cartViewModel.mListSachLiveData.observe(this, Observer {
                        var total = 0
                        for (sach in it){
                            if (sach.sachId.equals(gioHang.sachId) && gioHang.status){
                                Log.d(
                                    TAG,
                                    "initViewModel: sachId: ${sach.sachId} ====>>>> gio hang sach id; ${gioHang.sachId}"
                                )
                                total += sach!!.price*gioHang.amount
                                tvTotal.text = ConvertToVND(total)
                            }
                        }
                    })
                }
            }
        })
    }

    private fun getListDonHangByUserId(){
        var total = 0
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
                                if(donHang.status){
                                    val sach: SachModel? = task.toObject(SachModel::class.java)
                                    total += sach!!.price*donHang.amount
                                    Log.d(TAG, "getListDonHangByUserId: total= $total")
                                    tvTotal.text = ConvertToVND(total)
                                }

                            }
                    }
                }
                cartUnpaidAdapter.setList(list)
            }
            .addOnFailureListener{
                Log.e(TAG, "getDonHang: ${it.message}", )
            }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}