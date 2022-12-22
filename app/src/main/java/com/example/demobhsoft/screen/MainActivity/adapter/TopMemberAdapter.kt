package com.example.demobhsoft.screen.MainActivity.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demobhsoft.R
import com.example.demobhsoft.model.UserModel
import de.hdodenhof.circleimageview.CircleImageView

class TopMemberAdapter(val mContext: Context, val listUser: ArrayList<UserModel>):
    RecyclerView.Adapter<TopMemberAdapter.TopMemberViewHolder>() {

    private var mlistUser: ArrayList<UserModel> = listUser

    fun setList(list: ArrayList<UserModel>) {
        mlistUser = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopMemberViewHolder {
        return TopMemberViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_member, parent, false))
    }

    override fun onBindViewHolder(holder: TopMemberViewHolder, position: Int) {
        val user: UserModel = listUser.get(position)
        Glide.with(mContext)
            .load(user?.avatar)
            .centerCrop()
            .placeholder(R.drawable.ic_baseline_person_24)
            .into(holder.imgUser);

        holder.tvFullname.text = user.fullName
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    class TopMemberViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imgUser: CircleImageView = itemView.findViewById(R.id.img_user)
        val tvFullname: TextView = itemView.findViewById(R.id.tv_fullname)
    }
}