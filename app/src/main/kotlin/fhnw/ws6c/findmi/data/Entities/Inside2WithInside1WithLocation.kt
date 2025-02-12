package fhnw.ws6c.findmi.data.Entities

import androidx.room.Embedded
import androidx.room.Relation

data class Inside2WithInside1WithLocation(
    @Embedded val inside2: Inside2,

    @Relation(
        parentColumn = "inside1ID",
        entityColumn = "inside1ID",
        entity = Inside1::class
    )
    val inside1WithLocation: Inside1WithLocation
)
