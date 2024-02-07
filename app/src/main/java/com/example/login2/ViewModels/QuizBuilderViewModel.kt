package com.example.login2.ViewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.login2.Models.QuestionModel
import com.example.login2.QuizBuilderUiState
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

	private val TAG: String = QuizBuilderViewModel::class.java.simpleName

	private var options: MutableList<String> = MutableList(4) {""}
	private var quizTitle: String = ""
	private var questionTitle: String = ""
	private var correctAnswer: Int = -1

	private var quiz: MutableList<QuestionModel> = mutableListOf()

	private var numberOfQuestions = 1
	private var currentQuestionIndex = 0

	init {
		quiz.add(
			QuestionModel()
		)
	}


	fun setQuestionTitle(questionTitleInput: String) {
		quiz[currentQuestionIndex].question = questionTitleInput

		_uiState.update { currentState ->
			currentState.copy(
				currentQuestionTitle = quiz[currentQuestionIndex].question,
				isQuestionReady = !quiz[currentQuestionIndex].options.contains("") &&
				                  quiz[currentQuestionIndex].question != "" &&
				                  quiz[currentQuestionIndex].correctAnswer != -1
			)
		}
	}

	val setOptions: (Int, String) -> Unit = { index, option ->
		options[index] = option
		quiz[currentQuestionIndex].options = options
//		quiz[currentQuestionIndex].optionstoMutableList().apply {
//			this[index] = option
//		}

		_uiState.update { currentState ->
			currentState.copy(
//				currentQuestionOptions = options,
//				currentQuestionOptions = quiz[currentQuestionIndex].options,
//				currentQuestionOptions = currentState.currentQuestionOptions.apply {
//					this[index] = options[index]
//				},
//				currentQuestionOptions = currentState.currentQuestionOptions.().apply {
//					this[index] = quiz[currentQuestionIndex].options[index]
//				}
				currentQuestionOptions = currentState.currentQuestionOptions.toMutableList().apply {
					this[index] = quiz[currentQuestionIndex].options[index]
				},
				isQuestionReady = isQuestionReady()
			)
		}
		Log.v(TAG, "onOptionsChange called: index=$index, option=$option\"")
	}

	fun setCorrectAnswer(desiredCorrectAnswer: Int) {
		quiz[currentQuestionIndex].correctAnswer = desiredCorrectAnswer
		_uiState.update { currentState ->
			currentState.copy(
				correctAnswerIndex = desiredCorrectAnswer,
				isQuestionReady = isQuestionReady()
			)
		}
	}

	fun onAddQuestionButtonClicked() {
		quiz.add(
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

//		resetQuestionBuilder()
	}

	fun onBackButtonClicked() {

		if (currentQuestionIndex == 0 )
			return

		currentQuestionIndex--
		Log.d(TAG, "currentQuestionIndex = $currentQuestionIndex")
		_uiState.update { currentState ->
			currentState.copy(
				currentQuestionTitle = quiz[currentQuestionIndex].question,
				currentQuestionOptions = quiz[currentQuestionIndex].options.toList(),
				correctAnswerIndex = quiz[currentQuestionIndex].correctAnswer,
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
				currentQuestionTitle = quiz[currentQuestionIndex].question,
				currentQuestionOptions = quiz[currentQuestionIndex].options.toList(),
				correctAnswerIndex = quiz[currentQuestionIndex].correctAnswer,
				currentQuestionNumber = currentState.currentQuestionNumber.inc(),
				isQuestionReady = isQuestionReady()
			)
		}
	}

	fun onSubmit() {
		val quizMap: MutableMap<String, Any> = convertQuizToMap(quiz.subList(0, quiz.size - 1))
		quizMap["quizTitle"] = quizTitle
		db.collection("quizzes")
			.add(quizMap)
			.addOnSuccessListener { documentReference ->
				Log.d(
					TAG,
					"DocumentSnapshot added with ID: " + documentReference.id
				)
			}
			.addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
	}

	private fun convertQuizToMap(quiz: MutableList<QuestionModel>): MutableMap<String, Any> {
		val quizMap: MutableMap<String, Any> = mutableMapOf()

		quiz.forEachIndexed { index, question ->
			val questionKey = "question$index"
			val questionValues = mapOf(
				"question" to question.question,
				"options" to question.options,
				"correctAnswer" to question.correctAnswer
			)

			quizMap[questionKey] = questionValues
		}

		return quizMap
	}

	private fun resetQuestionBuilder() {
		options.clear()
		questionTitle = ""
		correctAnswer = -1

		_uiState.update {
			QuizBuilderUiState()
		}
	}

	fun setQuizTitle(quizTitleInput: String) {
		quizTitle = quizTitleInput

		_uiState.update { currentState ->
			currentState.copy(
				currentQuizTitle = quizTitle,
				isQuestionReady = isQuestionReady(),
				isQuizBuilderDone = isQuizReady(),
			)
		}
	}

	private fun isQuestionReady() : Boolean {
		return !quiz[currentQuestionIndex].options.contains("") &&
		       quiz[currentQuestionIndex].question != "" &&
		       quiz[currentQuestionIndex].correctAnswer != -1
	}

	private fun isQuizReady() : Boolean {
		return quizTitle.isNotEmpty() && quiz.size > 1
	}
}
