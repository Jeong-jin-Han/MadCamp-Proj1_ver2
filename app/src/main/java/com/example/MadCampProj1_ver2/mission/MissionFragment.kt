package com.example.MadCampProj1_ver2.mission

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.MadCampProj1_ver2.R
import com.example.MadCampProj1_ver2.sampledata.MissionData
import com.example.MadCampProj1_ver2.sampledata.MissionDto

@Suppress("DEPRECATION")
class MissionFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_mission, container, false)
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val topBarTextView = view.findViewById<TextView>(R.id.top_bar_text)
        topBarTextView.text = "도전 과제"

        val recyclerView: RecyclerView = view.findViewById(R.id.mission_recycler_view)
        val missionList: List<MissionDto> = MissionData.getMissionDataList()

        val progressView: LinearLayout = view.findViewById(R.id.progress_btn)
        val layoutParams = progressView.layoutParams as FrameLayout.LayoutParams
        val inProgressText: TextView = view.findViewById(R.id.btn_in_progress)
        val completeText: TextView = view.findViewById(R.id.btn_completed)
        val uploadButton: Button = view.findViewById(R.id.btn_add)
        //상단바
        val filteredList = if (layoutParams.gravity == Gravity.START) {
            missionList.filter { it.isDone == false }
        } else {
            missionList.filter { it.isDone == true }
        }
        inProgressText.setOnClickListener{
            if(layoutParams.gravity == Gravity.END){
                layoutParams.gravity = Gravity.START
                inProgressText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                completeText.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_color2))
                progressView.layoutParams = layoutParams
                updateRecyclerView(recyclerView, missionList.filter { !it.isDone }) // 미완료 필터링
            }
        }
        completeText.setOnClickListener{
            if(layoutParams.gravity == Gravity.START){
                layoutParams.gravity = Gravity.END
                inProgressText.setTextColor(ContextCompat.getColor(requireContext(), R.color.main_color2))
                completeText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                progressView.layoutParams = layoutParams
                updateRecyclerView(recyclerView, missionList.filter { it.isDone })
            }
        }

        // 미션 업로드 이벤트 설정
        uploadButton.setOnClickListener {
            val fragment = MissionUploadFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame_ver2, fragment)
                .addToBackStack(null)
                .commit()
        }

        // RecyclerView 설정
        recyclerView.layoutManager = LinearLayoutManager(activity)  // 아이템 세로로 나열
        recyclerView.adapter = MissionAdapter(filteredList.toMutableList()) { id ->
            // onItemClick 이벤트 처리
            val fragment = MissionCompleteFragment().apply {
                arguments = Bundle().apply {
                    putInt("missionId", id)
                }
            }
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame_ver2, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    // RecyclerView 업데이트
    private fun updateRecyclerView(recyclerView: RecyclerView, filteredList: List<MissionDto>) {
        val adapter = recyclerView.adapter as MissionAdapter
        adapter.updateData(filteredList)
    }
}