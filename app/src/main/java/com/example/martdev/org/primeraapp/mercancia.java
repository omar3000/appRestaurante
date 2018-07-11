package com.example.martdev.org.primeraapp;


/**
 * Created by omar on 12/09/2015.
 */
public class Mercancia
{
    private int id;
    private float cantidad;
    private short silla;
    private boolean tipo;
    private String producto;
    private boolean estatus;
    private String observaciones;

    public Mercancia(float cantidad, short silla,/* boolean tipo*/ String producto, boolean estatus, String observaciones, int id)
    {
        this.silla = silla;
        this.cantidad = cantidad;
        /* this.tipo = tipo; */
        this.producto = producto;
        this.estatus = estatus;
        this.observaciones = observaciones;
        this.id = id;

    }

    public short getSilla(){
        return silla;
    }

    public int getId() {return id; }


    public boolean getTipo(){
        return tipo;
    }

    public boolean getSurtido(){
        return estatus;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }


    public String getProducto()
    {
        return this.producto;
    }


    public String getObservaciones(){return this.observaciones; }

}


