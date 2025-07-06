package com.example.MadCampProj1_ver2.mission

object FontColorList {
    private val colorList: MutableList<String> =
        mutableListOf("#E5AEB3", "#CC9573", "#FFD662", "#79AC2C", "#78BAEC", "#E5AEB3", "#CC9573", "#FFD662", "#79AC2C", "#78BAEC")

    fun getFontColorList(): MutableList<String> {
        return FontColorList.colorList
    }
}
