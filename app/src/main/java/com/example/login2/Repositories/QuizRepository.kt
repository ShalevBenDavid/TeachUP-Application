package com.example.login2.Repositories

import android.util.Log
import com.example.login2.Models.Quiz
import com.example.login2.Utils.Constants
import com.example.login2.Utils.CourseManager
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.util.Objects

class QuizRepository {
	private var db: FirebaseFirestore = FirebaseFirestore.getInstance()
	private val TAG: String = QuizRepository::class.java.simpleName
//	fun getCourseQuizzes(): Query {
//		return db
//			.collection("quizzes").orderBy("time", Query.Direction.ASCENDING)
//	}

	fun getCourseQuizzes(): Query {
		return db.collection(Constants.COURSE_COLLECTION)
			.document(CourseManager.getInstance().currentCourse.courseId)
			.collection("quizzes").orderBy("time", Query.Direction.ASCENDING)
	}

	fun addQuiz(quiz: Quiz, callback: FirestoreCallback) {
		db
			.collection("quizzes")
			.document()
			.set(quiz).addOnCompleteListener { task: Task<Void?> ->
				if (task.isSuccessful) {
					callback.onSuccess(null)
				} else {
					callback.onError(Objects.requireNonNull(task.exception)?.message)
					Log.w(TAG, "Error getting documents.", task.exception)
				}
			}
	}

//	fun addQuiz(quiz: Quiz) {
//		db.collection(Constants.COURSE_COLLECTION)
//			.document(CourseManager.getInstance().currentCourse.courseId)
//			.collection("quizzes")
//			.document().set(quiz)
//	}

	interface FirestoreCallback {
		fun onSuccess(quizzes: List<Quiz?>?)
		fun onError(error: String?)
	}
}