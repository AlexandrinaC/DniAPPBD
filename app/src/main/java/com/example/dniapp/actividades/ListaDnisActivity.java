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
import com.example.dniapp.util.ComparatorDni;
import com.example.dniapp.util.ComparatorNumero;
import com.example.dniapp.util.Preferencias;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListaDnisActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_dnis);

        List<Dni> dniList = Preferencias.cargarFicheroDni(this);

        if (dniList!=null && dniList.size() > 0)
        {
            findViewById(R.id.caja_no_resultado).setVisibility(View.GONE);
            DniAdapter dniAdapter = new DniAdapter(dniList);
            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            recyclerView.setAdapter(dniAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        }

    }

         //Para ordenar por el numero
    public void ordenarPorDni (View view)
    {
        List<Dni> lista_dnis = Preferencias.cargarFicheroDni(this);
        DniAdapter dniAdapter = new DniAdapter(lista_dnis);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(dniAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        ComparatorNumero comparatorNumero = new ComparatorNumero();
        Collections.sort(lista_dnis, comparatorNumero);
        dniAdapter.setDniList(lista_dnis);
        dniAdapter.notifyDataSetChanged();

        String text = "Ordenado de menor a mayor";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this, text, duration); //This porque es el contexto en el que lo utilizamos.
        toast.show();

    }
     //Para ordenar por la letra
    public void ordenarLetraDni(View view) {
        List<Dni> lista_dnis = Preferencias.cargarFicheroDni(this);
        DniAdapter dniAdapter = new DniAdapter(lista_dnis);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(dniAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));


        ComparatorDni comparatorDni = new ComparatorDni();
        Collections.sort(lista_dnis, comparatorDni);
        dniAdapter.setDniList(lista_dnis);
        dniAdapter.notifyDataSetChanged();

        String text = "Ordenado alfabéticamente";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }





}
