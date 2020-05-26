package com.example.android.creativeim.utils

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.creativeim.data.User
import com.example.android.creativeim.messagedata.MessageData
import com.example.android.creativeim.ui.MessagesAdapter
import com.example.android.creativeim.ui.UserAdpater

@BindingAdapter("items")
fun setItems(listView: RecyclerView, items: List<MessageData>?) {
    items?.let {
        val sortedItems = it.sortedWith(compareBy { data -> data.timeStamp })
        (listView.adapter as MessagesAdapter).differ.submitList(sortedItems)
        (listView.adapter as MessagesAdapter).notifyDataSetChanged()
    }
}

@BindingAdapter("items")
fun setUserItems(listView: RecyclerView, items: List<User>?) {
    items?.let {
//        val sortedItems = it.sortedWith(compareBy {  data -> data.timeStamp  })
        (listView.adapter as UserAdpater).differ.submitList(items)
        (listView.adapter as UserAdpater).notifyDataSetChanged()
    }
}