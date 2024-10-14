package com.mascherpa.proyectojubbler.data;

import com.mascherpa.proyectojubbler.model.Dolar;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService { //uso la interfaz "clase simple" solo para usar el get y el llmaado al metodo
    @GET("dolares")
    Call<List<Dolar>> obtainDolars();//pasandole el response
}
