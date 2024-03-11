package com.example.login2.Models

import com.google.firebase.Timestamp

data class Quiz(
    var quizTitle: String = "",
    var questions: MutableList<QuestionModel> = mutableListOf(
        QuestionModel()
    ),
    var timestamp: Timestamp = Timestamp.now() // Timestamp field to store when the quiz was added
)
