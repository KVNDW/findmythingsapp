package fhnw.ws6c.findmi.data.Entities

import androidx.room.Embedded
import androidx.room.Relation

data class Inside1WithLocation(
    @Embedded val inside1: Inside1,

    @Relation(
        parentColumn = "locationID",
        entityColumn = "locationID"
    )
    val location: Location
)
