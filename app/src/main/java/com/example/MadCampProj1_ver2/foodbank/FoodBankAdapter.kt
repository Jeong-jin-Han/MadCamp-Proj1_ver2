package com.example.MadCampProj1_ver2.foodbank

import CVData
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
import com.example.MadCampProj1_ver2.myfooddata.MyFoodData
import com.example.MadCampProj1_ver2.phone.ContactViewHolder
import com.example.MadCampProj1_ver2.phone.HeaderViewHolder
import com.example.MadCampProj1_ver2.sampledata.MemberDto
import com.example.MadCampProj1_ver2.samplefooddata.FoodDto

sealed class ListItem {
    data class Header(val title: String) : ListItem()
//    data class Contact(val member: MemberDto, val qualification: String) : ListItem()
    data class Contact(val food: FoodDto, val qualification: String) : ListItem()

}

//import com.example.MadCampProj1_ver2.phone.ListItem

class FoodBankAdapter(
    private var sectionedList: List<ListItem>,
    private val context: Context,
    private val onItemClick: (Int) -> Unit,
    private val onLocationClick: (Int) -> Unit,
    private val onCalanderClick: (Int, TextView) -> Unit,
    private val onPlusClick: (Int, TextView) -> Unit,
    private val onMinusClick: (Int, TextView) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_CONTACT = 1
    }

    private var previousClick: Int = -1 // 클릭 상태 변수

    override fun getItemViewType(position: Int): Int {
        return when (sectionedList[position]) {
            is ListItem.Header -> VIEW_TYPE_HEADER
            is ListItem.Contact -> VIEW_TYPE_CONTACT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        TODO("Not yet implemented")
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_section_header, parent, false)
                HeaderViewHolder(view)
            }
            VIEW_TYPE_CONTACT -> {
                val view = LayoutInflater.from(parent.context)
//                    .inflate(R.layout.phone_component, parent, false)
                    .inflate(R.layout.phone_component_ver2, parent, false)
                ContactViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("FoodBankAdapter", "onBindViewHolder position=$position, item=${sectionedList[position]}")

        when (val item = sectionedList[position]) {
            is ListItem.Header -> {
                Log.d("PhoneAdapter", "Binding Header: ${item.title}")

                (holder as HeaderViewHolder).bind(item)
            }
            is ListItem.Contact -> {
                val isExpanded = position == previousClick
                val isFirstInSection = (position > 0 && sectionedList[position - 1] is ListItem.Header) || (position==0 && !(sectionedList[position] is ListItem.Header))
                val isLastInSection = when {
                    position == sectionedList.size -1 -> true // 리스트의 마지막 요소인 경우
                    sectionedList[position + 1] is ListItem.Header -> true // 다음 아이템의 헤더인 경우
                    else -> false // 그 외
                }

                Log.d("hello", isFirstInSection.toString())
                Log.d("hello", isLastInSection.toString())
                Log.d("hello", isExpanded.toString())
                Log.d("hello", position.toString())
                Log.d("hello", previousClick.toString())

                (holder as ContactViewHolder).bind(
                    item.food, item.qualification, onItemClick, onLocationClick, onCalanderClick, onPlusClick, onMinusClick, isExpanded, onCardClick = {
                        clickedPosition ->
                        if (previousClick != -1 && previousClick != clickedPosition) {
                            notifyItemChanged(previousClick)
                        }
                        previousClick = if (previousClick == clickedPosition) - 1 else clickedPosition
                        notifyItemChanged(clickedPosition)
                    },
                    isFirstInSection = isFirstInSection,
                    isLastInSection = isLastInSection,
                    context
                )


            }
        }
    }

    override fun getItemCount(): Int {
//        TODO("Not yet implemented")
        return sectionedList.size
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val headerTextView: TextView = view.findViewById(R.id.headerTextView)

        fun bind(header: ListItem.Header) {
            headerTextView.text = header.title
        }
    }

    fun updateData(newData: List<ListItem>) {
        Log.d("FoodBankAdapter", "updateData 호출됨 - 아이템 수: ${newData.size}")
        sectionedList = newData
        notifyDataSetChanged()
    }

    @Suppress("DEPRECATION")
    class ContactViewHolder(
        view: View,
    )  : RecyclerView.ViewHolder(view) {
        private val nameTextView: TextView = view.findViewById(R.id.phone_component_name_ver2)
        private val statusTextView: TextView = view.findViewById(R.id.phone_component_status_ver2)
        private val imageView: ImageView = view.findViewById(R.id.phone_component_image_ver2)
        private val callView: ImageView = view.findViewById(R.id.phone_call_ver2)
        private val messageView: ImageView = view.findViewById(R.id.phone_message_ver2)
        private val infoView: ImageView = view.findViewById(R.id.phone_info_ver2)
        private val locationView: ImageView = view.findViewById(R.id.phone_location_ver2)
        private val moreView: LinearLayout = view.findViewById(R.id.phone_component_more_ver2)
        private val cardView: CardView = view.findViewById(R.id.phone_ver2)
        private val frameView: FrameLayout = view.findViewById(R.id.phone_frame_ver2)
        private val lineView: View = view.findViewById(R.id.linetop_ver2)
        private val numberTextView: TextView = view.findViewById(R.id.phone_component_name_2_ver2)
        private val numberView: TextView = view.findViewById(R.id.phone_component_status_2_ver2)

//        fun bind(
//            member: MemberDto,
//            qualification: String,
//            onItemClick: (Int) -> Unit,
//            onLocationClick: (Int) -> Unit,
//            isExpanded: Boolean,
//            onCardClick: (Int) -> Unit,
//            isFirstInSection: Boolean,
//            isLastInSection: Boolean,
//            context: Context
//        ) {
//            nameTextView.text = member.name
//            for (cv in CVData.getCVDataList(context)) {
//                if (cv.memberId == member.memberId) {
//                    statusTextView.text = cv.studentID
//                    break
//                }
//            }
//            imageView.setImageResource(member.imgPath)
//            // View 초기화
//
//
//            frameView.setBackgroundResource(R.color.background)
//            cardView.setCardBackgroundColor(cardView.context.getColor(R.color.background)) // 기본 색상
//            lineView.visibility = View.GONE // 기본적으로 숨김 처리
//
//            when {
//                isFirstInSection && isLastInSection -> {
//                    // 둘 다 true일 때 처리
//                    frameView.setBackgroundResource(R.drawable.rounded_card_background)
//                    lineView.visibility = View.GONE
//                }
//                isFirstInSection -> {
//                    frameView.setBackgroundResource(R.drawable.rounded_card_background_top)
//                    lineView.visibility = View.GONE
//                }
//                isLastInSection -> {
//                    frameView.setBackgroundResource(R.drawable.rounded_card_background_bottom)
//                    lineView.visibility = View.VISIBLE
//                }
//                else -> {
//                    Log.d("hello", "It is in the middle")
//                    frameView.setBackgroundResource(R.drawable.rounded_card_background_middle)
//                    cardView.setCardBackgroundColor(cardView.context.getColor(R.color.background2))
//                    lineView.visibility = View.VISIBLE
//                }
//            }
//
//            // "더보기" 상태 반영
//            moreView.visibility = if (isExpanded) View.VISIBLE else View.GONE
//
//            cardView.setOnClickListener {
//
//                onCardClick(adapterPosition) // 클릭된 위치 전달
//            }
//
////            // 전화 걸기
////            callView.setOnClickListener {
////                val intent = Intent(Intent.ACTION_DIAL).apply {
////                    data = Uri.parse("tel:${member.phone}")
////                }
////                it.context.startActivity(intent)
////            }
////            // 메시지 보내기
////            messageView.setOnClickListener {
////                val intent = Intent(Intent.ACTION_SENDTO).apply {
////                    data = Uri.parse("smsto:${member.phone}")
////                }
////                it.context.startActivity(intent)
////            }
////            // 정보 보기
////            infoView.setOnClickListener {
////                onItemClick(member.memberId)
////            }
////            // 위치 보기
////            locationView.setOnClickListener {
////                onLocationClick(member.memberId)
////            }
//        }

        fun bind(
            food: FoodDto,
            qualification: String,
            onItemClick: (Int) -> Unit,
            onLocationClick: (Int) -> Unit,
            onCalanderClick: (Int, TextView) -> Unit,
            onPlusClick: (Int, TextView) -> Unit,
            onMinusClick: (Int, TextView) -> Unit,

            isExpanded: Boolean,
            onCardClick: (Int) -> Unit,
            isFirstInSection: Boolean,
            isLastInSection: Boolean,
            context: Context
        ) {
            nameTextView.text = food.name
            numberTextView.text = "개수"
//            for (cv in CVData.getCVDataList(context)) {
//                if (cv.foodId == food.foodId) {
//                    statusTextView.text = cv.studentID
//                    break
//                }
//            }
            val matched = MyFoodData.getMyFoodDataAllItems().find { it.foodId == food.foodId }

            if (matched != null) {
                statusTextView.text = matched.foodDuedate
                numberView.text = "${matched.foodNumber} 개"
            } else {
                statusTextView.text = "유통기한: 미입력"
                numberView.text = "0 개"
            }

            imageView.setImageResource(food.imgPath)
            // View 초기화


            frameView.setBackgroundResource(R.color.background)
            cardView.setCardBackgroundColor(cardView.context.getColor(R.color.background)) // 기본 색상
            lineView.visibility = View.GONE // 기본적으로 숨김 처리

            when {
                isFirstInSection && isLastInSection -> {
                    // 둘 다 true일 때 처리
                    frameView.setBackgroundResource(R.drawable.rounded_card_background)
                    lineView.visibility = View.GONE
                }
                isFirstInSection -> {
                    frameView.setBackgroundResource(R.drawable.rounded_card_background_top)
                    lineView.visibility = View.GONE
                }
                isLastInSection -> {
                    frameView.setBackgroundResource(R.drawable.rounded_card_background_bottom)
                    lineView.visibility = View.VISIBLE
                }
                else -> {
                    Log.d("hello", "It is in the middle")
                    frameView.setBackgroundResource(R.drawable.rounded_card_background_middle)
                    cardView.setCardBackgroundColor(cardView.context.getColor(R.color.background2))
                    lineView.visibility = View.VISIBLE
                }
            }

            // "더보기" 상태 반영
            moreView.visibility = if (isExpanded) View.VISIBLE else View.GONE

            cardView.setOnClickListener {

                onCardClick(adapterPosition) // 클릭된 위치 전달
            }

            // 전화 걸기
//            callView.setOnClickListener {
//                val intent = Intent(Intent.ACTION_DIAL).apply {
//                    data = Uri.parse("tel:${food.imgPath}")
//                }
//                it.context.startActivity(intent)
//            }
            callView.setOnClickListener {
                onCalanderClick(food.foodId, statusTextView)  // 또는 food.foodId 등
//                statusTextView.text = "유통기한: 미입력"
            }
//            // 메시지 보내기
//            messageView.setOnClickListener {
//                val intent = Intent(Intent.ACTION_SENDTO).apply {
//                    data = Uri.parse("smsto:${member.phone}")
//                }
//                it.context.startActivity(intent)
//            }
            messageView.setOnClickListener {
                Log.d("FoodBankDebug", "Plus clicked for ${food.foodId}")
                onPlusClick(food.foodId, numberView)
            }

//            // 정보 보기
//            infoView.setOnClickListener {
//                onItemClick(member.memberId)
//            }
            infoView.setOnClickListener {
                onMinusClick(food.foodId, numberView)
            }

//            // 위치 보기
//            locationView.setOnClickListener {
//                onLocationClick(member.memberId)
//            }
        }

    }
}