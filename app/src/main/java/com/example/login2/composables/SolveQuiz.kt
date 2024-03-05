package com.example.login2.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.login2.R
import com.example.login2.ViewModels.QuizViewModel
import com.example.login2.ui.theme.TeachUp_QuizTheme

@Composable
fun SolveQuizScreen(
	quizViewModel: QuizViewModel,
	onExitClicked: () -> Unit,
) {
	Image(
		painter = painterResource(R.drawable.bg2),
		contentDescription = null,
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.primaryContainer),
		contentScale = ContentScale.FillBounds
	)
	val quizUiState by quizViewModel.uiState.collectAsState()

	if (!quizUiState.isQuizDone) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(16.dp)
		) {
			LinearProgressIndicator(
				progress = quizUiState.currentQuestionNumber / quizUiState.quizNumberOfQuestions.toFloat(),
				modifier = Modifier
					.fillMaxWidth()
					.padding(bottom = 16.dp),
			)
			Text(
				modifier = Modifier
					.clip(MaterialTheme.shapes.medium)
//					.background(MaterialTheme.colorScheme.surfaceTint)
					.padding(horizontal = 10.dp, vertical = 4.dp)
					.align(alignment = Alignment.End),
				text = "${quizUiState.currentQuestionNumber}/${quizUiState.quizNumberOfQuestions}",
				style = MaterialTheme.typography.titleMedium,
				color = MaterialTheme.colorScheme.onPrimary
			)

			QuizQuestion(
				questionTitle = quizUiState.currentQuestionTitle,
				questionOptions = quizUiState.currentQuestionOptions,
				onAnswerSelected = { quizViewModel.updateUserAnswer(it) },
				selectedAnswerIndex = quizUiState.selectedUserAnswer
			)

			// Navigation and Submitting Buttons
			Row(
				verticalAlignment = Alignment.Bottom,
				modifier = Modifier.fillMaxSize(),
			) {
				if (quizUiState.currentQuestionNumber > 1) {
					Button(
						onClick = {
							quizViewModel.onPreviousPressed()
						}, enabled = !quizUiState.isFirstQuestion
					) {
						Text(text = "Previous")
					}
				}
				Spacer(modifier = Modifier.weight(1f))


				if (quizUiState.isLastQuestion) {
					Button(
						onClick = {
							quizViewModel.onSubmit()
						}, enabled = quizUiState.isNextButtonEnabled, modifier = Modifier
					) {
						Text(text = "Submit")
					}
				} else {
					Button(
						onClick = {
							quizViewModel.onNextPressed()
						}, enabled = quizUiState.isNextButtonEnabled, modifier = Modifier
					) {
						Text(text = "Next")
					}
				}
			}
		}
	} else {
		QuizScore(
			score = quizViewModel.getScore(),
			quizNumberOfQuestions = quizUiState.quizNumberOfQuestions,
			onExitClicked
		)
	}
}

@Composable
fun QuizQuestion(
	questionTitle: String,
	questionOptions: List<String>,
	onAnswerSelected: (Int) -> Unit,
	selectedAnswerIndex: Int,
) {
	Column(
		modifier = Modifier
//			.background(MaterialTheme.colorScheme.background)
			.padding(16.dp)
	) {
		Text(
			text = questionTitle, fontWeight = FontWeight.Bold, fontSize = 20.sp
		)
		Spacer(modifier = Modifier.height(16.dp))

		// Question Options
		Column {
			questionOptions.forEachIndexed { index, option ->
				QuizAnswerOption(text = option,
				                 isSelected = index == selectedAnswerIndex,
				                 onAnswerSelected = {
					                 onAnswerSelected(index)
				                 })
				Spacer(modifier = Modifier.height(8.dp))
			}
		}
	}
}

@Composable
fun QuizAnswerOption(text: String, isSelected: Boolean, onAnswerSelected: () -> Unit) {
	Row(modifier = Modifier
		.fillMaxWidth()
		.background(
			color = if (isSelected) MaterialTheme.colorScheme.primary else
				colorResource(id = R.color.PrimaryBlue)
			,
			shape = MaterialTheme.shapes.small
		)
		.clip(MaterialTheme.shapes.small)
		.clickable { onAnswerSelected() }
		.padding(16.dp),
	    verticalAlignment = Alignment.CenterVertically) {
		Text(
			text = text,
			color = Color.White
		)
		Spacer(modifier = Modifier.weight(1f))
		if (isSelected) {
			Icon(imageVector = Icons.Default.Check, contentDescription = null)
		}
	}
}


@Preview(showBackground = true)
@Composable
fun QuizPreview() {
	TeachUp_QuizTheme { }
}