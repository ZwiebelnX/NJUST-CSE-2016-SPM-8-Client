package com.otherclass;

public class Check_Student {
    private String studentName;
    private String studentID;
    private String cs_state;

    public Check_Student(){

    }
    public Check_Student(String studentName,String studentID,String cs_state){
        this.studentID = studentID;
        this.studentName = studentName;
        this.cs_state = cs_state;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getCs_state() {
        return cs_state;
    }

    public void setCs_state(String cs_state) {
        this.cs_state = cs_state;
    }
}
