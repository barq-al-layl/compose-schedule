package com.ba.schedule.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ba.schedule.ui.theme.alphaAtHalf

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TableCell(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    selectedColor: Color = MaterialTheme.colorScheme.errorContainer,
    emptyContainerColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    content: String = "",
    fontWeight: FontWeight = FontWeight.Normal,
    enabled: Boolean = false,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(
                color = when {
                    isSelected -> selectedColor.copy(alpha = .4f)
                    content.isNotEmpty() -> containerColor.copy(alpha = alphaAtHalf)
                    else -> emptyContainerColor.copy(alpha = .2f)
                }
            )

            .combinedClickable(
                enabled = enabled,
                onClick = onClick,
                onLongClick = onLongClick,
                onDoubleClick = onLongClick,
            )
            .padding(all = 4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = content,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            fontWeight = fontWeight,
            lineHeight = 14.sp,
        )
    }
}