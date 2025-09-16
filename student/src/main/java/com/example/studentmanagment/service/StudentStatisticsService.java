package com.example.studentmanagment.service;

public interface StudentStatisticsService {
    long getTotalStudentCount();
    long getStudentCountByCourse(String course);
}
