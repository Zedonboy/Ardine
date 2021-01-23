package com.declantech_softwares.ardine.types

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import kotlin.Exception

// DB means Data binding
class CustomDBViewHolder : RecyclerView.ViewHolder {
    private lateinit var _binder : ViewDataBinding
    constructor(_binder : ViewDataBinding) : super(_binder.root){
        this._binder = _binder
    }
    constructor(view: View) : super(view)

    fun<T> getBinder() : T {
        return _binder as T
    }
   companion object {
       fun from(view: View) : CustomDBViewHolder {
           val b = DataBindingUtil.bind<ViewDataBinding>(view) ?: throw Exception("View not data bindable")
           return CustomDBViewHolder(b)
       }
   }
}