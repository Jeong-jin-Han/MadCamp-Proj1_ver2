package com.example.MadCampProj1_ver2.myfoodnotificationdata

import com.example.MadCampProj1_ver2.foodnotification.FoodNotificationType

data class FoodNotificationDto(
    val id : Int,
    val type :FoodNotificationType,
    val message : String,
    val imgPath: Int,
)
