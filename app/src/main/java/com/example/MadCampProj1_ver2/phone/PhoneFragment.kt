package com.example.MadCampProj1_ver2.gallery

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
import com.example.MadCampProj1_ver2.phone.ListItem
import com.example.MadCampProj1_ver2.sampledata.CVDto
import com.example.MadCampProj1_ver2.sampledata.MemberData
import com.example.MadCampProj1_ver2.sampledata.MemberDto
import com.example.MadCampProj1_ver2.phone.PhoneAdapter
import com.example.MadCampProj1_ver2.sampledata.NotificationData


@Suppress("DEPRECATION")
class PhoneFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_phone, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.phone_recycler_view)
        val memberDataList: List<MemberDto> = MemberData.getPhoneDataList()
        val cvDataList: List<CVDto> = CVData.getCVDataList()

        // 섹션화된 데이터 준비

        val sectionedList = prepareSectionedList(memberDataList, cvDataList)
        val notificationNumber = view.findViewById<TextView>(R.id.notificationNumber)
        notificationNumber.text = NotificationData.getCheckdNotificationDataList().toString()

        val searchButton = view.findViewById<ImageView>(R.id.top_bar_search)
        searchButton.setOnClickListener {

            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.phone_slide_in_right, // 새로운 Fragment가 오른쪽에서 들어오는 애니메이션
                    R.anim.phone_slide_out_left, // 기존 Fragment가 왼쪽으로 밀리는 애니메이션
                    R.anim.phone_slide_in_left,  // 뒤로가기 시 기존 Fragment가 왼쪽에서 들어오는 애니메이션
                    R.anim.phone_slide_out_right
                )
                .replace(R.id.content_frame, PhoneSearchFragment())
                .addToBackStack(null)
                .commit()
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


        // RecyclerView 설정
        recyclerView.layoutManager = LinearLayoutManager(activity)  // 아이템 세로로 나열
        Log.d("hi", sectionedList.toString())
        recyclerView.adapter = PhoneAdapter(sectionedList, { id ->
            // onItemClick 이벤트 처리
            val fragment = PhoneDetailFragment().apply {
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
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit()
        }, { id ->
            // onLocationClick 이벤트 처리
            val member = memberDataList.find { it.memberId == id }

            if(member != null){
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
        })

    }


    fun prepareSectionedList(memberList: List<MemberDto>, cvList: List<CVDto>): List<ListItem> {
        val groupedData = memberList.mapNotNull { member ->
            val cv = cvList.find { it.memberId == member.memberId }
            cv?.let { member to it }
        }.groupBy { it.second.qualification } // 그룹화: 박사, 석사, 학사

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