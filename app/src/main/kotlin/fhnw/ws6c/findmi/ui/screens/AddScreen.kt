package fhnw.ws6c.findmi.ui.screens
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fhnw.ws6c.findmi.model.Screen
import fhnw.ws6c.findmi.model.ThingModel
import fhnw.ws6c.findmi.model.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap

@Composable
fun AddScreen(model: ThingModel) {
    LaunchedEffect(Unit) {
        model.photo = null
    }
    ModalNavigationDrawer(
        drawerContent = {},
        content = {
            Scaffold(
                topBar = { AboutTopBar(model) },
                bottomBar = { BottomNavigationBar(model) },
                content = {
                    Body(model, viewModel = ViewModel, paddingValues = it)
                }
            )
        }
    )
    BackHandler(enabled = true) {
        model.currentScreen = Screen.List
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Body(model: ThingModel, viewModel: ViewModel, paddingValues: PaddingValues) {
    val scrollState = rememberScrollState()
    val itemName = remember { mutableStateOf("") }
    val itemDescription = remember { mutableStateOf("") }
    val photoPath = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.initializeLists(model.db)
    }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(start = 10.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        VerticalSpacer(16)
//        Text("Foto:",
//            style = MaterialTheme.typography.titleMedium,
//            modifier = Modifier.padding(start = 20.dp))

        if (!model.isCameraGranted) {
            RequestCameraPermission(
                onPermissionGranted = {
                    model.isCameraGranted = true
                }
            )
        } else {
            if (model.photo == null) {
                IconButton(
                    onClick = {
                        model.takePhoto { capturedPath ->
                            photoPath.value = capturedPath
                        }
                    },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = "Kamera starten",
                        modifier = Modifier.size(150.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                Image(
                    bitmap = model.photo!!.asImageBitmap(),
                    contentDescription = "Gemachtes Foto",
                    modifier = Modifier
                        .size(128.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .padding(8.dp)
                )
            }
        }

        VerticalSpacer(16)

        Text(
            "Was?",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 20.dp)
        )

        LabeledTextField("Ding", itemName)


        LabeledTextField("Details", itemDescription)
        VerticalSpacer(20)

        photoPath.value?.let {
        }

        VerticalSpacer(16)

        Text("Wo?",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 20.dp))

        Somelist(
            someList = viewModel.locationList,
            placeholder = "Raum",
            itemLabel = { it.name },
            itemId = { it.locationID },
            onItemSelected = { selectedId ->
                val selectedLocation =
                    viewModel.locationList.find { it.locationID == selectedId }
                viewModel.selectedLocation = selectedLocation
                if (selectedLocation != null) {
                    viewModel.fetchInside1ByLocation(model.db, selectedLocation.locationID)
                }
            }
        )

        Somelist(
            someList = viewModel.filteredInside1List,
            placeholder = "Möbel",
            itemLabel = { it.name },
            itemId = { it.inside1ID },
            onItemSelected = { selectedId ->
                val selectedInside1 =
                    viewModel.filteredInside1List.find { it.inside1ID == selectedId }
                viewModel.selectedInside1 = selectedInside1
                if (selectedInside1 != null) {
                    viewModel.fetchInside2ByInside1(model.db, selectedInside1.inside1ID)
                }
            }
        )

        Somelist(
            someList = viewModel.filteredInside2List,
            placeholder = "Detail",
            itemLabel = { it.name },
            itemId = { it.inside2ID },
            onItemSelected = { selectedId ->
                viewModel.selectedInside2 =
                    viewModel.filteredInside2List.find { it.inside2ID == selectedId }
            }
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SaveButton(
                itemName = itemName,
                itemDescription = itemDescription,
                viewModel = viewModel,
                model = model,
                photoPath = photoPath.value
            )
        }
        VerticalSpacer(20)
    }
}

