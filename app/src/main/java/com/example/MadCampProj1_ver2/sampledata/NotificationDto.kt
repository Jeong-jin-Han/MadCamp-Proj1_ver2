package com.example.MadCampProj1_ver2.sampledata

import com.example.MadCampProj1_ver2.notification.NotificationType

data class NotificationDto (
    val id: Int,
    val type: NotificationType,
    val message: String,
    val targetId: Int, // 이동 대상 ID
    var clicked: Boolean
)