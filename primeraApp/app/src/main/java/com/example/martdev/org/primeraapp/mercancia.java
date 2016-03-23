package com.example.martdev.org.primeraapp;

/**
 * Created by omar on 12/09/2015.
 */
public class mercancia
{
    private int cantidad;
    private String nombre;
    private String carne;
    private int estado;
    private int tipo;
    private int silla;

    public mercancia(int var1,int var2, String var3,String var4,int var5,int var6)
    {
        silla = var6;
        cantidad = var2;
        nombre = var3;
        carne = var4;
        estado = var5;
        tipo = var1;
    }

    public int getSilla(){
        return silla;
    }

    public int getTipo(){
        return tipo;
    }

    public int getSurtido(){
        return estado;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCarne() {
        return carne;
    }

    public void setCarne(String carne) {
        this.carne = carne;
    }
}
