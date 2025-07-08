package com.example.MadCampProj1_ver2.myfoodmemberdata

import android.content.Context
import com.example.MadCampProj1_ver2.myfooddata.MyFoodDto
import com.example.MadCampProj1_ver2.sampledata.MemberData
import com.example.MadCampProj1_ver2.sampledata.MemberDto
import com.example.MadCampProj1_ver2.samplefooddata.FoodDto
import com.google.gson.Gson
import org.json.JSONArray
import java.io.InputStreamReader
import kotlin.random.Random


object MyFoodMemberData {
    private val myFoodMemberList: MutableList<MyFoodMemberDto> = mutableListOf()
    private val myFoodMemberIdsWithItems = listOf(1, 3, 10, 25, 30)
    private var initialized = false

    private val sampleDueDates = listOf(
        "2025-07-30", "2025-08-05", "2025-08-12",
        "2025-09-01", "2025-09-15", "2025-10-01"
    )

    fun initializeIfNeeded(context: Context) {
        if (initialized) return

        val jsonString = loadJSONFromAsset(context) ?: return
        val jsonArray = JSONArray(jsonString)

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)

            val memberId = jsonObject.getInt("memberId")
            val name = jsonObject.getString("name")
            val phone = jsonObject.getString("phone")
            val lat = jsonObject.getDouble("lat")
            val lng = jsonObject.getDouble("lng")
            val img = getDrawableResourceId(context, jsonObject.getString("imgPath"))
            val imgCircle = getDrawableResourceId(context, jsonObject.getString("imgCirclePath"))

            // 랜덤 식재료는 특정 멤버에게만 추가
            val foods = if (memberId in myFoodMemberIdsWithItems) {
                generateRandomMyFoodDtoSet()
            } else {
                null
            }

            val dto = MyFoodMemberDto(
                memberId = memberId,
                name = name,
                phone = phone,
                lat = lat,
                lng = lng,
                imgPath = img,
                imgCirclePath = imgCircle,
                foods = foods
            )

            myFoodMemberList.add(dto)
        }

        initialized = true
    }

    fun getAllMyFoodMembers(): List<MyFoodMemberDto> = myFoodMemberList

    fun getMyFoodMemberIfExists(memberId: Int): MyFoodMemberDto? {
        return myFoodMemberList.find { it.memberId == memberId }
    }

    fun getFoodDtoListfromMemberId(memberId: Int, foodDataList: List<FoodDto>): List<FoodDto> {
        val member = MyFoodMemberData.getMyFoodMemberIfExists(memberId) ?: return emptyList()

        val foodIdSet = member.foods?.map { it.foodId }?.toSet() ?: return emptyList()

        return foodDataList.filter { it.foodId in foodIdSet }
    }

    private fun generateRandomMyFoodDtoSet(): Set<MyFoodDto> {
        val random = Random(System.currentTimeMillis())
        return (0 until 50).shuffled(random).take(5).map { foodId ->
            val number = (1..3).random(random)
            val dueDate = sampleDueDates.random(random)
            MyFoodDto(foodId, number, dueDate)
        }.toSet()
    }

    private fun getDrawableResourceId(context: Context, resourceName: String): Int {
        return context.resources.getIdentifier(resourceName, "drawable", context.packageName)
    }

    private fun loadJSONFromAsset(context: Context): String? {
        return try {
            val inputStream = context.assets.open("members.json")
            val inputStreamReader = InputStreamReader(inputStream)
            inputStreamReader.readText()
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }
}