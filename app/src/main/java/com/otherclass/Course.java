package com.otherclass;
import java.io.Serializable;
public class Course implements Serializable{
    private String courseID;
    private String courseName;
    private String courseClassroom;

    public Course() {
    }
    public Course(String courseID,String courseName,String courseClassroom){
        this.courseID=courseID;
        this.courseName=courseName;
        this.courseClassroom=courseClassroom;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseClassroom() {
        return courseClassroom;
    }

    public void setCourseClassroom(String courseClassroom) {
        this.courseClassroom = courseClassroom;
    }
    public String toString(){
        return "Course{"+"courseID='"+courseID+"',courseName='"
                +courseName+"',courseClassroom='"+courseClassroom+"'}";
    }
}
