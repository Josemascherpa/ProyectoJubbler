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
import android.widget.BaseAdapter;
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
    List<String> listDateFromDb = new ArrayList<>();
    private boolean isSpinnerInitialized = false;
    ArrayAdapter<String> adapter;
    public ContainerDolarFragment() {

    }

    //Creo unanueva instancia
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
        View view = inflater.inflate(R.layout.fragment_container_dolar_layout, container, false);
        FrameLayout frameLayout = view.findViewById(R.id.container_dolar);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(widthFrag, heightFrag);
        frameLayout.setLayoutParams(params);
        createSpinner(view);
        return view;
    }

    //traigo los dolares guardados en la db, leo sus fechas y guardo en la lista de la clase
    //creo el arrayadapter con lsa fechas parapoder setearlas
    public void createSpinner(View view) {
        Spinner spinner = view.findViewById(R.id.spinner_options);

        new Thread(() -> {
            List<Dolar> dolarsFromDb = dolarDB.getAllDolars();
            for (Dolar dolar : dolarsFromDb) {
                listDateFromDb.add(dolar.getUpdateDate());
            }
            getActivity().runOnUiThread(() -> {
                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, listDateFromDb);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                if (!listDateFromDb.isEmpty()) {
                    spinner.setSelection(listDateFromDb.size() - 1);
                }
            });
        }).start();

        //set evento configuracion al seleccionar una opcion
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!isSpinnerInitialized) {
                    isSpinnerInitialized = true;
                    return;
                }
                String selectedOption = listDateFromDb.get(position);

                new Thread(() -> {
                    Dolar dolar = getDolarByDate(selectedOption);
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

    //Consulta a la base de datos, la columna fecha pasandole la nueva fecha
    public Dolar getDolarByDate(String date) {
        Dolar dolar = null;
        String query = "SELECT * FROM dolar_table WHERE updateDate = ?";
        SQLiteDatabase db = dolarDB.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{date});//para ser mas exacto

        if (cursor != null) {
            if (cursor.moveToFirst()) {
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

    //Set Info Dolar in TV
    public void updateDolarData(Dolar dolar) {
        TextView textViewDate = getView().findViewById(R.id.show_date);
        TextView textViewName = getView().findViewById(R.id.date_name);
        TextView textViewBuy = getView().findViewById(R.id.dale_buy);
        TextView textViewSell = getView().findViewById(R.id.date_sell);

        if (!dolar.getName().equals("Oficial")) {
            textViewDate.setVisibility(View.VISIBLE);
            textViewDate.setText("Actual.:" + dolar.getUpdateDate());
        } else {
            Spinner spinner = getView().findViewById(R.id.spinner_options);
            spinner.setVisibility(View.VISIBLE);
            if(adapter!=null){
                adapter.notifyDataSetChanged();
            }
        }
        textViewName.setText(dolar.getName());
        textViewBuy.setText("$" + dolar.getBuy());
        textViewSell.setText("$" + dolar.getSell());
    }

}