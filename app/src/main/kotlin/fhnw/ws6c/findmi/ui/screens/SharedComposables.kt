package fhnw.ws6c.findmi.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fhnw.ws6c.findmi.model.Screen
import fhnw.ws6c.findmi.model.ThingModel
import androidx.compose.material.icons.filled.FormatListNumberedRtl
import androidx.compose.material.icons.filled.PostAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fhnw.ws6c.findmi.data.Entities.Item
import fhnw.ws6c.findmi.model.ViewModel
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.ui.Alignment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import androidx.compose.material3.Divider
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import fhnw.ws6c.R
import fhnw.ws6c.findmi.data.AppDatabase
import fhnw.ws6c.findmi.data.Entities.ItemWithFullLocation
import fhnw.ws6c.findmi.ui.styles.defaultButtonTextStyle
import fhnw.ws6c.findmi.ui.styles.defaultShape
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun BottomNavigationBar(model: ThingModel) {
    Column {
        // Fine semi-transparent white divider line above the BottomNavigationBar
        Divider(
            thickness = 1.dp,
            color = Color.White.copy(alpha = 0.2f)
        )
        // Bottom Navigation Bar with color adaptation
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                label = { Text("Home") },
                selected = model.currentScreen == Screen.Home,
                onClick = { model.currentScreen = Screen.Home },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.FormatListNumberedRtl, contentDescription = "Liste") },
                label = { Text("Liste") },
                selected = model.currentScreen == Screen.List,
                onClick = { model.currentScreen = Screen.List },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                    indicatorColor = Color.Transparent
                )
            )
            NavigationBarItem(
                icon = { Icon(Icons.Filled.PostAdd, contentDescription = "Plus") },
                label = { Text("Neues Ding") },
                selected = model.currentScreen == Screen.Add,
                onClick = { model.currentScreen = Screen.Add },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun AboutTopBar(model: ThingModel) {
    val currentTitle = if (model.currentScreen == Screen.Home) {
        "findmi"
    } else {
        model.currentScreen.title
    }

    TopBar(
        title = currentTitle,
        isHomeScreen = model.currentScreen == Screen.Home,
        showBackButton = model.currentScreen != Screen.Home,
        onBackClick = {
            model.currentScreen = model.lastScreen
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    isHomeScreen: Boolean = false,
    showBackButton: Boolean = false,
    onBackClick: (() -> Unit)? = null
) {
    Column {
        TopAppBar(
            title = {
                if (isHomeScreen) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 40.sp)
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (showBackButton) {
                            IconButton(onClick = { onBackClick?.invoke() }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Zurück",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(start = if (showBackButton) 8.dp else 0.dp)
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            )
        )

        HorizontalDivider(
            thickness = 0.8.dp,
            color = Color.White.copy(alpha = 0.1f)
        )
    }
}


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun <T> Somelist(
    someList: List<T>,
    placeholder: String,
    itemLabel: (T) -> String,
    itemId: (T) -> Int,
    onItemSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(placeholder) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        // TextField mit transparentem Platzhalter
        TextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            placeholder = {
                Text(
                    placeholder,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown Pfeil",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            shape = MaterialTheme.shapes.medium,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary,

                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,

            )
        )

        // Dropdown-Menü mit feineren Texten (BodyMedium)
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        ) {
            someList.forEachIndexed { index, listValue ->
                DropdownMenuItem(
                    text = {
                        Text(
                            itemLabel(listValue),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    onClick = {
                        selectedText = itemLabel(listValue)
                        expanded = false
                        onItemSelected(itemId(listValue))
                    },
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .background(MaterialTheme.colorScheme.background)
                )


                if (index != someList.lastIndex) {
                    Divider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    )
                }
            }
        }
    }
}




@Composable
fun LabeledTextField(label: String, value: MutableState<String>) {
    TextField(
        value = value.value,
        onValueChange = { value.value = it },
        label = { Text(label) },
        textStyle = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .background(MaterialTheme.colorScheme.background),

        shape = MaterialTheme.shapes.medium,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
fun VerticalSpacer(height: Int) {
    Spacer(modifier = Modifier.height(height.dp))
}

@Composable
fun SaveButton(
    itemName: MutableState<String>,
    itemDescription: MutableState<String>,
    viewModel: ViewModel,
    model: ThingModel,
    photoPath: String?
) {
    var isSaving by remember { mutableStateOf(false) }

    Button(
        onClick = {
            val itemNameToSave = itemName.value.trim()
            val itemDescriptionToSave = itemDescription.value.trim()

            if (itemNameToSave.isNotEmpty() && viewModel.selectedLocation != null && viewModel.selectedInside1 != null && viewModel.selectedInside2 != null) {
                isSaving = true

                val finalPhotoPath = photoPath ?: "Kein Foto verfügbar"


                val newItem = Item(
                    name = itemNameToSave,
                    inside2ID = viewModel.selectedInside2!!.inside2ID,
                    description = itemDescriptionToSave,
                    foto = finalPhotoPath,
                    timestamp = System.currentTimeMillis()
                )

                CoroutineScope(Dispatchers.IO).launch {
                    val savedItem = model.saveNewItem(
                        name = newItem.name,
                        description = newItem.description,
                        inside2ID = newItem.inside2ID,
                        photoPath = newItem.foto
                    )
                    delay(1500)

                    withContext(Dispatchers.Main) {
                        isSaving = false

                        viewModel.addSavedEntry(
                            item = savedItem,
                            location = viewModel.selectedLocation!!,
                            inside1 = viewModel.selectedInside1!!,
                            inside2 = viewModel.selectedInside2!!
                        )

                        itemName.value = ""
                        itemDescription.value = ""
                        model.selectedItem = savedItem
                        model.lastScreen = model.currentScreen
                        model.currentScreen = Screen.Detail
                    }
                }
            } else {
                Log.d("SaveButton", "Bitte fülle alle Felder aus.")
            }
        },
        enabled = !isSaving
    ) {
        if (isSaving) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                "Speichern",
                style = defaultButtonTextStyle()
            )
        }
    }
}

@Composable
fun DisplayTimestamp(item: Item) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    val dateString = dateFormat.format(Date(item.timestamp))

    Text("Zeit: $dateString")
}

@Composable
fun timeAgo(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp

    return when {
        diff < TimeUnit.MINUTES.toMillis(1) -> "Jetzt"
        diff < TimeUnit.HOURS.toMillis(1) -> "vor ${TimeUnit.MILLISECONDS.toMinutes(diff)} Minuten"
        diff < TimeUnit.DAYS.toMillis(1) -> "vor ${TimeUnit.MILLISECONDS.toHours(diff)} Stunden"
        diff < TimeUnit.DAYS.toMillis(7) -> "vor ${TimeUnit.MILLISECONDS.toDays(diff)} Tagen"
        diff < TimeUnit.DAYS.toMillis(30) -> "vor ${TimeUnit.MILLISECONDS.toDays(diff) / 7} Wochen"
        else -> "Vor mehr als einem Monat"
    }
}

fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp)
    return SimpleDateFormat("dd.MM.yy 'um' HH:mm 'Uhr'", Locale.GERMAN).format(date)
}



@Composable
fun ItemRow(item: Item, db: AppDatabase, onClick: () -> Unit, model: ThingModel) {
    val itemWithFullLocation = remember { mutableStateOf<ItemWithFullLocation?>(null) }

    LaunchedEffect(item.itemID) {
        ViewModel.fetchItemWithFullLocation(db, item.itemID) { result ->
            itemWithFullLocation.value = result
        }
    }
    Divider(
        thickness = 1.dp,
        color = Color.White.copy(alpha = 0.2f)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clip(defaultShape())
            .clickable { onClick() }
    ) {

        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(androidx.compose.foundation.shape.CircleShape)
        ) {
            if (item.foto.isNotEmpty()) {
                val bitmap = model.loadImageFromPath(item.foto)
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Saved Photo",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(androidx.compose.foundation.shape.CircleShape),
                        contentScale = ContentScale.Crop // Bild korrekt zuschneiden
                    )
                } ?: Image(
                    painter = painterResource(id = R.drawable.wimmelbild),
                    contentDescription = "Platzhalter",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(androidx.compose.foundation.shape.CircleShape),
                    contentScale = ContentScale.Crop,
                    alpha = 0.3f
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.wimmelbild),
                    contentDescription = "Platzhalter",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(androidx.compose.foundation.shape.CircleShape),
                    contentScale = ContentScale.Crop,
                    alpha = 0.3f
                )
            }
        }


        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 15.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = item.name,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            itemWithFullLocation.value?.let { details ->
                val inside1 = details.inside2WithInside1WithLocation.inside1WithLocation.inside1
                val location = details.inside2WithInside1WithLocation.inside1WithLocation.location

                Text(
                    text = "${location.name} – ${inside1.name}",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }

        }

        Text(
            text = timeAgo(item.timestamp),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}


@Composable
fun RequestCameraPermission(onPermissionGranted: () -> Unit) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                Log.d("CameraDebug", "Berechtigung für Kamera erteilt.")
                onPermissionGranted()
            } else {
                Log.d("CameraDebug", "Kamera abgelehnt")
                Toast.makeText(context, "Kamera-Berechtigung abgelehnt", Toast.LENGTH_SHORT).show()
            }
        }
    )

    LaunchedEffect(Unit) {
        Log.d("CameraDebug", "Fordere Kamera-Berechtigung an.")
        launcher.launch(android.Manifest.permission.CAMERA)
    }
}