package com.mascherpa.proyectojubbler.data;

import com.mascherpa.proyectojubbler.model.Dolar;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService { //uso la interfaz "clase simple" solo para usar el get y el endpoint
    @GET("dolares")//rut
    Call<List<Dolar>> obtainDolars();
}
