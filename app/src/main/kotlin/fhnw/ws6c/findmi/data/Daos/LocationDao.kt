package fhnw.ws6c.findmi.data.Daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import fhnw.ws6c.findmi.data.Entities.Location

@Dao
interface LocationDao {
    @Insert
    suspend fun insert(location: Location)

    @Query("SELECT * FROM location")
    suspend fun getAllLocations(): List<Location>

    @Query("SELECT COUNT(*) FROM location")
    suspend fun getCount(): Int
}