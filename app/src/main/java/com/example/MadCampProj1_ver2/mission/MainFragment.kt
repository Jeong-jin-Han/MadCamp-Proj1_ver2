package com.example.MadCampProj1_ver2.mission

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.MadCampProj1_ver2.R
import com.example.MadCampProj1_ver2.mypage.MypageFragment
import com.example.MadCampProj1_ver2.notification.NotificationFragment
import com.example.MadCampProj1_ver2.sampledata.NotificationData

class MainFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_mission, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val topBarTextView = view.findViewById<TextView>(R.id.top_bar_text)
        topBarTextView.text = "도전 과제"

        val progressView: LinearLayout = view.findViewById(R.id.progress_btn)
        val layoutParams = progressView.layoutParams as FrameLayout.LayoutParams
        val inProgressText: TextView = view.findViewById(R.id.btn_in_progress)
        val completeText: TextView = view.findViewById(R.id.btn_completed)
        val uploadButton: Button = view.findViewById(R.id.btn_add)
        val searchButton = view.findViewById<ImageView>(R.id.top_bar_search)
        searchButton.visibility = View.GONE


        inProgressText.setOnClickListener {
            layoutParams.gravity = Gravity.START
            updateButtonColors(inProgressText, completeText)
            progressView.layoutParams = layoutParams
            switchFragment(InProgressMissionFragment())
        }

        completeText.setOnClickListener {
            layoutParams.gravity = Gravity.END
            updateButtonColors(completeText, inProgressText)
            progressView.layoutParams = layoutParams
            switchFragment(CompletedMissionFragment())
        }
        val notificationButton = view.findViewById<ImageView>(R.id.top_bar_bell)
        notificationButton.setOnClickListener {

            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.phone_slide_in_right, // 새로운 Fragment가 오른쪽에서 들어오는 애니메이션
                    R.anim.phone_slide_out_left, // 기존 Fragment가 왼쪽으로 밀리는 애니메이션
                    R.anim.phone_slide_in_left,  // 뒤로가기 시 기존 Fragment가 왼쪽에서 들어오는 애니메이션
                    R.anim.phone_slide_out_right
                )
                .replace(R.id.content_frame, NotificationFragment())
                .addToBackStack(null)
                .commit()
        }
        val notificationNumber = view.findViewById<TextView>(R.id.notificationNumber)
        notificationNumber.text = NotificationData.getCheckdNotificationDataList().toString()
        val mypageButton = view.findViewById<ImageView>(R.id.top_bar_person)
        mypageButton.setOnClickListener {

            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.phone_slide_in_right, // 새로운 Fragment가 오른쪽에서 들어오는 애니메이션
                    R.anim.phone_slide_out_left, // 기존 Fragment가 왼쪽으로 밀리는 애니메이션
                    R.anim.phone_slide_in_left,  // 뒤로가기 시 기존 Fragment가 왼쪽에서 들어오는 애니메이션
                    R.anim.phone_slide_out_right
                )
                .replace(R.id.content_frame, MypageFragment())
                .addToBackStack(null)
                .commit()
        }

        // 미션 업로드 이벤트 설정
        uploadButton.setOnClickListener {
            val fragment = MissionUploadFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit()
        }

        // 초기 화면 설정 (미완료 미션 리스트)
        switchFragment(InProgressMissionFragment())
    }

    private fun updateButtonColors(selectedText: TextView, deselectedText: TextView) {
        selectedText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        deselectedText.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_color2))
    }

    private fun switchFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.inner_content_frame, fragment)
            .commit()
    }
}
