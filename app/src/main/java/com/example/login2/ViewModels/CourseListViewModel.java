package com.example.login2.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.login2.Models.CourseModel;
import com.example.login2.Repositories.CourseRepository;
import com.example.login2.Repositories.EnrollmentsRepository;
import com.example.login2.Utils.Constants;
import com.example.login2.Utils.UserManager;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CourseListViewModel extends ViewModel {
    private MutableLiveData<List<CourseModel>> courses;
    private final CourseRepository courseRepository = new CourseRepository();
    private MutableLiveData<String> errorMessages = new MutableLiveData<>();
    private EnrollmentsRepository enrollmentsRepository;

    public LiveData<List<CourseModel>> getCourses(String userType) {
        if (courses == null) {
            courses = new MutableLiveData<>();
            if (userType.equals(Constants.TYPE_STUDENT)) {
                enrollmentsRepository = new EnrollmentsRepository();
                loadStudentCourses(userType);
            } else {
                loadTeacherCourses();

            }
        }
        return courses;
    }

    private void loadStudentCourses(String userType) {
        enrollmentsRepository.getUserEnrollments(UserManager.getInstance().getUserId())
                .addOnSuccessListener(ids ->{
                    courseRepository.getCourses(ids)
                            .addOnSuccessListener(courseList ->{
                                courses.setValue(courseList);
                    });
                });
    }

    private void loadTeacherCourses() {
        courseRepository.getAllCoursesTaughtBy(UserManager.getInstance().getUserId())
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        errorMessages.setValue("Error fetching courses: " + e.getMessage());
                        return;
                    }

                    if (!(querySnapshot == null || querySnapshot.isEmpty())) {
                        List<CourseModel> courseList = new ArrayList<>();
                        for (DocumentSnapshot snapshot : querySnapshot.getDocuments()){
                            courseList.add(snapshot.toObject(CourseModel.class));
                        }
                        courses.setValue(courseList);
                    }
                });

    }
}
