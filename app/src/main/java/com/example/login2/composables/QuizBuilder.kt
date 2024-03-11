package com.example.login2.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.login2.R
import com.example.login2.ViewModels.QuizBuilderViewModel
import com.example.login2.ui.theme.TeachUp_QuizTheme


@Composable
fun QuizBuilder(
	onSubmitQuizClicked: () -> Unit,
) {
	TeachUp_QuizTheme {
		Scaffold(topBar = {
			QuizBuilderAppBar()
		}) { innerPadding ->
			Column(modifier = Modifier.padding(innerPadding)) {
				QuizBuilderScreen(onSubmitQuizClicked = onSubmitQuizClicked)
			}
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizBuilderAppBar(
	modifier: Modifier = Modifier,
) {
	TopAppBar(
		title = { Text(text = "Quiz Builder") },
		colors = TopAppBarDefaults.mediumTopAppBarColors(
			containerColor = colorResource(id = R.color.PrimaryBlue),
			titleContentColor = Color.White
		),
		modifier = modifier,
	)
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun QuizBuilderScreen(
	quizBuilderViewModel: QuizBuilderViewModel = viewModel(),
	onSubmitQuizClicked: () -> Unit,
) {
	val quizBuilderUiState by quizBuilderViewModel.uiState.collectAsState()
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp)
	) {
		Column {
			// Quiz number of questions
			Text(
				modifier = Modifier
					.clip(MaterialTheme.shapes.medium)
					.background(MaterialTheme.colorScheme.surfaceTint)
					.padding(horizontal = 10.dp, vertical = 4.dp)
					.align(alignment = Alignment.End),
				text = "${quizBuilderUiState.currentQuestionNumber}/${quizBuilderUiState.quizNumberOfQuestions}",
				style = MaterialTheme.typography.titleMedium,
				color = MaterialTheme.colorScheme.onPrimary
			)

			// Quiz Title
			OutlinedTextField(
				value = quizBuilderUiState.quiz.quizTitle,
				onValueChange = { quizBuilderViewModel.setQuizTitle(it) },
				label = { Text("Quiz Title") },
				maxLines = 2,
				modifier = Modifier
					.fillMaxWidth()
					.padding(bottom = 8.dp)
					.verticalScroll(rememberScrollState())
			)

			// Question Title
			OutlinedTextField(
				value = quizBuilderUiState.currentQuestionTitle,
				onValueChange = { quizBuilderViewModel.setQuestionTitle(it) },
				label = { Text("Question Title") },
				maxLines = 3,
				modifier = Modifier
					.fillMaxWidth()
					.padding(bottom = 8.dp)
					.verticalScroll(rememberScrollState())
			)
		}

		QuestionOptionsList(modifier = Modifier
			.fillMaxHeight()
			.weight(1f),
		                    options = quizBuilderUiState.currentQuestionOptions,
		                    selectedOptionIndex = quizBuilderUiState.correctAnswerIndex,
		                    onSelectionChanged = { quizBuilderViewModel.setCorrectAnswer(it) },
		                    onOptionsChange = { index, newValue ->
			                    quizBuilderViewModel.setOptions(index, newValue)
		                    })

		// Navigation between questions and submitting buttons
		Column {
			Spacer(modifier = Modifier.padding(8.dp))
			if (quizBuilderUiState.currentQuestionNumber == quizBuilderUiState.quizNumberOfQuestions) {
				Button(
					onClick = { quizBuilderViewModel.onAddQuestionButtonClicked() },
					enabled = quizBuilderUiState.isQuestionReady,
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = 16.dp)
				) {
					Text("Add Question")
				}
			}
			Row(
				modifier = Modifier.fillMaxWidth()
			) {
				Button(
					onClick = { quizBuilderViewModel.onBackButtonClicked() },
					enabled = quizBuilderUiState.currentQuestionNumber != 1,
					modifier = Modifier
						.padding(horizontal = 16.dp)
						.weight(1f)
				) {
					Text("Back")
				}

				Button(
					onClick = { quizBuilderViewModel.onNextButtonClicked() },
					enabled = quizBuilderUiState.currentQuestionNumber < quizBuilderUiState.quizNumberOfQuestions,
					modifier = Modifier
						.padding(horizontal = 16.dp)
						.weight(1f)
				) {
					Text("Next")
				}
			}
		}

		Column {
			Button(
				onClick = {
					quizBuilderViewModel.onSubmit()
					onSubmitQuizClicked()
				},
				enabled = quizBuilderUiState.isQuizBuilderDone,
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 16.dp)
			) {
				Text("Submit")
			}
		}
	}
}

@Composable
fun QuestionOptionsList(
	modifier: Modifier = Modifier,
	options: List<String>,
	selectedOptionIndex: Int,
	onSelectionChanged: (Int) -> Unit,
	onOptionsChange: (Int, String) -> Unit,
) {
	LazyColumn(
		modifier = modifier
	) {
		items(options.size) { index ->
			Row(modifier = Modifier
				.clickable {
					onSelectionChanged(index)
				}
				.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
				RadioButton(selected = selectedOptionIndex == index, onClick = {
					onSelectionChanged(index)
				})
				OutlinedTextField(value = options[index],
				                  onValueChange = { newValue ->
					                  onOptionsChange(index, newValue)
				                  },
				                  label = { Text("Option ${(index + 1)}") },
				                  maxLines = 2,
				                  modifier = Modifier
					                  .fillMaxWidth()
					                  .background(Color.Transparent)
				)
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
fun QuizBuilderExamplePreview() {
	TeachUp_QuizTheme {
//		QuizBuilder()
	}
}