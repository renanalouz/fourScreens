package com.example.fourscreens;

import androidx.annotation.NonNull;

public class Student {
    private int grade;
    private String fullName, id;

    public Student(){}

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

    @NonNull
    @Override
    public String toString() {
        return "\n\n****************************\nשם מלא: "+this.getFullName()+"\nמספר ת.ז.: "+this.getId()+"\nציון סופי: "+this.getGrade()+"\n****************************\n\n";
    }
}
