package com.declantech_softwares.ardine.types

interface AppCallback<T> {
    fun onSuccess(data : T)
    fun onFail(e : Error)
}