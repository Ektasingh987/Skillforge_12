package com.example.skillforge.ui.lesson

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skillforge.data.repository.SkillforgeRepository
import com.example.skillforge.ui.components.LessonRow
import com.example.skillforge.ui.theme.TealGradientEnd
import com.example.skillforge.ui.theme.TealGradientStart
import com.example.skillforge.ui.theme.TealPrimary

import com.example.skillforge.SetStatusBarColor

@Composable
fun LessonPlayerScreen(
    courseId: String,
    initialLessonId: String,
    repository: SkillforgeRepository,
    onNavigateBack: () -> Unit
) {
    SetStatusBarColor(darkIcons = false)
    val course = repository.getCourseById(courseId)
    var currentLessonId by remember { mutableStateOf(initialLessonId) }

    if (course == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Course not found")
        }
        return
    }

    val currentLesson = course.lessons.find { it.id == currentLessonId } ?: course.lessons.first()

    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        VideoPlayerPlaceholder(
            category = course.categoryName,
            onNavigateBack = onNavigateBack
        )
        
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "LESSON ${currentLesson.orderIndex + 1} · ${course.title.uppercase()}",
                color = TealPrimary,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = currentLesson.title,
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = currentLesson.content.ifEmpty { "Set up your environment and run your first code snippet." },
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 22.sp
            )
        }
        
        TabsSection()
        
        LazyColumn(
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            items(course.lessons) { lesson ->
                LessonRow(
                    lesson = lesson,
                    isPlaying = lesson.id == currentLessonId,
                    onClick = {
                        if (lesson.isFree) {
                            currentLessonId = lesson.id
                        }
                    },
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun VideoPlayerPlaceholder(category: String, onNavigateBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 280.dp)
            .background(Brush.verticalGradient(listOf(TealGradientStart, TealGradientEnd)))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.3f))
                    .size(40.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .background(Color.Black.copy(alpha = 0.3f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text("// ${category.lowercase()}", color = Color.White, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
            }
            
            IconButton(
                onClick = { /* Fullscreen */ },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.3f))
                    .size(40.dp)
            ) {
                Icon(Icons.Default.Fullscreen, contentDescription = "Fullscreen", tint = Color.White)
            }
        }
        
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(64.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable { /* Toggle Play/Pause */ },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = "Play", tint = TealPrimary, modifier = Modifier.size(32.dp))
        }
        
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("02:14", color = Color.White, style = MaterialTheme.typography.labelMedium)
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .background(Color.White.copy(alpha = 0.3f))
            ) {
                Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.3f).background(Color.White))
                Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(Color.White).align(Alignment.CenterStart).offset(x = 100.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("06:00", color = Color.White, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
fun TabsSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(end = 24.dp)) {
            Text("Lessons", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.height(4.dp))
            Box(modifier = Modifier.height(2.dp).width(24.dp).background(TealPrimary))
        }
        Text("Notes", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(end = 24.dp))
        Text("Resources", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
    Spacer(modifier = Modifier.height(16.dp))
}
