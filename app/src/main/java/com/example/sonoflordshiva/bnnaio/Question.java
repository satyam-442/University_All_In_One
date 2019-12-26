package com.example.sonoflordshiva.bnnaio;

public class Question
{
    public String Fullname, date, description, profile, time, uid, subject;

    public Question(){

    }

    public Question(String fullname, String date, String description, String subject, String profile, String time, String uid) {
        this.Fullname = fullname;
        this.date = date;
        this.description = description;
        this.subject = subject;
        this.profile = profile;
        this.time = time;
        this.uid = uid;
    }

    public String getFullnames()
    {
        return Fullname;
    }

    public void setFullnames(String fullname)
    {
        this.Fullname = fullname;
    }


    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
