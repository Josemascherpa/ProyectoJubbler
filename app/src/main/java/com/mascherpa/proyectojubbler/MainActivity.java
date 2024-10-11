package com.mascherpa.proyectojubbler;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        LinearLayout linearLayout = findViewById(R.id.containerFragments);

        //espero uqe el layout este listo..
        linearLayout.post(() -> {
            int linearWidth = linearLayout.getWidth();
            int linearHeight = linearLayout.getHeight();
            int newWidth = (int) (linearWidth * 0.95);//ancho en base al ancho del cel
            int newHeight = (linearHeight - (30 * 7)) / 7;//resto margin de cada cuadro

            // Setear cantidad de dolares
            for (int i = 0; i < 7; i++) {

                FrameLayout frameLayout = new FrameLayout(MainActivity.this);//Instancio fragments
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(newWidth, newHeight);
                params.setMargins(0, 15, 0, 15); //seteo margenes
                frameLayout.setLayoutParams(params);
                frameLayout.setId(View.generateViewId());//recomendado setear id por fragments.. por si luego
                //necesito identificarlos
                linearLayout.addView(frameLayout);

                //instancio fragments
                ContainerDolarLayout fragment = ContainerDolarLayout.newInstance(newWidth, newHeight);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(frameLayout.getId(), fragment)
                        .commit();
            }
        });
    }
}
