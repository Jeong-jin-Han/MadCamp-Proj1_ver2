package com.example.MadCampProj1_ver2.map

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.MadCampProj1_ver2.R
import com.example.MadCampProj1_ver2.sampledata.CVDto
import com.example.MadCampProj1_ver2.sampledata.MemberDto

class MapAdapter (private var memberDataList: List<MemberDto>,
                      private val onItemClick: (Int) -> Unit) //람다식으로 인자값 받음
    : RecyclerView.Adapter<MapAdapter.MapViewHolder>(){

    class MapViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.phone_component_name)
        val statusTextView: TextView = view.findViewById(R.id.phone_component_status)
        val imageView: ImageView = view.findViewById(R.id.phone_component_image)
        val cardView: CardView = view.findViewById(R.id.phone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.phone_component, parent, false)
        return MapViewHolder(view)
    }

    override fun onBindViewHolder(holder: MapViewHolder, position: Int) {
        val member = memberDataList[position]
        val cvDataList: List<CVDto> = CVData.getCVDataList()
        val cv = cvDataList.find {it.memberId == member.memberId}
        if(cv != null){
            holder.nameTextView.text = member.name
            holder.statusTextView.text = cv.qualification
            holder.imageView.setImageResource(member.imgPath)
        }
        holder.cardView.radius = 50f
        //길이 변경
        val layoutParams = holder.cardView.layoutParams
        layoutParams.width = dpToPx(holder.cardView.context, 200)
        holder.cardView.layoutParams = layoutParams

        // 클릭 이벤트 설정
        holder.cardView.setOnClickListener {
            onItemClick(member.memberId) // 클릭된 아이템의 memberId를 전달
        }
    }

    fun dpToPx(context: Context, dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    // 아이템 개수 반환
    override fun getItemCount(): Int = memberDataList.size
}