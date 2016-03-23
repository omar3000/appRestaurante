package com.example.martdev.org.primeraapp;

import org.json.JSONArray;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar on 12/09/2015.
 */
public class funcionesMercancia
{
    private final int TIPO_ALIMENTOS = 12;
    private final String CAMPO_SURTIDO = "surtido";
    private final String CAMPO_CANTIDAD = "cantidad";
    private final String CAMPO_PRODUCTO = "producto";
    private final String CAMPO_NO_SILLA = "numeroSilla";
    private final String CAMPO_TIPO_CARNE = "carne";

    public Socket socket;

    private List<mercancia> Mercancia = new ArrayList<mercancia>();
    private List<mercancia> MercanciaPendiente = new ArrayList<mercancia>();

    public funcionesMercancia()
    {

    }

    public void agregarElemento(int tipo,int cantidad,String producto,String carne,int surtido,
                                int silla,int bandera)
    {
        if(bandera == 1)
            Mercancia.add(new mercancia(tipo,cantidad,producto,carne,surtido,silla));
        else
            MercanciaPendiente.add(new mercancia(tipo,cantidad,producto,carne,surtido,silla));
    }

    public int dameSilla(int pos,int bandera)
    {
        if(bandera == 1)
             return Mercancia.get(pos).getSilla();
        else
             return MercanciaPendiente.get(pos).getSilla();
    }

    public int dameSurtido(int pos,int bandera)
    {
        if(bandera == 1)
            return Mercancia.get(pos).getSurtido();
        else
            return MercanciaPendiente.get(pos).getSurtido();
    }


    public int dameCantidad(int pos,int bandera)
    {
        if(bandera == 1)
            return Mercancia.get(pos).getCantidad();
        else
            return MercanciaPendiente.get(pos).getCantidad();
    }

    public void fijaCantidad(int cantidad,int pos,int bandera)
    {
        if(bandera == 1)
            Mercancia.get(pos).setCantidad(cantidad);
        else
            MercanciaPendiente.get(pos).setCantidad(cantidad);
    }

    public String dameNombre(int pos,int bandera)
    {
        if(bandera == 1)
            return Mercancia.get(pos).getNombre();
        else
            return MercanciaPendiente.get(pos).getNombre();
    }

    public void eliminarElemento(int pos)
    {
        MercanciaPendiente.remove(pos);
    }

    public void limpiarLista(int bandera)
    {
        if(bandera == 1)
            Mercancia.clear();
        else
            MercanciaPendiente.clear();
    }

    public int tamanioLista(int bandera)
    {
        if(bandera ==1)
            return Mercancia.size();
        else
            return MercanciaPendiente.size();
    }

    //recuperamos el pedido
    public void actualizar()
    {
        for (int i = 0; i < Mercancia.size(); i++)
            if (Mercancia.get(i).getSurtido() == 0)
                MercanciaPendiente.add(new mercancia(Mercancia.get(i).getTipo(),
                        Mercancia.get(i).getCantidad(),
                        Mercancia.get(i).getNombre(),
                        Mercancia.get(i).getCarne(),
                        Mercancia.get(i).getSurtido(),
                        Mercancia.get(i).getSilla()
                ));
    }


    public int busquedaCantidad(String producto,int noSilla)
    {
        for(int i=0;i < MercanciaPendiente.size();i++)
            if(producto.equals(MercanciaPendiente.get(i).getNombre())
                    && MercanciaPendiente.get(i).getSilla() == noSilla)
                return i;

        return -1;
    }

    public void guardarTipo(String elemento,String tipo,int noSilla)
    {
        for (int i = 0; i < MercanciaPendiente.size(); i++)
        {
            if(MercanciaPendiente.get(i).getTipo()== TIPO_ALIMENTOS)
            {
                if (elemento.equals(MercanciaPendiente.get(i).getNombre()) &&
                        MercanciaPendiente.get(i).getSilla() == noSilla)
                {
                    MercanciaPendiente.get(i).setCarne(tipo);
                    i = MercanciaPendiente.size();
                }
            }
        }
    }

    public boolean verificarTipo(String elemento,int noSilla)
    {
        for (int i = 0; i < MercanciaPendiente.size(); i++)
            if(MercanciaPendiente.get(i).getSilla() == noSilla
                    && MercanciaPendiente.get(i).getTipo() == TIPO_ALIMENTOS)
                if (elemento.equals(MercanciaPendiente.get(i).getNombre()))
                    return true;

        return false;
    }

    public void enviarPedido(int numeroEmpleado,int noMesa)
    {
        int estado = 1;
        //borra toda la informacion de esa mesa
        httpHandler.enviarGet(noMesa, 0, "borrar", estado, 0, "", 0,0);
        estado = 0;
        int comodin = 100;

        for (int i = 0; i < MercanciaPendiente.size(); i++)
        {
            //envia por php con el metodo get el pedido registro por registro
            if (comodin == 100)
                httpHandler.enviarGet(noMesa, MercanciaPendiente.get(i).getCantidad(),
                        MercanciaPendiente.get(i).getNombre().toString(), comodin,
                        MercanciaPendiente.get(i).getSilla(), MercanciaPendiente.get(i).getCarne(),
                        numeroEmpleado,MercanciaPendiente.get(i).getTipo());
            else
                httpHandler.enviarGet(noMesa, MercanciaPendiente.get(i).getCantidad(),
                        MercanciaPendiente.get(i).getNombre().toString(), estado,
                        MercanciaPendiente.get(i).getSilla(), MercanciaPendiente.get(i).getCarne(),
                        0,MercanciaPendiente.get(i).getTipo());

            comodin = 1;
        }
        conectarSocketServidor(noMesa);
    }

    private void conectarSocketServidor(int noMesa)
    {
        try
        {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            out.println(noMesa);
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //recuperar un arreglo json desde php
    public boolean obtDatosJSON_2(String response)
    {
        JSONArray json;
        try
        {
            json = new JSONArray(response);
            //este for añade a la lista los productos que no se han surtido y se pueden modificar
            for (int i = 0; i < json.length(); i++)
            {
                //si es igual a borrar no lo tomamos en cuenta ya que ese registro solo es temporal
                //y es para control de las mesas abiertas en los diferente dispositivos
                if (!json.getJSONObject(i).getString(CAMPO_PRODUCTO).toString().equals("borrar"))
                {
                    Mercancia.add(new mercancia(json.getJSONObject(i).getInt("tipo"),
                            json.getJSONObject(i).getInt(CAMPO_CANTIDAD),
                            json.getJSONObject(i).getString(CAMPO_PRODUCTO),
                            json.getJSONObject(i).getString(CAMPO_TIPO_CARNE),
                            json.getJSONObject(i).getInt(CAMPO_SURTIDO),
                            json.getJSONObject(i).getInt(CAMPO_NO_SILLA)
                    ));
                }
            }
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
