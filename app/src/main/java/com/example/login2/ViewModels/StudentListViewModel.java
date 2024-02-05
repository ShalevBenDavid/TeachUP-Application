package com.example.login2.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.login2.Models.UserModel;
import com.example.login2.Repositories.EnrollmentsRepository;

import java.util.List;

public class StudentListViewModel extends ViewModel {
    private final EnrollmentsRepository repository = new EnrollmentsRepository();

    public LiveData<List<UserModel>> getEnrolledStudents(String courseId){
        return repository.getCourseEnrollments(courseId);
    }
}
