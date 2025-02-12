package fhnw.ws6c.findmi.model

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fhnw.ws6c.findmi.data.AppDatabase
import fhnw.ws6c.findmi.data.Entities.Inside1
import fhnw.ws6c.findmi.data.Entities.Inside2
import fhnw.ws6c.findmi.data.Entities.Item
import fhnw.ws6c.findmi.data.Entities.Location
import fhnw.ws6c.findmi.data.Entities.ItemWithFullLocation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object ViewModel {

    var item by mutableStateOf<Item?>(null)
    var itemList = mutableStateListOf<Item>()
    var locationList by mutableStateOf<List<Location>>(emptyList())

    var filteredInside1List by mutableStateOf<List<Inside1>>(emptyList())
    var filteredInside2List by mutableStateOf<List<Inside2>>(emptyList())

    var selectedLocation by mutableStateOf<Location?>(null)
    var selectedInside1 by mutableStateOf<Inside1?>(null)
    var selectedInside2 by mutableStateOf<Inside2?>(null)

    var savedEntries = mutableStateListOf<SavedItem>()


    fun reset(){

        selectedLocation = null
        selectedInside1 = null
        selectedInside2 = null

        filteredInside1List = emptyList()
        filteredInside2List = emptyList()
    }

    fun initializeLists(db: AppDatabase) {
        fetchAllLocations(db)
        fetchAllItems(db)
    }

    fun fetchAllItems(db: AppDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            val fetchedItems = db.itemDao().getAllItems()
            synchronized(itemList) {
                itemList.clear()
                itemList.addAll(fetchedItems.distinctBy { it.itemID })
            }
        }
    }

    fun fetchAllLocations(db: AppDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            val fetchedLocations = db.locationDao().getAllLocations()
            locationList = fetchedLocations
        }
    }

    fun fetchInside1ByLocation(db: AppDatabase, locationID: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val fetchedInside1 = db.inside1Dao().getInside1ByLocation(locationID)
            filteredInside1List = fetchedInside1
            // fetch all Inside2 to prevent filter error
            val fetchedInside2 = fetchedInside1.flatMap { inside1 ->
                db.inside2Dao().getInside2ByInside1(inside1.inside1ID)
            }
            filteredInside2List = fetchedInside2
        }
    }

    fun fetchInside2ByInside1(db: AppDatabase, inside1ID: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val fetchedInside2 = db.inside2Dao().getInside2ByInside1(inside1ID)
            filteredInside2List = fetchedInside2
        }
    }

    fun addSavedEntry(item: Item, location: Location, inside1: Inside1, inside2: Inside2) {
        val newEntry = SavedItem(item, location, inside1, inside2)
        savedEntries.add(newEntry)

        if (itemList.none { it.name == item.name }) {
            itemList.add(item)
        }
    }

    fun saveNewItem(db: AppDatabase, name: String, description: String, foto: String, inside2ID: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val newItem = Item(name = name, description = description, foto = foto, inside2ID = inside2ID)
            db.itemDao().insert(newItem)

            fetchAllItems(db)
        }
    }

    @Composable
    fun SavedItemsList(entries: List<SavedItem>) {
        Column {
            entries.forEach { entry ->
                SavedItemRow(entry.item, entry.inside1, entry.inside2, entry.location)
            }
        }
    }

    @Composable
    fun SavedItemRow(item: Item, inside1: Inside1, inside2: Inside2, location: Location) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clickable {
                    Log.d("SavedItemRow", "Clicked on item: ${item.name}")
                }
        ) {
            Text("Ding: ${item.name}")
            Text("Beschreibung: ${item.description}")
            Text("Raum: ${location.name}")
            Text("Möbel: ${inside1.name}")
            Text("Möbel Spezifizierung: ${inside2.name}")
        }
    }

    fun fetchItemWithFullLocation(db: AppDatabase, itemID: Int, onResult: (ItemWithFullLocation?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val itemWithFullLocation = db.itemDao().getItemWithFullLocation(itemID)
            onResult(itemWithFullLocation)
        }
    }

    fun getFilteredItems(searchQuery: String): List<Item> {
        return itemList.filter { item ->
            val matchesLocation = selectedLocation == null || filteredInside2List.any { inside2 ->
                val inside1 = filteredInside1List.find { it.inside1ID == inside2.inside1ID }
                inside1?.locationID == selectedLocation?.locationID && inside2.inside2ID == item.inside2ID
            }
            val matchesInside1 = selectedInside1 == null || filteredInside2List.any { it.inside1ID == selectedInside1!!.inside1ID && it.inside2ID == item.inside2ID }
            val matchesInside2 = selectedInside2 == null || item.inside2ID == selectedInside2!!.inside2ID
            val matchesSearch = searchQuery.isEmpty() || item.name.contains(searchQuery, ignoreCase = true)
            matchesLocation && matchesInside1 && matchesInside2 && matchesSearch
        }.sortedByDescending { it.timestamp }
    }
}