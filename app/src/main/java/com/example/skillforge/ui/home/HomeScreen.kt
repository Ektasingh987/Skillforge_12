package com.example.skillforge.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skillforge.ui.components.CategoryCard
import com.example.skillforge.ui.components.CourseCard
import com.example.skillforge.ui.components.ErrorView
import com.example.skillforge.ui.components.LoadingView
import com.example.skillforge.ui.theme.TealPrimary
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.skillforge.SetStatusBarColor

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToCourse: (String) -> Unit
) {
    SetStatusBarColor(darkIcons = true)
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is HomeUiState.Loading -> LoadingView()
        is HomeUiState.Error -> ErrorView(message = state.message, onRetry = { viewModel.fetchData() })
        is HomeUiState.Success -> HomeContent(state = state, onNavigateToCourse = onNavigateToCourse)
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    state: HomeUiState.Success,
    onNavigateToCourse: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        item {
            HomeHeader()
        }
        
        item {
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it }
            )
        }
        
        item {
            SectionHeader(title = "Categories", actionText = "See all")
            LazyRow(
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.categories) { category ->
                    val colorHint = if (category.name.contains("Backend", true)) Color(0xFF4ADE80) else Color(0xFFFBBF24)
                    CategoryCard(category = category, colorHint = colorHint)
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(24.dp))
            SectionHeader(title = "Popular courses", actionText = "See all")
        }
        
        val allCourses = state.categories.flatMap { it.courses }
        val displayedCourses = if (searchQuery.isBlank()) {
            allCourses
        } else {
            allCourses.filter { it.title.contains(searchQuery, ignoreCase = true) || it.categoryName.contains(searchQuery, ignoreCase = true) }
        }
        
        items(displayedCourses) { course ->
            CourseCard(
                course = course,
                onClick = { onNavigateToCourse(course.id) },
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
            )
        }
        
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun HomeHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Welcome back",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Find your next skill",
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 28.sp, lineHeight = 34.sp)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = MaterialTheme.colorScheme.onBackground)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(TealPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "AS",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp)),
        placeholder = { Text("Search courses, topics...", color = MaterialTheme.colorScheme.onSurfaceVariant) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true
    )
    Spacer(modifier = Modifier.height(32.dp))
}

@Composable
fun SectionHeader(title: String, actionText: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = actionText,
            color = TealPrimary,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
        )
    }
}
