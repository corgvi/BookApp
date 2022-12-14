package com.example.demobhsoft.screen.CheckOutActivity

import android.content.Context
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
import com.example.demobhsoft.utils.Constant
import com.example.demobhsoft.utils.ConvertToVND
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CheckOutAdapter(listGioHang: ArrayList<GioHang>, val mContext: Context) :
    RecyclerView.Adapter<CheckOutAdapter.CheckOutViewHolder>() {

    private var mListGioHang: ArrayList<GioHang> = listGioHang
    private val db = Firebase.firestore

    fun setList(list: ArrayList<GioHang>){
        mListGioHang = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckOutViewHolder {
        return CheckOutViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_check_out, parent, false))
    }

    override fun onBindViewHolder(holder: CheckOutViewHolder, position: Int) {
        val gioHang: GioHang = mListGioHang.get(position)
        db.collection(Constant.SACH.TB_SACH)
            .document(gioHang.sachId)
            .get()
            .addOnSuccessListener { task ->
                val sach: SachModel = task.toObject<SachModel>(SachModel::class.java)!!
                Glide.with(mContext)
                    .load(sach?.thumbnail)
                    .centerCrop()
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .into(holder.imgBook);
                holder.tvName.text = sach?.name
                holder.tvAuthor.text = sach?.author
                holder.tvPrice.text = ConvertToVND(sach.price)
            }
        holder.tvAmount.text = "Số lượng: ${gioHang.amount}"
    }

    override fun getItemCount(): Int {
        return mListGioHang.size
    }

    class CheckOutViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView){
         val tvName: TextView = itemView.findViewById(R.id.tv_name_book)
         val tvAuthor: TextView = itemView.findViewById(R.id.tv_author)
         val tvAmount: TextView = itemView.findViewById(R.id.tv_amount)
         val imgBook: ImageView = itemView.findViewById(R.id.img_book)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_price)
    }
}