package com.example.martdev.org.primeraapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar on 05/11/2015.
 */
public class listaPedidos {

    private short numeroMesa;
    private short cantidad;
    private String producto;
    private short verificar;
    private short numeroSilla;
    private String tipoCarne;
    private int numeroEmpleado;
    private short tipoProducto;

    private List<listaPedidos> pedidos;

    public listaPedidos ()
    {
        pedidos = new ArrayList<listaPedidos>();
    }

    public void agregarPedido(short var1,short var2, String var3, short var4,
                               short var5, String var6, int var7, short var8)
    {
        listaPedidos pedido = new listaPedidos();
        pedido.numeroMesa = var1;
        pedido.cantidad = var2;
        pedido.producto = var3;
        pedido.verificar = var4;
        pedido.numeroSilla = var5;
        pedido.tipoCarne = var6;
        pedido.numeroEmpleado = var7;
        pedido.tipoProducto = var8;
        pedidos.add(pedido);

    }


}
