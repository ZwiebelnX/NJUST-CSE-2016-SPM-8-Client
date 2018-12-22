package com.otherclass;

import java.io.Serializable;

public class Student implements Serializable{
    private String studentName;
    private String studentID;

    public Student(){

    }
    public Student(String studentName,String studentID){
        this.studentName=studentName;
        this.studentID=studentID;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
    public String toString(){
        return "Student{studentName='"+studentName+"',studentID='"+studentID+"'}";
    }
}
