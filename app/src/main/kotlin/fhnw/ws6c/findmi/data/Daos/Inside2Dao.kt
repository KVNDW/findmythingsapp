package fhnw.ws6c.findmi.data.Daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fhnw.ws6c.findmi.data.Entities.Inside2

@Dao
interface Inside2Dao {
    @Insert
    suspend fun insert(inside2: Inside2)

    @Query("SELECT * FROM inside2 WHERE inside1ID = :inside1ID")
    suspend fun getInside2ByInside1(inside1ID: Int): List<Inside2>

    @Query("SELECT * FROM inside2")
    suspend fun getAllInside2(): List<Inside2>

}