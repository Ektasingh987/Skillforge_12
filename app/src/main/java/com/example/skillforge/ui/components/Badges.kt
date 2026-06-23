package com.example.skillforge.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skillforge.ui.theme.BadgeBeginner
import com.example.skillforge.ui.theme.BadgeIntermediate
import com.example.skillforge.ui.theme.TealPrimary

@Composable
fun LevelBadge(level: String, modifier: Modifier = Modifier) {
    val color = when (level.lowercase()) {
        "beginner" -> BadgeBeginner
        "intermediate" -> BadgeIntermediate
        else -> TextColorForBadge(level)
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = level.uppercase(),
            color = color,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )
        )
    }
}

@Composable
fun FreeBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(TealPrimary.copy(alpha = 0.1f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = "FREE",
            color = TealPrimary,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )
        )
    }
}

private fun TextColorForBadge(text: String): Color {
    // Simple fallback
    return TealPrimary
}
