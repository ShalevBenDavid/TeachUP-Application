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
    private EnrollmentsRepository enrollmentsRepository;

    public LiveData<List<CourseModel>> getCourses(String userType) {
        if (courses == null) {
            courses = new MutableLiveData<>();
            if (userType.equals(Constants.TYPE_STUDENT)) {
                enrollmentsRepository = new EnrollmentsRepository();
                loadStudentCourses(userType);
            } else {
                return getAllCoursesTaughtBy();
            }
        }
        return courses;
    }

    private LiveData<List<CourseModel>> loadStudentCourses(String userType) {
        enrollmentsRepository.getUserEnrollments(UserManager.getInstance().getUserId())
                .addOnSuccessListener(ids ->{
                    LiveData<List<CourseModel>> liveDataCourses = courseRepository.getCourses(ids);
                    liveDataCourses.observeForever(coursesList -> courses.setValue(coursesList));
                });
        return courses;
    }

    private LiveData<List<CourseModel>> getAllCoursesTaughtBy() {
        return courseRepository.getAllCoursesTaughtBy(UserManager.getInstance().getUserId());
    }

    public void addCourse(CourseModel courseModel){
        courseRepository.addCourse(courseModel);
    }
}
