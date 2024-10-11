package com.mascherpa.proyectojubbler.model;

public class Dolar {
    private String name;
    private String buy;
    private String sell;
    private String updateDate;

    public Dolar(String nameDolar,String buy,String sell,String updateDate){
        this.name = nameDolar;
        this.buy=buy;
        this.sell = sell;
        this.updateDate = updateDate;
    }

    public String GetName() {
        return name;
    };
    public String GetSell() {
        return sell;
    };
    public String GetBuy() {
        return buy;
    };
    public String GetUpdateDate() {
        return updateDate;
    };


}
