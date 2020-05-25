package com.example.android.creativeim.utils

import androidx.lifecycle.Observer

open class EventProgress<out T>(private val content: T) {

    var hasBeenHandled = false

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

}

class EventObserver<T>(private val onEventUnhandledContent: (T) -> Unit) :
    Observer<EventProgress<T>> {
    override fun onChanged(event: EventProgress<T>?) {
        event?.getContentIfNotHandled()?.let {
            onEventUnhandledContent(it)
        }
    }
}