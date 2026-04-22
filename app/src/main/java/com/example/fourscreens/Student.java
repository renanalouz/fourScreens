package com.example.fourscreens;

import androidx.annotation.NonNull;

public class Student {
    private int grade; // יכול לשמש כדירוג מוכר/קונה באפליקציית כרטיסים
    private String fullName;
    private String id; // המזהה הייחודי (UID) מ-Firebase Auth
    private String fcmToken; // השדה החדש להתראות

    // קונסטרקטור ריק חובה עבור Firestore
    public Student(){}

    public Student(String id, String fullName, int grade, String fcmToken) {
        this.id = id;
        this.fullName = fullName;
        this.grade = grade;
        this.fcmToken = fcmToken;
    }

    // Getters & Setters
    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    @NonNull
    @Override
    public String toString() {
        return "\n\n****************************\n" +
                "שם מלא: " + this.getFullName() + "\n" +
                "מזהה משתמש: " + this.getId() + "\n" +
                "דירוג: " + this.getGrade() + "\n" +
                "****************************\n\n";
    }
}