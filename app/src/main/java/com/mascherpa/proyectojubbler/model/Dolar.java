package com.mascherpa.proyectojubbler.model;


import com.google.gson.annotations.SerializedName;

public class Dolar {
    //uso del serilizedname para traerme solo los datos que necesito :)

    @SerializedName("nombre")
    private String name;
    @SerializedName("compra")
    private String buy;
    @SerializedName("venta")
    private String sell;
    @SerializedName("fechaActualizacion")
    private String updateDate;

    public Dolar(String name,String buy,String sell,String updateDate){
        this.name=name;
        this.buy=buy;
        this.sell=sell;
        this.updateDate=updateDate;
    }

    public String getName() {
        return name;
    }
    public String getBuy() {
        return buy;
    }
    public String getSell() {
        return sell;
    }
    public String getUpdateDate() {
        return updateDate;
    }

}
