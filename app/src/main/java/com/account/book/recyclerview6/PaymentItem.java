package com.account.book.recyclerview6;

public class PaymentItem {

    boolean check;
    String titlename, content, content2, clear;
    int id, year, month, day, money;

    public PaymentItem(boolean check, int id, int year, int month, int day, String titlename, String content, String content2, int money, String clear){
        this.check = check;
        this.id = id;
        this.year = year;
        this.month = month;
        this.day = day;
        this.titlename = titlename;
        this.content = content;
        this.content2 = content2;
        this.money = money;
        this.clear = clear;
    }

    public void setCheck(boolean check){
        this.check = check;
    }

    public boolean getCheck(){
        return this.check;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return this.id;
    }

    public void setYear(int year){
        this.year = year;
    }

    public int getYear(){
        return this.year;
    }

    public void setMonth(int month){
        this.month = month;
    }

    public int getMonth(){
        return this.month;
    }

    public void setDay(int day){
        this.day = day;
    }

    public int getDay(){
        return this.day;
    }

    public void setTitlename(String titlename){
        this.titlename = titlename;
    }

    public String getTitlename(){
        return this.titlename;
    }

    public void setContent(String content){
        this.content = content;
    }

    public String getContent(){
        return this.content;
    }

    public void setContent2(String content2){
        this.content2 = content2;
    }

    public String getContent2(){
        return this.content2;
    }

    public void setMoney(int money){
        this.money = money;
    }

    public int getMoney(){
        return this.money;
    }

    public void setClear(String clear){
        this.clear = clear;
    }

    public String getClear(){
        return this.clear;
    }

}
