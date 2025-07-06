package com.example.MadCampProj1_ver2.mission

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.graphics.Color
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.MadCampProj1_ver2.R
import com.example.MadCampProj1_ver2.sampledata.MissionDto
import java.time.format.DateTimeFormatter
import java.util.Locale

class MissionAdapter (private var missionDataList: MutableList<MissionDto>,
                      private val onItemClick: (Int) -> Unit) //람다식으로 인자값 받음
    : RecyclerView.Adapter<MissionAdapter.MissionViewHolder>(){

    class MissionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.mission_name)
        val dateTextView: TextView = view.findViewById(R.id.mission_date)
        val percentTextView: TextView = view.findViewById(R.id.mission_percent)
        val categoryView: ImageView = view.findViewById(R.id.mission_category)
        val cardView: CardView = view.findViewById(R.id.mission)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MissionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.mission_component, parent, false)
        return MissionViewHolder(view)
    }

    @SuppressLint("DiscouragedApi", "SetTextI18n")
    override fun onBindViewHolder(holder: MissionViewHolder, position: Int) {
        val data = missionDataList[position]
        val colorList: List<String> = FontColorList.getFontColorList()

        //날짜 format
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
        val startDateFormatted = data.startDate.format(dateFormatter)
        val endDateFormatted = data.endDate.format(dateFormatter)
        //사진 링크
        val link = "mission_${data.category}"
        val context = holder.categoryView.context
        val resId = context.resources.getIdentifier(link, "drawable", context.packageName)

        if (resId != 0) {
            holder.categoryView.setImageResource(resId)
        } else {
            holder.categoryView.setImageResource(R.drawable.mission_1) // 기본 이미지
        }

        holder.nameTextView.text = data.name
        holder.dateTextView.text = "인증 기간: $startDateFormatted ~ $endDateFormatted"
        holder.percentTextView.text = "달성률\n${data.percent}%"
        holder.percentTextView.setTextColor(Color.parseColor(colorList[data.category - 1]))

        holder.cardView.setOnClickListener {
            onItemClick(data.id)
        }
    }

    // 아이템 개수 반환
    override fun getItemCount(): Int = missionDataList.size

    // 데이터 업데이트 메서드
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newMissionList: List<MissionDto>) {
        missionDataList.clear()
        missionDataList.addAll(newMissionList)
        notifyDataSetChanged()
    }
}