package com.example.dniapp.beans;

import java.util.Comparator;

/**
 * Clase que representa un DNI
 */
public class Dni {

    private int numero;
    private char letra;

    protected static final String SECUENCIA_LETRAS_DNI = "TRWAGMYFPDXBNJZSQVHLCKE";



    public Dni(int numero) {

        this.numero = numero;
    }

    public Dni(int numero, char letra) {
        this.numero = numero;
        this.letra = letra;
    }

    public Dni(){

    }

    public int getNumero() {
        return this.numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public char getLetra() {
        return this.letra;
    }

    public void setLetra(char letra) {
        this.letra = letra;
    }

    public char calculaLetra ()
    {
        char letra_calculada =  ' ';
        int resto = -1;

            resto = (this.numero%SECUENCIA_LETRAS_DNI.length());
            letra_calculada = SECUENCIA_LETRAS_DNI.charAt(resto);

        return letra_calculada;
    }

    //Añadimos el compareTo a la clase Dni. Implementandolo también arriba.

}
