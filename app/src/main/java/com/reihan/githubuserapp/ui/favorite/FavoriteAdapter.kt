package com.reihan.githubuserapp.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.reihan.githubuserapp.data.database.UserEntity
import com.reihan.githubuserapp.databinding.ItemUserBinding

class FavoriteAdapter(private val listFavorite: List<UserEntity>) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ViewHolder(binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvName: TextView = binding.tvUser
        val ivPhoto: ImageView = binding.ivUser
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = listFavorite[position].username
        Glide.with(holder.itemView.context)
            .load(listFavorite[position].avatar)
            .into(holder.ivPhoto)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listFavorite[holder.absoluteAdapterPosition])
        }
    }

    override fun getItemCount(): Int = listFavorite.size

    interface OnItemClickCallback {
        fun onItemClicked(data: UserEntity)
    }
}