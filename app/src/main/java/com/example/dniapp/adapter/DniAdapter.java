package com.example.dniapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dniapp.R;
import com.example.dniapp.beans.Dni;

import java.util.List;

public class DniAdapter extends RecyclerView.Adapter<DniHolder> {

    private List<Dni> dniList;

    @NonNull
    @Override
    public DniHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DniHolder dniHolder = null;

            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View itemView = inflater.inflate(R.layout.fila_dni, parent, false);
            dniHolder = new DniHolder(itemView);

        return dniHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DniHolder holder, int position) {

        Dni dni = dniList.get(position);
        holder.cargarDniEnHolder(dni);
    }

    @Override
    public int getItemCount() {
        return dniList.size();
    }

    public DniAdapter (List<Dni> lista_dnis)
    {
        this.dniList = lista_dnis;
    }

    public void setDniList(List<Dni> lista_dnis) {
        this.dniList = lista_dnis;
    }
}
