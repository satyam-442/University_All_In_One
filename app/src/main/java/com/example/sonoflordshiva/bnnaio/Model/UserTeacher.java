package com.example.sonoflordshiva.bnnaio.Model;

public class UserTeacher
{
    public String Name, Password, Phone, Qualification, Email;

    public UserTeacher() {
    }

    public UserTeacher(String name, String password, String phone, String qualification, String email) {
        Name = name;
        Password = password;
        Phone = phone;
        Qualification = qualification;
        Email = email;
    }

    public String getNamee() {
        return Name;
    }

    public void setNamee(String name) {
        Name = name;
    }

    public String getPasswordd() {
        return Password;
    }

    public void setPasswordd(String password) {
        Password = password;
    }

    public String getPhonee() {
        return Phone;
    }

    public void setPhonee(String phone) {
        Phone = phone;
    }

    public String getQualificationn() {
        return Qualification;
    }

    public void setQualificationn(String qualification) {
        Qualification = qualification;
    }

    public String getEmaill() {
        return Email;
    }

    public void setEmaill(String email) {
        Email = email;
    }
}
