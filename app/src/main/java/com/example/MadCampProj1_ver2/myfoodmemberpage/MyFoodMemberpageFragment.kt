package com.example.MadCampProj1_ver2.myfoodmemberpage

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.MadCampProj1_ver2.R
import com.example.MadCampProj1_ver2.foodbank.FoodBankAdapter
import com.example.MadCampProj1_ver2.samplefooddata.FoodData
import com.example.MadCampProj1_ver2.samplefooddata.FoodDto
import com.example.MadCampProj1_ver2.foodbank.ListItem
import com.example.MadCampProj1_ver2.myfooddata.MyFoodData
import com.example.MadCampProj1_ver2.myfooddata.MyFoodDto
import com.example.MadCampProj1_ver2.foodbank.FoodBankDetailFragment
import com.example.MadCampProj1_ver2.myfoodmemberdata.MyFoodMemberData
import com.example.MadCampProj1_ver2.myfoodmemberdata.MyFoodMemberData.getMyFoodMemberIfExists

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
                result
            }
        }


        val sectionedList_vegatable = prepareSectionedList_with_MyFood(foodDataList, memberfoodDataList,"채소")
        val sectionedList_meat = prepareSectionedList_with_MyFood(foodDataList, memberfoodDataList,"육류와 가공육")
        val sectionedList_dairy = prepareSectionedList_with_MyFood(foodDataList, memberfoodDataList,"유제품과 가공식품")
        val sectionedList_sauce = prepareSectionedList_with_MyFood(foodDataList, memberfoodDataList,"양념류")
        val sectionedList_etc = prepareSectionedList_with_MyFood(foodDataList, memberfoodDataList,"기타" )

        // vegetable
        recyclerView_vegatable.layoutManager = LinearLayoutManager(activity) // 아이템을 세트별로 나열
        Log.d("hi", sectionedList_vegatable.toString())
//        recyclerView.adapter = PhoneAdapter(sectionedList, requireContext(), {id ->
        recyclerView_vegatable.adapter = FoodBankAdapter(sectionedList_vegatable, requireContext(),
            {
                    id ->
                // onItemClick 이벤트 처리
//                val fragment = FoodBankDetailFragment().apply {
//                    arguments = Bundle().apply {
//                        putInt("id", id)
//                    }
//                }
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .setCustomAnimations(
//                        R.anim.slide_in_up,
//                        0,
//                        0,
//                        R.anim.slide_out_down
//                    )
//                    .replace(R.id.content_frame_ver2, fragment)
//                    .addToBackStack(null)
//                    .commit()
            },
            {
                    id ->
//                val member = memberDataList.find { it.memberId == id }
//
//                if (member != null) {
//                    val fragment = MapFragment().apply {
//                        arguments = Bundle().apply {
//                            putDouble("lat", member.lat)
//                            putDouble("lng", member.lng)
//                            putInt("memberId", member.memberId)
//                        }
//                    }
//
//                    requireActivity().supportFragmentManager.beginTransaction()
//                        .replace(R.id.content_frame_ver2, fragment)
//                        .addToBackStack(null)
//                        .commit()
//                }
            },
            onCalanderClick = {
                    foodId, statusTextView ->
                // 달력 열고 선택된 날짜를 처리
                showDatePickerDialog { selectedDate ->
                    // 1. MyFoodData에 추가
                    MyFoodData.addMyFoodDataDueDate(foodId, selectedDate)

                    Toast.makeText(requireContext(), "[$foodId] 날짜 선택됨: $selectedDate", Toast.LENGTH_SHORT).show()

                    statusTextView.text = "유통기한: $selectedDate"
                    // 여기서 id는 클릭된 FoodItem의 id (또는 foodId 등)
                    // 필요하면 선택된 날짜와 id로 서버에 저장하거나 다른 UI 업데이트도 가능
                }
            },
            onPlusClick = {
                    foodId, numberView ->
                MyFoodData.addMyFoodDataNumber(foodId)
                Toast.makeText(requireContext(), "[$foodId] 수량 +1", Toast.LENGTH_SHORT).show()
                val number = MyFoodData.getMyFoodDataNumberfromFoodId(foodId)
                numberView.text = "$number 개"

            },
            onMinusClick = {
                    foodId, numberView ->
                MyFoodData.deleteMyFoodDataNumber(foodId)
                Toast.makeText(requireContext(), "[$foodId] 수량 -1", Toast.LENGTH_SHORT).show()
                val number = MyFoodData.getMyFoodDataNumberfromFoodId(foodId)
                numberView.text = "$number 개"
            }
        )

        // meat
        recyclerView_meat.layoutManager = LinearLayoutManager(activity) // 아이템을 세트별로 나열
        Log.d("hi", sectionedList_meat.toString())
//        recyclerView.adapter = PhoneAdapter(sectionedList, requireContext(), {id ->
        recyclerView_meat.adapter = FoodBankAdapter(sectionedList_meat, requireContext(),
            {
                    id ->
                // onItemClick 이벤트 처리
//                val fragment = FoodBankDetailFragment().apply {
//                    arguments = Bundle().apply {
//                        putInt("id", id)
//                    }
//                }
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .setCustomAnimations(
//                        R.anim.slide_in_up,
//                        0,
//                        0,
//                        R.anim.slide_out_down
//                    )
//                    .replace(R.id.content_frame_ver2, fragment)
//                    .addToBackStack(null)
//                    .commit()
            },
            {
                    id ->
//                val member = memberDataList.find { it.memberId == id }
//
//                if (member != null) {
//                    val fragment = MapFragment().apply {
//                        arguments = Bundle().apply {
//                            putDouble("lat", member.lat)
//                            putDouble("lng", member.lng)
//                            putInt("memberId", member.memberId)
//                        }
//                    }
//
//                    requireActivity().supportFragmentManager.beginTransaction()
//                        .replace(R.id.content_frame_ver2, fragment)
//                        .addToBackStack(null)
//                        .commit()
//                }
            },
            onCalanderClick = {
                    foodId, statusTextView ->
                // 달력 열고 선택된 날짜를 처리
                showDatePickerDialog { selectedDate ->
                    // 1. MyFoodData에 추가
                    MyFoodData.addMyFoodDataDueDate(foodId, selectedDate)

                    Toast.makeText(requireContext(), "[$foodId] 날짜 선택됨: $selectedDate", Toast.LENGTH_SHORT).show()

                    statusTextView.text = "유통기한: $selectedDate"
                    // 여기서 id는 클릭된 FoodItem의 id (또는 foodId 등)
                    // 필요하면 선택된 날짜와 id로 서버에 저장하거나 다른 UI 업데이트도 가능
                }
            },
            onPlusClick = {
                    foodId, numberView ->
                MyFoodData.addMyFoodDataNumber(foodId)
                Toast.makeText(requireContext(), "[$foodId] 수량 +1", Toast.LENGTH_SHORT).show()
                val number = MyFoodData.getMyFoodDataNumberfromFoodId(foodId)
                numberView.text = "$number 개"

            },
            onMinusClick = {
                    foodId, numberView ->
                MyFoodData.deleteMyFoodDataNumber(foodId)
                Toast.makeText(requireContext(), "[$foodId] 수량 -1", Toast.LENGTH_SHORT).show()
                val number = MyFoodData.getMyFoodDataNumberfromFoodId(foodId)
                numberView.text = "$number 개"
            }
        )

        // diary
        recyclerView_dairy.layoutManager = LinearLayoutManager(activity) // 아이템을 세트별로 나열
        Log.d("hi", sectionedList_dairy.toString())
//        recyclerView.adapter = PhoneAdapter(sectionedList, requireContext(), {id ->
        recyclerView_dairy.adapter = FoodBankAdapter(sectionedList_dairy, requireContext(),
            {
                    id ->
//                // onItemClick 이벤트 처리
//                val fragment = FoodBankDetailFragment().apply {
//                    arguments = Bundle().apply {
//                        putInt("id", id)
//                    }
//                }
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .setCustomAnimations(
//                        R.anim.slide_in_up,
//                        0,
//                        0,
//                        R.anim.slide_out_down
//                    )
//                    .replace(R.id.content_frame_ver2, fragment)
//                    .addToBackStack(null)
//                    .commit()
            },
            {
                    id ->
//                val member = memberDataList.find { it.memberId == id }
//
//                if (member != null) {
//                    val fragment = MapFragment().apply {
//                        arguments = Bundle().apply {
//                            putDouble("lat", member.lat)
//                            putDouble("lng", member.lng)
//                            putInt("memberId", member.memberId)
//                        }
//                    }
//
//                    requireActivity().supportFragmentManager.beginTransaction()
//                        .replace(R.id.content_frame_ver2, fragment)
//                        .addToBackStack(null)
//                        .commit()
//                }
            },
            onCalanderClick = {
                    foodId, statusTextView ->
                // 달력 열고 선택된 날짜를 처리
                showDatePickerDialog { selectedDate ->
                    // 1. MyFoodData에 추가
                    MyFoodData.addMyFoodDataDueDate(foodId, selectedDate)

                    Toast.makeText(requireContext(), "[$foodId] 날짜 선택됨: $selectedDate", Toast.LENGTH_SHORT).show()

                    statusTextView.text = "유통기한: $selectedDate"
                    // 여기서 id는 클릭된 FoodItem의 id (또는 foodId 등)
                    // 필요하면 선택된 날짜와 id로 서버에 저장하거나 다른 UI 업데이트도 가능
                }
            },
            onPlusClick = {
                    foodId, numberView ->
                MyFoodData.addMyFoodDataNumber(foodId)
                Toast.makeText(requireContext(), "[$foodId] 수량 +1", Toast.LENGTH_SHORT).show()
                val number = MyFoodData.getMyFoodDataNumberfromFoodId(foodId)
                numberView.text = "$number 개"

            },
            onMinusClick = {
                    foodId, numberView ->
                MyFoodData.deleteMyFoodDataNumber(foodId)
                Toast.makeText(requireContext(), "[$foodId] 수량 -1", Toast.LENGTH_SHORT).show()
                val number = MyFoodData.getMyFoodDataNumberfromFoodId(foodId)
                numberView.text = "$number 개"
            }
        )

        // sauce
        recyclerView_sauce.layoutManager = LinearLayoutManager(activity) // 아이템을 세트별로 나열
        Log.d("hi", sectionedList_sauce.toString())
//        recyclerView.adapter = PhoneAdapter(sectionedList, requireContext(), {id ->
        recyclerView_sauce.adapter = FoodBankAdapter(sectionedList_sauce, requireContext(),
            {
                    id ->
//                // onItemClick 이벤트 처리
//                val fragment = FoodBankDetailFragment().apply {
//                    arguments = Bundle().apply {
//                        putInt("id", id)
//                    }
//                }
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .setCustomAnimations(
//                        R.anim.slide_in_up,
//                        0,
//                        0,
//                        R.anim.slide_out_down
//                    )
//                    .replace(R.id.content_frame_ver2, fragment)
//                    .addToBackStack(null)
//                    .commit()
            },
            {
                    id ->
//                val member = memberDataList.find { it.memberId == id }
//
//                if (member != null) {
//                    val fragment = MapFragment().apply {
//                        arguments = Bundle().apply {
//                            putDouble("lat", member.lat)
//                            putDouble("lng", member.lng)
//                            putInt("memberId", member.memberId)
//                        }
//                    }
//
//                    requireActivity().supportFragmentManager.beginTransaction()
//                        .replace(R.id.content_frame_ver2, fragment)
//                        .addToBackStack(null)
//                        .commit()
//                }
            },
            onCalanderClick = {
                    foodId, statusTextView ->
                // 달력 열고 선택된 날짜를 처리
                showDatePickerDialog { selectedDate ->
                    // 1. MyFoodData에 추가
                    MyFoodData.addMyFoodDataDueDate(foodId, selectedDate)

                    Toast.makeText(requireContext(), "[$foodId] 날짜 선택됨: $selectedDate", Toast.LENGTH_SHORT).show()

                    statusTextView.text = "유통기한: $selectedDate"
                    // 여기서 id는 클릭된 FoodItem의 id (또는 foodId 등)
                    // 필요하면 선택된 날짜와 id로 서버에 저장하거나 다른 UI 업데이트도 가능
                }
            },
            onPlusClick = {
                    foodId, numberView ->
                MyFoodData.addMyFoodDataNumber(foodId)
                Toast.makeText(requireContext(), "[$foodId] 수량 +1", Toast.LENGTH_SHORT).show()
                val number = MyFoodData.getMyFoodDataNumberfromFoodId(foodId)
                numberView.text = "$number 개"

            },
            onMinusClick = {
                    foodId, numberView ->
                MyFoodData.deleteMyFoodDataNumber(foodId)
                Toast.makeText(requireContext(), "[$foodId] 수량 -1", Toast.LENGTH_SHORT).show()
                val number = MyFoodData.getMyFoodDataNumberfromFoodId(foodId)
                numberView.text = "$number 개"
            }
        )

        recyclerView_etc.layoutManager = LinearLayoutManager(activity) // 아이템을 세트별로 나열
        Log.d("hi", sectionedList_etc.toString())
//        recyclerView.adapter = PhoneAdapter(sectionedList, requireContext(), {id ->
        recyclerView_etc.adapter = FoodBankAdapter(sectionedList_etc, requireContext(),
            {
                    id ->
//                // onItemClick 이벤트 처리
//                val fragment = FoodBankDetailFragment().apply {
//                    arguments = Bundle().apply {
//                        putInt("id", id)
//                    }
//                }
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .setCustomAnimations(
//                        R.anim.slide_in_up,
//                        0,
//                        0,
//                        R.anim.slide_out_down
//                    )
//                    .replace(R.id.content_frame_ver2, fragment)
//                    .addToBackStack(null)
//                    .commit()
            },
            {
                    id ->
//                val member = memberDataList.find { it.memberId == id }
//
//                if (member != null) {
//                    val fragment = MapFragment().apply {
//                        arguments = Bundle().apply {
//                            putDouble("lat", member.lat)
//                            putDouble("lng", member.lng)
//                            putInt("memberId", member.memberId)
//                        }
//                    }
//
//                    requireActivity().supportFragmentManager.beginTransaction()
//                        .replace(R.id.content_frame_ver2, fragment)
//                        .addToBackStack(null)
//                        .commit()
//                }
            },
            onCalanderClick = {
                    foodId, statusTextView ->
                // 달력 열고 선택된 날짜를 처리
                showDatePickerDialog { selectedDate ->
                    // 1. MyFoodData에 추가
                    MyFoodData.addMyFoodDataDueDate(foodId, selectedDate)

                    Toast.makeText(requireContext(), "[$foodId] 날짜 선택됨: $selectedDate", Toast.LENGTH_SHORT).show()

                    statusTextView.text = "유통기한: $selectedDate"
                    // 여기서 id는 클릭된 FoodItem의 id (또는 foodId 등)
                    // 필요하면 선택된 날짜와 id로 서버에 저장하거나 다른 UI 업데이트도 가능
                }
            },
            onPlusClick = {
                    foodId, numberView ->
                MyFoodData.addMyFoodDataNumber(foodId)
                Toast.makeText(requireContext(), "[$foodId] 수량 +1", Toast.LENGTH_SHORT).show()
                val number = MyFoodData.getMyFoodDataNumberfromFoodId(foodId)
                numberView.text = "$number 개"

            },
            onMinusClick = {
                    foodId, numberView ->
                MyFoodData.deleteMyFoodDataNumber(foodId)
                Toast.makeText(requireContext(), "[$foodId] 수량 -1", Toast.LENGTH_SHORT).show()
                val number = MyFoodData.getMyFoodDataNumberfromFoodId(foodId)
                numberView.text = "$number 개"
            }
        )
    }
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

    fun prepareSectionedList_with_MyFood(
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
                    targetFoodList.map { food -> ListItem.Contact(food, food.category) }
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