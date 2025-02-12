package fhnw.ws6c.findmi.data

import androidx.room.Database
import androidx.room.RoomDatabase
import fhnw.ws6c.findmi.data.Daos.Inside1Dao
import fhnw.ws6c.findmi.data.Daos.Inside2Dao
import fhnw.ws6c.findmi.data.Daos.ItemDao
import fhnw.ws6c.findmi.data.Daos.LocationDao
import fhnw.ws6c.findmi.data.Entities.Location
import fhnw.ws6c.findmi.data.Entities.Inside1
import fhnw.ws6c.findmi.data.Entities.Inside2
import fhnw.ws6c.findmi.data.Entities.Item

@Database(entities = [Location::class, Inside1::class, Inside2::class, Item::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
    abstract fun inside1Dao(): Inside1Dao
    abstract fun inside2Dao(): Inside2Dao
    abstract fun itemDao(): ItemDao
}