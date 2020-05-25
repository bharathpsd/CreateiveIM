package com.example.android.creativeim.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.creativeim.R
import com.example.android.creativeim.data.User
import com.example.android.creativeim.databinding.UserItemBinding
import com.example.android.creativeim.utils.Logger

private const val TAG = "UserAdpater"

class UserAdpater(
    private val mainViewModel: MainViewModel
) : RecyclerView.Adapter<UserAdpater.UserDataAdapterViewHolder>() {

    class UserDataAdapterViewHolder(private val binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: MainViewModel, user: User) {
            binding.viewmodel = viewModel
            binding.user = user
            binding.executePendingBindings()
        }
    }

    private val differCallBack = object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserDataAdapterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UserDataAdapterViewHolder(
            DataBindingUtil.inflate(layoutInflater, R.layout.user_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: UserDataAdapterViewHolder, position: Int) {
        val user = differ.currentList[position]
        Logger.log(TAG, "At position $position : $user")
        holder.bind(mainViewModel, user)
    }

    private var onItemClickListener: ((User) -> Unit)? = null

    fun setOnItemClickListener(listener: (User) -> Unit) {
        onItemClickListener = listener
    }
}