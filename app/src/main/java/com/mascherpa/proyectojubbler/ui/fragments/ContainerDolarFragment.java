package com.mascherpa.proyectojubbler.ui.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mascherpa.proyectojubbler.R;
import com.mascherpa.proyectojubbler.database.DolarDatabase;
import com.mascherpa.proyectojubbler.model.Dolar;

import java.util.ArrayList;
import java.util.List;

public class ContainerDolarFragment extends Fragment {

    private int widthFrag;
    private int heightFrag;
    DolarDatabase dolarDB;
    List<String> listDateFromDb= new ArrayList<>();
    private boolean isSpinnerInitialized = false;


    public ContainerDolarFragment() {
        //vacio paso parametros por bundle
    }


    //para setear nuevas instancias y pasarlas por bundle..
    public static ContainerDolarFragment newInstance(int width, int height) {
        ContainerDolarFragment fragment = new ContainerDolarFragment();
        Bundle args = new Bundle();
        args.putInt("width", width);
        args.putInt("height", height);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            widthFrag = getArguments().getInt("width");
            heightFrag = getArguments().getInt("height");
        }
        dolarDB = new DolarDatabase(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // traigo el layout del fragment
        View view = inflater.inflate(R.layout.fragment_container_dolar_layout, container, false);

        // traigo el container fragmento que voy a reutilizar
        FrameLayout frameLayout = view.findViewById(R.id.container_dolar);

        //parametros que voy a pasarle.. faltarian los datos del dolar
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(widthFrag, heightFrag);
        frameLayout.setLayoutParams(params);

        createSpinner(view);

        return view;
    }

    public void createSpinner( View view){

        Spinner spinner = view.findViewById(R.id.spinner_options);

        // Obtener los datos de la base de datos en un hilo separado
        new Thread(() -> {
            List<Dolar> dolarsFromDb = dolarDB.getAllDolars();
            for (Dolar dolar : dolarsFromDb) {
                //en base a los datos guardados, traigo las fechas y las guardo en la lista de fechas
                listDateFromDb.add(dolar.getUpdateDate());

                // Mostrar datos en el log
//                Log.d("Dolar", "Nombre: " + dolar.getName() + ", Compra: " + dolar.getBuy() + ", Venta: " + dolar.getSell() + ", Fecha: " + dolar.getUpdateDate());
            }

            //actualizo el adaptador en el hilo principal, paso el array al spinner
            getActivity().runOnUiThread(() -> {
                //un ArrayAdapter usando la lista de opciones obtenidas
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listDateFromDb);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            });
        }).start();

            //set evento configuracion al seleccionar una opcion
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isSpinnerInitialized) {//para que no se presione al comenzar, uso un bool para setearlo inicializado
                    isSpinnerInitialized = true;
                    return;
                }
                String selectedOption = listDateFromDb.get(position);//guardo la string seleccionda


                new Thread(() -> {//nuevo hilo para obtener los datos del dólar por fecha

                    Dolar dolar = getDolarByDate(selectedOption);//busco el doalr que tenga esa fecha en la bd

                    //actualizo el fragment en el hilo principal
                    getActivity().runOnUiThread(() -> {
                        if (dolar != null) {
                            updateDolarData(dolar);
                        } else {
                            Toast.makeText(getContext(), "no se encontro fecha: " + selectedOption, Toast.LENGTH_SHORT).show();
                        }
                    });
                }).start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public Dolar getDolarByDate(String date) {
        Dolar dolar=null;
        String query = "SELECT * FROM dolar_table WHERE fechaActualizacion = ?";//consult: desde mi dolar_table traeme el dato proximo, para evitar una inyex
        SQLiteDatabase db = dolarDB.getReadableDatabase(); //traigo la instancia para leer la bd
        Cursor cursor = db.rawQuery(query, new String[]{date});//cursor para iterar, mando la consulta y el parametro para la consulta
        //lo paso como arreglo para en cuyo caso, reemplazen los ?

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                Log.d("Dolar","seteo");
                String name = cursor.getString(0);
                String buy = cursor.getString(1);
                String sell = cursor.getString(2);
                dolar = new Dolar(name, buy, sell, date);
            }
            cursor.close();
        }

        db.close();
        return dolar; //retorno dolar
    }

    public void updateDolarData(Dolar dolar) {
        if(!dolar.getName().equals("Oficial")){
            Spinner spinner = getView().findViewById(R.id.spinner_options);
            spinner.setVisibility(View.GONE);
        }
        TextView textViewName = getView().findViewById(R.id.date_name);
        TextView textViewBuy = getView().findViewById(R.id.dale_buy);
        TextView textViewSell = getView().findViewById(R.id.date_sell);
        textViewName.setText(dolar.getName());
        textViewBuy.setText("$"+dolar.getBuy());
        textViewSell.setText("$"+dolar.getSell());
    }

}