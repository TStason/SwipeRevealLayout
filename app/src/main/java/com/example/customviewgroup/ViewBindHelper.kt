package com.example.customviewgroup

import android.util.Log
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class ViewBindHelper {
    private val TAG = "ViewBindHelper"
    private val defaultState = SwipeRevealLayout.State(1f, 0f, 0f)
    private val viewsStates = arrayListOf<SwipeRevealLayout.State>()
    fun addCustomVIew(key: Int, cv: SwipeRevealLayout){
        Log.e(TAG, "try to add observer object at $key")
        if (viewsStates.elementAtOrNull(key) == null){
            Log.d(TAG, "Object observer ${viewsStates.size}")
            viewsStates.add(defaultState)
        }
    }

    fun updateState(position: Int, cv: SwipeRevealLayout){
        if (viewsStates.size > position && position >= 0){
            viewsStates[position] = cv.getState()
        }
    }
    //probably crash
    private fun getView(position: Int) = viewsStates[position]

    fun bind(position: Int, cv: SwipeRevealLayout){
        cv.restoreState(getView(position))
    }


    companion object{

        fun getOnItemTouchListener() = object: RecyclerView.OnItemTouchListener{
            private val TAG = "ViewBindHelper"
            private var viewOnStartSwipe: SwipeRevealLayout? = null

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                if (e.action == MotionEvent.ACTION_DOWN){
                    Log.d(TAG, "recycler onTouch ACTION_DOWN")
                } else if (e.action == MotionEvent.ACTION_MOVE){
                    Log.d(TAG, "recycler onTouch ACTION_MOVE")
                } else if (e.action == MotionEvent.ACTION_UP){
                    Log.d(TAG, "recycler onTouch ACTION_UP")
                } else if (e.action == MotionEvent.ACTION_CANCEL){
                    Log.d(TAG, "recycler onTouch ACTION_CANCEL")
                }
            }

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                if (e.action == MotionEvent.ACTION_DOWN){
                    Log.d(TAG, "recycler intercept ACTION_DOWN")
                    viewOnStartSwipe = rv.findChildViewUnder(e.x, e.y) as? SwipeRevealLayout
                    return false
                } else if (e.action == MotionEvent.ACTION_MOVE){
                    Log.d(TAG, "recycler intercept ACTION_MOVE ")
                    viewOnStartSwipe?.let {
                        Log.d(TAG, "isSwiped: ${it.isActiveSwipe()}")
                        if (it.isActiveSwipe())
                            rv.requestDisallowInterceptTouchEvent(false)
                        else
                            rv.requestDisallowInterceptTouchEvent(true)
                    }
                    return false
                } else if (e.action == MotionEvent.ACTION_UP){
                    Log.d(TAG, "recycler intercept ACTION_UP")
                    return false
                }
                Log.e(TAG, "${e.action}")
                return true
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                Log.d(TAG, "recycler disallowIntercept $disallowIntercept")
            }

        }
    }
}
