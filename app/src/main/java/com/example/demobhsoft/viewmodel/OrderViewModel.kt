package com.example.demobhsoft.viewmodel

import android.app.Application
import android.app.ProgressDialog
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.demobhsoft.firebase.SachDAO
import com.example.demobhsoft.model.SachModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderViewModel(var sach: SachModel, application: Application, mContext: Context) : ViewModel() {

    val mSachDetailLiveData = MutableLiveData<SachModel>()
    val progressDialog = ProgressDialog(mContext)

    private suspend fun doInBackground(): String = withContext(Dispatchers.IO) {
        delay(1000L)
        SachDAO().getSach(sach.sachId)
        return@withContext "SomeResult"
    }

    private fun onPreExecute(){
        progressDialog.setTitle("Progress Bar")
        progressDialog.setMessage("Application is loading, please wait...")
        progressDialog.show()
    }

    private fun onPostExecute(result: String){
        progressDialog.dismiss()
    }

    fun getSachDetails() = viewModelScope.launch{
        onPreExecute()
        val result = doInBackground()
        onPostExecute(result)
    }

    class OrderViewModelFactory(private val application: Application, private val context: Context, private val sach: SachModel): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(OrderViewModel::class.java)){
                return  OrderViewModelFactory(application, context, sach) as T
            }
            throw  IllegalStateException("Unable construct viewmodel")
        }
    }
}