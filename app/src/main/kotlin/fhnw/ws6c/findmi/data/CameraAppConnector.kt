package fhnw.ws6c.findmi.data
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CameraAppConnector(private val activity: ComponentActivity) {

    private var onSuccess: ((Bitmap, String) -> Unit)? = null
    private var onCanceled: (() -> Unit)? = null
    private lateinit var photoFile: File
    private var lastBitmap: Bitmap? = null

    private val cameraLauncher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try {
                val options = BitmapFactory.Options().apply {
                    inMutable = true
                    lastBitmap?.let { inBitmap = it }
                }

                val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath, options)
                lastBitmap = bitmap
                onSuccess?.invoke(bitmap, photoFile.absolutePath)
            } catch (e: Exception) {
                onCanceled?.invoke()
            }
        } else {
            onCanceled?.invoke()
        }
    }

    fun getBitmapWithPath(onSuccess: (Bitmap, String) -> Unit, onCanceled: () -> Unit) {
        this.onSuccess = onSuccess
        this.onCanceled = onCanceled

        try {
            photoFile = createPhotoFile()
            val photoUri = FileProvider.getUriForFile(activity, "fhnw.ws6c.fileprovider", photoFile)

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            }

            cameraLauncher.launch(intent)
        } catch (e: Exception) {
            onCanceled()
        }
    }

    private fun createPhotoFile(): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir!!)
    }
}