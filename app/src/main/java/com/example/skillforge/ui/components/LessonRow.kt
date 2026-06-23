package com.example.skillforge.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.skillforge.data.model.Lesson
import com.example.skillforge.ui.theme.CardBackground
import com.example.skillforge.ui.theme.TealPrimary

@Composable
fun LessonRow(
    lesson: Lesson,
    isPlaying: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isPlaying) TealPrimary.copy(alpha = 0.1f) else CardBackground
    val borderColor = if (isPlaying) TealPrimary.copy(alpha = 0.3f) else Color.Transparent

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = lesson.isFree || isPlaying) { onClick() }
            .border(1.dp, borderColor, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isPlaying) 0.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (lesson.isFree) TealPrimary.copy(alpha = 0.1f) else Color(0xFFF0F0F0)),
                contentAlignment = Alignment.Center
            ) {
                if (!lesson.isFree && !isPlaying) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        tint = TealPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = lesson.title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                if (isPlaying) {
                    Text(
                        text = "Now playing · ${lesson.durationMinutes} min",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TealPrimary
                    )
                } else {
                    Text(
                        text = "${lesson.durationMinutes} min",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if (lesson.isFree && !isPlaying) {
                FreeBadge()
            }
        }
    }
}
