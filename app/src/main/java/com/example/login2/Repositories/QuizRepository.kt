package com.example.login2.Repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.login2.Models.QuestionModel
import com.example.login2.Models.Quiz
import com.example.login2.Utils.Constants
import com.example.login2.Utils.CourseManager
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.Objects

class QuizRepository {
	private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
	private val TAG: String = QuizRepository::class.java.simpleName

	private val quizzesLiveData = MutableLiveData<List<Quiz>>()

	fun getCourseQuizzes(): LiveData<List<Quiz>> {
		db.collection(Constants.COURSE_COLLECTION)
			.document(CourseManager.getInstance().getCourseId())
			.collection(Constants.QUIZZES_COLLECTION).orderBy("timestamp", Query.Direction.DESCENDING)
			.addSnapshotListener { value, error ->
				if (error != null) {
					Log.e(TAG, "Error getting documents.", error)
					return@addSnapshotListener
				}

				val fetchedQuizzes = mutableListOf<Quiz>()
				for (document in value!!) {
					val quizMap = document.data as MutableMap<String, Any>
					val quiz = Quiz(
						quizTitle = quizMap["quizTitle"] as String,
						questions = convertMapToQuiz(quizMap["questions"] as List<Map<String, Any>>)
					)
					fetchedQuizzes.add(quiz)
				}
				quizzesLiveData.postValue(fetchedQuizzes.toList())
			}

		return quizzesLiveData
	}

	private fun convertMapToQuiz(questionsList: List<Map<String, Any>>): MutableList<QuestionModel> {
		val quiz: MutableList<QuestionModel> = mutableListOf()

		questionsList.forEach { questionMap ->
			val question = QuestionModel(
				question = questionMap["question"] as String,
				options = questionMap["options"] as MutableList<String>,
				correctAnswer = (questionMap["correctAnswer"] as Long).toInt() // Assuming correctAnswer is of type Long
			)

			quiz.add(question)
		}

		return quiz
	}

	fun addQuiz(quiz: Quiz, firestoreCallback: FirestoreCallback) {
		val newDocRef = db.collection(Constants.COURSE_COLLECTION)
			.document(CourseManager.getInstance().getCourseId())
			.collection(Constants.QUIZZES_COLLECTION)
			.document()

		quiz.timestamp = Timestamp.now()

		newDocRef.set(quiz).addOnCompleteListener { task ->
			if (task.isSuccessful) {
				Log.d(
					TAG,
					"quiz added"
				)
			} else {
				firestoreCallback.onError(Objects.requireNonNull(task.exception)?.message)
			}
		}
	}

	interface FirestoreCallback {
		fun onSuccess(quizzes: List<Quiz?>?)
		fun onError(error: String?)
	}
}