package com.example.MadCampProj1_ver2

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.FrameLayout
import kotlin.math.abs

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
                if (e1 != null && e2 != null) {
                    val deltaX = e2.x - e1.x
                    val deltaY = e2.y - e1.y

                    // 수평 슬라이드 감지
                    if (abs(deltaX) > abs(deltaY) && abs(deltaX) > touchSlop) {
                        isSliding = true
                        if (deltaX > 0) {
                            performSlideRight() // 오른쪽에서 왼쪽으로
                        } else {
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
        (context as? SlideHandler)?.onSlideLeft()
    }

    private fun performSlideRight() {
        // 오른쪽으로 슬라이드 시 동작 처리
        (context as? SlideHandler)?.onSlideRight()
    }


}
