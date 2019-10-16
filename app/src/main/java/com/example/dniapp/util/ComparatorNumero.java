package com.example.dniapp.util;

import com.example.dniapp.beans.Dni;

import java.util.Comparator;

public class ComparatorNumero implements Comparator <Dni> {
    @Override
    public int compare(Dni o1, Dni o2) {
        int numero;
        numero = o1.getNumero() - o2.getNumero();
        return numero;
    }
}
