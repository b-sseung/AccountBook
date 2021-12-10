package com.account.book.recyclerview4;

public class ListItem {

    int year, month, day;

    public ListItem(int year, int month, int day){

        this.year = year;
        this.month = month;
        this.day = day;

    }

    public void setDay(int day){
        this.day = day;
    }

    public int getDay(){
        return day;
    }

    public void setYear(int year){
        this.year = year;
    }

    public int getYear(){
        return year;
    }

    public void setMonth(int month){
        this.month = month;
    }

    public int getMonth(){
        return month;
    }
}
