package com.example.MadCampProj1_ver2.myfooddata

import android.util.Log

object MyFoodData {
    private val foodList = mutableListOf<MyFoodDto>()

    fun addMyFoodDataDueDate(foodId: Int, dueDate: String) {
        val existing = foodList.find { it.foodId == foodId }
        if (existing != null) {
            existing.foodDuedate = dueDate
            Log.d("MyFoodData", "Updated dueDate of foodId=$foodId to $dueDate")
        } else {
            foodList.add(MyFoodDto(foodId, 1, dueDate))
            Log.d("MyFoodData", "Added new food with foodId=$foodId and dueDate=$dueDate")
        }
    }

    fun addMyFoodDataNumber(foodId: Int) {
        val existing = foodList.find { it.foodId == foodId }
        if (existing != null) {
            existing.foodNumber += 1
            Log.d("MyFoodData", "Increased foodNumber of foodId=$foodId to ${existing.foodNumber}")
        } else {
            Log.d("MyFoodData", "Tried to increase number, but foodId=$foodId not found")
        }
    }

    fun deleteMyFoodDataNumber(foodId: Int) {
        val existing = foodList.find { it.foodId == foodId }
        if (existing != null) {
            existing.foodNumber -= 1
            Log.d("MyFoodData", "Decreased foodNumber of foodId=$foodId to ${existing.foodNumber}")
            if (existing.foodNumber <= 0) {
                foodList.remove(existing)
                Log.d("MyFoodData", "Removed foodId=$foodId because quantity reached zero")
            }
        } else {
            Log.d("MyFoodData", "Tried to decrease number, but foodId=$foodId not found")
        }
    }

    fun deleteMyFoodDataItem(foodId: Int) {
        foodList.removeAll { it.foodId == foodId }
        Log.d("MyFoodData", "Deleted all entries with foodId=$foodId")
    }

    fun updateMyFoodDataDueDate(foodId: Int, newDate: String) {
        val existing = foodList.find { it.foodId == foodId }
        if (existing != null) {
            existing.foodDuedate = newDate
            Log.d("MyFoodData", "Updated dueDate of foodId=$foodId to $newDate")
        } else {
            Log.d("MyFoodData", "Tried to update dueDate, but foodId=$foodId not found")
        }
    }

    fun addMyFoodDataItem(foodId: Int, dueDate: String, number: Int = 1) {
        val existing = foodList.find { it.foodId == foodId }
        if (existing != null) {
            existing.foodDuedate = dueDate
            existing.foodNumber = number
            Log.d("MyFoodData", "Updated foodId=$foodId with new dueDate=$dueDate and number=$number")
        } else {
            foodList.add(MyFoodDto(foodId, number, dueDate))
            Log.d("MyFoodData", "Added new food item: foodId=$foodId, number=$number, dueDate=$dueDate")
        }
    }

    fun getMyFoodDataAllItems(): List<MyFoodDto> {
        Log.d("MyFoodData", "Retrieving all items (${foodList.size})")
        return foodList.toList()
    }

    fun getMyFoodDataNumberfromFoodId(foodId: Int): Int {
        val existing = foodList.find { it.foodId == foodId }
        if (existing != null) {
            return existing.foodNumber
        } else {
            return 0
        }
    }

}