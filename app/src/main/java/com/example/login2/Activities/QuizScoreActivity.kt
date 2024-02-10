package com.example.login2.Activities

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.login2.ui.theme.TeachUp_QuizTheme

@Composable
fun QuizScore(score: Int, quizNumberOfQuestions: Int) {
	Scaffold(
		topBar = {
			QuizScoreScreenAppBar()
		}
	) { innerPadding ->
		Column (modifier = Modifier.padding(innerPadding)) {
			QuizScoreScreen(score, quizNumberOfQuestions)
		}
	}
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScoreScreenAppBar(
	modifier: Modifier = Modifier
) {	TopAppBar(
	title = {Text(text = "Quiz Score")},
	colors = TopAppBarDefaults.mediumTopAppBarColors(
		containerColor = MaterialTheme.colorScheme.primaryContainer
	),
	modifier = modifier,
)
}

@Composable
fun QuizScoreScreen(
	score: Int, quizNumberOfQuestions: Int
) {
	QuizScoreCard(score, quizNumberOfQuestions)
}

@Composable
fun QuizScoreCard(score: Int, totalQuestions: Int, modifier: Modifier = Modifier) {
	Box(
		modifier = modifier
			.fillMaxSize()
			.padding(16.dp),
		contentAlignment = Alignment.Center
	) {
		Card(
			modifier = modifier,
		) {
			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(16.dp),
				horizontalAlignment = Alignment.CenterHorizontally
			) {
				Text(
					text = "Quiz Completed!",
					style = MaterialTheme.typography.headlineLarge,
					fontWeight = FontWeight.Bold,
					color = MaterialTheme.colorScheme.primary
				)
				Spacer(modifier = Modifier.padding(16.dp))

				Text(
					text = "Your Score",
					style = MaterialTheme.typography.bodyLarge,
					color = MaterialTheme.colorScheme.primary
				)
				Text(
					text = "$score / $totalQuestions",
					style = MaterialTheme.typography.headlineSmall,
					fontWeight = FontWeight.Bold,
					color = MaterialTheme.colorScheme.secondary
				)
			}
		}
	}
}


@Preview(showBackground = true)
@Composable
fun QuizScoreScreenPreview() {
	TeachUp_QuizTheme {
		QuizScore(10, 15)
	}
}