package com.account.book.recyclerview2;

public class AccountItem {

    String what; //수입이냐 지출이냐
    String title; //결제 방법
    String content; //내용, 항목
    String content2; //내용, 항목
    int money; //금액

    public AccountItem(String what, String title, String content, String content2, int money){
        this.what = what;
        this.title = title;
        this.content = content;
        this.content2 = content2;
        this.money = money;
    }

    public void setWhat(String what){
        this.what = what;
    }

    public String getWhat(){
        return this.what;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return this.title;
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
}
