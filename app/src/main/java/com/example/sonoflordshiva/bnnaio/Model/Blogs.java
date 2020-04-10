package com.example.sonoflordshiva.bnnaio.Model;

public class Blogs
{
    public String Fullname, blogId, date, description, subject, time, title, uid;

    public Blogs() {
    }

    public Blogs(String fullname, String blogId, String counter, String date, String description, String subject, String time, String title, String uid) {
        Fullname = fullname;
        this.blogId = blogId;
        //this.counter = counter;
        this.date = date;
        this.description = description;
        this.subject = subject;
        this.time = time;
        this.title = title;
        this.uid = uid;
    }

    public String getFullnamee() {
        return Fullname;
    }

    public void setFullnamee(String fullname) {
        Fullname = fullname;
    }

    public String getBlogIdd() {
        return blogId;
    }

    public void setBlogIdd(String blogId) {
        this.blogId = blogId;
    }

    /*public String getCounterr() {
        return counter;
    }

    public void setCounterr(String counter) {
        this.counter = counter;
    }*/

    public String getDatee() {
        return date;
    }

    public void setDatee(String date) {
        this.date = date;
    }

    public String getDescriptionn() {
        return description;
    }

    public void setDescriptionn(String description) {
        this.description = description;
    }

    public String getSubjectt() {
        return subject;
    }

    public void setSubjectt(String subject) {
        this.subject = subject;
    }

    public String getTimee() {
        return time;
    }

    public void setTimee(String time) {
        this.time = time;
    }

    public String getTitlee() {
        return title;
    }

    public void setTitlee(String title) {
        this.title = title;
    }

    public String getUidd() {
        return uid;
    }

    public void setUidd(String uid) {
        this.uid = uid;
    }
}
