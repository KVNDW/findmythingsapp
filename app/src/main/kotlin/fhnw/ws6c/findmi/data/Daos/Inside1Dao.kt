package fhnw.ws6c.findmi.data.Daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fhnw.ws6c.findmi.data.Entities.Inside1

@Dao
interface Inside1Dao {
    @Insert
    suspend fun insert(inside1: Inside1)

    @Query("SELECT * FROM inside1 WHERE locationID = :locationID")
    suspend fun getInside1ByLocation(locationID: Int): List<Inside1>

    @Query("SELECT * FROM inside1")
    suspend fun getAllInside1(): List<Inside1>

}
