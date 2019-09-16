package com.example.customviewgroup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val TAG = "qdwqdqwdwqdqd"
    private lateinit var text: TextView
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: Adapter
    private val listOfString = mutableListOf(
//        "test1",
//        "test2",
//        "test3",
//        "test4",
//        "test5",
//        "test6",
//        "test7",
//        "test8",
//        "test9",
//        "test10",
//        "test11",
//        "test12",
//        "test13",
        "test14"
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text = findViewById(R.id.textView)
        text.setOnClickListener {
            listOfString.add("TEST_TE1T")
            adapter.notifyDataSetChanged()
            Log.e(TAG, "onclick")
        }

        adapter = Adapter(listOfString)
        recycler = findViewById(R.id.recycler)
        //helper
        recycler.addOnItemTouchListener(ViewBindHelper.getOnItemTouchListener())
        recycler.layoutManager = LinearLayoutManager(this.applicationContext)
        recycler.adapter = adapter
        val test = findViewById<SwipeRevealLayout>(R.id.customGV)
        Log.d(TAG, "$test")
        test.setOnClickListenerOnSideView(0, SwipeRevealLayout.Orientation.LEFT){
            Toast.makeText(this,"Call from first left item", Toast.LENGTH_SHORT).show()
        }
        test.setImageOnSideView(0,SwipeRevealLayout.Orientation.LEFT, R.drawable.ic_thumb_down_white_24dp)
        /*test.setOnClickListenerOnSideView(1, SwipeRevealLayout.Orientation.RIGHT){
            Toast.makeText(this,"Call from second right item", Toast.LENGTH_SHORT).show()
        }*//*
        test.setOnClickListenerOnSideView(2, SwipeRevealLayout.Orientation.RIGHT){
            Toast.makeText(this,"Call from third right item", Toast.LENGTH_SHORT).show()
        }*/
    }
}