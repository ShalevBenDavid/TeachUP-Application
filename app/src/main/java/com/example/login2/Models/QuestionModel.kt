package com.example.login2.Models

data class QuestionModel(
    var question: String = "",
    var options: MutableList<String> = MutableList(4) { "" },
    var correctAnswer: Int = -1,
)
