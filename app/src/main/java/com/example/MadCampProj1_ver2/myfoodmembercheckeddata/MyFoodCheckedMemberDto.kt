package com.example.MadCampProj1_ver2.myfoodmembercheckeddata

import com.example.MadCampProj1_ver2.myfooddata.MyFoodDto
import com.example.MadCampProj1_ver2.myfoodmemberdata.MyFoodMemberDto

data class MyFoodCheckedMemberDto(
    val foodmember: MyFoodMemberDto,
    val checked: Boolean
)
