package fhnw.ws6c.findmi.data.Entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "item",
    foreignKeys = [ForeignKey(
        entity = Inside2::class,
        parentColumns = arrayOf("inside2ID"),
        childColumns = arrayOf("inside2ID"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Item(
    @PrimaryKey(autoGenerate = true) val itemID: Int = 0,
    val inside2ID: Int,  // Foreign key from Inside2
    val name: String,
    val description: String,
    val foto: String,
    val timestamp: Long = System.currentTimeMillis()
)