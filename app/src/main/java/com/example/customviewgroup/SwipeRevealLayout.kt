package com.example.customviewgroup

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import java.util.ArrayList
import kotlin.math.absoluteValue
import kotlin.math.min

class SwipeRevealLayout(context: Context, attrs: AttributeSet): ViewGroup(context, attrs) {
    private val TAG = "SwipeRevealLayout"
    //xml param
    private var numberOfLeftViews = 1
    private var numberOfRightViews = 1
    private var defaultBlockBackgroundColor = Color.parseColor("#000000")
    private var defaultTextColor = Color.parseColor("#FFFFFF")
    private var coefficientNeededToExpand = 0.2f
    private var maxOccupyingSpaceCoefficient = 0.6f
    private var blockWidth = 200
    private var blockHeight = 200
    //
    private var mainBlockView: View? = null
    private val leftBlockViews = arrayListOf<BlockView>()
    private val rightBlockViews = arrayListOf<BlockView>()
    private var mainBlockCoefficient = 1f
    private var leftBlockCoefficient = 0f
    private var rightBlockCoefficient = 0f
    //
    private var isNeededToAddChilds = true
    private lateinit var onSwipePrevPoint: Point
    private var isActiveSwipe = false

    init{
        context.theme.obtainStyledAttributes(attrs, R.styleable.SwipeRevealLayout, 0, 0).apply {
            try{
                numberOfLeftViews = getInteger(R.styleable.SwipeRevealLayout_numberOfLeftViews, numberOfLeftViews)
                numberOfRightViews = getInteger(R.styleable.SwipeRevealLayout_numberOfRightViews, numberOfRightViews)
                defaultBlockBackgroundColor = getInteger(R.styleable.SwipeRevealLayout_defaultBlockBackgroundColor, defaultBlockBackgroundColor)
                defaultTextColor = getInteger(R.styleable.SwipeRevealLayout_defaultTextColor, defaultTextColor)
                coefficientNeededToExpand = getFloat(R.styleable.SwipeRevealLayout_coefficientNeededToExpand, coefficientNeededToExpand)
                maxOccupyingSpaceCoefficient = getFloat(R.styleable.SwipeRevealLayout_maxOccupyingSpaceCoefficient, maxOccupyingSpaceCoefficient)
                blockWidth = getInteger(R.styleable.SwipeRevealLayout_blockWidth, blockWidth)
                blockHeight = getInteger(R.styleable.SwipeRevealLayout_blockHeight, blockHeight)
            } finally {
                recycle()
            }

        }
        initializeComponents()
    }

    private fun initializeComponents(){
        leftBlockViews.initializeViews(numberOfLeftViews)
        rightBlockViews.initializeViews(numberOfRightViews)
    }

    private fun  ArrayList<BlockView>.initializeViews(size: Int){
        for (i in 0 until size){
            this.add(getBlockView())
        }
    }

    private fun getBlockView(): BlockView = BlockView(context).apply {
        this.setBackColor(defaultBlockBackgroundColor)
        this.setTextColor(defaultTextColor)
    }

    fun restoreState(state: State){
        mainBlockCoefficient = state.mainBlockCoefficient
        leftBlockCoefficient = state.leftBlockCoefficient
        rightBlockCoefficient = state.rightBlockCoefficient
    }

    fun getState(): State = State(mainBlockCoefficient, leftBlockCoefficient, rightBlockCoefficient)

    fun isActiveSwipe() = isActiveSwipe

    fun setOnClickListenerOnSideView(index: Int, orientation: Orientation, action: (View)-> Unit){
        getSideViewAt(index, orientation)?.setOnClickListener(action) ?: Log.e(TAG, "some trouble")
    }

    fun setImageOnSideView(index: Int, orientation: Orientation, resource: Int){
        getSideViewAt(index, orientation)?.setImage(resource) ?: Log.e(TAG, "some trouble")
    }

    fun setTextOnSideView(index: Int, orientation: Orientation, text: String){
        getSideViewAt(index, orientation)?.setText(text) ?: Log.e(TAG, "some trouble")
    }

    fun setColorOnSideView(index: Int, orientation: Orientation, color: Int){
        getSideViewAt(index, orientation)?.setBackColor(color) ?: Log.e(TAG, "some trouble")
    }

    private fun getSideViewAt(index: Int, orientation: Orientation):IBlockView? =
        when(orientation){
            Orientation.LEFT -> {
                leftBlockViews.getAt(index)
            }
            Orientation.RIGHT -> {
                rightBlockViews.getAt(index)
            }
            else -> {
                Log.e(TAG, "This orientation not allowed at this time")
                null
            }
        }

    private fun <T> ArrayList<T>.getAt(index: Int): T? {
        if (index < 0 || index >= numberOfRightViews){
            //throw ArrayIndexOutOfBoundsException("Size $numberOfRightViews, you try $index")
            return null
        }
        return this[index]
    }

    override fun onAttachedToWindow() {
        Log.d(TAG, "onAttachedToWindow")
        if (isNeededToAddChilds){
            setMainBlockView()
            addSideChilds()
            isNeededToAddChilds = false
        }
        super.onAttachedToWindow()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var swipeRevealLayoutWidth: Int = 0
        var swipeRevealLayoutHeight: Int = 0
        //
//        if (isNeededToAddChilds){
//            setMainBlockView()
//            addSideChilds()
//            isNeededToAddChilds = false
//        }
        //measure childs
        //measureChildren(widthMeasureSpec, heightMeasureSpec)
        measureMainBlockView(widthMeasureSpec, heightMeasureSpec)
        measureChilds()
        //MeasureSpec state processing
        swipeRevealLayoutWidth = recognizeSize(widthMeasureSpec, mainBlockView!!.measuredWidth)
        swipeRevealLayoutHeight = recognizeSize(heightMeasureSpec, mainBlockView!!.measuredHeight)

        setMeasuredDimension(swipeRevealLayoutWidth, swipeRevealLayoutHeight)
    }

    private fun setMainBlockView(){
        //set user view
        mainBlockView = getChildAt(0)
    }

    private fun addSideChilds(){
        Log.d(TAG, "childs $childCount")
        //add left views
        for (i in 0 until numberOfLeftViews){
            addView(leftBlockViews[i])
        }
        //add right views
        for (i in 0 until numberOfRightViews){
            addView(rightBlockViews[i])
        }
    }

    private fun measureMainBlockView(widthMeasureSpec: Int, heightMeasureSpec: Int){
        measureChild(getChildAt(0), widthMeasureSpec, heightMeasureSpec)
    }

    private fun measureChilds(){
        val heightMeasureSpec = MeasureSpec.makeMeasureSpec(blockHeight, MeasureSpec.EXACTLY)
        val widthMeasureSpec = MeasureSpec.makeMeasureSpec(blockWidth, MeasureSpec.EXACTLY)
        for (i in 1 until childCount)
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec)
    }

    private fun recognizeSize(measureSpec: Int, measuredSize: Int): Int {
        when (MeasureSpec.getMode(measureSpec)){
            MeasureSpec.EXACTLY -> {
                Log.d(TAG, "onMeasure EXACTLY")
                return MeasureSpec.getSize(measureSpec)
            }
            MeasureSpec.AT_MOST ->{
                Log.d(TAG, "onMeasure AT_MOST")
                return min(measuredSize, MeasureSpec.getSize(measureSpec))
            }
            MeasureSpec.UNSPECIFIED -> {
                Log.d(TAG, "onMeasure UNSPECIFIED")
                return measuredSize
            }
        }
        return 0
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val widthVG = r - l
        if ((childCount - numberOfLeftViews - numberOfRightViews) != 1)
            throw IllegalArgumentException("Work with only one child! You try ${childCount - numberOfLeftViews - numberOfRightViews}")
        mainBlockView?.let {
            it.layout(
                paddingStart + (widthVG * (leftBlockCoefficient - rightBlockCoefficient)).toInt(),
                paddingTop,
                (widthVG - widthVG * rightBlockCoefficient).toInt() - paddingEnd,
                paddingTop+it.measuredHeight - paddingBottom
            )
        }
        var rightPrevViewLeftCorner = (widthVG - widthVG * rightBlockCoefficient).toInt() - paddingEnd
        rightBlockViews.forEach {
            it.layout(
                rightPrevViewLeftCorner,
                paddingTop,
                (rightPrevViewLeftCorner + widthVG * rightBlockCoefficient / numberOfRightViews).toInt(),
                paddingTop+it.measuredHeight - paddingBottom
            )
            rightPrevViewLeftCorner += (widthVG * rightBlockCoefficient / numberOfRightViews).toInt()
        }
        var leftPrevViewRightCorner = paddingStart
        leftBlockViews.forEach {
            it.layout(
                leftPrevViewRightCorner,
                paddingTop,
                leftPrevViewRightCorner + (widthVG * leftBlockCoefficient / numberOfLeftViews).toInt(),
                paddingTop+it.measuredHeight - paddingBottom
            )
            leftPrevViewRightCorner += (widthVG * leftBlockCoefficient / numberOfLeftViews).toInt()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let{
            if (event.action == MotionEvent.ACTION_DOWN){
                Log.e(TAG, "SwipeRevealLayout TouchEvent ACTION_DOWN")
            }
            else if (event.action == MotionEvent.ACTION_MOVE){
                Log.e(TAG, "SwipeRevealLayout TouchEvent ACTION_MOVE")
                calculateBlocksCoefficients(Point(event.rawX, event.rawY))
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
                setPrevPoint(Point(event.rawX, event.rawY))
            } else if (event.action == MotionEvent.ACTION_MOVE){
                Log.e(TAG, "SwipeRevealLayout Intercept MOVE")
                val deltaY = (onSwipePrevPoint.y - event.rawY).absoluteValue
                val deltaX = (onSwipePrevPoint.x - event.rawX).absoluteValue
                return deltaY < deltaX
            } else if (event.action == MotionEvent.ACTION_UP){
                Log.e(TAG, "SwipeRevealLayout Intercept UP")
            }
        }
        return super.onInterceptTouchEvent(event)
    }

    private fun setPrevPoint(point: Point){
        onSwipePrevPoint = point
    }

    private fun calculateBlocksCoefficients(currentPoint: Point){
        val layoutWidth = this.width
        val delta = onSwipePrevPoint.x - currentPoint.x
        val shiftPercent = delta / layoutWidth
        setPrevPoint(currentPoint)
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
        if(rightBlockCoefficient >= coefficientNeededToExpand){
            rightBlockCoefficient = calculateCoefficient(numberOfRightViews)
            leftBlockCoefficient = 0f
        } else if (leftBlockCoefficient >= coefficientNeededToExpand){
            leftBlockCoefficient = calculateCoefficient(numberOfLeftViews)
            rightBlockCoefficient = 0f
        } else {
            leftBlockCoefficient = 0f
            rightBlockCoefficient = 0f
        }
        this.requestLayout()
        this.invalidate()
    }

    private fun calculateCoefficient(size: Int): Float{
        val layoutWidth = this.width
        val tmp = size * blockWidth.toFloat() / layoutWidth
        return min(tmp, maxOccupyingSpaceCoefficient)
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
    interface IBlockView{
        fun setImage(resId: Int)
        fun setText(text: String)
        fun setBackColor(color: Int)
        fun setTextColor(color: Int)
        fun setOnClickListener(action: (View)-> Unit)
    }
}