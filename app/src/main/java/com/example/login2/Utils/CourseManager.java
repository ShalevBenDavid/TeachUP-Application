package com.example.login2.Utils;

import com.example.login2.Models.CourseModel;

public class CourseManager {
    private static volatile CourseManager instance;
    private CourseModel currentCourse;

    private CourseManager(){}

    public static CourseManager getInstance(){
        if(instance == null){
            synchronized (CourseManager.class){
                if(instance == null){
                    instance = new CourseManager();
                }
            }
        }
        return instance;
    }

    public CourseModel getCurrentCourse(){
        return currentCourse;
    }

    public void setCurrentCourse(CourseModel currentCourse) {
        this.currentCourse = currentCourse;
    }

}
