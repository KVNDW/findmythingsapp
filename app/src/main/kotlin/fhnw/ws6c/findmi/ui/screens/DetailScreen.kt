package fhnw.ws6c.findmi.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fhnw.ws6c.findmi.model.ThingModel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import fhnw.ws6c.R
import fhnw.ws6c.findmi.data.AppDatabase
import fhnw.ws6c.findmi.data.Entities.ItemWithFullLocation
import fhnw.ws6c.findmi.model.Screen
import fhnw.ws6c.findmi.model.ViewModel
import fhnw.ws6c.findmi.ui.styles.defaultButtonTextStyle
import java.util.Date


@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(model: ThingModel, db: AppDatabase, viewModel: ViewModel) {
    val item = model.selectedItem
    val itemWithFullLocation = remember { mutableStateOf<ItemWithFullLocation?>(null) }

    LaunchedEffect(item?.itemID) {
        item?.itemID?.let { id ->
            ViewModel.fetchItemWithFullLocation(db, id) { result ->
                itemWithFullLocation.value = result
            }
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Detail Ansicht",
                showBackButton = true,
                onBackClick = {
                    viewModel.selectedLocation = null
                    viewModel.selectedInside1 = null
                    viewModel.selectedInside2 = null
                    model.currentScreen = model.lastScreen
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    val bitmap = remember { model.loadImageFromPath(item?.foto ?: "") }
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Gespeichertes Foto",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.wimmelbild),
                            contentDescription = "Platzhalterbild",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                            alpha = 0.5f
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Hervorgehobener Name
                itemWithFullLocation.value?.let { details ->
                    Text(
                        text = details.item.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))

                    // Beschreibung mit Dividern und Icons
                    TextSectionWithIcon(
                        icon = Icons.Default.Info,
                        label = "Beschreibung",
                        text = details.item.description
                    )

                    Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))

                    TextSectionWithIcon(
                        icon = Icons.Default.LocationOn,
                        label = "Raum",
                        text = details.inside2WithInside1WithLocation.inside1WithLocation.location.name
                    )

                    Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))

                    TextSectionWithIcon(
                        icon = Icons.Default.Event,
                        label = "Hinzugefügt",
                        text = "${timeAgo(details.item.timestamp)}, am ${formatTimestamp(details.item.timestamp)}"
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            viewModel.selectedLocation = null
                            viewModel.selectedInside1 = null
                            viewModel.selectedInside2 = null

                            model.lastScreen = model.currentScreen
                            model.currentScreen = Screen.List
                        },
                        modifier = Modifier
                            .padding(20.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            "Liste meiner Dinge",
                            style = defaultButtonTextStyle()
                        )
                    }

                    Button(
                        onClick = {
                            model.deleteItem(item!!.itemID)
                            model.currentScreen = model.lastScreen
                        },
                        modifier = Modifier
                            .padding(20.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            "Ding löschen",
                            style = defaultButtonTextStyle()
                        )
                    }
                }
            }
        }
    )
}

// TextSection mit Icon
@Composable
fun TextSectionWithIcon(icon: ImageVector, label: String, text: String) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "$label Icon",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(end = 8.dp)
        )
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
