package com.example.MadCampProj1_ver2.sampledata

object GalleryGroupData {
    fun getGalleryGroupDataList(): List<GalleryGroupDto> {
        return listOf(
            GalleryGroupDto(1, "한식", 20),
            GalleryGroupDto(2, "일식&중식", 20),
            GalleryGroupDto(3, "양식&퓨전", 20),
            GalleryGroupDto(4, "브런치", 20),
            GalleryGroupDto(5, "My recipe", 20)

        )
    }
}
