package com.example.customviewgroup

import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ViewHolder(view: View): RecyclerView.ViewHolder(view), SwipeRevealLayout.ISwipeRevealLayout {
    //var text: TextView = view.findViewById(R.id.textView)
    var text: TextView = TextView(itemView.context)

    fun bind(str: String){
        text.text = str
        this.getLayout().setOnClickListenerOnSideItem(0,SwipeRevealLayout.Orientation.LEFT){
            Toast.makeText(itemView.context,"Call from first left item ${this.text.text}", Toast.LENGTH_SHORT).show()
        }
        this.getLayout().setOnClickListenerOnSideItem(0,SwipeRevealLayout.Orientation.RIGHT){
            Toast.makeText(itemView.context,"Call from first right item ${this.text.text}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getLayout(): SwipeRevealLayout = itemView.findViewById(R.id.customLayout)
}