package com.example.MadCampProj1_ver2.foodbank

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.MadCampProj1_ver2.R
import com.example.MadCampProj1_ver2.map.MapFragment
import com.example.MadCampProj1_ver2.mypage.MypageFragment
import com.example.MadCampProj1_ver2.notification.NotificationFragment
//import com.example.MadCampProj1_ver2.phone.ListItem

import com.example.MadCampProj1_ver2.sampledata.CVDto
import com.example.MadCampProj1_ver2.sampledata.MemberData
import com.example.MadCampProj1_ver2.sampledata.MemberDto

import com.example.MadCampProj1_ver2.sampledata.NotificationData

class FoodBankFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_phone_ver2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.phone_recycler_view_ver2)
        val memberDataList: List<MemberDto> = MemberData.getPhoneDataList(requireContext())
        val cvDataList: List<CVDto> = CVData.getCVDataList(requireContext())

        // 섹션화된 데잍 ㅓ준비

        val sectionedList = prepareSectionedList(memberDataList, cvDataList)
        val notificationNumber = view.findViewById<TextView>(R.id.notificationNumber_ver2)
        notificationNumber.text = NotificationData.getCheckdNotificationDataList(requireContext()).toString()

        val searchButton = view.findViewById<ImageView>(R.id.top_bar_search_ver2)
        searchButton.setOnClickListener {

            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.phone_slide_in_right, // 새로운 Fragment가 오른쪽에서 들어오는 애니메이션
                    R.anim.phone_slide_out_left, // 새로운 Fragment가 왼쪽에서 밀리는 애니메이션
                    R.anim.phone_slide_in_left, // 뒤로가기 시 기존 Fragmet가 왼쪽에서 들어오는 애니메이션
                    R.anim.phone_slide_out_right
                )
                .replace(R.id.content_frame_ver2, FoodBankSearchFragment())
                .addToBackStack(null)
                .commit()
        }

        val notificationButton = view.findViewById<ImageView>(R.id.top_bar_bell_ver2)
        notificationButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.phone_slide_in_right,
                    R.anim.phone_slide_out_left,
                    R.anim.phone_slide_in_left,
                    R.anim.phone_slide_out_right,
                )
                .replace(R.id.content_frame_ver2, NotificationFragment())
                .addToBackStack(null)
                .commit()
        }

        val mypageButton = view.findViewById<ImageView>(R.id.top_bar_person_ver2)
        mypageButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.phone_slide_in_right,
                    R.anim.phone_slide_out_left,
                    R.anim.phone_slide_in_left,
                    R.anim.phone_slide_out_right,
                )
                .replace(R.id.content_frame_ver2, MypageFragment())
                .addToBackStack(null)
                .commit()
        }

        // RecycleView 설정
        recyclerView.layoutManager = LinearLayoutManager(activity) // 아이템을 세트별로 나열
        Log.d("hi", sectionedList.toString())
//        recyclerView.adapter = PhoneAdapter(sectionedList, requireContext(), {id ->
        recyclerView.adapter = FoodBankAdapter(sectionedList, requireContext(), {id ->
            // onItemClick 이벤트 처리
            val fragment = FoodBankDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt("id", id)
                }
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_up,
                    0,
                    0,
                    R.anim.slide_out_down
                )
                .replace(R.id.content_frame_ver2, fragment)
                .addToBackStack(null)
                .commit()
        },
            {id ->
                val member = memberDataList.find { it.memberId == id }

                if (member != null) {
                    val fragment = MapFragment().apply {
                        arguments = Bundle().apply {
                            putDouble("lat", member.lat)
                            putDouble("lng", member.lng)
                            putInt("memberId", member.memberId)
                        }
                    }

                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.content_frame_ver2, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            })

    }
    fun prepareSectionedList(memberList: List<MemberDto>, cvList: List<CVDto>): List<ListItem> {
        val groupedData = memberList.mapNotNull { member ->
            val cv = cvList.find {it.memberId == member.memberId} // CVDto 객체
            cv?.let {member to it} // member 와 cv 즉 (CVDto 객체) 쌍을 반환
        }.groupBy { it.second.qualification } // cv 객체의 second feature 로 group화 진행

        val sectionedList = mutableListOf<ListItem>()

        // 그룹별로 정렬 후 헤더와 연락처 추가
        listOf("박사", "석사", "인턴").forEach { qualification ->
            val group = groupedData[qualification]
            if (!group.isNullOrEmpty()) {
                sectionedList.add(ListItem.Header(qualification))
                sectionedList.addAll(group.map { ListItem.Contact(it.first, qualification) })
            }
        }

        return sectionedList
    }
}
