package com.example.MadCampProj1_ver2.myfoodmembercheckeddata

import com.example.MadCampProj1_ver2.myfooddata.MyFoodDto
import com.example.MadCampProj1_ver2.myfoodmemberdata.MyFoodMemberData
import com.example.MadCampProj1_ver2.myfoodmemberdata.MyFoodMemberDto

object MyFoodCheckedMemberData {
    private val checkedList = mutableListOf<MyFoodCheckedMemberDto>()
    private var initialized = false

    // 전체 멤버 리스트를 초기화하면서, 체크 여부에 따라 값 설정
    fun initializeWithMembers(allMembers: List<MyFoodMemberDto>) {
        if (MyFoodCheckedMemberData.initialized) return

        checkedList.clear()
        allMembers.forEach { member ->
            checkedList.add(MyFoodCheckedMemberDto(member, checked = false)) // 멤버는 항상 존재
        }

        initialized = true
    }

    // 체크 상태 변경
    fun toggleChecked(memberId: Int): Boolean {
        var result = false
        checkedList.replaceAll { item ->
            if (item.foodmember.memberId == memberId) {
                val newChecked = !item.checked
                result = newChecked
                item.copy(checked = newChecked)
            } else item
        }
        return result
    }

    // 특정 멤버 체크 상태 직접 설정
    fun setChecked(memberId: Int, isChecked: Boolean) {
        checkedList.replaceAll { item ->
            if (item.foodmember?.memberId == memberId) {
                item.copy(checked = isChecked)
            } else item
        }
    }

    // 선택된 멤버만 반환
    fun getCheckedMembers(): List<MyFoodCheckedMemberDto> =
        checkedList.filter { it.checked }

    // 체크된 멤버들의 MyFoodMemberDto만 반환
    fun getCheckedFoodMembers(): List<MyFoodMemberDto> {
        return checkedList.filter { it.checked }
            .map { it.foodmember }
    }

    // 선택된 멤버들의 음식 리스트 병합
    fun getMergedFoodList(): List<MyFoodDto> {
        val merged = mutableSetOf<MyFoodDto>()
        checkedList.filter { it.checked }.forEach { dto ->
            dto.foodmember.foods?.let { merged.addAll(it) } // 상위 부분 관점에 대한 it
        }
        return merged.toList()
    }

    // 전체 리스트 조회
    fun getAll(): List<MyFoodCheckedMemberDto> = checkedList.toList()

    fun isChecked(memberId: Int): Boolean {
        return checkedList.find { it.foodmember.memberId == memberId }?.checked ?: false
    }
}