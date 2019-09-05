package com.example.customviewgroup

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class Adapter(val listItem: MutableList<String>) : RecyclerView.Adapter<ViewHolder>(){//, ItemTouchHelperAdapter{

    //helper
    private val helper = ViewBindHelper()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.view_holder, parent, false))
    }

    override fun getItemCount(): Int = listItem.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("ViewBindHelper", "position onBindViewHolder: $position")
        holder.bind(listItem[position])
        //helper
        helper.addCustomVIew(position, holder.getLayout())
        helper.bind(position, holder.getLayout())
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        Log.d("ViewBindHelper", "onDetached layoutPosition: ${holder.layoutPosition}")
        helper.updateState(holder.layoutPosition, holder.getLayout())
        super.onViewDetachedFromWindow(holder)
    }


    //LEGACY
    /*
    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        var fromPos = fromPosition
        var toPos = toPosition
        if (fromPos > toPos){
            fromPos = toPos.also {
                toPos = fromPos
            }
        }
        for (i in fromPos until toPos){
            Collections.swap(listItem, i, i+1)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        listItem.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onItemEdit(position: Int) {
        notifyItemChanged(position)
    }
    */
}