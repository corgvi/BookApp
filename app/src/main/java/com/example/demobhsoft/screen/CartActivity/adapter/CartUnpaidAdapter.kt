package com.example.demobhsoft.screen.CartActivity.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.demobhsoft.R
import com.example.demobhsoft.model.GioHang
import com.example.demobhsoft.model.SachModel
import com.example.demobhsoft.utils.Constant
import com.example.demobhsoft.utils.ConvertToVND
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class CartUnpaidAdapter(val listDonHang: ArrayList<GioHang>, val mContext: Context,val callbacks:((GioHang, amount: Int, isCheck: Boolean) -> Unit)) :
    RecyclerView.Adapter<CartUnpaidAdapter.CartUnpaidViewHolder>() {

    private var mListDonHang: ArrayList<GioHang> = listDonHang
    private val db = Firebase.firestore
    private val TAG = "CartUnpaidAdapter"

    fun setList(list: ArrayList<GioHang>) {
        mListDonHang = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartUnpaidViewHolder {
        return CartUnpaidViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CartUnpaidViewHolder, position: Int) {
        val gioHang: GioHang = mListDonHang.get(position)
        var amount: Int = gioHang.amount
        var isCheck: Boolean = gioHang.status
        db.collection(Constant.SACH.TB_SACH)
            .document(gioHang.sachId)
            .get()
            .addOnSuccessListener { task ->
                val sach: SachModel? = task.toObject<SachModel>(SachModel::class.java)
                Log.d(TAG, "onBindViewHolder: ${sach.toString()}")
                holder.imgBook.load(sach?.thumbnail){
                    crossfade(true)
                    crossfade(500)
                    placeholder(R.drawable.ic_baseline_cached_24)
                }
                holder.tvNameBook.text = sach?.name
                holder.tvAmount.text = amount.toString()
                holder.tvPrice.text = ConvertToVND(sach?.price!! *amount)
                holder.status.isChecked = gioHang.status
                holder.status.setOnClickListener{
                    holder.status.isChecked = !isCheck
                    isCheck = !isCheck
                    callbacks(gioHang, amount, isCheck)
                    Log.d(TAG, "onBindViewHolder: check: $isCheck")
                }
                holder.btnPlus.setOnClickListener{
                    if(amount > 10 && amount == 11){
                        amount = 10
                        return@setOnClickListener
                    }
                    amount++
                    holder.tvAmount.text = amount.toString()
                    holder.tvPrice.text = ConvertToVND(sach?.price!! *amount)
                    callbacks(gioHang, amount, isCheck)
                }
                holder.btnMinus.setOnClickListener{
                    if(amount < 1 && amount == 0){
                        amount = 1
                        return@setOnClickListener
                    }
                    amount--
                    holder.tvAmount.text = amount.toString()
                    holder.tvPrice.text = ConvertToVND(sach?.price!! *amount)
                    callbacks(gioHang, amount, isCheck)
                }
            }
        holder.btnDelete.setOnClickListener{

                    val builder = AlertDialog.Builder(mContext)
                    builder.setTitle("Alert")
                    builder.setMessage("Do you want to delete this item ?")
                    builder.setPositiveButton("OK"){dialog, which ->
                        GlobalScope.launch(Dispatchers.IO) {
                            delay(1000L)

                            var delete = db.collection(Constant.GIOHANG.TB_GIOHANG)
                                .document(gioHang.id)
                                .delete().await()

                            withContext(Dispatchers.Main) {
                                notifyDataSetChanged()
                            }
                        }
                    }
                    builder.setNegativeButton("No"){dialog, which ->
                        return@setNegativeButton
                    }
                    builder.show()


        }
    }

    override fun getItemCount(): Int {
        return mListDonHang.size
    }

    class CartUnpaidViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val tvPrice: TextView = itemView.findViewById(R.id.tv_price)
        val tvNameBook: TextView = itemView.findViewById(R.id.tv_name_book)
        val tvAmount: TextView = itemView.findViewById(R.id.tv_amount)
        val btnPlus: ImageView = itemView.findViewById(R.id.imgPlus)
        val btnMinus: ImageView = itemView.findViewById(R.id.imgMinus)
        val imgBook: ImageView = itemView.findViewById(R.id.img_book)
        val btnDelete: ImageView = itemView.findViewById(R.id.btn_delete)
        val status: CheckBox = itemView.findViewById(R.id.check_status)
    }

}