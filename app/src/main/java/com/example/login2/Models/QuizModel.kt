package com.example.login2.Models

data class Quiz(
    var quizTitle: String = "",
    var questions: MutableList<QuestionModel> = mutableListOf(
        QuestionModel()
    )
)
