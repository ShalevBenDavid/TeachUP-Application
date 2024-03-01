package com.example.login2.Models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CourseModel {
    private String courseName;
    private String courseId;
    private String courseDescription;
    private String courseLogoUrl;
    private String courseTeacherId;
    private String teacherName;
}
