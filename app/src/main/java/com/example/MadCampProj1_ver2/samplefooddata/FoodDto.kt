package com.example.MadCampProj1_ver2.samplefooddata

data class FoodDto(
    val name: String,
    val category: String,
    val storage: String,
    var isChecked: Boolean = false,
    var expiry: String? = null,
    var count: Int = 1
)
