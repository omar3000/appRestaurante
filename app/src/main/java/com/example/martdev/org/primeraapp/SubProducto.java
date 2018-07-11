package com.example.martdev.org.primeraapp;

public class SubProducto {

    private String descripcion;
    private String concepto;

    public SubProducto(String descripcion, String concepto)
    {
        this.descripcion = descripcion;
        this.concepto = concepto;
    }

    public String getDescripcion(){ return this.descripcion; }
    public String getConcepto(){ return this.concepto;}
}
