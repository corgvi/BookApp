package com.example.demobhsoft.screen.MainActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demobhsoft.R
import com.example.demobhsoft.datalocal.MySharedPreferences
import com.example.demobhsoft.firebase.SachDAO
import com.example.demobhsoft.firebase.UserDAO
import com.example.demobhsoft.model.SachModel
import com.example.demobhsoft.model.UserModel
import com.example.demobhsoft.screen.MainActivity.adapter.PopularBooksAdapter
import com.example.demobhsoft.screen.MainActivity.adapter.TopMemberAdapter
import com.example.demobhsoft.screen.MainActivity.adapter.TrendingBooksAdapter
import com.example.demobhsoft.screen.ProfileActivity
import com.example.demobhsoft.utils.Constant
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private lateinit var userDAO: UserDAO
    val TAG = "zzzz"
    private lateinit var mySharedPreferences: MySharedPreferences
    private lateinit var tvFullname: TextView
    private lateinit var tvEmail: TextView
    private lateinit var imgUser: CircleImageView
    private lateinit var rcvTopMembers: RecyclerView
    private lateinit var rcvTrendingBooks: RecyclerView
    private lateinit var rcvPopularBooks: RecyclerView
    private var listUser = ArrayList<UserModel>()
    private var listSach = ArrayList<SachModel>()
    private lateinit var topMemberAdapter: TopMemberAdapter
    private lateinit var trendingBooksAdapter: TrendingBooksAdapter
    private lateinit var popularBooksAdapter: PopularBooksAdapter
    private val db = Firebase.firestore
    private val sachDAO = SachDAO()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvFullname = findViewById(R.id.tv_fullname)
        tvEmail = findViewById(R.id.tv_email)
        imgUser = findViewById(R.id.img_avatar)
        rcvTopMembers = findViewById(R.id.rcv_top_members)
        rcvPopularBooks = findViewById(R.id.rcv_popular_books)
        rcvTrendingBooks = findViewById(R.id.rcv_trend_books)
        mySharedPreferences = MySharedPreferences()
        val user: UserModel? = mySharedPreferences.getModel(this)

        tvFullname.text = user?.fullName
        tvEmail.text = user?.email
        Glide.with(this)
            .load(user?.avatar)
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_person_24)
            .into(imgUser);
        imgUser.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        userDAO = UserDAO()
        listUser.addAll(userDAO.getListUser())
        rcvTopMembers.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        topMemberAdapter = TopMemberAdapter(this, listUser)
        Log.d(TAG, "onCreate: listUser ${listUser.size}")
        rcvTopMembers.adapter = topMemberAdapter
        getListUser()
        sachDAO.initSachModel()
        getListSach()
        trendingBooksAdapter = TrendingBooksAdapter(listSach, this)
        rcvTrendingBooks.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rcvTrendingBooks.adapter = trendingBooksAdapter
        popularBooksAdapter = PopularBooksAdapter(listSach, this)
        rcvPopularBooks.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rcvPopularBooks.adapter = popularBooksAdapter
    }

    private fun getListUser(){
        db.collection(Constant.USER.TB_USER)
            .get()
            .addOnSuccessListener { task ->
                listUser.clear()
                for (document in task) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val user: UserModel = document.toObject(UserModel::class.java)
                    listUser.add(user)
                }
                topMemberAdapter.setList(listUser)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }

    private fun getListSach(){

        db.collection(Constant.SACH.TB_SACH)
            .get()
            .addOnSuccessListener { task ->
                listSach.clear()
                for (document in task) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val sach: SachModel = document.toObject(SachModel::class.java)
                    listSach.add(sach)
                }
                trendingBooksAdapter.setList(listSach)
                popularBooksAdapter.setList(listSach)
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }

}