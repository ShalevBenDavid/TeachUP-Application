package com.example.login2.Activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.login2.composables.CourseQuizzesScreen

class ComposeCourseQuizzesActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			CourseQuizzesScreen()
		}
	}
}
