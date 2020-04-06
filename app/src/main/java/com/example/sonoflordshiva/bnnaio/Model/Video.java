package com.example.sonoflordshiva.bnnaio.Model;

public class Video
{
    public String date, description, subject, teacherName, time, title, video, videoId;

    public Video() {
    }

    public Video(String date, String description, String subject, String teacherName, String time, String title, String video, String videoId) {
        this.date = date;
        this.description = description;
        this.subject = subject;
        this.teacherName = teacherName;
        this.time = time;
        this.title = title;
        this.video = video;
        this.videoId = videoId;
    }

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

    public String getTeacherNamee() {
        return teacherName;
    }

    public void setTeacherNamee(String teacherName) {
        this.teacherName = teacherName;
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

    public String getVideoo() {
        return video;
    }

    public void setVideoo(String video) {
        this.video = video;
    }

    public String getVideoIdd() {
        return videoId;
    }

    public void setVideoIdd(String videoId) {
        this.videoId = videoId;
    }
}
