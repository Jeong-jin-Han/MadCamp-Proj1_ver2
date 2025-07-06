package com.example.MadCampProj1_ver2.sampledata

object MissionCompleteData {
    private val missionCompleteList: MutableList<MissionCompleteDto> =
        mutableListOf(
            // Mission 1
            MissionCompleteDto(1, 1),
            MissionCompleteDto(1, 2),
            MissionCompleteDto(1, 3),
            MissionCompleteDto(1, 4),
            MissionCompleteDto(1, 5),

            // Mission 2
            MissionCompleteDto(2, 1),
            MissionCompleteDto(2, 6),
            MissionCompleteDto(2, 7),
            MissionCompleteDto(2, 8),
            MissionCompleteDto(2, 9),
            MissionCompleteDto(2, 10),

            // Mission 3
            MissionCompleteDto(3, 1),
            MissionCompleteDto(3, 11),
            MissionCompleteDto(3, 12),
            MissionCompleteDto(3, 13),
            MissionCompleteDto(3, 14),

            // Mission 4
            MissionCompleteDto(4, 1),
            MissionCompleteDto(4, 16),
            MissionCompleteDto(4, 17),
            MissionCompleteDto(4, 18),

            // Mission 5
            MissionCompleteDto(5, 1),
            MissionCompleteDto(5, 21),
            MissionCompleteDto(5, 22),

            // Mission 6
            MissionCompleteDto(6, 1),
            MissionCompleteDto(6, 26),
            MissionCompleteDto(6, 27),
            MissionCompleteDto(6, 28),
            MissionCompleteDto(6, 29),

            // Mission 7
            MissionCompleteDto(7, 1),
            MissionCompleteDto(7, 1),
            MissionCompleteDto(7, 2),
            MissionCompleteDto(7, 3),

            // Mission 10
            MissionCompleteDto(10, 1),
            MissionCompleteDto(10, 16),
            MissionCompleteDto(10, 17),
            MissionCompleteDto(10, 18),
            MissionCompleteDto(10, 19),
            MissionCompleteDto(10, 20),

            // Mission 11
            MissionCompleteDto(11, 1),
            MissionCompleteDto(11, 21),
            MissionCompleteDto(11, 22),
            MissionCompleteDto(11, 23),
            MissionCompleteDto(11, 24),
            MissionCompleteDto(11, 25),

            // Mission 12
            MissionCompleteDto(12, 1),
            MissionCompleteDto(12, 26),

            // Mission 13
            MissionCompleteDto(13, 1),
            MissionCompleteDto(13, 4),
            MissionCompleteDto(13, 5),

            // Mission 15
            MissionCompleteDto(15, 11),
            MissionCompleteDto(15, 12),
            MissionCompleteDto(15, 13),
            MissionCompleteDto(15, 14),

            // Mission 16
            MissionCompleteDto(16, 16),
            MissionCompleteDto(16, 17),
            MissionCompleteDto(16, 20),

            // Mission 17
            MissionCompleteDto(17, 21),
            MissionCompleteDto(17, 22),
            MissionCompleteDto(17, 23),
            MissionCompleteDto(17, 24),


            // Mission 18
            MissionCompleteDto(18, 26),
            MissionCompleteDto(18, 27),
            MissionCompleteDto(18, 28),
            MissionCompleteDto(18, 29),
            MissionCompleteDto(18, 30),

            // Mission 19
            MissionCompleteDto(19, 1),


            // Mission 20
            MissionCompleteDto(20, 6),
            MissionCompleteDto(20, 9),
            MissionCompleteDto(20, 10)
        )

    fun getMissionCompleteList(): MutableList<MissionCompleteDto> {
        return missionCompleteList
    }
}
