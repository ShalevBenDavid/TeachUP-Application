package com.example.login2.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.login2.Models.Quiz
import com.example.login2.QuizUiState
import com.example.login2.Repositories.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class QuizViewModel(quiz_: Quiz = Quiz()) : ViewModel() {
	private var quiz: Quiz = quiz_
	private val _uiState = MutableStateFlow(
		QuizUiState(currentQuestionOptions = quiz
			.questions[0].options)
	)
	val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

	private val TAG: String = QuizViewModel::class.java.simpleName
	private val repository = QuizRepository()

	private val _quizzes = MutableStateFlow<List<Quiz>>(emptyList())
	val quizzes: StateFlow<List<Quiz>> = _quizzes.asStateFlow()
	private val quizzesLiveData: LiveData<List<Quiz>> = repository.getCourseQuizzes()

	private var currentQuestionIndex = 0
	// this list will contain the answers selected by the user for each question. + 1 for past
	// last question
	private var selectedUserAnswers: MutableList<Int> = MutableList(quiz.questions.size) { -1 }
	init {
		_uiState.value = QuizUiState(
			currentQuestionTitle = quiz.questions[currentQuestionIndex].question,
			currentQuestionOptions = quiz.questions[currentQuestionIndex].options,
			isLastQuestion = currentQuestionIndex == quiz.questions.size - 1,
			quizNumberOfQuestions = quiz.questions.size,
			isQuizDone = false
		)

		quizzesLiveData.observeForever { fetchedQuizzes ->
			_quizzes.value = fetchedQuizzes
		}
	}

	fun getQuizzes(): LiveData<List<Quiz>> {
		return repository.getCourseQuizzes()
	}

	fun onNextPressed() {
		_uiState.update { currentState ->
			val nextQuestionIndex = if (currentQuestionIndex < quiz.questions.size)
				currentQuestionIndex + 1 else currentQuestionIndex
			val nextQuestionNumber = nextQuestionIndex + 1
			currentQuestionIndex = nextQuestionIndex
			currentState.copy(
				currentQuestionTitle = quiz.questions[nextQuestionIndex].question,
				currentQuestionOptions = quiz.questions[nextQuestionIndex].options,
				selectedUserAnswer = selectedUserAnswers[nextQuestionIndex],
				currentQuestionNumber = nextQuestionNumber,
				isFirstQuestion = nextQuestionIndex == 0,
				isLastQuestion = nextQuestionIndex == quiz.questions.size - 1,
				isNextButtonEnabled = selectedUserAnswers[currentQuestionIndex] != -1,
			)
		}
	}

	fun onPreviousPressed() {
		_uiState.update { currentState ->
			val previousQuestionIndex = if (currentQuestionIndex > 0)
				currentQuestionIndex - 1 else currentQuestionIndex
			val previousQuestionNumber = previousQuestionIndex + 1
			currentQuestionIndex = previousQuestionIndex
			currentState.copy(
				currentQuestionTitle = quiz.questions[previousQuestionIndex].question,
				currentQuestionOptions = quiz.questions[previousQuestionIndex].options,
				selectedUserAnswer = selectedUserAnswers[previousQuestionIndex],
				currentQuestionNumber = previousQuestionNumber,
				isFirstQuestion = previousQuestionIndex == 0,
				isLastQuestion = previousQuestionIndex == quiz.questions.size - 1,
				isNextButtonEnabled = true,
			)
		}
	}

	fun updateUserAnswer(userGuess: Int) {
		selectedUserAnswers[currentQuestionIndex] = userGuess
		_uiState.update { currentState ->
			currentState.copy(
				selectedUserAnswer = userGuess,
				isNextButtonEnabled = true,
			)
		}
	}

	fun onSubmit() {
		_uiState.update { currentState ->
			currentState.copy(
				isQuizDone = true
			)
		}

		Log.v(TAG, "quiz submitted")
	}

	private fun resetQuiz() {
		currentQuestionIndex = 0
		selectedUserAnswers = MutableList(quiz.questions.size) { -1 }
		_uiState.value = QuizUiState(
			currentQuestionTitle = quiz.questions[currentQuestionIndex].question,
			currentQuestionOptions = quiz.questions[currentQuestionIndex].options,
			isLastQuestion = currentQuestionIndex == quiz.questions.size - 1,
			quizNumberOfQuestions = quiz.questions.size,
			isQuizDone = false,
			currentQuestionNumber = 1,
			isFirstQuestion = true,
		)
	}

	fun getScore(): Int {
		var score = 0
		selectedUserAnswers.forEachIndexed { index, option ->
			if (quiz.questions[index].correctAnswer == option)
				score++
		}

		return score
	}

	fun setQuiz(newQuiz: Quiz) {
		this.quiz = newQuiz
		resetQuiz()
	}
}
