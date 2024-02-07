package com.example.login2

data class QuizBuilderUiState(
	val currentQuizTitle: String = "",
	val numberOfQuestion: Int = 0,
	val currentQuestionTitle: String = "",
	val currentQuestionOptions: List<String> = List(4) { "" },
	val correctAnswerIndex: Int = -1,
	val currentQuestionNumber: Int = 1,
	val quizNumberOfQuestions: Int = 1,
	val isQuizBuilderDone: Boolean = false,
	val isQuestionReady: Boolean = false,
)
