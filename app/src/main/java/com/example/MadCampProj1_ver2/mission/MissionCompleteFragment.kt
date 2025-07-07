package com.example.MadCampProj1_ver2.mission

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.MadCampProj1_ver2.R
import com.example.MadCampProj1_ver2.phone.PhoneDetailFragment
import com.example.MadCampProj1_ver2.map.MapFragment
import com.example.MadCampProj1_ver2.mypage.MypageFragment
import com.example.MadCampProj1_ver2.notification.NotificationFragment
import com.example.MadCampProj1_ver2.phone.ListItem
import com.example.MadCampProj1_ver2.phone.PhoneAdapter
import com.example.MadCampProj1_ver2.sampledata.CVDto
import com.example.MadCampProj1_ver2.sampledata.MemberData
import com.example.MadCampProj1_ver2.sampledata.MemberDto
import com.example.MadCampProj1_ver2.sampledata.MissionCompleteDto
import com.example.MadCampProj1_ver2.sampledata.MissionCompleteData
import com.example.MadCampProj1_ver2.sampledata.MissionData
import com.example.MadCampProj1_ver2.sampledata.MissionDto
import com.example.MadCampProj1_ver2.sampledata.NotificationData
import java.time.format.DateTimeFormatter
import java.util.Locale

class MissionCompleteFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PhoneAdapter
    private lateinit var originalData: List<ListItem>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_mission_complete, container, false)
    }

    @SuppressLint("SetTextI18n", "DiscouragedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topBarTextView = view.findViewById<TextView>(R.id.top_bar_text)
        topBarTextView.text = "도전 과제"

        //component 정보 입력
        val missionId = arguments?.getInt("missionId") ?: -1
        val backArrow = view.findViewById<ImageView>(R.id.top_bar_arrow)
        backArrow.visibility = View.VISIBLE

        backArrow.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
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
                .replace(R.id.content_frame_ver2, NotificationFragment())
                .addToBackStack(null)
                .commit()
        }

        val notificationNumber = view.findViewById<TextView>(R.id.notificationNumber)
        notificationNumber.text = NotificationData.getCheckdNotificationDataList(requireContext()).toString()
        val mypageButton = view.findViewById<ImageView>(R.id.top_bar_person)
        mypageButton.setOnClickListener {

            requireActivity().supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.phone_slide_in_right, // 새로운 Fragment가 오른쪽에서 들어오는 애니메이션
                    R.anim.phone_slide_out_left, // 기존 Fragment가 왼쪽으로 밀리는 애니메이션
                    R.anim.phone_slide_in_left,  // 뒤로가기 시 기존 Fragment가 왼쪽에서 들어오는 애니메이션
                    R.anim.phone_slide_out_right
                )
                .replace(R.id.content_frame_ver2, MypageFragment())
                .addToBackStack(null)
                .commit()
        }
        recyclerView = view.findViewById(R.id.complete_recycler_view)
        if(missionId != -1){
            val missionCompleteList: List<MissionCompleteDto> = MissionCompleteData.getMissionCompleteList()

            // missionId가 동일한 memberId값 리스트화
            val filteredMemberIds = missionCompleteList
                .filter { it.missionId == missionId }
                .map { it.memberId }

            // Load data and set up RecyclerView
            originalData = prepareSectionedList(MemberData.getPhoneDataList(requireContext()), CVData.getCVDataList(requireContext()), filteredMemberIds)
//            val initialData = listOf<ListItem>()
            adapter = PhoneAdapter(originalData,
                context = requireContext(),
                onItemClick = { id ->
                    // Handle item click (e.g., navigate to detail view)
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
                        .replace(R.id.content_frame_ver2, fragment)
                        .addToBackStack(null)
                        .commit()
                },
                onLocationClick = { id ->
                    val memberDataList: List<MemberDto> = MemberData.getPhoneDataList(requireContext())
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
                            .replace(R.id.content_frame_ver2, fragment)
                            .addToBackStack(null)
                            .commit()
                    }
                }
            )

            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter

            val missionList: List<MissionDto> = MissionData.getMissionDataList()
            val mission = missionList.find { it.id == missionId }
            //날짜 format
            if(mission != null){
                view.findViewById<TextView>(R.id.mission_name).text = mission.name
                val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
                val startDateFormatted = mission.startDate.format(dateFormatter)
                val endDateFormatted = mission.endDate.format(dateFormatter)
                view.findViewById<TextView>(R.id.mission_date).text = "인증 기간: $startDateFormatted ~ $endDateFormatted"
                view.findViewById<TextView>(R.id.mission_percent).text = "달성률\n${mission.percent}%"

                //사진 링크
                val link = "mission_${mission.category}"
                val context = view.findViewById<ImageView>(R.id.mission_category).context
                val resId = context.resources.getIdentifier(link, "drawable", context.packageName)

                if (resId != 0) {
                    view.findViewById<ImageView>(R.id.mission_category).setImageResource(resId)
                } else {
                    view.findViewById<ImageView>(R.id.mission_category).setImageResource(R.drawable.mission_1) // 기본 이미지
                }

                val colorList: List<String> = FontColorList.getFontColorList()
                view.findViewById<TextView>(R.id.mission_percent).setTextColor(Color.parseColor(colorList[mission.category - 1]))
            }
        }
    }

    fun prepareSectionedList(memberList: List<MemberDto>, cvList: List<CVDto>, filteredMemberIds: List<Int>): List<ListItem> {
        val filteredData = memberList.mapNotNull { member ->
            // memberId가 filteredMemberIds에 포함된 경우만 처리
            if (filteredMemberIds.contains(member.memberId)) {
                val cv = cvList.find { it.memberId == member.memberId }
                cv?.let { member to it }
            } else {
                null
            }
        }

        val sectionedList = mutableListOf<ListItem>()
        // 필터링된 데이터만 리스트에 추가
        filteredData.forEach { (member, cv) ->
            sectionedList.add(ListItem.Contact(member, cv.qualification))
        }
        return sectionedList
    }
}