package com.example.MadCampProj1_ver2

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.FrameLayout
import kotlin.math.abs


import android.util.Log

class CustomContainerLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val gestureDetector: GestureDetector
    private val touchSlop: Int
    private var isSliding = false

    interface SlideHandler {
        fun onSlideLeft()
        fun onSlideRight()
    }

    private var slideHandler: SlideHandler? = null

    fun setSlideHandler(handler: SlideHandler?) {
        this.slideHandler = handler
    }

    init {
        val viewConfig = ViewConfiguration.get(context)
        touchSlop = viewConfig.scaledTouchSlop

        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onScroll(
                e1: MotionEvent?,
                e2: MotionEvent,
                distanceX: Float,
                distanceY: Float
            ): Boolean {
                Log.d("SwipeEvent", "onScroll called")  // 추가된 로그
                if (e1 != null && e2 != null) {
                    val deltaX = e2.x - e1.x
                    val deltaY = e2.y - e1.y
                    Log.d("SwipeEvent", "deltaX: $deltaX, deltaY: $deltaY")  // deltaX, deltaY 값 로그

                    // 수평 슬라이드 감지
                    if (abs(deltaX) > abs(deltaY) && abs(deltaX) > touchSlop) {
                        isSliding = true
                        if (deltaX > 0) {
                            Log.d("SwipeEvent", "Swiped Right")
                            performSlideRight() // 오른쪽에서 왼쪽으로
                        } else {
                            Log.d("SwipeEvent", "Swiped Left")
                            performSlideLeft() // 왼쪽에서 오른쪽으로
                        }
                        return true
                    }
                }
                return false
            }
        })
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        // GestureDetector가 슬라이드 이벤트를 처리
        if (gestureDetector.onTouchEvent(ev)) {
            return true
        }
        // 수직 스크롤 동작은 RecyclerView 등 하위 뷰로 전달
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // GestureDetector로 슬라이드 이벤트를 계속 처리
        if (gestureDetector.onTouchEvent(event)) {
            return true
        }
        return super.onTouchEvent(event)
    }

    private fun performSlideLeft() {
        // 왼쪽으로 슬라이드 시 동작 처리
        Log.d("SwipeEvent", "performSlideLeft called")  // 슬라이드 왼쪽 동작 로그
        (context as? SlideHandler)?.onSlideLeft()
    }

    private fun performSlideRight() {
        // 오른쪽으로 슬라이드 시 동작 처리
        Log.d("SwipeEvent", "performSlideRight called")  // 슬라이드 오른쪽 동작 로그
        (context as? SlideHandler)?.onSlideRight()
    }


}
