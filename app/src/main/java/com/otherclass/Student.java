package com.otherclass;

import java.io.Serializable;

public class Student implements Serializable{
    private String studentName;
    private String studentID;
    private String signResult;
    private boolean ischecked;

    public Student(){
        ischecked=false;

    }
    public Student(String studentName,String studentID,String signCnt){
        this.studentName=studentName;
        this.studentID=studentID;
        this.signResult=signCnt;
        ischecked=false;
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
        return "Student{studentName='"+studentName+"',studentID='"+studentID+",signResult='"+signResult+"'}";
    }

    public String getSignResult() {
        return signResult;
    }

    public void setSignResult(String signCnt) {
        this.signResult = signCnt;
    }

    public boolean isIschecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }
}
