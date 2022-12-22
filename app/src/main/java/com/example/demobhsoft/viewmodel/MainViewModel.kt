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

class MainViewModel(application: Application, mContext: Context): ViewModel() {

    val mListSachLiveData = MutableLiveData<ArrayList<SachModel>>()
    val mListGioHangLiveData = MutableLiveData<ArrayList<GioHang>>()
    val progressDialog = ProgressDialog(mContext)

    fun getListSach() = viewModelScope.launch {
        onPreExecute()
        val result = doInBackground() // runs in background thread without blocking the Main Thread
        onPostExecute(result)
    }

    fun getListGioHang() = viewModelScope.launch {
        onPreExecute()
        val result = doInBackground()
        onPostExecute(result)
    }

    private suspend fun doInBackground(): String = withContext(Dispatchers.IO) { // to run code in Background Thread
        // do async work
        delay(1000) // simulate async work
        SachDAO().getListSach {
            mListSachLiveData.value = it
        }
        GioHangDAO().getListGioHangByUserId() {
            mListGioHangLiveData.value = it
        }
        return@withContext "SomeResult"
    }

    // Runs on the Main(UI) Thread
    private fun onPreExecute() {
        progressDialog.setTitle("Book store")
        progressDialog.setMessage("Application is loading, please wait...")
        progressDialog.show()
    }

    // Runs on the Main(UI) Thread
    private fun onPostExecute(result: String) {
        // hide progress
        progressDialog.dismiss()
    }

    class MainViewModelFactory(private val application: Application, private val context: Context): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MainViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(application, context) as T
            }
            throw IllegalStateException("Unable construct viewmodel")
        }
    }
}