package com.account.book.recyclerview9;

public class SavingItem {

    String content2;
    int money;

    public SavingItem(String content2, int money){
        this.content2 = content2;
        this.money = money;
    }

    public void setContent2(String content2){
        this.content2 = content2;
    }

    public String getContent2(){
        return content2;
    }

    public void setMoney(int money){
        this.money = money;
    }

    public int getMoney(){
        return money;
    }
}
