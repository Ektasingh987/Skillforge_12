package com.example.skillforge.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skillforge.data.model.Category
import com.example.skillforge.ui.theme.CardBackground

@Composable
fun CategoryCard(
    category: Category,
    colorHint: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(140.dp)
            .height(120.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colorHint.copy(alpha = 0.2f))
            ) {
                // simple colored box, can put icon here
                Box(modifier = Modifier.fillMaxSize().padding(8.dp).clip(RoundedCornerShape(4.dp)).background(colorHint))
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Column {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
                    maxLines = 2,
                    lineHeight = 18.sp
                )
                Text(
                    text = "${category.courses.size} courses",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}
