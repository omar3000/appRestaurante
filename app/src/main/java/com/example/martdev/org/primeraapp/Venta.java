package com.example.martdev.org.primeraapp;


import java.util.ArrayList;
import java.util.List;

public class Venta
{
    //private DateTime fecha;
    private String pin;
    private short mesa;
    private List<Mercancia> mercancia = new ArrayList<Mercancia>();
    private int key;


    public Venta (String pin, short mesa, List<Mercancia> mercancia, int key)
    {
        this.pin = pin;
        this.mesa = mesa;
        this.mercancia = mercancia;
        this.key = key;

    }



    public void setMercancia(List<Mercancia> mercancia) {
        this.mercancia = mercancia;
    }

    public void setPin(String pin)
    {
        this.pin = pin;
    }

    public void setMesa(short mesa)
    {
        this.mesa = mesa;
    }

}
