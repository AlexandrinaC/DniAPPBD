package com.example.dniapp.actividades;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.dniapp.R;
import com.example.dniapp.adapter.DniAdapter;
import com.example.dniapp.beans.Dni;
import com.example.dniapp.dao.BaseDatosDni;
import com.example.dniapp.util.ComparatorDni;
import com.example.dniapp.util.ComparatorNumero;
import com.example.dniapp.util.Preferencias;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListaDnisActivity extends AppCompatActivity {

    private BaseDatosDni baseDatosDni;
    private List<Dni> dniList;
    private DniAdapter dniAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_dnis);

        //Esto solo si recupero mis datos de las Preferences.
       // List<Dni> dniList = Preferencias.cargarFicheroDni(this);

        //Creo mi base de datos. La cual va a ser una primera versión.
        baseDatosDni = new BaseDatosDni( this , BaseDatosDni.NOMBRE_BD, null, 1);
        //Buscamos los Dnis.
        dniList = baseDatosDni.buscarDnis();



        //Si la lista es diferente de null y tiene un tamaño mayor que 0:
        if ((dniList!=null) && (dniList.size() > 0))
        {
            //Cojo la caja que voy a inflar y que luego rellenare con los datos.
            findViewById(R.id.caja_no_resultado).setVisibility(View.GONE);
            //Creo mi Adapter, al cual ,le voy a pasar mi dniList.
            this.dniAdapter = new DniAdapter(dniList);
            //Con el recycler, inflo la fila de datos.
            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            //Al recycler le digo que Adapter tiene que utilizar.
            recyclerView.setAdapter(dniAdapter);

            //ESTILO
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        }

    }

    //Para ordenar por el numero
    public void ordenarPorDni (View view)
    {

        ComparatorNumero comparatorNumero = new ComparatorNumero();
        Collections.sort(dniList, comparatorNumero);

        dniAdapter.notifyDataSetChanged();

        String text = "Ordenado de menor a mayor";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this, text, duration); //This porque es el contexto en el que lo utilizamos.
        toast.show();

    }
    //Para ordenar por la letra
    public void ordenarLetraDni(View view) {


        ComparatorDni comparatorDni = new ComparatorDni();
        Collections.sort(dniList, comparatorDni);
        dniAdapter.notifyDataSetChanged();

        String text = "Ordenado alfabéticamente";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }





}
