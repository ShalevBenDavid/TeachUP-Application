package com.example.login2.Repositories;

import com.example.login2.Utils.Constants;
import com.example.login2.Utils.CourseManager;
import com.example.login2.Models.QuizModelKt;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class QuizRepository {
    private final FirebaseFirestore db;

    public QuizRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    public Query getCourseQuizzes(){
        return db.collection(Constants.COURSE_COLLECTION)
                .document(CourseManager.getInstance().getCurrentCourse().getCourseId())
                .collection("quizzes").orderBy("time", Query.Direction.ASCENDING);
    }

    public void addQuiz(QuizModelKt quiz){
        db.collection(Constants.COURSE_COLLECTION)
                .document(CourseManager.getInstance().getCurrentCourse().getCourseId())
                .collection("quizzes")
                .document().set(quiz);
    }
}
