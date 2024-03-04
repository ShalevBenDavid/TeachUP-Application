package com.example.login2.composables

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.login2.Models.Quiz
import com.example.login2.R
import com.example.login2.Utils.Constants
import com.example.login2.Utils.UserManager
import com.example.login2.ViewModels.QuizViewModel
import com.example.login2.ui.theme.TeachUp_QuizTheme

@Composable
fun CourseQuizzesListScreen(
	onClicked: (Quiz) -> Unit,
	onAddQuizClicked: () -> Unit,
	modifier: Modifier = Modifier,
	quizViewModel: QuizViewModel = viewModel(),
) {
	val quizzes by quizViewModel.quizzes.collectAsState()

	Scaffold(
		modifier = modifier,
		topBar = {
			CourseQuizzesListAppBar()
		},
		content = {
			Image(
				painter = painterResource(R.drawable.bg2),
				contentDescription = null,
				modifier = Modifier
					.fillMaxSize()
					.background(MaterialTheme.colorScheme.primaryContainer),
				contentScale = ContentScale.FillBounds
			)
			Column(
				modifier = Modifier.fillMaxSize()
			) {
				LazyColumn(
					contentPadding = it, modifier = Modifier.padding(horizontal = 8.dp)
				) {
					items(quizzes) { quiz ->
						QuizItem(
							onClicked = onClicked, quiz = quiz, modifier = Modifier.padding(8.dp)
						)
					}
				}

				if (UserManager.getInstance().userType == Constants.TYPE_TEACHER) {
					Spacer(modifier = Modifier.weight(1f))

					FloatingActionButton(
						onClick = {
							onAddQuizClicked()
						},
						modifier = Modifier
							.align(Alignment.End)
							.absoluteOffset(x = (-32).dp, y = (-32).dp)
					) {
						Icon(imageVector = Icons.Default.Add, contentDescription = "Add new quiz")
					}
				}
			}
		}
	)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CourseQuizzesListAppBar(
	modifier: Modifier = Modifier,
) {
	TopAppBar(
		title = { Text(
			text = "Course Quizzes",
		) },
		colors = TopAppBarDefaults.mediumTopAppBarColors(
			containerColor = colorResource(id = R.color.PrimaryBlue),
			titleContentColor = Color.White
		),
		modifier = modifier,
	)
}

@Composable
fun QuizItem(
	onClicked: (Quiz) -> Unit,
	quiz: Quiz,
	modifier: Modifier = Modifier,
) {
	val color by animateColorAsState(
		targetValue = colorResource(id = R.color.PrimaryBlue),
		label = "",
	)

	Card(
		modifier = modifier
	) {
		Column(
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center,
			modifier = Modifier
				.background(color = color)
				.height(50.dp),
		) {
			Row(modifier = Modifier
				.fillMaxWidth()
				.padding(8.dp)
				.clickable { onClicked(quiz) }
			) {
				Text(
					text = quiz.quizTitle,
					style = MaterialTheme.typography.titleMedium,
					modifier = Modifier.padding(start = 8.dp),
					color = Color.White
				)
				Spacer(modifier = Modifier.weight(1f))
				Icon(
					imageVector = Icons.Filled.ArrowForwardIos,
					contentDescription = "",
					tint = MaterialTheme.colorScheme.secondary
				)
			}
		}
	}
}

@Preview(showBackground = true)
@Composable
fun QuizzesListPreview() {
	TeachUp_QuizTheme {
//		CourseQuizzesListScreen()
	}
}