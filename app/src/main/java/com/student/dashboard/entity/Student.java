package com.student.dashboard.entity;

import java.util.HashMap;

public class Student {

    public String aadhar, address, contact, gender, imageurl, name, roll, standard;
    public HashMap<String, Float> marks;
    public HashMap<String, String> attendance;

    public Student() {
    }

    public Student(String aadhar, String address, String contact, String gender, String imageurl,
                   String name, String roll, String standard, HashMap<String, Float> marks,
                   HashMap<String, String> attendance) {
        this.aadhar = aadhar;
        this.address = address;
        this.contact = contact;
        this.gender = gender;
        this.imageurl = imageurl;
        this.name = name;
        this.roll = roll;
        this.standard = standard;
        this.marks = marks;
        this.attendance = attendance;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public HashMap<String, Float> getMarks() {
        return marks;
    }

    public void setMarks(HashMap<String, Float> marks) {
        this.marks = marks;
    }

    public HashMap<String, String> getAttendance() {
        return attendance;
    }

    public void setAttendance(HashMap<String, String> attendance) {
        this.attendance = attendance;
    }
}
