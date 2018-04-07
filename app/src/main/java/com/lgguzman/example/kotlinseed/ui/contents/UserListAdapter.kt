package com.lgguzman.example.kotlinseed.ui.fragments

import android.app.Activity
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.lgguzman.example.kotlinseed.R
import com.lgguzman.example.kotlinseed.databinding.ItemUserListBinding
import com.lgguzman.example.kotlinseed.models.User

data class UserHolder(val itemBinding: ItemUserListBinding) : RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(  order: User) {
        itemBinding.user = order
        itemBinding.executePendingBindings()
    }

}

open class UserListAdapter(val activity: Activity, var listOrders: ArrayList<User>) : RecyclerView.Adapter<UserHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemUserListBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_user_list, parent, false)
        return UserHolder(binding)
    }

    override fun getItemCount(): Int {
        return listOrders.size
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val p: User = listOrders.get(position)
        holder.bind(  p);
    }

}