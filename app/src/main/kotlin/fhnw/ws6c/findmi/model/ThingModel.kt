package fhnw.ws6c.findmi.model
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import fhnw.ws6c.findmi.data.AppDatabase
import fhnw.ws6c.findmi.data.DatabaseProvider
import fhnw.ws6c.findmi.data.Entities.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.util.Log
import fhnw.ws6c.findmi.data.CameraAppConnector
import java.io.File

class ThingModel(private val cameraAppConnector: CameraAppConnector) {
    var screenKey by mutableStateOf(0)
    var isCameraGranted by mutableStateOf(false)
    var currentScreen by mutableStateOf(Screen.Home)
    var lastScreen by mutableStateOf(Screen.Home)

    var photo by mutableStateOf<Bitmap?>(null)
    var photoPath by mutableStateOf<String?>(null)


    var selectedItem by mutableStateOf<Item?>(null)

    lateinit var db: AppDatabase

    fun initializeDatabase(context: Context) {
        db = DatabaseProvider.getDatabase(context)
    }

    fun deleteItem(itemId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            db.itemDao().deleteItemById(itemId)
        }
    }

    fun takePhoto(onPhotoCaptured: (String) -> Unit) {
        cameraAppConnector.getBitmapWithPath(
            onSuccess = { bitmap, path ->
                photo = bitmap
                onPhotoCaptured(path)
            },
            onCanceled = {
                Log.d("CameraDebug", "Photo canceled")
            }
        )
    }

    suspend fun saveNewItem(name: String, description: String, inside2ID: Int, photoPath: String?): Item {
        val newItem = Item(name = name, description = description, foto = photoPath ?: "", inside2ID = inside2ID)
        val itemId = db.itemDao().insert(newItem)
        return db.itemDao().getItemById(itemId.toInt())!!
    }

    fun resetListScreen() {
        screenKey++
    }



    fun loadImageFromPath(filePath: String): Bitmap? {
        val file = File(filePath)
        return if (file.exists()) {
            BitmapFactory.decodeFile(filePath)
        } else {
            Log.e("DetailScreen", "File does not exist at path: $filePath")
            null
        }
    }

}