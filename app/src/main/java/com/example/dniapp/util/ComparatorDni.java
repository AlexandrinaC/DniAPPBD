package com.example.dniapp.util;

import com.example.dniapp.beans.Dni;

import java.util.Comparator;

public class ComparatorDni implements Comparator <Dni> {
    @Override
    public int compare(Dni o1, Dni o2) {
        int letra = (o1.getLetra() - o2.getLetra());

        return letra;
    }
}
