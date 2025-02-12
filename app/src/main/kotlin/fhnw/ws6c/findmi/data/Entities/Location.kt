package fhnw.ws6c.findmi.data.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "location")
data class Location(
    @PrimaryKey(autoGenerate = true) val locationID: Int = 0,
    val gps: Int,
    val name: String,
)
