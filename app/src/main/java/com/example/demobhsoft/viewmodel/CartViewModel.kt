package com.example.demobhsoft.viewmodel

import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.demobhsoft.firebase.GioHangDAO
import com.example.demobhsoft.firebase.SachDAO
import com.example.demobhsoft.model.GioHang
import com.example.demobhsoft.model.SachModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartViewModel(application: Application, mContext: Context) : ViewModel() {

    val mListSachLiveData = MutableLiveData<ArrayList<SachModel>>()
    val mListGioHangLiveData = MutableLiveData<ArrayList<GioHang>>()
    val mPriceLiveData = MutableLiveData<Int>()
    val mGioHangLiveData = MutableLiveData<GioHang>()
    val progressDialog = ProgressDialog(mContext)

    fun getListSach() = viewModelScope.launch {
        onPreExecute()
        val result = doInBackground()
        onPostExecute(result)
    }

    fun getListGioHang() = viewModelScope.launch {
        onPreExecute()
        val result = doInBackground()
        onPostExecute(result)
    }

    suspend fun doInBackground(): String = withContext(Dispatchers.IO){
        delay(1000L)
        SachDAO().getListSach {
            mListSachLiveData.value = it
        }

        GioHangDAO().getListGioHangByUserId {
            mListGioHangLiveData.value = it
        }

        return@withContext "Some results"
    }

    fun onPreExecute(){
        progressDialog.setTitle("Book store")
        progressDialog.setMessage("Application is loading, please wait...")
        progressDialog.show()
    }

    fun onPostExecute(result: String){
        progressDialog.dismiss()
    }

    class CartViewModelFactory(private val application: Application, private val mContext: Context): ViewModelProvider.Factory{
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(CartViewModel::class.java)) {
                return CartViewModel(application, mContext) as T
            }
            throw IllegalStateException("Unable construct view model")
        }
    }

}