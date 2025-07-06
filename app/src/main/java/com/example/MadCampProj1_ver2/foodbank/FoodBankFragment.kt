import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.MadCampProj1_ver2.R
import com.example.MadCampProj1_ver2.map.MapFragment
import com.example.MadCampProj1_ver2.mypage.MypageFragment
import com.example.MadCampProj1_ver2.notification.NotificationFragment
import com.example.MadCampProj1_ver2.phone.ListItem
import com.example.MadCampProj1_ver2.sampledata.CVDto
import com.example.MadCampProj1_ver2.sampledata.MemberData
import com.example.MadCampProj1_ver2.sampledata.MemberDto
import com.example.MadCampProj1_ver2.phone.PhoneAdapter
import com.example.MadCampProj1_ver2.sampledata.NotificationData

class FoodBankFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_phone_ver2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.phone_recycler_view_ver2)
        val memberDataList: List<MemberDto> = MemberData.getPhoneDataList(requireContext())
        val cvDataList: List<CVDto> = CVData.getCVDataList(requireContext())

        // 섹션화된 데잍 ㅓ준비

        val sectionedList = prepareSectionedList(memberDataList, cvDataList)
        val notificationNumber = view.findViewById<TextView>(R.id.notificationNumber_ver2)
        notificationNumber.text = NotificationData.getCheckdNotificationDataList(requireContext()).toString()

    }
    fun prepareSectionedList(memberList: List<MemberDto>, cvList: List<CVDto>): List<ListItem> {
        val groupedData = memberList.mapNotNull { member ->
            val cv = cvList.find {it.memberId == member.memberId} // CVDto 객체
            cv?.let {member to it} // member 와 cv 즉 (CVDto 객체) 쌍을 반환
        }.groupBy { it.second.qualification } // cv 객체의 second feature 로 group화 진행

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
