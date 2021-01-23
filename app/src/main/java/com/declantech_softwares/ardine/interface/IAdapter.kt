package com.declantech_softwares.ardine.`interface`

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface IAdapter{
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    fun getItemCount(): Int

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)

    fun getItemViewType(position: Int): Int {
        return 0
    }

    fun getItemId(position: Int): Long{
        return 0
    }
}