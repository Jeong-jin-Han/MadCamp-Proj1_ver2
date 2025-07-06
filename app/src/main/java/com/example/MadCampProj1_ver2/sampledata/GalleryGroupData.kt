package com.example.MadCampProj1_ver2.sampledata

object GalleryGroupData {
    fun getGalleryGroupDataList(): List<GalleryGroupDto> {
        return listOf(
            GalleryGroupDto(1, "2025-01-01", 20),
            GalleryGroupDto(2, "2024-12-31", 20),
            GalleryGroupDto(3, "2024-12-30", 20),
            GalleryGroupDto(4, "2024-12-29", 20),
            GalleryGroupDto(5, "2024-12-28", 20),
            GalleryGroupDto(6, "2024-12-27", 20),
            GalleryGroupDto(7, "2024-12-26", 20),
            GalleryGroupDto(8, "2024-12-25", 20),
            GalleryGroupDto(9, "2024-12-24", 20)
        )
    }
}
