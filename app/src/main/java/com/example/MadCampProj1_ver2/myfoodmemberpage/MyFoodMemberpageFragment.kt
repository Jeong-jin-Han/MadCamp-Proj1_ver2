package com.example.MadCampProj1_ver2.myfoodmemberpage

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
import com.example.MadCampProj1_ver2.foodbank.FoodBankAdapter
import com.example.MadCampProj1_ver2.samplefooddata.FoodData
import com.example.MadCampProj1_ver2.samplefooddata.FoodDto
//import com.example.MadCampProj1_ver2.foodbank.ListItem
import com.example.MadCampProj1_ver2.myfoodmemberpage.ListItem


import com.example.MadCampProj1_ver2.myfooddata.MyFoodData
import com.example.MadCampProj1_ver2.myfooddata.MyFoodDto
import com.example.MadCampProj1_ver2.foodbank.FoodBankDetailFragment
import com.example.MadCampProj1_ver2.myfoodmemberdata.MyFoodMemberData
import com.example.MadCampProj1_ver2.myfoodmemberdata.MyFoodMemberData.getMyFoodMemberIfExists
import com.example.MadCampProj1_ver2.sampledata.MemberData
import com.example.MadCampProj1_ver2.sampledata.MemberDto

import java.util.Calendar


@Suppress("DEPRECATION")
class MyFoodMemberpageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_mypage_ver2, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView_vegatable: RecyclerView = view.findViewById(R.id.recycler_vegatable)
        val recyclerView_meat: RecyclerView = view.findViewById(R.id.recycler_meat)
        val recyclerView_dairy: RecyclerView = view.findViewById(R.id.recycler_dairy)
        val recyclerView_sauce: RecyclerView = view.findViewById(R.id.recycler_sauce)
        val recyclerView_etc: RecyclerView = view.findViewById(R.id.recycler_etc)

        val backArrow = view.findViewById<ImageView>(R.id.top_bar_arrow)
        backArrow.visibility = View.VISIBLE

        backArrow.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val memberId = arguments?.getInt("memberId", -1)

//        val totalfoodDataList: List<FoodDto> = FoodData.getFoodDataList(requireContext())
//        val foodDataList = when (memberId) {
//            -1 -> FoodData.getFoodDataList(requireContext())
//            else -> MyFoodMemberData.getFoodDtoListfromMemberId(memberId ?: return, totalfoodDataList)
//        }

        val foodDataList: List<FoodDto> = FoodData.getFoodDataList(requireContext())
        Log.d("FoodDebug", "전체 FoodDto 리스트 불러옴: ${foodDataList.size}개")

        val memberfoodDataList = when (memberId) {
            -1 -> {
                Log.d("FoodDebug", "memberId가 -1이므로 기본 FoodData 사용")
                FoodData.getFoodDataList(requireContext())
            }
            else -> {
                Log.d("FoodDebug", "memberId: $memberId → 해당 멤버의 식재료만 필터링")
                val result = MyFoodMemberData.getFoodDtoListfromMemberId(memberId ?: return, foodDataList)
                Log.d("FoodDebug", "필터링된 식재료 수: ${result.size}")
                val memberDataList: List<MemberDto> = MemberData.getPhoneDataList(requireContext()) // member data
                val member = memberDataList.find {it.memberId == memberId}

                /* xml file 수정하기 */
                if (member != null) {
                    view.findViewById<ImageView>(R.id.phone_detail_image).setImageResource(member.imgPath)
                    view.findViewById<TextView>(R.id.phone_detail_name).text = member.name
                    view.findViewById<TextView>(R.id.phone_detail_major).text = member.home
                }


                result
            }
        }


        val sectionedList_vegatable = prepareSectionedList_with_MyFood(memberId, foodDataList, memberfoodDataList,"채소")
        val sectionedList_meat = prepareSectionedList_with_MyFood(memberId, foodDataList, memberfoodDataList,"육류와 가공육")
        val sectionedList_dairy = prepareSectionedList_with_MyFood(memberId, foodDataList, memberfoodDataList,"유제품과 가공식품")
        val sectionedList_sauce = prepareSectionedList_with_MyFood(memberId, foodDataList, memberfoodDataList,"양념류")
        val sectionedList_etc = prepareSectionedList_with_MyFood(memberId, foodDataList, memberfoodDataList,"기타" )

        // vegetable
        recyclerView_vegatable.layoutManager = LinearLayoutManager(activity) // 아이템을 세트별로 나열
        Log.d("hi", sectionedList_vegatable.toString())
//        recyclerView.adapter = PhoneAdapter(sectionedList, requireContext(), {id ->

        recyclerView_vegatable.adapter = MyFoodMemberPageAdapter(sectionedList_vegatable, requireContext())

        // meat
        recyclerView_meat.layoutManager = LinearLayoutManager(activity) // 아이템을 세트별로 나열
        Log.d("hi", sectionedList_meat.toString())
//        recyclerView.adapter = PhoneAdapter(sectionedList, requireContext(), {id ->
        recyclerView_meat.adapter = MyFoodMemberPageAdapter(sectionedList_meat, requireContext())


        // diary
        recyclerView_dairy.layoutManager = LinearLayoutManager(activity) // 아이템을 세트별로 나열
        Log.d("hi", sectionedList_dairy.toString())
//        recyclerView.adapter = PhoneAdapter(sectionedList, requireContext(), {id ->
        recyclerView_dairy.adapter = MyFoodMemberPageAdapter(sectionedList_dairy, requireContext())

        // sauce
        recyclerView_sauce.layoutManager = LinearLayoutManager(activity) // 아이템을 세트별로 나열
        Log.d("hi", sectionedList_sauce.toString())
//        recyclerView.adapter = PhoneAdapter(sectionedList, requireContext(), {id ->
        recyclerView_sauce.adapter = MyFoodMemberPageAdapter(sectionedList_sauce, requireContext())

        recyclerView_etc.layoutManager = LinearLayoutManager(activity) // 아이템을 세트별로 나열
        Log.d("hi", sectionedList_etc.toString())
//        recyclerView.adapter = PhoneAdapter(sectionedList, requireContext(), {id ->
        recyclerView_etc.adapter = MyFoodMemberPageAdapter(sectionedList_etc, requireContext())
    }
//    fun prepareSectionedList(foodList: List<FoodDto>): List<ListItem> {
//        val groupTitles = listOf("채소", "육류와 가공육", "유제품과 가공식품", "양념류", "기타")
//
//        val categoryGroups = mapOf(
//            "채소" to listOf("채소"),
//            "육류와 가공육" to listOf("육류", "가공육"),
//            "유제품과 가공식품" to listOf("유제품", "가공식품"),
//            "양념류" to listOf("장류", "조미료"),
//            "기타" to listOf("통조림", "곡류", "면류", "해산물", "건조식품", "베이커리", "발효식품")
//        )
//
//        // qualification → 상위 그룹 이름으로 매핑
//        val mapped = foodList.map { food ->
//            // qualification이 어떤 상위 그룹에 속하는지 찾기
//            val groupName = categoryGroups.entries.find { it.value.contains(food.category) }?.key ?: "기타"
//            groupName to food
//        }
//
//        // 상위 그룹별로 묶기
//        val groupedByCategory = mapped.groupBy { it.first }
//
//        val sectionedList = mutableListOf<ListItem>()
//
//        // groupTitles 순서대로 섹션 생성
//        groupTitles.forEach { title ->
//            val group = groupedByCategory[title]
//            if (!group.isNullOrEmpty()) {
//                sectionedList.add(ListItem.Header(title))
//                sectionedList.addAll(
//                    group.map { (_, food) ->
//                        ListItem.Contact(food, food.category)
//                    }
//                )
//            }
//        }
//
//        return sectionedList
//    }

    fun prepareSectionedList_with_MyFood(
        memberId: Int,
        foodList: List<FoodDto>,
        memberFoodList: List<FoodDto>,
        groupTitle: String
    ): List<ListItem> {
        val myFoodIds = memberFoodList
            .map { it.foodId }
            .toSet()

        val categoryGroups = mapOf(
            "채소" to listOf("채소"),
            "육류와 가공육" to listOf("육류", "가공육"),
            "유제품과 가공식품" to listOf("유제품", "가공식품"),
            "양념류" to listOf("장류", "조미료"),
            "기타" to listOf("통조림", "곡류", "면류", "해산물", "건조식품", "베이커리", "발효식품")
        )

        val allowedCategories = categoryGroups[groupTitle] ?: emptyList()

        val targetFoodList = foodList.filter {
            it.foodId in myFoodIds && it.category in allowedCategories
        }

        return if (targetFoodList.isNotEmpty()) {
            listOf(ListItem.Header(groupTitle)) +
                    targetFoodList.map { food -> ListItem.Contact(memberId, food, food.category) }
        } else {
            emptyList()
        }
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