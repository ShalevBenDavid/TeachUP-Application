package com.example.login2.Models

data class Quiz(
    val quizTitle: String = "",
    val questions: List<QuestionModel> = listOf(
        QuestionModel()
    )
)

val quizzes = listOf(
    Quiz(
        quizTitle = "quiz 1",
        questions = listOf(
            QuestionModel(
                question = "What is the capital of France?",
                options = mutableListOf("Berlin", "London", "Paris", "Madrid"),
                correctAnswer = 2
            ),
            QuestionModel(
                question = "Which planet is known as the Red Planet?",
                options = mutableListOf("Venus", "Mars", "Jupiter", "Saturn"),
                correctAnswer = 1
            ),
            QuestionModel(
                question = "What is the capital of Japan?",
                options = mutableListOf("Beijing", "Tokyo", "Seoul", "Bangkok"),
                correctAnswer = 1
            )
            // Add more questions as needed
        )
    ),
    Quiz(
        quizTitle = "quiz 2",
        questions = listOf(
            QuestionModel(
                question = "q1?",
                options = mutableListOf("Berlin", "London", "Paris", "Madrid"),
                correctAnswer = 2
            ),
            QuestionModel(
                question = "q2?",
                options = mutableListOf("Venus", "Mars", "Jupiter", "Saturn"),
                correctAnswer = 1
            ),
            QuestionModel(
                question = "q3?",
                options = mutableListOf("Beijing", "Tokyo", "Seoul", "Bangkok"),
                correctAnswer = 1
            )
            // Add more questions as needed
        )
    ),
    Quiz(
        quizTitle = "quiz 3",
        questions = listOf(
            QuestionModel(
                question = "q11?",
                options = mutableListOf("Berlin", "London", "Paris", "Madrid"),
                correctAnswer = 2
            ),
            QuestionModel(
                question = "q22?",
                options = mutableListOf("Venus", "Mars", "Jupiter", "Saturn"),
                correctAnswer = 1
            ),
            QuestionModel(
                question = "q33?",
                options = mutableListOf("Beijing", "Tokyo", "Seoul", "Bangkok"),
                correctAnswer = 1
            )
            // Add more questions as needed
        )
    ),
)
