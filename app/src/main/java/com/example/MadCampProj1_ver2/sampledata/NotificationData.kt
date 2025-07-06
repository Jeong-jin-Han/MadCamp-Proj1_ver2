package com.example.MadCampProj1_ver2.sampledata

import com.example.MadCampProj1_ver2.notification.NotificationType

object NotificationData {
    private val NotificationDataList : MutableList<NotificationDto> =
        mutableListOf(
            NotificationDto(1, NotificationType.MISSION_COMPLETE, "도전과제를 완료하였습니다", 1, false),
            NotificationDto(2, NotificationType.GALLERY_POST, "게시물을 업로드하였습니다", 9, false),
            NotificationDto(3, NotificationType.MAP_LOCATION, "이동하였습니다", 18, false),
            NotificationDto(4, NotificationType.GALLERY_POST, "게시물을 업로드하였습니다", 123, false),
            NotificationDto(5, NotificationType.MISSION_COMPLETE, "도전과제를 완료하였습니다", 2, false),
            NotificationDto(6, NotificationType.MAP_LOCATION, "이동하였습니다", 6, false),
            NotificationDto(7, NotificationType.GALLERY_POST, "게시물을 업로드하였습니다", 10, false),
            NotificationDto(8, NotificationType.MAP_LOCATION, "이동하였습니다", 2, false),
            NotificationDto(9, NotificationType.MISSION_COMPLETE, "도전과제를 완료하였습니다", 3, false),
            NotificationDto(10, NotificationType.MAP_LOCATION, "이동하였습니다", 1, false),
            NotificationDto(11, NotificationType.GALLERY_POST, "게시물을 업로드하였습니다", 45, false),
            NotificationDto(12, NotificationType.MISSION_COMPLETE, "도전과제를 완료하였습니다", 4, false),
            NotificationDto(13, NotificationType.MAP_LOCATION, "이동하였습니다", 14, false),
            NotificationDto(14, NotificationType.GALLERY_POST, "게시물을 업로드하였습니다", 19, false),
            NotificationDto(15, NotificationType.MISSION_COMPLETE, "도전과제를 완료하였습니다", 6, false),
            NotificationDto(16, NotificationType.GALLERY_POST, "게시물을 업로드하였습니다", 23, false),
            NotificationDto(17, NotificationType.MAP_LOCATION, "이동하였습니다", 5, false),
            NotificationDto(18, NotificationType.GALLERY_POST, "게시물을 업로드하였습니다", 67, false),
            NotificationDto(19, NotificationType.MISSION_COMPLETE, "도전과제를 완료하였습니다", 7, false),
            NotificationDto(20, NotificationType.MAP_LOCATION, "이동하였습니다", 16, false)
        )
    fun getNotificationDataList(): MutableList<NotificationDto> {
        return NotificationDataList
    }

    fun getCheckdNotificationDataList(): Int {
        var cnt: Int = 0
        for (l in NotificationDataList) {
            if (l.clicked == false) cnt += 1
        }
        return cnt
    }

    fun addNotificationItem(newMissionItem: NotificationDto) {
        NotificationDataList.add(0, newMissionItem)
    }
}