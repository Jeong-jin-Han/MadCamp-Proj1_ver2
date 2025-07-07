package com.example.MadCampProj1_ver2.mission

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.GestureDetector
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.MadCampProj1_ver2.R
import com.example.MadCampProj1_ver2.sampledata.MissionData
import com.example.MadCampProj1_ver2.sampledata.MissionDto
import kotlin.math.abs


class CompletedMissionFragment : Fragment() {

    private lateinit var gestureDetector: GestureDetector
    private val SWIPE_THRESHOLD = 100 // 스와이프 거리 임계값
    private val SWIPE_VELOCITY_THRESHOLD = 100 // 스와이프 속도 임계값


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_mission_inner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.mission_recycler_view)
        val missionList: List<MissionDto> = MissionData.getMissionDataList()

        gestureDetector = GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                if (e1 == null || e2 == null) return false

                val deltaX = e2.x - e1.x
                if (abs(deltaX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (deltaX > 0) {
                        switchToPreviousFragment() // 오른쪽 스와이프 -> 이전 사진
                    }
                    return true
                }
                return false
            }
        })


        recyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
            private var isScrolling = false

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                if (gestureDetector.onTouchEvent(e)) {
                    isScrolling = true
                    return true // 슬라이드 이벤트 가로채기
                }
                return false
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                if (isScrolling) {
                    gestureDetector.onTouchEvent(e)
                } else {
                    rv.onTouchEvent(e) // RecyclerView 자체 이벤트 처리
                }
            }

            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                // 필요 시 구현
            }
        })

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = MissionAdapter(missionList.filter { it.isDone }.toMutableList()) { id ->
            // onItemClick 이벤트 처리
            val fragment = MissionCompleteFragment().apply {
                arguments = Bundle().apply {
                    putInt("missionId", id)
                }
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame_ver2, fragment)
                .addToBackStack(null)
                .commit()
        }

        view.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            false
        }
    }

    private fun switchToPreviousFragment() {
        val previousFragment = InProgressMissionFragment()

        val progressButton: LinearLayout = requireActivity().findViewById(R.id.progress_btn)

        // 현재 위치 계산
        val parentWidth = (progressButton.parent as ViewGroup).width
        val currentGravity = (progressButton.layoutParams as FrameLayout.LayoutParams).gravity
        val inProgressText = requireActivity().findViewById<TextView>(R.id.btn_in_progress)
        val completedText = requireActivity(). findViewById<TextView>(R.id.btn_completed)
        // 목표 위치 계산
        val targetX = if (currentGravity == Gravity.END) {
            // END -> START로 이동
            -(parentWidth - progressButton.width).toFloat()
        } else {
            // START -> END로 이동
            0f
        }

        ObjectAnimator.ofFloat(progressButton, "translationX", targetX).apply {
            duration = 300 // 애니메이션 지속 시간 (밀리초)
            addUpdateListener {
                // 애니메이션 중간에 필요한 추가 동작 가능
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    // 애니메이션 시작 시 텍스트 색상 변
                    if (currentGravity == Gravity.END) {
                        inProgressText.setTextColor(ContextCompat.getColor(requireContext(), R.color.background2))
                        completedText.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_color))
                    } else {
                        inProgressText.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_color))
                        completedText.setTextColor(ContextCompat.getColor(requireContext(), R.color.background2))
                    }
                }

                override fun onAnimationEnd(animation: Animator) {
                    // 애니메이션이 끝난 후 layout_gravity 업데이트
                    (progressButton.layoutParams as FrameLayout.LayoutParams).gravity =
                        if (currentGravity == Gravity.END) Gravity.START else Gravity.END
                    progressButton.translationX = 0f
                    progressButton.requestLayout()
                }

                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            start()
        }
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
            .replace(R.id.inner_content_frame, previousFragment)
            .addToBackStack(null)
            .commit()
    }
}
