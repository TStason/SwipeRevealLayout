package com.example.customviewgroup

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class Adapter(val listItem: MutableList<String>) : RecyclerView.Adapter<ViewHolder>(){

    //helper
    private val helper = ViewBindHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("ADAPTER", " create view holder")
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.view_holder, parent, false))
    }

    override fun getItemCount(): Int = listItem.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("ADAPTER", " restoreState view holder ${holder.itemView}")
        Log.d("ViewBindHelper", "position onBindViewHolder: $position")
        holder.bind(listItem[position], position)
        helper.addStateIfNotExistAt(position)
        helper.restoreState(position, holder.getLayout())
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        Log.d("ADAPTER", " detach view holder ${holder.itemView}")
        Log.d("ViewBindHelper", "onDetached layoutPosition: ${holder.layoutPosition}")
        helper.updateState(holder.layoutPosition, holder.getLayout())
        super.onViewDetachedFromWindow(holder)
    }
}