package com.ba.schedule.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ba.schedule.ui.navigation.HomeSection

@Composable
fun ScheduleBottomBar(
    tabs: Array<HomeSection>,
    currentRoute: String,
    navigateToRoute: (String) -> Unit,
) {
    NavigationBar {
        tabs.forEach {
            NavigationBarItem(
                selected = it.route == currentRoute,
                onClick = { navigateToRoute(it.route) },
                icon = {
                    Icon(
                        imageVector = it.icon,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                    )
                },
                label = { Text(text = it.name) },
            )
        }
    }
}