package com.example.MadCampProj1_ver2.samplefooddata

import android.content.Context
import com.example.MadCampProj1_ver2.sampledata.MemberData
import com.example.MadCampProj1_ver2.sampledata.MemberDto
import org.json.JSONArray
import java.io.InputStreamReader

object FoodData {
    fun getFoodDataList(context: Context): List<FoodDto> {
        // JSON 파일을 읽어서 문자열로 반환
        val jsonString = loadJSONFromAsset(context)

        // JSON 배열을 파싱하여 MemberDto 리스트로 변환
        val jsonArray = JSONArray(jsonString)

        val foodList = mutableListOf<FoodDto>()
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)

            val foodId = jsonObject.getInt("foodId")
            val name = jsonObject.getString("name")
            val category = jsonObject.getString("category")
            val storage = jsonObject.getString("storage")
            val image = jsonObject.getString("image")

            val imgPath = getDrawableResourceId(context, image)

            foodList.add(
                FoodDto(
                    foodId,
                    name,
                    category,
                    storage,
                    imgPath
                )
            )
        }
        return foodList
    }

    // JSON에서 읽어온 String 값을 실제 drawable 리소스 ID로 변환
    private fun getDrawableResourceId(context: Context, resourceName: String): Int {
        // 리소스 이름을 사용하여 실제 리소스 ID를 가져옴
        return context.resources.getIdentifier(resourceName, "drawable", context.packageName)
    }

    // assets 폴더에서 JSON 파일을 읽어오는 메서드
    private fun loadJSONFromAsset(context: Context): String? {
        var json: String? = null
        try {
            // "members.json" 파일을 assets 폴더에서 연다
            val inputStream = context.assets.open("ingredients.json")
            val inputStreamReader = InputStreamReader(inputStream)
            json = inputStreamReader.readText()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return json
    }
}