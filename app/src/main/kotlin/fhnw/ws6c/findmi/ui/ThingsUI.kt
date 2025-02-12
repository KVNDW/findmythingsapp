package fhnw.ws6c.findmi.ui

import HomeScreen
import androidx.compose.animation.Crossfade
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import fhnw.ws6c.findmi.model.Screen
import fhnw.ws6c.findmi.model.ThingModel
import fhnw.ws6c.findmi.model.ViewModel
import fhnw.ws6c.findmi.model.ViewModel.reset
import fhnw.ws6c.findmi.ui.screens.DetailScreen
import fhnw.ws6c.findmi.ui.screens.ListScreen
import fhnw.ws6c.findmi.ui.screens.AddScreen


@Composable
fun AppUI(model : ThingModel){
    with(model){
        MaterialTheme {
            Crossfade(targetState = currentScreen) { screen ->
                when (screen) {
                    Screen.Home -> { HomeScreen(model = model, viewModel = ViewModel)
                        reset()}
                    Screen.List -> { ListScreen(model, db)
                        reset()}
                    Screen.Add -> { AddScreen(model) }
                    Screen.Detail -> {DetailScreen(model, db, viewModel = ViewModel)
                        reset()}
                }
            }
        }
    }
}