package com.example.MadCampProj1_ver2.customview

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ScrollView


import android.util.Log

class CustomScrollView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ScrollView(context, attrs) {

    var gestureDetector: GestureDetector? = null

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        Log.d("SwipeEvent", "onTouchEvent called")  // 이 로그로 이 메서드가 호출되는지 확인
        gestureDetector?.onTouchEvent(ev)
        return super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        Log.d("SwipeEvent", "onInterceptTouchEvent called")  // 이 로그로 이 메서드가 호출되는지 확인
        gestureDetector?.onTouchEvent(ev)
        return super.onInterceptTouchEvent(ev)
    }
}
