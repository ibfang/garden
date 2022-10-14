package com.example.garden.enums;

public enum StockCashFlowEnum {

    STOCK_REGULAR_RATE("stockRegularRate","股票定期支付股利"),
    STOCK_SALE_INCOME("stockSaleIncome","股票出售收入"),
    ZERO("zero","零增长率"),
    VARYING("varying","变动增长率"),
    UNVARYING("unvarying","固定增长率");


    private String key;
    private String value;

    private StockCashFlowEnum(String key,String value){
        this.key=key;
        this.value=value;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
