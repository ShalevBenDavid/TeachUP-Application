package com.example.login2.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.login2.ViewModels.QuizViewModel
import com.example.login2.ui.theme.TeachUp_QuizTheme


enum class QuizzesScreen(val title: String) {
	Start(title = "list of quizzes"), InQuiz(title = "in quiz"), CreateNewQuiz(title = "create a new quiz"),
}

@Composable
fun CourseQuizzesScreen(
	quizViewModel: QuizViewModel = viewModel(),
	navController: NavHostController = rememberNavController(),
) {
	NavHost(
		navController = navController,
		startDestination = QuizzesScreen.Start.name,
	) {
		composable(route = QuizzesScreen.Start.name) {
			CourseQuizzesListScreen(onClicked = {
				quizViewModel.setQuiz(it)
				navController.navigate(QuizzesScreen.InQuiz.name)
			}, onAddQuizClicked = {
				navController.navigate(QuizzesScreen.CreateNewQuiz.name) { }
			})
		}

		composable(route = QuizzesScreen.InQuiz.name) {
			SolveQuizScreen(quizViewModel, onExitClicked = {
				navController.navigate(QuizzesScreen.Start.name) {
					popUpTo(QuizzesScreen.Start.name) { inclusive = true }
				}
			})
		}

		composable(route = QuizzesScreen.CreateNewQuiz.name) {
			QuizBuilder(
				onSubmitQuizClicked = {
					navController.navigate(QuizzesScreen.Start.name) {
						popUpTo(QuizzesScreen.Start.name) { inclusive = true }
					}
				}
			)
		}
	}
}

@Preview(showBackground = true)
@Composable
fun QuizzesPreview() {
	TeachUp_QuizTheme {
//		CourseQuizzesListScreen()
	}
}