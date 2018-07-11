package com.example.martdev.org.primeraapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar on 11/11/2015.
 */
public class listaProductos {
    private short tipoProducto;
    private String descripcion;
    private String rutaImagen;
    private short status;

    private List<listaProductos> productos;

    public listaProductos ()
    {
        productos = new ArrayList<listaProductos>();
    }

    public void agregarProducto(short var1, String var2, String var3, short var4)
    {
        listaProductos producto = new listaProductos();
        producto.tipoProducto = var1;
        producto.descripcion = var2;
        producto.rutaImagen = var3;
        producto.status = var4;
        productos.add(producto);
    }

    public String getDescripcion(int pos) {
        return productos.get(pos).descripcion;
    }

    public short getTipoProducto(int pos) {
        return productos.get(pos).tipoProducto;
    }

    public String getRutaImagen(int pos) {
        return productos.get(pos).rutaImagen;
    }

    public short getStatus(int pos) {
        return productos.get(pos).status;
    }
}
