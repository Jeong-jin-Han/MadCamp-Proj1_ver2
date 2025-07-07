package com.example.MadCampProj1_ver2.foodbank

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.MadCampProj1_ver2.R
import com.example.MadCampProj1_ver2.map.MapFragment
//import com.example.MadCampProj1_ver2.phone.ListItem
import com.example.MadCampProj1_ver2.foodbank.ListItem

import com.example.MadCampProj1_ver2.phone.PhoneDetailFragment

import com.example.MadCampProj1_ver2.sampledata.CVDto
import com.example.MadCampProj1_ver2.sampledata.MemberData
import com.example.MadCampProj1_ver2.sampledata.MemberDto
import com.example.MadCampProj1_ver2.samplefooddata.FoodData
import com.example.MadCampProj1_ver2.samplefooddata.FoodDto

@Suppress("DEPRECATION")
class FoodBankSearchFragment : Fragment(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FoodBankAdapter
    private lateinit var originalData: List<ListItem>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_phone_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val memberDataList: List<MemberDto> = MemberData.getPhoneDataList(requireContext())

        val foodDataList: List<FoodDto> = FoodData.getFoodDataList(requireContext())

        val searchEditText = view.findViewById<EditText>(R.id.titleEditText)
        searchEditText.requestFocus()

        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT)

        val backArrow = view.findViewById<ImageView>(R.id.top_bar_arrow)
        backArrow.visibility = View.VISIBLE

        backArrow.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        recyclerView = view.findViewById(R.id.search_recycler_view)

        // load dataa and set up RecyclerView
//        originalData = prepareSectionedList(MemberData.getPhoneDataList(requireContext()), CVData.getCVDataList(requireContext()))
        originalData = prepareSectionedList(FoodData.getFoodDataList(requireContext()))
        Log.d("FoodBankDebug", "originalData loaded: size=${originalData.size}")

        val initialData = listOf<ListItem>()
        adapter = FoodBankAdapter(initialData,
            onItemClick = {
                    id ->
//                val fragment = FoodBankDetailFragment().apply {
//                    arguments = Bundle().apply {
//                        putInt("id", id)
//                    }
//                }
//
//                //  Handle item click (e.g. navigate to detail view)
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
            context = requireContext(),
            onLocationClick = {
                    id ->
                // Handle location click (e.g. open map view)
//                val member = memberDataList.find {it.memberId == id}


//                if (member != null) {
//                    val fragment = MapFragment().apply {
//                        arguments = Bundle().apply {
//                            putDouble("lat", member.lat)
//                            putDouble("lng", member.lng)
//                            putInt("memberId", member.memberId)
//                        }
//                    }
//                    requireActivity().supportFragmentManager.beginTransaction()
//                        .replace(R.id.content_frame_ver2, fragment)
//                        .addToBackStack(null)
//                        .commit()
//                }

            },
            onCalanderClick = {
                id ->
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        Log.d("FoodBankDebug", "RecyclerView adapter set")

        // Add TextWatcher to filter data
        searchEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                TODO("Not yet implemented")
                val query = s.toString()
                filterData(query)
            }

            override fun afterTextChanged(s: Editable?) {
//                TODO("Not yet implemented")
            }
        })

    }

    private fun filterData(query: String) {
        val filteredList = mutableListOf<ListItem>()
        if (query.isNotEmpty()) {
            // Filter contacts while keeping header
            var currentHeader: ListItem.Header? = null
            for (item in originalData) {
                when (item) {
                    is ListItem.Header -> {
                        currentHeader = item
                    }
                    is ListItem.Contact -> {
                        if (item.food.name.contains(query, ignoreCase = true)) {
                            // Add header before adding the first matching contact under it

                            if (currentHeader != null && !filteredList.contains(currentHeader)) {
                                filteredList.add(currentHeader)
                            }

                            filteredList.add(item)
                        }
                    }
                }
            }
        }
        adapter.updateData(filteredList)
    }


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