package com.example.MadCampProj1_ver2.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.MadCampProj1_ver2.R
import com.example.MadCampProj1_ver2.gallery.GalleryDetailFragmentNotification
import com.example.MadCampProj1_ver2.map.MapFragment
import com.example.MadCampProj1_ver2.mission.MissionCompleteFragment
import com.example.MadCampProj1_ver2.sampledata.MemberData
import com.example.MadCampProj1_ver2.sampledata.MemberDto
import com.example.MadCampProj1_ver2.sampledata.NotificationData
import com.example.MadCampProj1_ver2.sampledata.NotificationDto


enum class NotificationType {
    GALLERY_POST, // 게시물이 생성되었습니다
    MISSION_COMPLETE, // 도전과제가 완료되었습니다
    MAP_LOCATION // 이동하였습니다
}

@Suppress("DEPRECATION")
class NotificationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.notification_recycler_view)

        val backArrow = view.findViewById<ImageView>(R.id.top_bar_arrow)
        backArrow.visibility = View.VISIBLE

        backArrow.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        recyclerView.layoutManager = LinearLayoutManager(activity)  // 아이템 세로로 나열


        val notifications: List<NotificationDto> = NotificationData.getNotificationDataList()
        val memberDataList: List<MemberDto> = MemberData.getPhoneDataList(requireContext())


        recyclerView.adapter = NotificationAdapter(requireContext(), notifications) { notification ->
            // onItemClick 이벤트 처리
            when (notification.type) {
                NotificationType.GALLERY_POST -> {
                    val fragment = GalleryDetailFragmentNotification().apply {
                        arguments = Bundle().apply {
                            putInt("id", notification.targetId) // GalleryDetailFragment에 ID 전달
                        }
                    }
                    requireActivity().supportFragmentManager.beginTransaction()
                        .setCustomAnimations(
                            R.anim.phone_slide_in_right, // 새로운 Fragment가 오른쪽에서 들어오는 애니메이션
                            R.anim.phone_slide_out_left, // 기존 Fragment가 왼쪽으로 밀리는 애니메이션
                            R.anim.phone_slide_in_left,  // 뒤로가기 시 기존 Fragment가 왼쪽에서 들어오는 애니메이션
                            R.anim.phone_slide_out_right
                        )
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit()
                }

                NotificationType.MISSION_COMPLETE -> {
                    val fragment = MissionCompleteFragment().apply {
                        arguments = Bundle().apply {
                            putInt(
                                "missionId",
                                notification.targetId
                            ) // MissionCompleteFragment에 ID 전달
                        }
                    }
                    requireActivity().supportFragmentManager.beginTransaction()
                        .setCustomAnimations(
                            R.anim.phone_slide_in_right, // 새로운 Fragment가 오른쪽에서 들어오는 애니메이션
                            R.anim.phone_slide_out_left, // 기존 Fragment가 왼쪽으로 밀리는 애니메이션
                            R.anim.phone_slide_in_left,  // 뒤로가기 시 기존 Fragment가 왼쪽에서 들어오는 애니메이션
                            R.anim.phone_slide_out_right
                        )
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack(null)
                        .commit()
                }

                NotificationType.MAP_LOCATION -> {
                    val member = memberDataList.find { it.memberId == notification.targetId }

                    if (member != null) {
                        val fragment = MapFragment().apply {
                            arguments = Bundle().apply {
                                putDouble("lat", member.lat)
                                putDouble("lng", member.lng)
                                putInt("memberId", member.memberId)
                            }
                        }

                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.content_frame, fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                }
            }
        }
    }
}