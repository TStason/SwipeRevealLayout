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
    //private lateinit var itemTouch: ItemTouchHelper
    private val listOfString = mutableListOf(
        "test1",
        //"test2",
        //"test3",
        //"test4",
        //"test5",
        //"test6",
        //"test7",
        "test8"
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
        //handler
        recycler.addOnItemTouchListener(ViewBindHelper.getOnItemTouchListener())
        recycler.layoutManager = LinearLayoutManager(this.applicationContext)
        recycler.adapter = adapter
        //recycler.requestDisallowInterceptTouchEvent(true)
        /*itemTouch = ItemTouchHelper(ItemTouchHelperCallback(this.applicationContext, adapter))
        itemTouch.attachToRecyclerView(recycler)*/
        val test = findViewById<SwipeRevealLayout>(R.id.customGV)
        Log.d(TAG, "$test")
        test.setOnClickListenerOnSideItem(0, SwipeRevealLayout.Orientation.LEFT){
            Toast.makeText(this,"Call from first left item", Toast.LENGTH_SHORT).show()
        }
        test.setOnClickListenerOnSideItem(1, SwipeRevealLayout.Orientation.RIGHT){
            Toast.makeText(this,"Call from second right item", Toast.LENGTH_SHORT).show()
        }/*
        test.setOnClickListenerOnSideItem(2, SwipeRevealLayout.Orientation.RIGHT){
            Toast.makeText(this,"Call from third right item", Toast.LENGTH_SHORT).show()
        }*/
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }
}
/*
interface ItemTouchHelperAdapter{
    fun onItemMove(fromPosition: Int, toPosition: Int)

    fun onItemDismiss(position: Int)

    fun onItemEdit(position: Int)
}

class ItemTouchHelperCallback(context: Context, private var mAdapter: ItemTouchHelperAdapter): ItemTouchHelper.Callback(){

    private var isLeftSwipe = false
    private val bgDelete = ColorDrawable()
    private val bgDeleteColor = Color.parseColor("#f44336")
    private val iconDelete = context.getDrawable(R.drawable.ic_delete_forever_white_24dp)
    private val iconDeleteHeight = iconDelete.intrinsicHeight
    private val iconDeleteWidth = iconDelete.intrinsicWidth
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        mAdapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }
    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (isLeftSwipe)
            mAdapter.onItemDismiss(viewHolder.adapterPosition)
        else
            mAdapter.onItemEdit(viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        isLeftSwipe = dX < 0
        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG)
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        else if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            /*if (isLeftSwipe && (dX != 0f)) {
                val itemView = viewHolder.itemView
                val heightView = itemView.height
                val deleteRight = itemView.right
                var deleteLeft = deleteRight + (dX).toInt()

                if (dX.absoluteValue > itemView.width * 0.2){
                    deleteLeft = (itemView.width * 0.8).toInt()
                }

                bgDelete.color = bgDeleteColor
                bgDelete.setBounds(
                    deleteLeft,
                    itemView.top,
                    deleteRight,
                    itemView.bottom
                )
                //bgDelete.draw(c)

                // Calculate position of delete icon
                val iconTop = itemView.top + (heightView - iconDeleteHeight) / 2
                val iconMargin = (heightView - iconDeleteHeight) / 2
                val iconLeft = itemView.right - iconMargin - iconDeleteWidth
                val iconRight = itemView.right - iconMargin
                val iconBottom = iconTop + iconDeleteHeight
                iconDelete.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                //iconDelete.draw(c)
            }*/
            /*else if (dX != 0f){
                val itemView = viewHolder.itemView
                val heightView = itemView.height
                val deleteRight = itemView.right
                var deleteLeft = deleteRight + (dX).toInt()
/*
                if (dX.absoluteValue > itemView.width * 0.2){
                    deleteLeft = (itemView.width * 0.8).toInt()
                    newDx = -(itemView.width * 0.2).toFloat()
                    isFixedDx = true
                }
*/
                bgDelete.color = bgDeleteColor
                bgDelete.setBounds(
                    deleteLeft,
                    itemView.top,
                    deleteRight,
                    itemView.bottom
                )

                // Calculate position of delete icon
                val iconTop = itemView.top + (heightView - iconDeleteHeight) / 2
                val iconMargin = (heightView - iconDeleteHeight) / 2
                val iconLeft = itemView.right - iconMargin - iconDeleteWidth
                val iconRight = itemView.right - iconMargin
                val iconBottom = iconTop + iconDeleteHeight
                iconDelete.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            }*/
            //bgDelete.draw(c)
            //iconDelete.draw(c)
            Log.e("qdwqdqwdwqdqd", "dX: $dX")
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }

}
*/