package com.example.MadCampProj1_ver2.myfoodmemberdata

import com.example.MadCampProj1_ver2.myfooddata.MyFoodDto

data class MyFoodMemberDto(
    val memberId : Int,
    val name: String,
    val phone: String,
    val lat: Double,            // 위도
    val lng: Double,            // 경도
    val imgPath: Int,           // 정사각형 프로필 이미지 리소스
    val imgCirclePath: Int,     // 원형 이미지 리소스
    val foods: Set<MyFoodDto>?        // 보유 식재료 ID 목록 (FoodDto.foodId)
)
