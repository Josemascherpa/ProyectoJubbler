package com.mascherpa.proyectojubbler;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class ContainerDolarLayout extends Fragment {

    private int widthFrag;
    private int heightFrag;

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

        return view;
    }
}