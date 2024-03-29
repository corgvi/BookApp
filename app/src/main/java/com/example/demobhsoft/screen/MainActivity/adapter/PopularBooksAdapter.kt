package com.example.demobhsoft.screen.MainActivity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.demobhsoft.R
import com.example.demobhsoft.model.SachModel
import com.example.demobhsoft.utils.ConvertToVND

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
        val sach: SachModel = mListSach.get(position)
        holder.tvName.text = sach.name
        holder.tvPrice.text = ConvertToVND(sach.price)
//        Glide.with(mContext)
//            .load(sach?.thumbnail)
//            .centerCrop()
//            .placeholder(R.drawable.ic_baseline_file_download_off_24)
//            .into(holder.imgBook);
        holder.imgBook.load(sach.thumbnail){
            crossfade(true)
            crossfade(500)
            placeholder(R.drawable.ic_baseline_cached_24)
        }
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