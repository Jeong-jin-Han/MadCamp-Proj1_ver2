package com.example.MadCampProj1_ver2.myfoodpage

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
import com.example.MadCampProj1_ver2.foodbank.FoodBankFragment
import com.example.MadCampProj1_ver2.foodmap.FoodMapFragment
import com.example.MadCampProj1_ver2.myfoodmemberdata.MyFoodMemberData.getMyFoodMemberIfExists

import com.example.MadCampProj1_ver2.gallery.GalleryFragment

import java.util.Calendar


@Suppress("DEPRECATION")
class MyFoodpageFragment : Fragment() {
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

//        backArrow.setOnClickListener {
//            requireActivity().onBackPressedDispatcher.onBackPressed()
//        }

        backArrow.setOnClickListener {
            val source = arguments?.getString("source")

            val targetFragment = when (source) {
                "bank" -> FoodBankFragment()
                "map" -> FoodMapFragment()
                "gallery" -> GalleryFragment()
                else -> FoodBankFragment() // 기본 fallback
            }

            requireActivity().supportFragmentManager.beginTransaction()
                .hide(this@MyFoodpageFragment)
                .add(R.id.content_frame_ver2, targetFragment)
                .addToBackStack(null)
                .commit()
        }

        val foodDataList: List<FoodDto> = FoodData.getFoodDataList(requireContext())
        val sectionedList_vegatable = prepareSectionedList_with_MyFood(foodDataList, "채소")
        val sectionedList_meat = prepareSectionedList_with_MyFood(foodDataList, "육류와 가공육")
        val sectionedList_dairy = prepareSectionedList_with_MyFood(foodDataList, "유제품과 가공식품")
        val sectionedList_sauce = prepareSectionedList_with_MyFood(foodDataList, "양념류")
        val sectionedList_etc = prepareSectionedList_with_MyFood(foodDataList, "기타" )

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
//                    recyclerView_vegatable.adapter?.notifyDataSetChanged()
                }
            },
            onPlusClick = {
                    foodId, numberView, numberView2 ->
                MyFoodData.addMyFoodDataNumber(foodId)
                Toast.makeText(requireContext(), "[$foodId] 수량 +1", Toast.LENGTH_SHORT).show()
                val number = MyFoodData.getMyFoodDataNumberfromFoodId(foodId)
                numberView.text = "$number 개"
                numberView2.text = "$number"
            },
            onMinusClick = {
                    foodId, numberView, numberView2 ->
                MyFoodData.deleteMyFoodDataNumber(foodId)
                Toast.makeText(requireContext(), "[$foodId] 수량 -1", Toast.LENGTH_SHORT).show()
                val number = MyFoodData.getMyFoodDataNumberfromFoodId(foodId)
                numberView.text = "$number 개"
                numberView2.text = "$number"

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
                    foodId, numberView, numberView2 ->
                MyFoodData.addMyFoodDataNumber(foodId)
                Toast.makeText(requireContext(), "[$foodId] 수량 +1", Toast.LENGTH_SHORT).show()
                val number = MyFoodData.getMyFoodDataNumberfromFoodId(foodId)
                numberView.text = "$number 개"
                numberView2.text = "$number"

            },
            onMinusClick = {
                    foodId, numberView, numberView2 ->
                MyFoodData.deleteMyFoodDataNumber(foodId)
                Toast.makeText(requireContext(), "[$foodId] 수량 -1", Toast.LENGTH_SHORT).show()
                val number = MyFoodData.getMyFoodDataNumberfromFoodId(foodId)
                numberView.text = "$number 개"
                numberView2.text = "$number"
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
                    foodId, numberView, numberView2 ->
                MyFoodData.addMyFoodDataNumber(foodId)
                Toast.makeText(requireContext(), "[$foodId] 수량 +1", Toast.LENGTH_SHORT).show()
                val number = MyFoodData.getMyFoodDataNumberfromFoodId(foodId)
                numberView.text = "$number 개"
                numberView2.text = "$number"

            },
            onMinusClick = {
                    foodId, numberView, numberView2 ->
                MyFoodData.deleteMyFoodDataNumber(foodId)
                Toast.makeText(requireContext(), "[$foodId] 수량 -1", Toast.LENGTH_SHORT).show()
                val number = MyFoodData.getMyFoodDataNumberfromFoodId(foodId)
                numberView.text = "$number 개"
                numberView2.text = "$number"
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
                    foodId, numberView, numberView2 ->
                MyFoodData.addMyFoodDataNumber(foodId)
                Toast.makeText(requireContext(), "[$foodId] 수량 +1", Toast.LENGTH_SHORT).show()
                val number = MyFoodData.getMyFoodDataNumberfromFoodId(foodId)
                numberView.text = "$number 개"
                numberView2.text = "$number"

            },
            onMinusClick = {
                    foodId, numberView, numberView2 ->
                MyFoodData.deleteMyFoodDataNumber(foodId)
                Toast.makeText(requireContext(), "[$foodId] 수량 -1", Toast.LENGTH_SHORT).show()
                val number = MyFoodData.getMyFoodDataNumberfromFoodId(foodId)
                numberView.text = "$number 개"
                numberView2.text = "$number"
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
                    foodId, numberView, numberView2 ->
                MyFoodData.addMyFoodDataNumber(foodId)
                Toast.makeText(requireContext(), "[$foodId] 수량 +1", Toast.LENGTH_SHORT).show()
                val number = MyFoodData.getMyFoodDataNumberfromFoodId(foodId)
                numberView.text = "$number 개"
                numberView2.text = "$number"

            },
            onMinusClick = {
                    foodId, numberView, numberView2 ->
                MyFoodData.deleteMyFoodDataNumber(foodId)
                Toast.makeText(requireContext(), "[$foodId] 수량 -1", Toast.LENGTH_SHORT).show()
                val number = MyFoodData.getMyFoodDataNumberfromFoodId(foodId)
                numberView.text = "$number 개"
                numberView2.text = "$number"
            }
        )

        refreshAdapters(
            recyclerView_vegatable,
            recyclerView_meat,
            recyclerView_dairy,
            recyclerView_sauce,
            recyclerView_etc
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
        groupTitle: String
    ): List<ListItem> {
        val myFoodIds = MyFoodData.getMyFoodDataAllItems()
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

    private fun refreshAdapters(
        recyclerView_vegatable: RecyclerView,
        recyclerView_meat: RecyclerView,
        recyclerView_dairy: RecyclerView,
        recyclerView_sauce: RecyclerView,
        recyclerView_etc: RecyclerView
    ) {
        val foodDataList = FoodData.getFoodDataList(requireContext())

        fun createAdapter(category: String): FoodBankAdapter {
            val sectionedList = prepareSectionedList_with_MyFood(foodDataList, category)
            return FoodBankAdapter(
                sectionedList,
                requireContext(),
                onItemClick = { /* 필요 시 구현 */ },
                onLocationClick = { /* 필요 시 구현 */ },
                onCalanderClick = { foodId, statusTextView ->
                    showDatePickerDialog { selectedDate ->
                        MyFoodData.addMyFoodDataDueDate(foodId, selectedDate)
                        Toast.makeText(requireContext(), "[$foodId] 날짜 선택됨: $selectedDate", Toast.LENGTH_SHORT).show()
                        statusTextView.text = "유통기한: $selectedDate"
                        // 👉 refresh
                        refreshAdapters(recyclerView_vegatable, recyclerView_meat, recyclerView_dairy, recyclerView_sauce, recyclerView_etc)
                    }
                },
                onPlusClick = { foodId, numberView, numberView2 ->
                    MyFoodData.addMyFoodDataNumber(foodId)
                    Toast.makeText(requireContext(), "[$foodId] 수량 +1", Toast.LENGTH_SHORT).show()
                    val number = MyFoodData.getMyFoodDataNumberfromFoodId(foodId)
                    numberView.text = "$number 개"
                    numberView2.text = "$number"
                    // 👉 refresh
                    refreshAdapters(recyclerView_vegatable, recyclerView_meat, recyclerView_dairy, recyclerView_sauce, recyclerView_etc)
                },
                onMinusClick = { foodId, numberView, numberView2 ->
                    MyFoodData.deleteMyFoodDataNumber(foodId)
                    Toast.makeText(requireContext(), "[$foodId] 수량 -1", Toast.LENGTH_SHORT).show()
                    val number = MyFoodData.getMyFoodDataNumberfromFoodId(foodId)
                    numberView.text = "$number 개"
                    numberView2.text = "$number"
                    // 👉 refresh
                    refreshAdapters(recyclerView_vegatable, recyclerView_meat, recyclerView_dairy, recyclerView_sauce, recyclerView_etc)
                }
            )
        }

        recyclerView_vegatable.adapter = createAdapter("채소")
        recyclerView_meat.adapter = createAdapter("육류와 가공육")
        recyclerView_dairy.adapter = createAdapter("유제품과 가공식품")
        recyclerView_sauce.adapter = createAdapter("양념류")
        recyclerView_etc.adapter = createAdapter("기타")
    }

}