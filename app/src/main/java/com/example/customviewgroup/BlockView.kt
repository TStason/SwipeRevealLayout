package com.example.customviewgroup

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlin.math.max
import kotlin.math.min

class BlockView: ViewGroup, SwipeRevealLayout.IBlockView {
    private val TAG = "BlockView"
    private val imageView = ImageView(context)
    private val textView = TextView(context)
    //private val defaultWidth = 100
    //private val defaultHeight = 100
    private val margin = 5
    private var isNeededToAddChilds = true
    init{
        imageView.apply {
        }
        textView.apply{
            text = ""
            textSize = 10f
            textAlignment = View.TEXT_ALIGNMENT_CENTER
        }
    }
    constructor(context: Context, attrs: AttributeSet): super(context, attrs)
    constructor(context: Context): super(context)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.d(TAG, "onMeasure")
        var blockViewWidth: Int = 0
        var blockViewHeight: Int = 0

        if (isNeededToAddChilds){
            addView(imageView)
            addView(textView)
            isNeededToAddChilds = false
        }

        measureChildren(widthMeasureSpec, heightMeasureSpec)
        //MeasureSpec state processing
        blockViewWidth = recognizeSize(widthMeasureSpec, measureWidth())
        blockViewHeight = recognizeSize(heightMeasureSpec, measureHeight())
        setMeasuredDimension(blockViewWidth, blockViewHeight)
    }

    private fun recognizeSize(measureSpec: Int, viewMeasure: Int): Int {
        when (MeasureSpec.getMode(measureSpec)){
            MeasureSpec.EXACTLY -> {
                Log.d(TAG, "onMeasure EXACTLY")
                return MeasureSpec.getSize(measureSpec)
            }
            MeasureSpec.AT_MOST ->{
                Log.d(TAG, "onMeasure AT_MOST")
                return min(viewMeasure, MeasureSpec.getSize(measureSpec))
            }
            MeasureSpec.UNSPECIFIED -> {
                Log.d(TAG, "onMeasure UNSPEC")
                return viewMeasure
            }
        }
        return 0
    }

    private fun measureWidth(): Int = max(imageView.measuredWidth, textView.measuredWidth) + margin * 2
    private fun measureHeight(): Int = imageView.measuredHeight + textView.measuredHeight + margin * 3

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val width = r - l
        val height = b - t
        imageView.layout(
            margin,
            margin,
            width - margin,
            imageView.measuredHeight + margin
        )
        textView.layout(
            margin,
            imageView.measuredHeight + margin,
            width - margin,
            height - margin
        )
    }

    override fun setImage(resId: Int) {
        this.imageView.setImageResource(resId)
    }

    override fun setText(text: String) {
        textView.text = text
        requestLayout()
        invalidate()
    }

    override fun setBackColor(color: Int){
        this.setBackgroundColor(color)
        invalidate()
    }

    override fun setTextColor(color: Int){
        this.textView.setTextColor(color)
        invalidate()
    }

    override fun setOnClickListener(action: (View)-> Unit) {
        //not good
        imageView.setOnClickListener(action)
        textView.setOnClickListener(action)
    }
}