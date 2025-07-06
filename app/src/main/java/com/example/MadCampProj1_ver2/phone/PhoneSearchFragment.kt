package com.example.MadCampProj1_ver2.gallery

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.MadCampProj1_ver2.phone.ListItem
import com.example.MadCampProj1_ver2.phone.PhoneAdapter
import com.example.MadCampProj1_ver2.sampledata.CVDto
import com.example.MadCampProj1_ver2.sampledata.MemberData
import com.example.MadCampProj1_ver2.sampledata.MemberDto

@Suppress("DEPRECATION")
class PhoneSearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PhoneAdapter
    private lateinit var originalData: List<ListItem>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_phone_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val memberDataList: List<MemberDto> = MemberData.getPhoneDataList(requireContext())

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

        // Load data and set up RecyclerView
        originalData = prepareSectionedList(MemberData.getPhoneDataList(requireContext()), CVData.getCVDataList())
        val initialData = listOf<ListItem>()
        adapter = PhoneAdapter(initialData,
            onItemClick = { id ->
                val fragment = PhoneDetailFragment().apply {
                    arguments = Bundle().apply {
                        putInt("id", id)
                    }
                }
                // Handle item click (e.g., navigate to detail view)
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
            },
            onLocationClick = { id ->
                // Handle location click (e.g., open map view)
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
            }
        )


        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        // Add TextWatcher to filter data
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                filterData(query)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterData(query: String) {
        val filteredList = mutableListOf<ListItem>()
        if (query.isNotEmpty()) {
            // Filter contacts while keeping headers
            var currentHeader: ListItem.Header? = null
            for (item in originalData) {
                when (item) {
                    is ListItem.Header -> {
                        currentHeader = item
                    }

                    is ListItem.Contact -> {
                        if (item.member.name.contains(query, ignoreCase = true)) {
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
