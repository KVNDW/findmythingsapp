package fhnw.ws6c.findmi.data.Daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import fhnw.ws6c.findmi.data.Entities.Item
import fhnw.ws6c.findmi.data.Entities.ItemWithFullLocation

@Dao
interface ItemDao {
    @Insert
    suspend fun insert(item: Item): Long

    @Query("SELECT * FROM item WHERE inside2ID = :inside2ID")
    suspend fun getItemsByInside2(inside2ID: Int): List<Item>

    @Query("SELECT * FROM item")
    suspend fun getAllItems(): List<Item>

    @Query("SELECT * FROM item WHERE itemID = :itemId")
    suspend fun getItemById(itemId: Int): Item?

    @Transaction
    @Query("SELECT * FROM item WHERE itemID = :itemID")
    suspend fun getItemWithFullLocation(itemID: Int): ItemWithFullLocation?

    @Query("DELETE FROM Item WHERE itemID = :itemId")
    suspend fun deleteItemById(itemId: Int)

    @Query("SELECT * FROM item ORDER BY itemID DESC LIMIT 1")
    suspend fun getLastSavedItem(): Item?

}