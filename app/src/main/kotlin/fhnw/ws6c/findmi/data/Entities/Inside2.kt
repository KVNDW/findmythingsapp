package fhnw.ws6c.findmi.data.Entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "inside2",
    foreignKeys = [ForeignKey(
        entity = Inside1::class,
        parentColumns = arrayOf("inside1ID"),
        childColumns = arrayOf("inside1ID"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Inside2(
    @PrimaryKey(autoGenerate = true) val inside2ID: Int = 0,
    val inside1ID: Int,
    val name: String
)