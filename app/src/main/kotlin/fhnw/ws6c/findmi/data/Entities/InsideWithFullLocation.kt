package fhnw.ws6c.findmi.data.Entities

import androidx.room.Embedded
import androidx.room.Relation

data class ItemWithFullLocation(
    @Embedded val item: Item,

    @Relation(
        parentColumn = "inside2ID",
        entityColumn = "inside2ID",
        entity = Inside2::class
    )
    val inside2WithInside1WithLocation: Inside2WithInside1WithLocation
)