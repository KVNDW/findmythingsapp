import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fhnw.ws6c.R
import fhnw.ws6c.findmi.model.Screen
import fhnw.ws6c.findmi.model.ThingModel
import fhnw.ws6c.findmi.model.ViewModel
import fhnw.ws6c.findmi.ui.screens.AboutTopBar
import fhnw.ws6c.findmi.ui.screens.BottomNavigationBar
import fhnw.ws6c.findmi.ui.screens.ItemRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(model: ThingModel, viewModel: ViewModel) {
    Scaffold(
        topBar = { AboutTopBar(model) },
        bottomBar = { BottomNavigationBar(model) },
        content = {
            HomeScreenBody(model = model, viewModel = viewModel, paddingValues = it)
        }
    )
}

@Composable
private fun HomeScreenBody(model: ThingModel, viewModel: ViewModel, paddingValues: PaddingValues) {
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.initializeLists(model.db)
    }

    val latestItems = viewModel.itemList.sortedByDescending { it.timestamp }.take(4)

    // Scrollbarer Bereich
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // Hintergrundbild und Button in einer Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f) // Verhältnis statt fester Höhe
        ) {
            Image(
                painter = painterResource(id = R.drawable.wimmelbild),
                contentDescription = "Hintergrundbild",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.7f
            )

            Button(
                onClick = { model.currentScreen = Screen.Add },
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.Center),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "+",
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 60.sp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(50.dp))

        // Abschnitt "Zuletzt hinzugefügt"
        Text(
            "Zuletzt hinzugefügt",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 20.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Zuletzt hinzugefügte Items
        if (latestItems.isNotEmpty()) {
            latestItems.forEach { item ->
                ItemRow(
                    item = item,
                    db = model.db,
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
}
