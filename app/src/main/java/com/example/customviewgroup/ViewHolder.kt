package com.example.customviewgroup

import android.graphics.Color
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class ViewHolder(view: View): RecyclerView.ViewHolder(view), SwipeRevealLayout.ISwipeRevealLayout {
    private lateinit var swipeRevealLayout: SwipeRevealLayout
    var text: TextView = TextView(itemView.context)
    var intProp = 0

    fun bind(str: String, id: Int){
        swipeRevealLayout = getLayout()
        intProp = id
        text.text = str
        this.getLayout().setOnClickListenerOnSideView(1,SwipeRevealLayout.Orientation.LEFT){
            Toast.makeText(itemView.context,"Recommended", Toast.LENGTH_SHORT).show()
        }
        this.getLayout().setOnClickListenerOnSideView(0,SwipeRevealLayout.Orientation.LEFT){
            Toast.makeText(itemView.context,"Not recommended", Toast.LENGTH_SHORT).show()
        }
        swipeRevealLayout.setImageOnSideView(0,SwipeRevealLayout.Orientation.LEFT, R.drawable.ic_thumb_down_white_24dp)
        swipeRevealLayout.setTextOnSideView(0,SwipeRevealLayout.Orientation.LEFT,"Dis")
        swipeRevealLayout.setColorOnSideView(0, SwipeRevealLayout.Orientation.LEFT, Color.parseColor("#000000"))
        swipeRevealLayout.setImageOnSideView(1,SwipeRevealLayout.Orientation.LEFT, R.drawable.ic_thumb_up_white_24dp)
        swipeRevealLayout.setTextOnSideView(1,SwipeRevealLayout.Orientation.LEFT,"Rec")
        swipeRevealLayout.setColorOnSideView(1, SwipeRevealLayout.Orientation.LEFT, Color.parseColor("#aa9Faa"))
        swipeRevealLayout.setImageOnSideView(1,SwipeRevealLayout.Orientation.RIGHT, R.drawable.ic_delete_forever_white_24dp)
        swipeRevealLayout.setTextOnSideView(1,SwipeRevealLayout.Orientation.RIGHT,"DELETE")
        swipeRevealLayout.setColorOnSideView(1, SwipeRevealLayout.Orientation.RIGHT, Color.parseColor("#000000"))
    }

    override fun getLayout(): SwipeRevealLayout = itemView.findViewById(R.id.customLayout)
}