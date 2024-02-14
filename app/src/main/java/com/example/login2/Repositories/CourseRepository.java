package com.example.login2.Repositories;

import android.util.Log;

import com.example.login2.Models.CourseModel;
import com.example.login2.Models.UserModel;
import com.example.login2.Utils.Constants;
import com.example.login2.Utils.UserManager;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CourseRepository {
    private final FirebaseFirestore db;

    public CourseRepository(){
        db = FirebaseFirestore.getInstance();
    }


    public void editCourse(String courseId){
    }

    public void deleteCourse(String courseId){
        db.collection(Constants.COURSE_COLLECTION).document(courseId).delete().addOnSuccessListener(aVoid->{
        });

    }

    public Task<CourseModel>  getCourse(String courseId){
        TaskCompletionSource<CourseModel> taskCompletionSource = new TaskCompletionSource<>();

        db.collection(Constants.COURSE_COLLECTION).document(courseId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    CourseModel course = documentSnapshot.toObject(CourseModel.class);
                    taskCompletionSource.setResult(course);
                })
                .addOnFailureListener(taskCompletionSource::setException);

        return taskCompletionSource.getTask();
    }

    public Query getAllCoursesTaughtBy(String userId){
        return db.collection("courses").
                whereEqualTo("courseTeacherId",userId);
    }

    public void addCourse(CourseModel newCourse) {
        DocumentReference reference = db.collection(Constants.COURSE_COLLECTION).document();

        newCourse.setCourseId(reference.getId());

        reference.set(newCourse);

        EnrollmentsRepository repository = new EnrollmentsRepository();
        repository.enrollStudent(newCourse.getCourseId(), UserManager.getInstance().getUserId(), new EnrollmentsRepository.EnrollmentCallback() {
            @Override
            public void onSuccess(List<UserModel> students) {

            }

            @Override
            public void onError(String error) {

            }
        });
    }


    public Task<Boolean> doesExist(String code) {
        TaskCompletionSource<Boolean> taskCompletionSource = new TaskCompletionSource<>();

        db.collection(Constants.COURSE_COLLECTION).document(code).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                boolean exists = document != null && document.exists();
                taskCompletionSource.setResult(exists);
            } else {
                taskCompletionSource.setException(Objects.requireNonNull(task.getException()));
            }
        });

        return taskCompletionSource.getTask();
    }

    public Task<List<CourseModel>> getCourses(List<String> courseIds){
        List<Task<CourseModel>> tasks = new ArrayList<>();
        for(String courseId: courseIds){
            tasks.add(getCourse(courseId));
        }

        TaskCompletionSource<List<CourseModel>> taskCompletionSource = new TaskCompletionSource<>();

        Tasks.whenAllSuccess(tasks).addOnSuccessListener(courseModels ->{
            List<CourseModel> courses = new ArrayList<>();
            for(Object model : courseModels){
                courses.add((CourseModel) model);
            }
            taskCompletionSource.setResult(courses);
        }).addOnFailureListener(taskCompletionSource::setException);

        return taskCompletionSource.getTask();
    }
}