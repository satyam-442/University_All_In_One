package com.example.sonoflordshiva.bnnaio;

public class Teacher
{
    public String Email, Fullname, Qualification, image;

    public Teacher() {

    }

    public Teacher(String email, String fullname, String qualification, String image) {
        Email = email;
        Fullname = fullname;
        Qualification = qualification;
        this.image = image;
    }

    public String getTeachEmail() {
        return Email;
    }

    public void setTeachEmail(String email) {
        Email = email;
    }

    public String getTeachFullname() {
        return Fullname;
    }

    public void setTeachFullname(String fullname) {
        Fullname = fullname;
    }

    public String getTeachQualification() {
        return Qualification;
    }

    public void setTeachQualification(String qualification) {
        Qualification = qualification;
    }

    public String getTeachImage() {
        return image;
    }

    public void setTeachImage(String image) {
        this.image = image;
    }
}
