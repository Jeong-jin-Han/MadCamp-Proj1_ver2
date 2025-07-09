package com.example.MadCampProj1_ver2.foodmap

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.MadCampProj1_ver2.R
import com.example.MadCampProj1_ver2.map.MapAdapter
import com.example.MadCampProj1_ver2.myfoodmembercheckeddata.MyFoodCheckedMemberData
import com.example.MadCampProj1_ver2.myfoodmemberdata.MyFoodMemberData
import com.example.MadCampProj1_ver2.myfoodmemberdata.MyFoodMemberDto
import com.example.MadCampProj1_ver2.sampledata.CVDto
import com.example.MadCampProj1_ver2.sampledata.MemberDto

class FoodMapAdapter (
    private var foodmemberList: List<MyFoodMemberDto>,
    private val context: Context,
//    private val onItemClick: (Int) -> Unit) //람다식으로 인자값 받음
    private val onFridgeClick: (Int) -> Unit,
    private val onLocationClick: (Int) -> Unit,
    private val onCheckClick: (Int) -> Unit)
    : RecyclerView.Adapter<FoodMapAdapter.MapViewHolder>(){

    private var previousClick: Int = -1 // 클릭 상태 변수

    class MapViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.phone_component_name_ver3)
//        val statusTextView: TextView = view.findViewById(R.id.phone_component_status_ver3)
        val imageView: ImageView = view.findViewById(R.id.phone_component_image_ver3)

        val moreView: LinearLayout = view.findViewById(R.id.phone_component_more_ver3)
        val cardView: CardView = view.findViewById(R.id.phone_ver3)
        val frameView: FrameLayout = view.findViewById(R.id.phone_frame_ver3)
        val lineView: View = view.findViewById(R.id.linetop_ver3)
        // 아이콘들 추가 연결
        val fridgeBtn: ImageView = view.findViewById(R.id.member_fridge)
        val locationBtn: ImageView = view.findViewById(R.id.member_location)
        val messageBtn: ImageView = view.findViewById(R.id.member_message)
        val checkBtn: ImageView = view.findViewById(R.id.member_checkbox)

        fun bind(
            member: MyFoodMemberDto,
            qualification: String,
            context: Context,
            onFridgeClick: (Int) -> Unit,
            onLocationClick: (Int) -> Unit,
            onCheckClick: (Int) -> Unit,

            isExpanded: Boolean,
            isChecked: Boolean, // ✅ 추가
            onCardClick: (Int) -> Unit,
        ) {
            nameTextView.text = member.name
//            statusTextView.text = qualification
            imageView.setImageResource(member.imgPath)

            cardView.radius = 50f
            val layoutParams = cardView.layoutParams
            layoutParams.width = (200 * context.resources.displayMetrics.density).toInt()
            cardView.layoutParams = layoutParams

            moreView.visibility = if (isExpanded) View.VISIBLE else View.GONE

            // 클릭 시 더보기 토글
            cardView.setOnClickListener {
                onCardClick(adapterPosition)
            }

            // 각 버튼 클릭 이벤트
            fridgeBtn.setOnClickListener { onFridgeClick(member.memberId) }

            locationBtn.setOnClickListener { onLocationClick(member.memberId) }

            messageBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("smsto:${member.phone}")
                }
                it.context.startActivity(intent)
            }

//            checkBtn.setOnClickListener { onCheckClick(member.memberId) }

            // ✅ 최초 체크 상태에 따라 아이콘 설정
//            val isChecked = MyFoodCheckedMemberData.isChecked(member.memberId)
            checkBtn.setImageResource(
                if (isChecked) R.drawable.checkbox_selected else R.drawable.checkbox_unselected
            )


            checkBtn.setOnClickListener {
                val memberId = member.memberId
//                isChecked = !isChecked // 임시방편 memberId 별로 기억을해서 각각에 대해서 적용이 되게 만들어주기
                val isChecked = MyFoodCheckedMemberData.toggleChecked(memberId)

                // 이미지 변경
                if (isChecked) {
                    checkBtn.setImageResource(R.drawable.checkbox_selected)
                } else {
                    checkBtn.setImageResource(R.drawable.checkbox_unselected)
                }
                // ✅ 반드시 호출해야 adapter 갱신됨!
                onCheckClick(memberId)
//                notifyDataSetChanged()  // RecyclerView 갱신
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapViewHolder {
//        TODO("Not yet implemented")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.phone_component_ver3, parent, false)

        return MapViewHolder(view)
    }

    override fun onBindViewHolder(holder: MapViewHolder, position: Int) {
        val member = foodmemberList[position]
        val cvDataList: List<CVDto> = CVData.getCVDataList(context)
        val cv = cvDataList.find { it.memberId == member.memberId }
        val qualification = cv?.qualification ?: "정보 없음"

        val isExpanded = position == previousClick
        val isChecked = MyFoodCheckedMemberData.isChecked(member.memberId)

        holder.bind(
            member = member,
            qualification = qualification,
            context = context,
            onFridgeClick = onFridgeClick,
            onLocationClick = onLocationClick,
            isExpanded = isExpanded,
            isChecked = isChecked,
            onCheckClick = onCheckClick,
            onCardClick = { clickedPosition ->
                if (previousClick != -1 && previousClick != clickedPosition) {
                    notifyItemChanged(previousClick)
                }
                previousClick = if (previousClick == clickedPosition) -1 else clickedPosition
                notifyItemChanged(clickedPosition)
            }
        )
    }

    fun dpToPx(context: Context, dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density).toInt()
    }

    override fun getItemCount(): Int {
//        TODO("Not yet implemented")
        return foodmemberList.size
    }

    fun updateData(newList: List<MyFoodMemberDto>) {
        Log.d("FoodMapAdapter", "Updating data. New size: ${newList.size}")
        foodmemberList = newList
        notifyDataSetChanged()
    }
}