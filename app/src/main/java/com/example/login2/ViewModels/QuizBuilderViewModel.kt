package com.example.login2.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.login2.Models.QuestionModel
import com.example.login2.Models.Quiz
import com.example.login2.QuizBuilderUiState
import com.example.login2.Repositories.QuizRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class QuizBuilderViewModel : ViewModel() {
	private val _uiState = MutableStateFlow(
		QuizBuilderUiState()
	)
	val uiState: StateFlow<QuizBuilderUiState> = _uiState.asStateFlow()

	var db: FirebaseFirestore = FirebaseFirestore.getInstance()
	private val repository = QuizRepository()

	private val TAG: String = QuizBuilderViewModel::class.java.simpleName

	private var options: MutableList<String> = MutableList(4) {""}
	private var questionTitle: String = ""
	private var correctAnswer: Int = -1

	private var quiz: Quiz = Quiz()

	private var numberOfQuestions = 1
	private var currentQuestionIndex = 0


	fun setQuestionTitle(questionTitleInput: String) {
		quiz.questions[currentQuestionIndex].question = questionTitleInput

		_uiState.update { currentState ->
			currentState.copy(
				quiz = quiz,
				currentQuestionTitle = quiz.questions[currentQuestionIndex].question,
				isQuestionReady = !quiz.questions[currentQuestionIndex].options.contains("") &&
				                  quiz.questions[currentQuestionIndex].question != "" &&
				                  quiz.questions[currentQuestionIndex].correctAnswer != -1
			)
		}
	}

	val setOptions: (Int, String) -> Unit = { index, option ->
		options[index] = option
		quiz.questions[currentQuestionIndex].options = options

		_uiState.update { currentState ->
			currentState.copy(
				currentQuestionOptions = currentState.currentQuestionOptions.toMutableList().apply {
					this[index] = quiz.questions[currentQuestionIndex].options[index]
				},
				isQuestionReady = isQuestionReady()
			)
		}
		Log.v(TAG, "onOptionsChange called: index=$index, option=$option\"")
	}

	fun setCorrectAnswer(desiredCorrectAnswer: Int) {
		quiz.questions[currentQuestionIndex].correctAnswer = desiredCorrectAnswer
		_uiState.update { currentState ->
			currentState.copy(
				correctAnswerIndex = desiredCorrectAnswer,
				isQuestionReady = isQuestionReady()
			)
		}
	}

	fun onAddQuestionButtonClicked() {
		quiz.questions.add(
			QuestionModel()
		)

		numberOfQuestions++
		currentQuestionIndex++

		questionTitle = ""
		options = MutableList(4) {""}
		correctAnswer = -1

		_uiState.update { currentState ->
			currentState.copy(
				currentQuestionTitle = questionTitle,
				currentQuestionOptions = options.toList(),
				correctAnswerIndex = correctAnswer,
				currentQuestionNumber = currentState.currentQuestionNumber.inc(),
				quizNumberOfQuestions = currentState.quizNumberOfQuestions.inc(),
				isQuestionReady = isQuestionReady(),
				isQuizBuilderDone = isQuizReady(),
			)
		}
	}

	fun onBackButtonClicked() {

		if (currentQuestionIndex == 0 )
			return

		currentQuestionIndex--
		Log.d(TAG, "currentQuestionIndex = $currentQuestionIndex")
		_uiState.update { currentState ->
			currentState.copy(
				currentQuestionTitle = quiz.questions[currentQuestionIndex].question,
				currentQuestionOptions = quiz.questions[currentQuestionIndex].options.toList(),
				correctAnswerIndex = quiz.questions[currentQuestionIndex].correctAnswer,
				currentQuestionNumber = currentState.currentQuestionNumber.dec(),
				isQuestionReady = isQuestionReady()
			)
		}
	}

	fun onNextButtonClicked() {
		if (currentQuestionIndex == numberOfQuestions - 1 )
			return

		currentQuestionIndex++
		Log.d(TAG, "currentQuestionIndex = $currentQuestionIndex")
		_uiState.update { currentState ->
			currentState.copy(
				currentQuestionTitle = quiz.questions[currentQuestionIndex].question,
				currentQuestionOptions = quiz.questions[currentQuestionIndex].options.toList(),
				correctAnswerIndex = quiz.questions[currentQuestionIndex].correctAnswer,
				currentQuestionNumber = currentState.currentQuestionNumber.inc(),
				isQuestionReady = isQuestionReady()
			)
		}
	}

	fun onSubmit() {
		quiz.questions.removeAt(quiz.questions.size - 1)
		addQuiz(quiz, object : QuizRepository.FirestoreCallback {
			override fun onSuccess(quizzes: List<Quiz?>?) {
				Log.d(
					TAG,
					"quiz added with title: " + quiz.quizTitle
				)
			}

			override fun onError(error: String?) {
				Log.e(TAG, "Error onSubmit.$error")
			}
		})
	}

	private fun addQuiz(quiz: Quiz, firestoreCallback: QuizRepository.FirestoreCallback) {
		repository.addQuiz(quiz, firestoreCallback)
	}

	fun setQuizTitle(quizTitleInput: String) {
		quiz.quizTitle = quizTitleInput

		_uiState.update { currentState ->
			currentState.copy(
				quiz = quiz,
				currentQuizTitle = quiz.quizTitle,
				isQuestionReady = isQuestionReady(),
				isQuizBuilderDone = isQuizReady(),
			)
		}
	}

	private fun isQuestionReady() : Boolean {
		return !quiz.questions[currentQuestionIndex].options.contains("") &&
		       quiz.questions[currentQuestionIndex].question != "" &&
		       quiz.questions[currentQuestionIndex].correctAnswer != -1
	}

	private fun isQuizReady() : Boolean {
		return quiz.quizTitle.isNotEmpty() && quiz.questions.size > 1
	}
}
