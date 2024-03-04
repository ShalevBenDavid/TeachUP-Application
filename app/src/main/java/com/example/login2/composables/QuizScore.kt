package com.example.login2.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.login2.R
import com.example.login2.ui.theme.TeachUp_QuizTheme

@Composable
fun QuizScore(score: Int,
              quizNumberOfQuestions: Int,
              onExitClicked: () -> Unit
) {
	Scaffold(
		topBar = {
			QuizScoreScreenAppBar()
		}
	) { innerPadding ->
		Image(
			painter = painterResource(R.drawable.bg2),
			contentDescription = null,
			modifier = Modifier
				.fillMaxSize()
				.background(MaterialTheme.colorScheme.primaryContainer),
			contentScale = ContentScale.FillBounds
		)
		Column (
			modifier = Modifier
				.padding(innerPadding)
				.fillMaxSize()
		) {
			QuizScoreScreen(score,
			                quizNumberOfQuestions,
			)
			Spacer(modifier = Modifier.weight(1f))
			ExitButton(onExitClicked)
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
		containerColor = colorResource(id = R.color.PrimaryBlue),
		titleContentColor = Color.White
	),
	modifier = modifier,
)
}

@Composable
fun QuizScoreScreen(
	score: Int,
	quizNumberOfQuestions: Int,
) {
	QuizScoreCard(score, quizNumberOfQuestions)
}

@Composable
fun ExitButton(onExitClicked: () -> Unit) {
	// Exit Button at the bottom
	Button(
		onClick = onExitClicked,
		modifier = Modifier
			.padding(16.dp),
		colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.PrimaryBlue))
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(8.dp),
			horizontalArrangement = Arrangement.Center,
			verticalAlignment = Alignment.CenterVertically
		) {
			Icon(
				imageVector = Icons.Default.ExitToApp,
				contentDescription = null,
				tint = Color.White
			)
			Spacer(modifier = Modifier.width(8.dp))
			Text(
				text = "Return",
				style = MaterialTheme.typography.labelMedium,
				color = Color.White
			)
		}
	}
}

@Composable
fun QuizScoreCard(score: Int, totalQuestions: Int, modifier: Modifier = Modifier) {
	Box(
		modifier = modifier
//			.fillMaxSize()
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
		QuizScoreScreen(10, 15)
	}
}