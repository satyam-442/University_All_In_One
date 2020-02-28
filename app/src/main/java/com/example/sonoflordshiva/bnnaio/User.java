package com.example.sonoflordshiva.bnnaio;

public class User {
    public String Name, Password, Phone;

    public User() {
    }

    public User(String name, String phone, String password) {
        Name = name;
        Phone = phone;
        Password = password;
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
}
