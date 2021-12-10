package com.account.book.recyclerview1;

public class DayItem {

    String day;
    int year;
    int month;
    int day_num;
    int plus;
    int minus;

    public DayItem(String day, int year, int month, int day_num, int plus, int minus){
        this.day = day;
        this.year = year;
        this.month = month;
        this.day_num = day_num;
        this.plus = plus;
        this.minus = minus;
    }

    public String getDay(){
        return this.day;
    }

    public void setDay(String day){
        this.day = day;
    }

    public int getYear(){
        return this.year;
    }

    public void setYear(int year){
        this.year = year;
    }

    public int getMonth(){
        return this.month;
    }

    public void setMonth(int month){
        this.month = month;
    }

    public int getDayNum(){
        return this.day_num;
    }

    public void setDayNum(int day_num){
        this.day_num = day_num;
    }

    public int getPlus(){
        return this.plus;
    }

    public void setPlus(int plus){
        this.plus = plus;
    }

    public int getMinus(){
        return this.minus;
    }

    public void setMinus(int minus){
        this.minus = minus;
    }
}
