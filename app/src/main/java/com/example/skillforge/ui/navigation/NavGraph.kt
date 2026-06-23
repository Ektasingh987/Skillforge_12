package com.example.skillforge.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.skillforge.data.repository.SkillforgeRepository
import com.example.skillforge.ui.components.RepositoryViewModelFactory
import com.example.skillforge.ui.coursedetail.CourseDetailScreen
import com.example.skillforge.ui.home.HomeScreen
import com.example.skillforge.ui.home.HomeViewModel
import com.example.skillforge.ui.lesson.LessonPlayerScreen

@Composable
fun SkillforgeNavGraph(
    repository: SkillforgeRepository,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = "home") {
        
        composable("home") {
            val homeViewModel: HomeViewModel = viewModel(
                factory = RepositoryViewModelFactory(repository) { HomeViewModel(it) }
            )
            HomeScreen(
                viewModel = homeViewModel,
                onNavigateToCourse = { courseId ->
                    navController.navigate("course/$courseId")
                }
            )
        }
        
        composable(
            route = "course/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId") ?: return@composable
            CourseDetailScreen(
                courseId = courseId,
                repository = repository,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLesson = { lessonId ->
                    navController.navigate("lesson/$courseId/$lessonId")
                }
            )
        }
        
        composable(
            route = "lesson/{courseId}/{lessonId}",
            arguments = listOf(
                navArgument("courseId") { type = NavType.StringType },
                navArgument("lessonId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId") ?: return@composable
            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: return@composable
            
            LessonPlayerScreen(
                courseId = courseId,
                initialLessonId = lessonId,
                repository = repository,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
