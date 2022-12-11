package com.example.demobhsoft.screen.CartActivity.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demobhsoft.R
import com.example.demobhsoft.model.GioHang
import com.example.demobhsoft.model.SachModel
import com.example.demobhsoft.screen.CartActivity.OnClickChangeAmount
import com.example.demobhsoft.utils.Constant
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CartUnpaidAdapter(val listDonHang: ArrayList<GioHang>, val mContext: Context,val callbacks:((GioHang, amount: Int) -> Unit)) :
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
        db.collection(Constant.SACH.TB_SACH)
            .document(gioHang.sachId)
            .get()
            .addOnSuccessListener { task ->
                val sach: SachModel? = task.toObject<SachModel>(SachModel::class.java)
                Log.d(TAG, "onBindViewHolder: ${sach.toString()}")
                Glide.with(mContext)
                    .load(sach?.thumbnail)
                    .centerCrop()
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .into(holder.imgBook);
                holder.tvNameBook.text = sach?.name
                holder.tvAmount.text = amount.toString()
                holder.tvPrice.text = "${sach?.price!! *amount} VND"
                holder.btnPlus.setOnClickListener{
                    if(amount > 10 && amount == 11){
                        amount = 10
                        return@setOnClickListener
                    }
                    amount++
                    holder.tvAmount.text = amount.toString()
                    holder.tvPrice.text = "${sach?.price!! *amount} VND"
                    callbacks(gioHang, amount)
                }
                holder.btnMinus.setOnClickListener{
                    if(amount < 1 && amount == 0){
                        amount = 1
                        return@setOnClickListener
                    }
                    amount--
                    holder.tvAmount.text = amount.toString()
                    holder.tvPrice.text = "${sach?.price!! *amount} VND"
                    callbacks(gioHang, amount)
                }
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

    }

}