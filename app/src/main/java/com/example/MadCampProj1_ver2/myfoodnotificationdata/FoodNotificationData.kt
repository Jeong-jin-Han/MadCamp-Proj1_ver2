package com.example.MadCampProj1_ver2.myfoodnotificationdata

import android.util.Log

object FoodNotificationData {
    private val notificationList = mutableListOf<FoodNotificationDto>()

    // 🔸 전체 알림 반환
    fun getAll(): List<FoodNotificationDto> {
        return notificationList.toList()
    }

    // 🔸 알림 추가
    fun add(notification: FoodNotificationDto) {
        notificationList.add(0, notification) // 최신 알림이 위로
        Log.d("FoodNotification", "Added: ${notification.message}")
    }

    // 🔸 알림 수정 (id 기준으로 찾고 내용만 교체)
    fun update(updated: FoodNotificationDto) {
        val index = notificationList.indexOfFirst { it.id == updated.id }
        if (index != -1) {
            notificationList[index] = updated
            Log.d("FoodNotification", "Updated: ${updated.message}")
        } else {
            Log.d("FoodNotification", "Update failed: ID ${updated.id} not found")
        }
    }

    // 🔸 알림 삭제 (id 기준)
    fun delete(id: Int) {
        val removed = notificationList.removeIf { it.id == id }
        if (removed) {
            Log.d("FoodNotification", "Deleted notification with id: $id")
        } else {
            Log.d("FoodNotification", "Delete failed: ID $id not found")
        }
    }

}

// add와 delete 기능


/*

SEND_MESSAGE
항상 add를 함

ADD_FOOD
유통기한 바뀌면 notification messag도 바뀌고
수량이 0이 되면 유통기한: 미입력으로 바뀌면 notifcation도






*/