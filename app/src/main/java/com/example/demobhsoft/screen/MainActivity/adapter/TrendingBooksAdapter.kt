package com.example.demobhsoft.screen.MainActivity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demobhsoft.R
import com.example.demobhsoft.model.SachModel
import com.example.demobhsoft.screen.OrderActivity
import java.text.DecimalFormat
import java.text.NumberFormat

class TrendingBooksAdapter(val listSach: ArrayList<SachModel>, val mContext: Context) :
    RecyclerView.Adapter<TrendingBooksAdapter.TrendingBooksViewHolder>() {

    private var mListSach: ArrayList<SachModel> = listSach

    @SuppressLint("NotifyDataSetChanged")
    fun setList(list: ArrayList<SachModel>) {
        mListSach = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingBooksViewHolder {
        return TrendingBooksViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_book_trend, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TrendingBooksViewHolder, position: Int) {
        val formatter: NumberFormat = DecimalFormat("#,###")
        val sach: SachModel = mListSach.get(position)
        holder.tvName.text = sach.name
        holder.tvPrice.text = "${formatter.format(sach.price)} VND"
        Glide.with(mContext)
            .load(sach?.thumbnail)
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_file_download_off_24)
            .into(holder.imgBook);

        holder.imgBook.setOnClickListener {
            val intent = Intent(mContext, OrderActivity::class.java);
            var bundle = Bundle()
            bundle.putSerializable("sach", sach)
            intent.putExtras(bundle)
            mContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mListSach.size
    }

    class TrendingBooksViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView){
        val tvName: TextView = itemView.findViewById(R.id.tv_name_book)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_price)
        val imgBook: ImageView = itemView.findViewById(R.id.img_book)
    }
}