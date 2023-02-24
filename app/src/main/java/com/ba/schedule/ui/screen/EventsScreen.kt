package com.ba.schedule.ui.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ba.schedule.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.events)) }
            )
        }
    ) {
        it
    }
}