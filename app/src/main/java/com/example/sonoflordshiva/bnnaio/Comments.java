package com.example.sonoflordshiva.bnnaio;

public class Comments
{
    public String comment, date, studname, time;

    public Comments()
    {
    }


    public Comments(String comment, String date, String studname, String time) {
        this.comment = comment;
        this.date = date;
        this.studname = studname;
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStudname() {
        return studname;
    }

    public void setStudname(String studname) {
        this.studname = studname;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
