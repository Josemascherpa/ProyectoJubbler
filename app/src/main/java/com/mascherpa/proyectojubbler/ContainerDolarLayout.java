package com.mascherpa.proyectojubbler;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.security.SecurityPermission;

public class ContainerDolarLayout extends Fragment {

    private int widthFrag;
    private int heightFrag;
    private String[] arraySpinner = {"Opción 1", "Opción 2", "Opción 3"};

    public ContainerDolarLayout() {
        //vacio paso parametros por bundle
    }


    //para setear nuevas instancias y pasarlas por bundle..
    public static ContainerDolarLayout newInstance(int width, int height) {
        ContainerDolarLayout fragment = new ContainerDolarLayout();
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

        Spinner spinner = view.findViewById(R.id.spinner_options);
        // Crear un ArrayAdapter usando el layout simple_spinner_item y la lista de opciones
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, arraySpinner);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Aplicar el adaptador al spinner
        spinner.setAdapter(adapter);

        // Configurar un Listener para el evento de selección
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                /*String selectedOption = arraySpinner[position];
                Toast.makeText(getContext(), "Seleccionaste: " + selectedOption, Toast.LENGTH_SHORT).show();
            */
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se seleccionó nada
            }
        });

        return view;
    }

}