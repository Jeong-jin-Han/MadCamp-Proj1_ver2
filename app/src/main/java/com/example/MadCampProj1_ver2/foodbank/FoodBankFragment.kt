package com.example.MadCampProj1_ver2.foodbank

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.MadCampProj1_ver2.R
import com.example.MadCampProj1_ver2.map.MapFragment
import com.example.MadCampProj1_ver2.myfoodpage.MyFoodpageFragment
import com.example.MadCampProj1_ver2.mypage.MypageFragment
import com.example.MadCampProj1_ver2.notification.NotificationFragment
import com.example.MadCampProj1_ver2.phone.PhoneSearchFragment
//import com.example.MadCampProj1_ver2.phone.ListItem

import com.example.MadCampProj1_ver2.sampledata.CVDto
import com.example.MadCampProj1_ver2.sampledata.MemberData
import com.example.MadCampProj1_ver2.sampledata.MemberDto

import com.example.MadCampProj1_ver2.sampledata.NotificationData
import com.example.MadCampProj1_ver2.samplefooddata.FoodData
import com.example.MadCampProj1_ver2.samplefooddata.FoodDto
import java.util.Calendar

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
        val foodDataList: List<FoodDto> = FoodData.getFoodDataList(requireContext())

        val cvDataList: List<CVDto> = CVData.getCVDataList(requireContext())

        // 섹션화된 데잍 ㅓ준비

//        val sectionedList = prepareSectionedList(memberDataList, cvDataList)
        val sectionedList = prepareSectionedList(foodDataList)
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
//                .replace(R.id.content_frame_ver2, PhoneSearchFragment())

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
//                .replace(R.id.content_frame_ver2, MypageFragment())
                .replace(R.id.content_frame_ver2, MyFoodpageFragment())
                .addToBackStack(null)
                .commit()
        }

        // RecycleView 설정
        recyclerView.layoutManager = LinearLayoutManager(activity) // 아이템을 세트별로 나열
        Log.d("hi", sectionedList.toString())
//        recyclerView.adapter = PhoneAdapter(sectionedList, requireContext(), {id ->
        recyclerView.adapter = FoodBankAdapter(sectionedList, requireContext(),
            {
                id ->
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
            {
                id ->
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
            },
            onCalanderClick = {
                id ->
                // 달력 열고 선택된 날짜를 처리
                showDatePickerDialog { selectedDate ->
                    Toast.makeText(requireContext(), "[$id] 날짜 선택됨: $selectedDate", Toast.LENGTH_SHORT).show()

                    // 여기서 id는 클릭된 FoodItem의 id (또는 foodId 등)
                    // 필요하면 선택된 날짜와 id로 서버에 저장하거나 다른 UI 업데이트도 가능
                }
            }
        )

    }
//    fun prepareSectionedList(memberList: List<MemberDto>, cvList: List<CVDto>): List<ListItem> {
//        val groupedData = memberList.mapNotNull { member ->
//            val cv = cvList.find {it.memberId == member.memberId} // CVDto 객체
//            cv?.let {member to it} // member 와 cv 즉 (CVDto 객체) 쌍을 반환
//        }.groupBy { it.second.qualification } // cv 객체의 second feature 로 group화 진행
//
//        val sectionedList = mutableListOf<ListItem>()
//
//        // 그룹별로 정렬 후 헤더와 연락처 추가
//        listOf("박사", "석사", "인턴").forEach { qualification ->
//            val group = groupedData[qualification]
//            if (!group.isNullOrEmpty()) {
//                sectionedList.add(ListItem.Header(qualification))
//                sectionedList.addAll(group.map { ListItem.Contact(it.first, qualification) })
//            }
//        }
//
//        return sectionedList
//    }

    fun prepareSectionedList(foodList: List<FoodDto>): List<ListItem> {
        val groupTitles = listOf("채소", "육류와 가공육", "유제품과 가공식품", "양념류", "기타")

        val categoryGroups = mapOf(
            "채소" to listOf("채소"),
            "육류와 가공육" to listOf("육류", "가공육"),
            "유제품과 가공식품" to listOf("유제품", "가공식품"),
            "양념류" to listOf("장류", "조미료"),
            "기타" to listOf("통조림", "곡류", "면류", "해산물", "건조식품", "베이커리", "발효식품")
        )

        // qualification → 상위 그룹 이름으로 매핑
        val mapped = foodList.map { food ->
            // qualification이 어떤 상위 그룹에 속하는지 찾기
            val groupName = categoryGroups.entries.find { it.value.contains(food.category) }?.key ?: "기타"
            groupName to food
        }

        // 상위 그룹별로 묶기
        val groupedByCategory = mapped.groupBy { it.first }

        val sectionedList = mutableListOf<ListItem>()

        // groupTitles 순서대로 섹션 생성
        groupTitles.forEach { title ->
            val group = groupedByCategory[title]
            if (!group.isNullOrEmpty()) {
                sectionedList.add(ListItem.Header(title))
                sectionedList.addAll(
                    group.map { (_, food) ->
                        ListItem.Contact(food, food.category)
                    }
                )
            }
        }

        return sectionedList
    }

    //DatePicker
    private fun showDatePickerDialog(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "${selectedYear}-${selectedMonth + 1}-${String.format("%02d", selectedDay)}"
                onDateSelected(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }
}
