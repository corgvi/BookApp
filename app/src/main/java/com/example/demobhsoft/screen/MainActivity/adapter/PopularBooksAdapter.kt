package com.example.demobhsoft.screen.MainActivity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demobhsoft.R
import com.example.demobhsoft.model.SachModel
import java.text.DecimalFormat
import java.text.NumberFormat

class PopularBooksAdapter(val listSach: ArrayList<SachModel>, val mContext: Context) :
    RecyclerView.Adapter<PopularBooksAdapter.PopularBooksViewHolder>() {

    private var mListSach: ArrayList<SachModel> = listSach

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: ArrayList<SachModel>) {
        mListSach = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularBooksViewHolder {
        return PopularBooksViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_book_trend, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PopularBooksViewHolder, position: Int) {
        val formatter: NumberFormat = DecimalFormat("#,###")
        val sach: SachModel = mListSach.get(position)
        holder.tvName.text = sach.name
        holder.tvPrice.text = "${formatter.format(sach.price)} VND"
        Glide.with(mContext)
            .load(sach?.thumbnail)
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_person_24)
            .into(holder.imgBook);
    }

    override fun getItemCount(): Int {
        return mListSach.size
    }

    class PopularBooksViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView){
        val tvName: TextView = itemView.findViewById(R.id.tv_name_book)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_price)
        val imgBook: ImageView = itemView.findViewById(R.id.img_book)
    }
}