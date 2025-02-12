package fhnw.ws6c.findmi.data.Entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "inside1",
    foreignKeys = [ForeignKey(
        entity = Location::class,
        parentColumns = arrayOf("locationID"),
        childColumns = arrayOf("locationID"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Inside1(
    @PrimaryKey(autoGenerate = true) val inside1ID: Int = 0,
    val locationID: Int,
    val name: String
)