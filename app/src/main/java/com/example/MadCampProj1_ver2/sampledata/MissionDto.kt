package com.example.MadCampProj1_ver2.sampledata

import java.time.LocalDate

data class MissionDto (
    val id: Int,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val isDone: Boolean,
    val category: Int,   //선택한 순서대로 번호 지정
    val percent: Int
)