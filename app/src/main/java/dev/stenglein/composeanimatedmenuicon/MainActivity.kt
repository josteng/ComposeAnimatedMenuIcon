package dev.stenglein.composeanimatedmenuicon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.stenglein.composeanimatedmenuicon.ui.theme.ComposeAnimatedMenuIconTheme


/**
 * Main activity of the app that displays the sample.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnimatedMenuIconSample()
        }
    }
}


/**
 * Sample usage of [AnimatedMenuIcon].
 */
@Composable
private fun AnimatedMenuIconSample() {
    ComposeAnimatedMenuIconTheme {
        var isMenuOpened by remember { mutableStateOf(false) }
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .clickable { isMenuOpened = !isMenuOpened },
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                AnimatedMenuIcon(isMenuOpened = isMenuOpened, sizeInDp = 96.dp)
                Text("Tap anywhere to toggle the icon.")
            }
        }
    }
}