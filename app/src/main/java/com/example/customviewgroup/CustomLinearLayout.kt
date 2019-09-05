package com.example.customviewgroup

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class CustomLinearLayout(context: Context): LinearLayoutManager(context) {

    private var isScrollEnabled = true

    override fun canScrollVertically(): Boolean = isScrollEnabled && this.canScrollVertically()

    fun test(flag: Boolean){
        this.isScrollEnabled = flag
    }


}