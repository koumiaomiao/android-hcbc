package com.kmj.hcbc.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class ActionLiveData<T> : MutableLiveData<T>() {
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer { data ->
            if (data == null) {
                return@Observer
            }
            observer.onChanged(data)
            value = null
        })
    }

    fun sendAction(data: T) {
        postValue(data)
    }
}
