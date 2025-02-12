package fhnw.ws6c.findmi.ui.styles

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight

@Composable
fun defaultButtonModifier() = Modifier
    .padding(horizontal = 16.dp, vertical = 8.dp)
    .clip(defaultShape())

@Composable
fun defaultButtonColors(): ButtonColors = ButtonDefaults.buttonColors(
    containerColor = MaterialTheme.colorScheme.primary,
    contentColor = MaterialTheme.colorScheme.background
)

@Composable
fun defaultButtonTextStyle(): TextStyle = MaterialTheme.typography.titleMedium.copy(
    fontWeight = FontWeight.Bold
)