package com.account.book.recyclerview9;

public class SavingSubItem {

    int year, month, day, money;

    public SavingSubItem(int year, int month, int day, int money){
        this.year = year;
        this.month = month;
        this.day = day;
        this.money = money;
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

    public void setDay(int day){
        this.day = day;
    }

    public int getDay(){
        return day;
    }

    public void setMoney(int money){
        this.money = money;
    }

    public int getMoney(){
        return money;
    }
}
