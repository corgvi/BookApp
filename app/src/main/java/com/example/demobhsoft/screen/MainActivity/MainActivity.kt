package com.example.demobhsoft.screen.MainActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demobhsoft.R
import com.example.demobhsoft.datalocal.MySharedPreferences
import com.example.demobhsoft.firebase.SachDAO
import com.example.demobhsoft.firebase.UserDAO
import com.example.demobhsoft.model.GioHang
import com.example.demobhsoft.model.SachModel
import com.example.demobhsoft.model.UserModel
import com.example.demobhsoft.screen.CartActivity.CartActivity
import com.example.demobhsoft.screen.MainActivity.adapter.PopularBooksAdapter
import com.example.demobhsoft.screen.MainActivity.adapter.TopMemberAdapter
import com.example.demobhsoft.screen.MainActivity.adapter.TrendingBooksAdapter
import com.example.demobhsoft.screen.ProfileActivity
import com.example.demobhsoft.utils.Constant
import com.example.demobhsoft.viewmodel.MainViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class MainActivity : AppCompatActivity() {
    private lateinit var userDAO: UserDAO
    val TAG = "zzzz"
    private lateinit var mySharedPreferences: MySharedPreferences
    private lateinit var searchView: EditText
    private lateinit var tvFullname: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvBadge: TextView
    private lateinit var imgCart: ImageView
    private lateinit var imgUser: CircleImageView
    private lateinit var rcvTopMembers: RecyclerView
    private lateinit var rcvTrendingBooks: RecyclerView
    private lateinit var rcvPopularBooks: RecyclerView
    private var listUser = ArrayList<UserModel>()
    private var listSach = ArrayList<SachModel>()
    private val listGioHang = ArrayList<GioHang>()
    private lateinit var topMemberAdapter: TopMemberAdapter
    private lateinit var trendingBooksAdapter: TrendingBooksAdapter
    private lateinit var popularBooksAdapter: PopularBooksAdapter
    private val db = Firebase.firestore
    private val sachDAO = SachDAO()
    private val mainViewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModel.MainViewModelFactory(this.application, this)
        )[MainViewModel::class.java]
    }
    private lateinit var user: UserModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvFullname = findViewById(R.id.tv_fullname)
        tvEmail = findViewById(R.id.tv_email)
        imgUser = findViewById(R.id.img_avatar)
        imgCart = findViewById(R.id.img_cart)
        tvBadge = findViewById(R.id.tv_badge)
        rcvTopMembers = findViewById(R.id.rcv_top_members)
        rcvPopularBooks = findViewById(R.id.rcv_popular_books)
        rcvTrendingBooks = findViewById(R.id.rcv_trend_books)
        mySharedPreferences = MySharedPreferences()
        user= mySharedPreferences.getModel(this)!!

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
        initViewModel()
        topMemberAdapter = TopMemberAdapter(this, listUser)
        rcvTopMembers.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rcvTopMembers.adapter = topMemberAdapter
        getListUser()
        sachDAO.initSachModel()
        trendingBooksAdapter = TrendingBooksAdapter(listSach, this)
        rcvTrendingBooks.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rcvTrendingBooks.adapter = trendingBooksAdapter
        popularBooksAdapter = PopularBooksAdapter(listSach, this)
        rcvPopularBooks.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rcvPopularBooks.adapter = popularBooksAdapter

        imgCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }
        initSearchView()

    }

    private fun initViewModel(){
        mainViewModel.getListSach()
        mainViewModel.getListGioHang()
        mainViewModel.mListSachLiveData.observe(this, Observer{
            trendingBooksAdapter.setList(it)
            popularBooksAdapter.setList(it)
        })
        mainViewModel.mListGioHangLiveData.observe(this, Observer{
            var list = ArrayList<GioHang>()
            for(gioHang in it){
                if(gioHang.userId.equals(user.userId)){
                    list.add(gioHang)
                }
            }
            tvBadge.setText(list.size.toString())
        })

    }

    private fun getListUser() {
        GlobalScope.launch(Dispatchers.IO) {
            delay(1000L)
            var list = db.collection(Constant.USER.TB_USER)
                .get().await().documents
            for (user in list) {
                listUser.add(user.toObject(UserModel::class.java)!!)
            }
            withContext(Dispatchers.Main) {
                topMemberAdapter.setList(listUser)
            }
        }
    }

    private fun initSearchView() {
        searchView = findViewById(R.id.ed_search)
        searchView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val str = s.toString()
                val listFilter = ArrayList<SachModel>()
                for (sach in listSach) {
                    if (sach.name.contains(str, true)) {
                        listFilter.add(sach)
                    }
                }

                if (s!!.isEmpty()) {
                    popularBooksAdapter.setList(listSach)
                    trendingBooksAdapter.setList(listSach)
                } else {
                    popularBooksAdapter.setList(listFilter)
                    trendingBooksAdapter.setList(listFilter)

                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.getListGioHang()
        mainViewModel.mListGioHangLiveData.observe(this, Observer{
            var list = ArrayList<GioHang>()
            for(gioHang in it){
                if(gioHang.userId.equals(user.userId)){
                    list.add(gioHang)
                }
            }
            tvBadge.setText(list.size.toString())
        })
    }

}