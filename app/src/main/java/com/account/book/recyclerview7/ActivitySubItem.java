package com.account.book.recyclerview7;

public class ActivitySubItem {

    String title;
    String titlename;
    int money;

    public ActivitySubItem(String title, String titlename, int money){
        this.title = title;
        this.titlename = titlename;
        this.money = money;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitlename(String titlename){
        this.titlename = titlename;
    }

    public String getTitlename(){
        return this.titlename;
    }

    public void setMoney(int money){
        this.money = money;
    }

    public int getMoney(){
        return this.money;
    }
}
