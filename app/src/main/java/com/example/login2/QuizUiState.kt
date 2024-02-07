package com.example.login2

data class QuizUiState(
	val currentQuestionTitle: String = "",
	val currentQuestionOptions: List<String> = List(4) {""},
	val selectedUserAnswer: Int = -1,
	val currentQuestionNumber: Int = 1,
	val isFirstQuestion: Boolean = true,
	val isLastQuestion: Boolean = false,
	val quizNumberOfQuestions: Int = 0,
	val isQuizDone: Boolean = false,
	val isNextButtonEnabled: Boolean = false,
)
