package com.example.android.creativeim.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.creativeim.R
import com.example.android.creativeim.databinding.MessageItemBinding
import com.example.android.creativeim.messagedata.MessageData
import com.example.android.creativeim.utils.Logger

private const val TAG = "MessagesAdapter"

class MessagesAdapter(
    private val mainViewModel: MainViewModel
) : RecyclerView.Adapter<MessagesAdapter.MessageDataAdapterViewHolder>() {

    class MessageDataAdapterViewHolder(private val binding: MessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(viewModel: MainViewModel, message: MessageData) {
            binding.viewmodel = viewModel
            binding.message = message
            binding.executePendingBindings()
        }
    }

    private val differCallBack = object : DiffUtil.ItemCallback<MessageData>() {
        override fun areItemsTheSame(oldItem: MessageData, newItem: MessageData): Boolean {
            return oldItem.fromId == newItem.fromId
        }

        override fun areContentsTheSame(oldItem: MessageData, newItem: MessageData): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MessageDataAdapterViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MessageDataAdapterViewHolder(
            DataBindingUtil.inflate(layoutInflater, R.layout.message_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: MessageDataAdapterViewHolder, position: Int) {
        val messageData = differ.currentList[position]
        Logger.log(TAG, "At position $position : $messageData")
        holder.bind(mainViewModel, messageData)
    }

    private var onItemClickListener: ((MessageData) -> Unit)? = null

    fun setOnItemClickListener(listener: (MessageData) -> Unit) {
        onItemClickListener = listener
    }
}