package com.school.tyari.models;

public class ModelAdmission {

    private String fullname,phoneNumber,state,city,address,comment,className,startArival,endDestination,schoolName;




    public ModelAdmission(String fullname, String phoneNumber, String state, String city, String address, String comment, String className, String startArival, String endDestination,String schoolName) {
        this.fullname = fullname;
        this.phoneNumber = phoneNumber;
        this.state = state;
        this.city = city;
        this.address = address;
        this.comment = comment;
        this.className = className;
        this.startArival = startArival;
        this.endDestination = endDestination;
        this.schoolName = schoolName;


    }


    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public ModelAdmission() {
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getStartArival() {
        return startArival;
    }

    public void setStartArival(String startArival) {
        this.startArival = startArival;
    }

    public String getEndDestination() {
        return endDestination;
    }

    public void setEndDestination(String endDestination) {
        this.endDestination = endDestination;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }



}
