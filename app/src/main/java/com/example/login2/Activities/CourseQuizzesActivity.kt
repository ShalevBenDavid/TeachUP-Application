package com.example.login2.Activities

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.example.login2.ViewModels.QuizViewModel
import com.example.login2.ui.theme.TeachUp_QuizTheme


enum class QuizzesScreen(val title: String) {
	Start(title = "list of quizzes"),
	InQuiz(title = "in quiz"),
	CreateNewQuiz(title = "create a new quiz"),
}

@Composable
fun CourseQuizzesScreen(
	quizViewModel: QuizViewModel = viewModel(),
	navController: NavHostController = rememberNavController()
) {
	val quizUiState by quizViewModel.uiState.collectAsState()

//	Scaffold(
//		topBar = {
//			QuizzesAppBar()
//		}
//	) { innerPadding ->

//		val navGraph by remember(navController) {
//
//		}
//		navController.createGraph(startDestination = "profile") {
//
//		}
		NavHost(
			navController = navController,
			startDestination = QuizzesScreen.Start.name,
//			modifier = Modifier.padding(innerPadding)
		) {
			composable(route = QuizzesScreen.Start.name) {
				CourseQuizzesListScreen(
					onClicked = {
						quizViewModel.setQuiz(it)
						navController.navigate(QuizzesScreen.InQuiz.name)
					},
					onAddQuizClicked = {
						navController.navigate(QuizzesScreen.CreateNewQuiz.name) {
//							popUpTo(QuizzesScreen.Start.name) { inclusive = true }
						}
					}
				)
			}

			composable(route = QuizzesScreen.InQuiz.name) {
				QuizScreen(
					quizViewModel,
					onExitClicked = {
						navController.navigate(QuizzesScreen.Start.name) {
							popUpTo(QuizzesScreen.Start.name) { inclusive = true }
						}
					}
				)
			}

			composable(route = QuizzesScreen.CreateNewQuiz.name) {
				QuizBuilder(
					onSubmitQuizClicked = {
						navController.navigate(QuizzesScreen.Start.name) {
							popUpTo(QuizzesScreen.Start.name) { inclusive = true }
						}
//						quizViewModel.getQuizzesFromDB()
					}
				)
			}
		}
//	}
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun QuizzesAppBar(
	modifier: Modifier = Modifier
) {
	TopAppBar(
		title = {Text(text = "Quizzes111")},
		colors = TopAppBarDefaults.mediumTopAppBarColors(
			containerColor = MaterialTheme.colorScheme.primaryContainer
		),
		modifier = modifier,
	)
}

@Preview(showBackground = true)
@Composable
fun QuizzesPreview() {
	TeachUp_QuizTheme {
//		CourseQuizzesListScreen()
	}
}