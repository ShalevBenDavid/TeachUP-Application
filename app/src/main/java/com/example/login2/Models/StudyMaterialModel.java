package com.example.login2.Models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StudyMaterialModel {
    private String title;
    private String description;
    private String fileType;
    private String fileUrl;
}
