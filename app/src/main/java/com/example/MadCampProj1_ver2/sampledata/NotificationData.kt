package com.example.MadCampProj1_ver2.sampledata

import com.example.MadCampProj1_ver2.notification.NotificationType

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import java.io.FileReader
import java.io.FileWriter
import java.io.InputStreamReader
import java.lang.reflect.Type

object NotificationData {

    private const val NOTIFICATION_FILE_NAME = "notifications.json"

    // 메모리에 데이터를 저장할 변수 (앱 실행 중에만 유지)
    private var notificationDataList: MutableList<NotificationDto> = mutableListOf()

    private fun loadJSONFromAsset(context: Context): String? {
        var json: String? = null
        try {
            // cvs.json 파일을 assets 폴더에서 연다.'
            val inputStream = context.assets.open("notifications.json")
            val inputStreamReader = InputStreamReader(inputStream)
            json = inputStreamReader.readText()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return json
    }

    // JSON 데이터를 읽어 리스트로 반환 (파일에서 불러온 후 메모리 내 리스트로 저장)
    fun getNotificationDataList(context: Context): MutableList<NotificationDto> {
        // JSON 파일을 읽어서 문자열로 반환
        val jsonString = loadJSONFromAsset(context)

        // Gson 객체 생성
        val gson = Gson()

        // JSON 배열을 파싱하여 NotificationDto 리스트로 변환
        val jsonArray = JSONArray(jsonString)

        val notificationList = mutableListOf<NotificationDto>()

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)

            // 각 필드별로 값을 추출하고, 필요한 타입으로 변환
            val id = jsonObject.getInt("id")
            val typeString = jsonObject.getString("type")  // NotificationType의 이름을 문자열로 받음
            val message = jsonObject.getString("message")
            val targetId = jsonObject.getInt("targetId")
            val clicked = jsonObject.getBoolean("clicked")

            // 문자열을 NotificationType Enum으로 변환
            val type = NotificationType.valueOf(typeString)

            // NotificationDto 객체 생성 후 리스트에 추가
            notificationList.add(NotificationDto(
                id,
                type,
                message,
                targetId,
                clicked
            ))
        }

        return notificationList
    }

    // 클릭되지 않은 알림의 개수를 반환
    fun getCheckdNotificationDataList(context: Context): Int {
        val notificationList = getNotificationDataList(context)
        var cnt = 0
        for (l in notificationList) {
            if (!l.clicked) cnt += 1
        }
        return cnt
    }

    // 새로운 알림 아이템 추가
    fun addNotificationItem(context: Context, newMissionItem: NotificationDto) {
        val notificationList = getNotificationDataList(context)
        notificationList.add(0, newMissionItem)  // 아이템을 리스트의 맨 앞에 추가
        notificationDataList = notificationList // 메모리 내 리스트 업데이트
    }
}




//object NotificationData {
//    private val NotificationDataList : MutableList<NotificationDto> =
//        mutableListOf(
//            NotificationDto(1, NotificationType.MISSION_COMPLETE, "도전과제를 완료하였습니다", 1, false),
//            NotificationDto(2, NotificationType.GALLERY_POST, "게시물을 업로드하였습니다", 9, false),
//            NotificationDto(3, NotificationType.MAP_LOCATION, "이동하였습니다", 18, false),
//            NotificationDto(4, NotificationType.GALLERY_POST, "게시물을 업로드하였습니다", 123, false),
//            NotificationDto(5, NotificationType.MISSION_COMPLETE, "도전과제를 완료하였습니다", 2, false),
//            NotificationDto(6, NotificationType.MAP_LOCATION, "이동하였습니다", 6, false),
//            NotificationDto(7, NotificationType.GALLERY_POST, "게시물을 업로드하였습니다", 10, false),
//            NotificationDto(8, NotificationType.MAP_LOCATION, "이동하였습니다", 2, false),
//            NotificationDto(9, NotificationType.MISSION_COMPLETE, "도전과제를 완료하였습니다", 3, false),
//            NotificationDto(10, NotificationType.MAP_LOCATION, "이동하였습니다", 1, false),
//            NotificationDto(11, NotificationType.GALLERY_POST, "게시물을 업로드하였습니다", 45, false),
//            NotificationDto(12, NotificationType.MISSION_COMPLETE, "도전과제를 완료하였습니다", 4, false),
//            NotificationDto(13, NotificationType.MAP_LOCATION, "이동하였습니다", 14, false),
//            NotificationDto(14, NotificationType.GALLERY_POST, "게시물을 업로드하였습니다", 19, false),
//            NotificationDto(15, NotificationType.MISSION_COMPLETE, "도전과제를 완료하였습니다", 6, false),
//            NotificationDto(16, NotificationType.GALLERY_POST, "게시물을 업로드하였습니다", 23, false),
//            NotificationDto(17, NotificationType.MAP_LOCATION, "이동하였습니다", 5, false),
//            NotificationDto(18, NotificationType.GALLERY_POST, "게시물을 업로드하였습니다", 67, false),
//            NotificationDto(19, NotificationType.MISSION_COMPLETE, "도전과제를 완료하였습니다", 7, false),
//            NotificationDto(20, NotificationType.MAP_LOCATION, "이동하였습니다", 16, false)
//        )
//    fun getNotificationDataList(): MutableList<NotificationDto> {
//        return NotificationDataList
//    }
//
//    fun getCheckdNotificationDataList(): Int {
//        var cnt: Int = 0
//        for (l in NotificationDataList) {
//            if (l.clicked == false) cnt += 1
//        }
//        return cnt
//    }
//
//    fun addNotificationItem(newMissionItem: NotificationDto) {
//        NotificationDataList.add(0, newMissionItem)
//    }
//}