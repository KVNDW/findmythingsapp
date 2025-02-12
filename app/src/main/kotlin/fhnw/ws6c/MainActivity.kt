package fhnw.ws6c
import MyAppTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import fhnw.ws6c.findmi.data.CameraAppConnector
import fhnw.ws6c.findmi.data.Entities.Inside1
import fhnw.ws6c.findmi.data.Entities.Inside2
import fhnw.ws6c.findmi.data.Entities.Item
import fhnw.ws6c.findmi.data.Entities.Location
import fhnw.ws6c.findmi.model.ThingModel
import fhnw.ws6c.findmi.ui.AppUI
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var model : ThingModel
    private var isDbInitialized by mutableStateOf(false)

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cam = CameraAppConnector(this)

        // Initialize the Room database
        applicationContext.deleteDatabase("inventory_DB")
        model = ThingModel(cameraAppConnector = cam)
        model.initializeDatabase(applicationContext)

        setContent {
            MyAppTheme(darkTheme = true) {
                if (isDbInitialized) {
                    AppUI(model = model)
                }
            }
        }

        lifecycleScope.launch {
            val locationDao = model.db.locationDao()
            val inside1Dao = model.db.inside1Dao()
            val inside2Dao = model.db.inside2Dao()
            val itemDao = model.db.itemDao()
            val locationCount = locationDao.getCount()

            if(locationCount == 0){

                // Locations
                locationDao.insert(Location(locationID = 1, gps = 101, name = "Büro"))
                locationDao.insert(Location(locationID = 2, gps = 102, name = "Wohnzimmer"))
                locationDao.insert(Location(locationID = 3, gps = 103, name = "Keller"))

                // Inside1  for Büro
                inside1Dao.insert(Inside1(inside1ID = 1, locationID = 1, name = "Rollcontainer"))
                inside1Dao.insert(Inside1(inside1ID = 2, locationID = 1, name = "Offenes Regal"))
                inside1Dao.insert(Inside1(inside1ID = 3, locationID = 1, name = "Schrank"))

                // Inside2 for Büro
                inside2Dao.insert(Inside2(inside2ID = 1, inside1ID = 1, name = "Schublade 1"))
                inside2Dao.insert(Inside2(inside2ID = 2, inside1ID = 1, name = "Schublade 2"))
                inside2Dao.insert(Inside2(inside2ID = 3, inside1ID = 1, name = "Schublade 3"))
                inside2Dao.insert(Inside2(inside2ID = 4, inside1ID = 1, name = "Schublade 4"))

                inside2Dao.insert(Inside2(inside2ID = 5, inside1ID = 2, name = "Tablar 1"))
                inside2Dao.insert(Inside2(inside2ID = 6, inside1ID = 2, name = "Tablar 2"))
                inside2Dao.insert(Inside2(inside2ID = 7, inside1ID = 2, name = "Tablar 3"))
                inside2Dao.insert(Inside2(inside2ID = 8, inside1ID = 2, name = "Tablar 4"))

                inside2Dao.insert(Inside2(inside2ID = 9, inside1ID = 3, name = "Tablar 1"))
                inside2Dao.insert(Inside2(inside2ID = 10, inside1ID = 3, name = "Tablar 2"))
                inside2Dao.insert(Inside2(inside2ID = 11, inside1ID = 3, name = "Tablar 3"))
                inside2Dao.insert(Inside2(inside2ID = 12, inside1ID = 3, name = "Kleiderstange"))

                // Inside1  for Wohnzimmer
                inside1Dao.insert(Inside1(inside1ID = 4, locationID = 2, name = "Sideboard"))
                inside1Dao.insert(Inside1(inside1ID = 5, locationID = 2, name = "Bücherregal"))

                // Inside2 for Wohnzimmer
                inside2Dao.insert(Inside2(inside2ID = 13, inside1ID = 4, name = "Schublade 1 rechts"))
                inside2Dao.insert(Inside2(inside2ID = 14, inside1ID = 4, name = "Schublade 2 rechts"))
                inside2Dao.insert(Inside2(inside2ID = 15, inside1ID = 4, name = "Schublade 1 links"))
                inside2Dao.insert(Inside2(inside2ID = 16, inside1ID = 4, name = "Schublade 2 links"))

                inside2Dao.insert(Inside2(inside2ID = 17, inside1ID = 5, name = "Tablar 1"))
                inside2Dao.insert(Inside2(inside2ID = 18, inside1ID = 5, name = "Tablar 2"))
                inside2Dao.insert(Inside2(inside2ID = 19, inside1ID = 5, name = "Tablar 3"))
                inside2Dao.insert(Inside2(inside2ID = 20, inside1ID = 5, name = "Tablar 4"))

                //  Inside1 for Keller
                inside1Dao.insert(Inside1(inside1ID = 6, locationID = 3, name = "Truhe"))
                inside1Dao.insert(Inside1(inside1ID = 7, locationID = 3, name = "Geschlossener Schrank"))
                inside1Dao.insert(Inside1(inside1ID = 8, locationID = 3, name = "Offener Schrank"))

                // Inside2 for Keller
                inside2Dao.insert(Inside2(inside2ID = 21, inside1ID = 6, name = "links"))
                inside2Dao.insert(Inside2(inside2ID = 22, inside1ID = 6, name = "rechts"))

                inside2Dao.insert(Inside2(inside2ID = 23, inside1ID = 7, name = "Tablar 1"))
                inside2Dao.insert(Inside2(inside2ID = 24, inside1ID = 7, name = "Tablar 2"))
                inside2Dao.insert(Inside2(inside2ID = 25, inside1ID = 7, name = "Tablar 3"))

                inside2Dao.insert(Inside2(inside2ID = 26, inside1ID = 8, name = "Tablar 1"))
                inside2Dao.insert(Inside2(inside2ID = 27, inside1ID = 8, name = "Tablar 2"))
                inside2Dao.insert(Inside2(inside2ID = 28, inside1ID = 8, name = "Tablar 3"))

                // Insert sample items
//                itemDao.insert(Item(itemID = 1, inside2ID = 1, name = "Schlüssel A", foto = "photo1", description = "Schlüssel"))
//                itemDao.insert(Item(itemID = 2, inside2ID = 2, name = "Werkzeug B", foto = "photo2", description = "Werkzeug"))
//                itemDao.insert(Item(itemID = 3, inside2ID = 13, name = "Fernbedienung", foto = "photo3", description = "Fernbedienung"))
//                itemDao.insert(Item(itemID = 4, inside2ID = 22, name = "Wasserschlauch", foto = "photo4", description = "Gartenschlauch"))

                isDbInitialized = true
            }
        }
    }

    /**
     * Eine der Activity-LiveCycle-Methoden als Beispiel, falls da weiteres benötigt wird.
     */
    override fun onStop() {
        super.onStop()
        //anything else?
        print("stopped")
    }
}