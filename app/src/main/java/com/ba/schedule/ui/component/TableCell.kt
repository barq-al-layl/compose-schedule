package com.ba.schedule.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TableCell(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    emptyContainerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    content: String = "",
    enabled: Boolean = false,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(
                color = emptyContainerColor
                    .copy(alpha = .2f)
                    .takeIf { content.isEmpty() }
                    ?: containerColor.copy(alpha = .5f),
            )
            .clickable(enabled = enabled, onClick = onClick)
            .padding(all = 4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = content, fontSize = 12.sp)
    }
}