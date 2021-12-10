package com.account.book.recyclerview3;

public class ChartItem {

    String chartcontent;
    int chartmoney;

    public ChartItem(String content, int money){
        this.chartcontent = content;
        this.chartmoney = money;
    }

    public void setChartcontent(String content){
        this.chartcontent = content;
    }

    public String getChartcontent(){
        return chartcontent;
    }

    public void setChartmoney(int money){
        this.chartmoney = money;
    }

    public int getChartmoney(){
        return chartmoney;
    }
}
