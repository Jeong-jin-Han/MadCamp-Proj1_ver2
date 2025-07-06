package com.example.MadCampProj1_ver2.sampledata

import com.example.MadCampProj1_ver2.R

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import java.io.InputStreamReader

object MemberData {

    fun getPhoneDataList(context: Context): List<MemberDto> {
        // JSON 파일을 읽어서 문자열로 반환
        val jsonString = loadJSONFromAsset(context)

        // Gson 객체 생성
        val gson = Gson()

        // JSON 배열을 파싱하여 MemberDto 리스트로 변환
        val jsonArray = JSONArray(jsonString)

        val memberList = mutableListOf<MemberDto>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)

            // 각 필드별로 값을 추출하고, imgPath와 imgCirclePath는 String을 리소스 ID로 변환
            val memberId = jsonObject.getInt("memberId")
            val name = jsonObject.getString("name")
            val phone = jsonObject.getString("phone")
            val email = jsonObject.getString("email")
            val lat = jsonObject.getDouble("lat")
            val lng = jsonObject.getDouble("lng")
            val imgPath = jsonObject.getString("imgPath")
            val imgCirclePath = jsonObject.getString("imgCirclePath")
            val major = jsonObject.getString("major")
            val minor = jsonObject.getString("minor")
            val birth = jsonObject.getString("birth")
            val home = jsonObject.getString("home")

            // imgPath와 imgCirclePath를 실제 리소스 ID로 변환
            val imgPathResId = getDrawableResourceId(context, imgPath)
            val imgCirclePathResId = getDrawableResourceId(context, imgCirclePath)

            // MemberDto 객체 생성 후 리스트에 추가
            memberList.add(MemberDto(
                memberId,
                name,
                phone,
                email,
                lat,
                lng,
                imgPathResId,
                imgCirclePathResId,
                major,
                minor,
                birth,
                home
            ))
        }

        return memberList
    }

    // JSON에서 읽어온 String 값을 실제 drawable 리소스 ID로 변환
    private fun getDrawableResourceId(context: Context, resourceName: String): Int {
        // 리소스 이름을 사용하여 실제 리소스 ID를 가져옴
        return context.resources.getIdentifier(resourceName, "drawable", context.packageName)
    }

    // assets 폴더에서 JSON 파일을 읽어오는 메서드
    private fun loadJSONFromAsset(context: Context): String? {
        var json: String? = null
        try {
            // "members.json" 파일을 assets 폴더에서 연다
            val inputStream = context.assets.open("members.json")
            val inputStreamReader = InputStreamReader(inputStream)
            json = inputStreamReader.readText()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return json
    }
}

//object MemberData {
//    fun getPhoneDataList(): List<MemberDto> {
//        return listOf(
//            MemberDto(1, "조성원", "010-0000-0000", "red_road_dong@gmail.com", 37.5665, 126.9780, R.drawable.profile_1, R.drawable.profile_circle_1, "컴퓨터공학", "인공지능", "January 1st, 1990", "Seoul, Korea"),
//            MemberDto(2, "김철수", "010-1234-5678", "cheolsoo_kim@gmail.com", 35.1796, 129.0756, R.drawable.profile_2, R.drawable.profile_circle_2,  "기계공학", "로봇공학", "February 2nd, 1992", "Busan, Korea"),
//            MemberDto(3, "이영희", "010-9876-5432", "younghee_lee@gmail.com", 37.4563, 126.7052, R.drawable.profile_3, R.drawable.profile_circle_3, "경영학", "마케팅", "March 3rd, 1993", "Incheon, Korea"),
//            MemberDto(4, "박지훈", "010-5555-1111", "jihoon_park@gmail.com", 35.1601, 126.8514, R.drawable.profile_4, R.drawable.profile_circle_4, "전기공학", "사물인터넷", "April 4th, 1994", "Gwangju, Korea"),
//            MemberDto(5, "최수민", "010-2222-3333", "sumin_choi@gmail.com", 36.6511, 127.4894, R.drawable.profile_15, R.drawable.profile_circle_15, "토목공학", "도시계획", "May 5th, 1995", "Daejeon, Korea"),
//            MemberDto(6, "정유진", "010-4444-5555", "yujin_jung@gmail.com", 37.8857, 127.7299, R.drawable.profile_16, R.drawable.profile_circle_16, "생명공학", "유전학", "June 6th, 1996", "Seoul, Korea"),
//            MemberDto(7, "오세현", "010-6666-7777", "sehyeon_oh@gmail.com", 34.8825, 128.6217, R.drawable.profile_17, R.drawable.profile_circle_17, "환경공학", "지속가능성", "July 7th, 1997", "Changwon, Korea"),
//            MemberDto(8, "한예진", "010-8888-9999", "yejin_han@gmail.com", 36.5714, 128.5117, R.drawable.profile_18, R.drawable.profile_circle_18, "경제학", "데이터 분석", "August 8th, 1998", "Andong, Korea"),
//            MemberDto(9, "유다인", "010-1212-3434", "dain_yoo@gmail.com", 37.3943, 129.1226, R.drawable.profile_19, R.drawable.profile_circle_19, "물리학", "천체물리학", "September 9th, 1999", "Gangneung, Korea"),
//            MemberDto(10, "강준혁", "010-5656-7878", "junhyuk_kang@gmail.com", 33.5007, 126.5322, R.drawable.profile_20, R.drawable.profile_circle_20, "건축학", "실내디자인", "October 10th, 1990", "Jeju, Korea"),
//            MemberDto(11, "이소현", "010-7878-9090", "sohyun_lee@gmail.com", 37.2980, 127.0384, R.drawable.profile_21, R.drawable.profile_circle_21, "의학", "소아과학", "November 11th, 1991", "Suwon, Korea"),
//            MemberDto(12, "김태연", "010-1414-5252", "taeyeon_kim@gmail.com", 35.5331, 129.3102, R.drawable.profile_22, R.drawable.profile_circle_22, "화학", "유기화학", "December 12th, 1992", "Ulsan, Korea"),
//            MemberDto(13, "노경민", "010-8585-9696", "kyungmin_no@gmail.com", 36.3323, 127.4415, R.drawable.profile_23, R.drawable.profile_circle_23, "법학", "기업법", "January 13th, 1993", "Daejeon, Korea"),
//            MemberDto(14, "윤다은", "010-2323-4545", "da-eun_yoon@gmail.com", 38.2034, 127.0746, R.drawable.profile_24, R.drawable.profile_circle_24, "정치외교학", "국제관계", "February 14th, 1994", "Seoul, Korea"),
//            MemberDto(15, "서지민", "010-3434-5656", "ji_min_seo@gmail.com", 36.8254, 128.6315, R.drawable.profile_25, R.drawable.profile_circle_25, "수학", "통계학", "March 15th, 1995", "Cheongju, Korea"),
//            MemberDto(16, "최민호", "010-4545-6767", "minho_choi@gmail.com", 36.0111, 129.3753, R.drawable.profile_5, R.drawable.profile_circle_5, "화학공학", "공정공학", "April 16th, 1996", "Pohang, Korea"),
//            MemberDto(17, "정하늘", "010-5656-7878", "sky_jung@gmail.com", 35.1277, 128.9815, R.drawable.profile_6, R.drawable.profile_circle_6, "재료공학", "나노기술", "May 17th, 1997", "Busan, Korea"),
//            MemberDto(18, "박소영", "010-6767-8989", "soyoung_park@gmail.com", 34.7604, 127.6622, R.drawable.profile_7, R.drawable.profile_circle_7, "철학", "윤리학", "June 18th, 1998", "Suncheon, Korea"),
//            MemberDto(19, "김도윤", "010-7878-9090", "doyun_kim@gmail.com", 37.4658, 129.1596, R.drawable.profile_8, R.drawable.profile_circle_8, "교육학", "유아교육", "July 19th, 1999", "Gangneung, Korea"),
//            MemberDto(20, "이준서", "010-8989-1010", "junseo_lee@gmail.com", 36.8020, 127.1333, R.drawable.profile_9, R.drawable.profile_circle_9, "예술학", "그래픽디자인", "August 20th, 1990", "Cheonan, Korea"),
//            MemberDto(21, "조유나", "010-1010-1212", "yuna_cho@gmail.com", 36.7188, 127.1489, R.drawable.profile_10, R.drawable.profile_circle_10, "예술학", "영상예술", "November 21st, 1991", "Daejeon, Korea"),
//            MemberDto(22, "권정우", "010-1111-1313", "jungwoo_kwon@gmail.com", 38.3204, 127.4176, R.drawable.profile_26, R.drawable.profile_circle_26, "사회학", "사회복지", "December 22nd, 1992", "Seoul, Korea"),
//            MemberDto(23, "한지훈", "010-1212-1414", "jihoon_han@gmail.com", 37.0410, 127.7481, R.drawable.profile_27, R.drawable.profile_circle_27, "통계학", "확률이론", "January 23rd, 1993", "Suwon, Korea"),
//            MemberDto(24, "김수정", "010-1313-1515", "sujung_kim@gmail.com", 37.4803, 126.8816, R.drawable.profile_28, R.drawable.profile_circle_28, "교육학", "교육심리", "February 24th, 1994", "Incheon, Korea"),
//            MemberDto(25, "오다영", "010-1414-1616", "dayoung_oh@gmail.com", 35.8151, 128.5745, R.drawable.profile_29, R.drawable.profile_circle_29, "경영학", "인적자원관리", "March 25th, 1995", "Daegu, Korea"),
//            MemberDto(26, "이채은", "010-1515-1717", "chaeun_lee@gmail.com", 36.3491, 127.4337, R.drawable.profile_11, R.drawable.profile_circle_11, "심리학", "임상심리", "April 26th, 1996", "Daejeon, Korea"),
//            MemberDto(27, "박민준", "010-1616-1818", "minjun_park@gmail.com", 35.9857, 128.6226, R.drawable.profile_12, R.drawable.profile_circle_12, "전자공학", "디지털회로", "May 27th, 1997", "Gwangju, Korea"),
//            MemberDto(28, "정유빈", "010-1717-1919", "yubin_jung@gmail.com", 37.8472, 126.8915, R.drawable.profile_13, R.drawable.profile_circle_13, "정치학", "국제정치", "June 28th, 1998", "Seoul, Korea"),
//            MemberDto(29, "최서연", "010-1818-2020", "seoyeon_choi@gmail.com", 36.7172, 126.8427, R.drawable.profile_14, R.drawable.profile_circle_14, "생물학", "동물생태학", "July 29th, 1999", "Cheongju, Korea"),
//            MemberDto(30, "김태우", "010-1919-2121", "taewoo_kim@gmail.com", 37.6497, 126.9694, R.drawable.profile_15, R.drawable.profile_circle_15, "화학", "무기화학", "August 30th, 2000", "Seoul, Korea")
//        )
//    }
//}

