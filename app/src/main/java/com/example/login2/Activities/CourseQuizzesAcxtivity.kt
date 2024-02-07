package com.example.login2.Activities

import androidx.annotation.StringRes
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.login2.ViewModels.QuizViewModel
import com.example.login2.ui.theme.TeachUp_QuizTheme


enum class QuizScreen(@StringRes val title: Int) {
	Start(title = 2),
	inQuiz(title = 1),
}

@Composable
fun CourseQuizzesScreen(
	quizViewModel: QuizViewModel = viewModel(),
	navController: NavHostController = rememberNavController()
) {
	val quizUiState by quizViewModel.uiState.collectAsState()

	Scaffold(
//		topBar = {
//			QuizzesAppBar()
//		}
	) { innerPadding ->
//		val quizUiState by quizViewModel.uiState.collectAsState()
		NavHost(
			navController = navController,
			startDestination = QuizScreen.Start.name,
			modifier = Modifier.padding(innerPadding)
		) {
			composable(route = QuizScreen.Start.name) {
				CourseQuizzesListScreen(
					onClicked = {
						quizViewModel.setQuiz(it)
						navController.navigate(QuizScreen.inQuiz.name)
					}

				)
			}

			composable(route = QuizScreen.inQuiz.name) {
				QuizScreen(
					quizViewModel,
				)
			}
		}
	}
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