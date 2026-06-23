package com.example.skillforge.ui.coursedetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.skillforge.data.repository.SkillforgeRepository
import com.example.skillforge.ui.components.LevelBadge
import com.example.skillforge.ui.components.LessonRow
import com.example.skillforge.ui.theme.TealGradientEnd
import com.example.skillforge.ui.theme.TealGradientStart
import com.example.skillforge.ui.theme.TealPrimary

import com.example.skillforge.SetStatusBarColor

@Composable
fun CourseDetailScreen(
    courseId: String,
    repository: SkillforgeRepository,
    onNavigateBack: () -> Unit,
    onNavigateToLesson: (String) -> Unit
) {
    SetStatusBarColor(darkIcons = false)
    val course = repository.getCourseById(courseId)

    if (course == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Course not found")
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // space for bottom bar
        ) {
            item {
                HeroBanner(
                    title = course.title,
                    category = course.categoryName,
                    onNavigateBack = onNavigateBack
                )
            }
            
            item {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = course.title,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Everything you need to start learning ${course.title.substringBefore(" ")}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    CourseStats(rating = course.rating, duration = course.durationHours, level = course.level)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    InstructorCard(
                        name = course.instructor.name,
                        title = course.instructor.title,
                        avatarUrl = course.instructor.avatarUrl
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Start from zero and learn the core concepts. By the end you'll be comfortable reading and writing idiomatic code.",
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = 22.sp
                    )
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Course content",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "${course.totalLessons} lessons · ${course.totalDurationMinutes} min",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            
            items(course.lessons) { lesson ->
                LessonRow(
                    lesson = lesson,
                    onClick = { if (lesson.isFree) onNavigateToLesson(lesson.id) },
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )
            }
        }
        
        // Bottom Fixed Bar
        Surface(
            modifier = Modifier.align(Alignment.BottomCenter),
            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("PRICE", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text("Free", style = MaterialTheme.typography.titleLarge.copy(color = TealPrimary))
                }
                
                Button(
                    onClick = { /* Enroll */ },
                    colors = ButtonDefaults.buttonColors(containerColor = TealPrimary),
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f)
                        .padding(start = 24.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Enroll now", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}

@Composable
fun HeroBanner(title: String, category: String, onNavigateBack: () -> Unit) {
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
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White)
                    .size(40.dp)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
            }
            
            IconButton(
                onClick = { /* Bookmark */ },
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White)
                    .size(40.dp)
            ) {
                Icon(Icons.Default.BookmarkBorder, contentDescription = "Bookmark", tint = MaterialTheme.colorScheme.onBackground)
            }
        }
        
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text("// ${category.lowercase()}", color = Color.White, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 28.sp, color = Color.White)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Mock tags
                TagChip("Kotlin")
                TagChip("Basics")
                TagChip("JVM")
            }
        }
    }
}

@Composable
fun TagChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.2f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text, color = Color.White, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
fun CourseStats(rating: Double, duration: Double, level: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF2994A), modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(rating.toString(), style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground))
        Spacer(modifier = Modifier.width(16.dp))
        
        Icon(Icons.Default.Schedule, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("${duration}h", style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.width(16.dp))
        
        LevelBadge(level = level)
    }
}

@Composable
fun InstructorCard(name: String, title: String, avatarUrl: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(TealPrimary),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    onError = {
                        // Fallback will just show the background
                    }
                )
                // Fallback text if image fails or is empty, but we'll let it be.
                Text("AS", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(name, style = MaterialTheme.typography.titleMedium)
                Text(title, style = MaterialTheme.typography.bodyMedium)
            }
            
            Text("Follow", color = TealPrimary, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold))
        }
    }
}
