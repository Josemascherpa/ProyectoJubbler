package com.mascherpa.proyectojubbler.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.mascherpa.proyectojubbler.R;
import com.mascherpa.proyectojubbler.data.ApiService;
import com.mascherpa.proyectojubbler.data.RetrofitClient;
import com.mascherpa.proyectojubbler.database.DolarDatabase;
import com.mascherpa.proyectojubbler.model.Dolar;
import com.mascherpa.proyectojubbler.ui.fragments.ContainerDolarFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    List<ContainerDolarFragment> listFragments = new ArrayList<ContainerDolarFragment>();
    int amountDolar = 0;
    List<Dolar> dolars = new ArrayList<Dolar>();
    DolarDatabase dolarDB;
    int margin = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dolarDB = new DolarDatabase(this);//quiza pueda hacer un singleton por si se disparan las instancias
        //Pero si manejo una activity no es mucho

        loadDollars(() -> {
            createAndModifyDolarFragments(amountDolar, MainActivity.this::loadFragments);
        });
    }

    private void createAndModifyDolarFragments(int amountDolar, Runnable onComplete) {
        LinearLayout linearLayout = findViewById(R.id.containerFragments);

        //espero uqe el layout este listo..
        linearLayout.post(() -> {
            int newWidth = linearLayout.getWidth();//ancho en base al ancho del cel
            int linearHeight = linearLayout.getHeight();
            int newHeight = (linearHeight - ((margin * 2) * amountDolar)) / amountDolar;//margin * 2, margen arriba y abajo, y multiplico por la cantiadd de daolares
//Y divido para que entren en la pantalla

            //creo cantidad de fragmentos por dolar
            for (int i = 0; i < amountDolar; i++) {
                createFragment(linearLayout, newWidth, newHeight);
            }

            if (onComplete != null) {
                onComplete.run();
            }
        });
    }

    private void createFragment(LinearLayout linearLayout, int width, int height) {
        // creoun framelayout y configurosus param
        FrameLayout frameLayout = new FrameLayout(MainActivity.this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);//padre linearlayout
        params.setMargins(0, margin, 0, margin); // Setear márgenes
        frameLayout.setLayoutParams(params);
        frameLayout.setId(View.generateViewId());//seteo id para la vista

        linearLayout.addView(frameLayout);//agrego los framelayout al linearlayout gral

        //instancio el fragmento y agrego al alista
        ContainerDolarFragment fragment = ContainerDolarFragment.newInstance(width, height);
        listFragments.add(fragment);

        getSupportFragmentManager()//Seteo el fragment en el framelayout
                .beginTransaction()
                .replace(frameLayout.getId(), fragment)
                .commitNow();//que los agregue sincronicos, es solo ui
    }

    private void loadDollars(Runnable onComplete) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class); //instancio apiservice
        Call<List<Dolar>> call = apiService.obtainDolars();
        // llamada async :)
        call.enqueue(new Callback<List<Dolar>>() {
            @Override
            public void onResponse(Call<List<Dolar>> call, Response<List<Dolar>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dolars = response.body();
                    amountDolar = dolars.size();
                    if (onComplete != null) {
                        onComplete.run();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "error en la respuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Dolar>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "errorf" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFragments() {
        for (int i = 0; i < amountDolar; i++) {
            Dolar dolar = dolars.get(i); //llevo dolar actual
            if (dolar.getName().equals("Oficial")) {
                dolarDB.saveNewDate(dolar);
            }
            if (i < listFragments.size()) {
                ContainerDolarFragment fragment = listFragments.get(i);//traigo el fragment por cada iteracion.. y seteo el dolar
                fragment.updateDolarData(dolar);
            }
        }
    }


}
