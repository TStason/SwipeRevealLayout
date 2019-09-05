package com.example.customviewgroup

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlin.math.absoluteValue
import kotlin.math.max

class SwipeRevealLayout(context: Context, attrs: AttributeSet): ViewGroup(context, attrs) {
    private val TAG = "SwipeRevealLayout"
    private val colors = listOf(
        "#009933",
        "#CCFF33",
        "#3399FF",
        "#3300FF",
        "#3399FF"
    )
    //add as param
    private var numberOfLeftViews = 1
    private var numberOfRightViews = 2
    //
    private val childViews = arrayListOf<View>()
    private val leftViews = arrayListOf<TextView>()
    private val rightViews = arrayListOf<TextView>()
    private var mainBlockCoefficient = 1f
    private var leftBlockCoefficient = 0f
    private var rightBlockCoefficient = 0f
    private val coefficientNeededToExpand = 0.2f
    //add as param
    private var maxViewWidth = 100
    private var maxViewHeight = 100
    //
    private var isInitialized = false
    private lateinit var startSwipePoint: Point
    private var isActiveSwipe = false

    init{
        initializeComponents()
    }

    private fun initializeComponents(){
        //set left views
        for (i in 0 until numberOfLeftViews){
            val textView = TextView(context).apply{
                text = "test$i"
                id = "test$i".hashCode()
                setTextColor(Color.BLACK)
                textSize = 15f
                minHeight = maxViewHeight
                maxWidth = maxViewWidth
                setBackgroundColor(Color.parseColor(colors[i % colors.size]))
            }
            leftViews.add(textView)
        }
        //set right views
        for (i in 0 until numberOfRightViews){
            val textView = TextView(context).apply{
                text = "TEST$i"
                id = "TEST$i".hashCode()
                textSize = 15f
                setTextColor(Color.BLACK)
                minHeight = maxViewHeight
                maxWidth = maxViewWidth
                setBackgroundColor(Color.parseColor(colors[i % colors.size]))
            }
            rightViews.add(textView)
        }
    }

    fun restoreState(state: State){
        mainBlockCoefficient = state.mainBlockCoefficient
        leftBlockCoefficient = state.leftBlockCoefficient
        rightBlockCoefficient = state.rightBlockCoefficient
    }

    fun getState(): State = State(mainBlockCoefficient, leftBlockCoefficient, rightBlockCoefficient)

    fun isActiveSwipe() = isActiveSwipe

    fun setOnClickListenerOnSideItem(index: Int, orientation: Orientation, action: (View)-> Unit){
        when(orientation){
            Orientation.LEFT -> {
                if (index > numberOfLeftViews - 1)
                    throw ArrayIndexOutOfBoundsException("Size $numberOfLeftViews, you try $index")
                leftViews[index].setOnClickListener(action)
            }
            Orientation.RIGHT -> {
                if (index > numberOfRightViews - 1)
                    throw ArrayIndexOutOfBoundsException("Size $numberOfRightViews, you try $index")
                rightViews[index].setOnClickListener(action)
            }
            else -> {Log.e(TAG, "This orientation not allowed at this time")}
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val width: Int
        var height: Int
        if (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST){
            width = MeasureSpec.getSize(widthMeasureSpec)
        }
        else {
            width = 500
        }
        if (heightMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.AT_MOST){
            height = MeasureSpec.getSize(heightMeasureSpec)
        }
        else {
            height = 400
        }
        if (!isInitialized){
            maxViewWidth = ((width * 0.6) / max(numberOfRightViews, numberOfLeftViews)).toInt()
            maxViewHeight = max(maxViewWidth, maxViewHeight)
            updateComponents()
            isInitialized = true
        }
        Log.e(TAG, "onMeasure ${childCount - numberOfLeftViews - numberOfRightViews}")
        for(i in 0 until childCount - numberOfLeftViews - numberOfRightViews){
            measureChild(childViews[i],widthMeasureSpec, heightMeasureSpec)
            height = childViews[i].measuredHeight
            Log.e(TAG, "measure child $i height -> $height")
        }
        //measure left views
        leftViews.forEach{
            measureChild(it, widthMeasureSpec, heightMeasureSpec)
        }
        //measure right views
        rightViews.forEach{
            measureChild(it, widthMeasureSpec, heightMeasureSpec)
        }
        
        setMeasuredDimension(width, height)
    }

    private fun updateComponents(){
        //set outside views
        Log.d(TAG, "childs $childCount")
        for (i in 0 until childCount)
            childViews.add(getChildAt(i))
        //add left views
        for (i in 0 until numberOfLeftViews){
            leftViews[i].apply{
                minHeight = maxViewHeight
                maxWidth = maxViewWidth
            }
            addView(leftViews[i])
        }
        //add right views
        for (i in 0 until numberOfRightViews){
            rightViews[i].apply{
                minHeight = maxViewHeight
                maxWidth = maxViewWidth
            }
            addView(rightViews[i])
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val widthVG = r - l
        Log.e(TAG, "onLayput: $changed , $l left, $t top, $r right, $b bottom")
        if ((childCount - numberOfLeftViews - numberOfRightViews) != 1)
            throw IllegalArgumentException("Work with only one child! You set ${childCount - numberOfLeftViews - numberOfRightViews}")
        childViews.forEach {
            it.layout(
                (widthVG * leftBlockCoefficient - widthVG * rightBlockCoefficient).toInt(),
                paddingTop,
                (widthVG - widthVG * rightBlockCoefficient).toInt(),
                paddingTop+it.measuredHeight
            )
        }
        var rightPrevViewLeftCorner = (widthVG - widthVG * rightBlockCoefficient).toInt()
        rightViews.forEach {
            it.layout(
                rightPrevViewLeftCorner,
                paddingTop,
                (rightPrevViewLeftCorner + widthVG * rightBlockCoefficient / numberOfRightViews).toInt(),
                paddingTop+it.measuredHeight
            )
            rightPrevViewLeftCorner = (rightPrevViewLeftCorner + widthVG * rightBlockCoefficient / numberOfRightViews).toInt()
        }
        var leftPrevViewRightCorner = l
        leftViews.forEach {
            it.layout(
                leftPrevViewRightCorner,
                paddingTop,
                leftPrevViewRightCorner + (widthVG * leftBlockCoefficient / numberOfLeftViews).toInt(),
                paddingTop+it.measuredHeight
            )
            leftPrevViewRightCorner = l + (widthVG * leftBlockCoefficient / numberOfLeftViews).toInt()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let{
            if (event.action == MotionEvent.ACTION_DOWN){
                Log.e(TAG, "SwipeRevealLayout TouchEvent ACTION_DOWN")
            }
            else if (event.action == MotionEvent.ACTION_MOVE){
                Log.e(TAG, "SwipeRevealLayout TouchEvent ACTION_MOVE")
                calculateView(Point(event.rawX, event.rawY))
                isActiveSwipe = true
            } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL){
                Log.e(TAG, "SwipeRevealLayout TouchEvent END")
                if (isActiveSwipe){
                    stopSwiping()
                    isActiveSwipe = false
                }
            }
            Log.e(TAG, "ActiveSwipe: $isActiveSwipe")
        }
        return true
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        event?.let{
            if (event.action == MotionEvent.ACTION_DOWN){
                Log.e(TAG, "SwipeRevealLayout Intercept DOWN")
                setStartSwipePoint(Point(event.rawX, event.rawY))
            } else if (event.action == MotionEvent.ACTION_MOVE){
                Log.e(TAG, "SwipeRevealLayout Intercept MOVE")
                val deltaY = (startSwipePoint.y - event.rawY).absoluteValue
                val deltaX = (startSwipePoint.x - event.rawX).absoluteValue
                return deltaY < deltaX
            } else if (event.action == MotionEvent.ACTION_UP){
                Log.e(TAG, "SwipeRevealLayout Intercept UP")
            }
        }
        return super.onInterceptTouchEvent(event)
    }

    private fun setStartSwipePoint(point: Point){
        startSwipePoint = point
    }

    private fun calculateView(pointTo: Point){
        val layoutWidth = this.width
        val delta = startSwipePoint.x - pointTo.x
        val shiftPercent = delta / layoutWidth
        setStartSwipePoint(pointTo)
        when(getSwipeHorizontalOrientation(delta)){
            Orientation.LEFT -> {
                if (leftBlockCoefficient > 0f){
                    leftBlockCoefficient -= shiftPercent
                } else {
                    rightBlockCoefficient += shiftPercent
                }
            }
            Orientation.RIGHT -> {
                if (rightBlockCoefficient > 0f){
                    rightBlockCoefficient += shiftPercent
                } else{
                    leftBlockCoefficient -= shiftPercent
                }
            }
            else -> {Log.e(TAG, "Unknown orientation for this case: X_X")}
        }
        mainBlockCoefficient -= shiftPercent.absoluteValue
        this.requestLayout()
        this.invalidate()
    }

    private fun stopSwiping(){
        val layoutWidth = this.width
        val q = maxViewWidth.toFloat() / layoutWidth
        if(rightBlockCoefficient >= coefficientNeededToExpand){
            rightBlockCoefficient = q*numberOfRightViews
            leftBlockCoefficient = 0f
        } else if (leftBlockCoefficient >= coefficientNeededToExpand){
            leftBlockCoefficient = q*numberOfLeftViews
            rightBlockCoefficient = 0f
        } else {
            leftBlockCoefficient = 0f
            rightBlockCoefficient = 0f
        }
        //Log.d(TAG, "rightBlockCoefficient -> $rightBlockCoefficient; leftBlockCoefficient -> $leftBlockCoefficient")
        this.requestLayout()
        this.invalidate()
    }

    private fun getSwipeVerticalOrientation(delta: Float): Orientation =
        if (delta >= 0) Orientation.TOP else Orientation.BOTTOM
    private fun getSwipeHorizontalOrientation(delta: Float): Orientation =
        if (delta >= 0) Orientation.LEFT else Orientation.RIGHT
    enum class Orientation{LEFT, RIGHT, TOP, BOTTOM}
    private data class Point(val x: Float, val y: Float)
    data class State(var mainBlockCoefficient: Float, var leftBlockCoefficient: Float, var rightBlockCoefficient: Float)
    interface ISwipeRevealLayout{
        fun getLayout(): SwipeRevealLayout
    }
}