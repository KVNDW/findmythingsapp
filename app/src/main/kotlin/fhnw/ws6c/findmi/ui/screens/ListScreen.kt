package fhnw.ws6c.findmi.ui.screens
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fhnw.ws6c.findmi.model.Screen
import fhnw.ws6c.findmi.model.ThingModel
import fhnw.ws6c.findmi.model.ViewModel
import fhnw.ws6c.findmi.data.AppDatabase
import fhnw.ws6c.findmi.ui.styles.defaultButtonTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(model: ThingModel, db: AppDatabase) {

    LaunchedEffect(Unit) {
        ViewModel.reset()
    }

    Scaffold(
        topBar = { AboutTopBar(model) },
        bottomBar = { BottomNavigationBar(model) },
        content = { paddingValues ->
            key(model.screenKey) {
                ListScreenBody(model = model, viewModel = ViewModel, paddingValues = paddingValues, db = db)
            }
        }
    )

    BackHandler(enabled = true) {
        model.currentScreen = Screen.Home
    }
}

@Composable
private fun ListScreenBody(model: ThingModel, viewModel: ViewModel, paddingValues: PaddingValues, db: AppDatabase) {
    LaunchedEffect(Unit) {
        viewModel.fetchAllItems(db)
        viewModel.initializeLists(db)
    }

    var searchQuery by remember { mutableStateOf("") }
    val filteredItems = viewModel.getFilteredItems(searchQuery)

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Suche und filtere meine Dinge",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 20.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Such-Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 8.dp) // Abstand zum Text
                    )
                    Text("Suche")
                }
            },
            textStyle = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .background(MaterialTheme.colorScheme.background),
            shape = MaterialTheme.shapes.medium,
            singleLine = true,
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


        Somelist(
            someList = viewModel.locationList,
            placeholder = "Filtere nach Raum",
            itemLabel = { it.name },
            itemId = { it.locationID },
            onItemSelected = {
                viewModel.selectedLocation = viewModel.locationList.find { loc -> loc.locationID == it }
                viewModel.fetchInside1ByLocation(db, it)
            },
        )

        Somelist(
            someList = viewModel.filteredInside1List,
            placeholder = "Filtere nach Möbel",
            itemLabel = { it.name },
            itemId = { it.inside1ID },
            onItemSelected = {
                viewModel.selectedInside1 = viewModel.filteredInside1List.find { inside1 -> inside1.inside1ID == it }
                viewModel.fetchInside2ByInside1(db, it)
            }
        )

        Somelist(
            someList = viewModel.filteredInside2List,
            placeholder = "Filtere nach Möbel Detail",
            itemLabel = { it.name },
            itemId = { it.inside2ID },
            onItemSelected = {
                viewModel.selectedInside2 = viewModel.filteredInside2List.find { inside2 -> inside2.inside2ID == it }
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    viewModel.selectedLocation = null
                    viewModel.selectedInside1 = null
                    viewModel.selectedInside2 = null

                    viewModel.filteredInside1List = emptyList()
                    viewModel.filteredInside2List = emptyList()
                    viewModel.fetchAllItems(db)

                    model.resetListScreen()
                },
                modifier = Modifier
                    .padding(top = 20.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {

                Text(
                    "Filter zurücksetzen",
                    style = defaultButtonTextStyle()
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            "Liste meiner Dinge",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 20.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Gefilterte Items anzeigen
        filteredItems.forEach { item ->
            ItemRow(
                item = item,
                db = db,
                onClick = {
                    model.lastScreen = model.currentScreen
                    model.selectedItem = item
                    model.currentScreen = Screen.Detail
                },
                model = model
            )
        }
    }
}

