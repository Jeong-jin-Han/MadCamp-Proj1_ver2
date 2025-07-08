package com.example.MadCampProj1_ver2.foodbank

import android.annotation.SuppressLint
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.MadCampProj1_ver2.R
import com.example.MadCampProj1_ver2.sampledata.CVDto
import com.example.MadCampProj1_ver2.sampledata.MemberData
import com.example.MadCampProj1_ver2.sampledata.MemberDto
import com.example.MadCampProj1_ver2.samplefooddata.FoodData
import com.example.MadCampProj1_ver2.samplefooddata.FoodDto

@Suppress("DEPRECATION")
class FoodBankDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_phone_detail, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val memberId = arguments?.getInt("id") ?: -1
        if (memberId != -1) {
            val memberDataList: List<MemberDto> = MemberData.getPhoneDataList(requireContext()) // member data
            val foodDataList: List<FoodDto> = FoodData.getFoodDataList(requireContext()) // food data

            val member = memberDataList.find {it.memberId == memberId}

            if (member != null) {
                val CVDataList: List<CVDto> = CVData.getCVDataList(requireContext()) // cv data
                val cv = CVDataList.find{it.memberId == memberId}
                if (cv != null) {
                    // 상단바 텍스트 변경 및 뒤로가기 버튼 추가
                    val backArrow = view.findViewById<ImageView>(R.id.top_bar_arrow)
                    backArrow.visibility = View.VISIBLE

                    backArrow.setOnClickListener {
                        requireActivity().onBackPressedDispatcher.onBackPressed()
                    }

                    // xml 파일 내용
                    view.findViewById<ImageView>(R.id.phone_detail_image).setImageResource(member.imgPath)
                    view.findViewById<TextView>(R.id.phone_detail_name).text = member.name
                    view.findViewById<TextView>(R.id.phone_detail_qualification).text = cv.qualification
                    view.findViewById<TextView>(R.id.phone_detail_email).text = member.email
                    view.findViewById<TextView>(R.id.phone_detail_phone).text = member.phone
                    view.findViewById<TextView>(R.id.phone_detail_edu).text = cv.edu
                    view.findViewById<TextView>(R.id.phone_detail_work).text = cv.experience

                    // 수정 부분
                    view.findViewById<TextView>(R.id.phone_detail_intro).text = cv.intro
                    view.findViewById<TextView>(R.id.phone_detail_major).text = member.major + " 주전공\n" + member.minor + " 부전공"
                    view.findViewById<TextView>(R.id.phone_detail_birth).text = member.birth
                    view.findViewById<TextView>(R.id.phone_detail_home).text = member.home
                }
            }
        }
    }
}