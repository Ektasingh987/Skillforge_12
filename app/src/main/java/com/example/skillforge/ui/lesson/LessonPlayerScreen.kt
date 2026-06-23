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

@androidx.annotation.OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun VideoPlayerPlaceholder(category: String, onNavigateBack: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val exoPlayer = remember {
        androidx.media3.exoplayer.ExoPlayer.Builder(context).build().apply {
            val videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
            setMediaItem(androidx.media3.common.MediaItem.fromUri(videoUrl))
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 280.dp)
            .background(Color.Black)
    ) {
        androidx.compose.ui.viewinterop.AndroidView(
            factory = { ctx ->
                androidx.media3.ui.PlayerView(ctx).apply {
                    player = exoPlayer
                    useController = true
                    setShowNextButton(false)
                    setShowPreviousButton(false)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 280.dp)
                .statusBarsPadding()
        )

        // Overlay top bar for back button and category
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
                    .background(Color.Black.copy(alpha = 0.5f))
                    .size(40.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .background(Color.Black.copy(alpha = 0.5f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text("// ${category.lowercase()}", color = Color.White, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
            }
            
            Spacer(modifier = Modifier.size(40.dp)) // Empty space for symmetry
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
