package com.example.martdev.org.primeraapp;


import android.util.Log;
import com.google.gson.Gson;
import org.json.JSONArray;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static com.example.martdev.org.primeraapp.MainActivity.GUARDAR_PEDIDO;

/**
 * Created by omar on 12/09/2015.
 */
public class funcionesMercancia
{
    private final boolean TIPO_ALIMENTOS = true;
    private final String CAMPO_SURTIDO = "preparado";
    private final String CAMPO_CANTIDAD = "cantidad";
    private final String CAMPO_PRODUCTO = "Descripcion";
    private final String CAMPO_NO_SILLA = "noSilla";
    private final String CAMPO_OBERVACION = "observaciones";
    private final String CAMPO_ID = "id";

    public Socket socket;

    private List<com.example.martdev.org.primeraapp.Mercancia> Mercancia = new ArrayList<com.example.martdev.org.primeraapp.Mercancia>();
    private List<com.example.martdev.org.primeraapp.Mercancia> MercanciaPendiente = new ArrayList<com.example.martdev.org.primeraapp.Mercancia>();
    private List<com.example.martdev.org.primeraapp.SubProducto> SubCategoria = new ArrayList<com.example.martdev.org.primeraapp.SubProducto>();
    private Venta venta;

    public funcionesMercancia()
    {

    }

    public void agregarSubProductos(String concepto, String subproducto)
    {
        SubCategoria.add(new SubProducto(subproducto,concepto));
    }

    public ArrayList<String> dameSubproductos(String concepto )
    {
        ArrayList<String> subproductos = new ArrayList<String>();
        for (SubProducto subProducto : SubCategoria) {
            if(subProducto.getConcepto() == concepto)
            {
                subproductos.add(subProducto.getDescripcion());
            }
        }

        return subproductos;
    }


    public void agregarElemento(boolean tipo,short cantidad,String producto,
                                short silla,boolean bandera, boolean estatus, String observaciones, int id)
    {
        if(bandera)
            Mercancia.add(new Mercancia(cantidad,silla,/*tipo*/ producto, estatus, observaciones, id));
        else
            MercanciaPendiente.add(new Mercancia(cantidad,silla,/*tipo*/ producto, estatus, observaciones, id));
    }

    public int dameSilla(int pos,boolean bandera)
    {
        if(bandera)
             return Mercancia.get(pos).getSilla();
        else
             return MercanciaPendiente.get(pos).getSilla();
    }

    public boolean dameSurtido(int pos,boolean bandera)
    {
        if(bandera)
            return Mercancia.get(pos).getSurtido();
        else
            return MercanciaPendiente.get(pos).getSurtido();
    }





    public int dameCantidad(int pos,boolean bandera)
    {
        if(bandera)
            return (int)Mercancia.get(pos).getCantidad();
        else
            return (int)MercanciaPendiente.get(pos).getCantidad();
    }

    public void fijaCantidad(float cantidad,int pos,boolean bandera)
    {
        if(bandera)
            Mercancia.get(pos).setCantidad(cantidad);
        else
            MercanciaPendiente.get(pos).setCantidad(cantidad);
    }

    public String dameNombre(int pos,boolean bandera)
    {
        if(bandera)
            return Mercancia.get(pos).getProducto();
        else
            return MercanciaPendiente.get(pos).getProducto();
    }

    public String dameObservaciones(int pos,boolean bandera)
    {
        if(bandera)
            return Mercancia.get(pos).getObservaciones();
        else
            return MercanciaPendiente.get(pos).getObservaciones();
    }


    public void eliminarElemento(int pos)
    {
        MercanciaPendiente.remove(pos);
    }

    public void limpiarLista(boolean bandera)
    {
        if(bandera)
            Mercancia.clear();
        else
            MercanciaPendiente.clear();
    }

    public int tamanioLista(boolean bandera)
    {
        if(bandera)
            return Mercancia.size();
        else
            return MercanciaPendiente.size();
    }

    //recuperamos el pedido
    public void actualizar()
    {
        Log.d("leerA", "1"+ Mercancia.size() + "2");
        for (int i = 0; i < Mercancia.size(); i++) {
            if (!Mercancia.get(i).getSurtido())
                MercanciaPendiente.add(new Mercancia(
                        Mercancia.get(i).getCantidad(),
                        Mercancia.get(i).getSilla(),
                        /* Mercancia.get(i).getTipo(),*/
                        Mercancia.get(i).getProducto(),
                        Mercancia.get(i).getSurtido(),
                        Mercancia.get(i).getObservaciones(),
                        Mercancia.get(i).getId()

                ));
            Log.d("leerLIST", "1"+ Mercancia.get(i).getProducto() + "2");
        }
    }


    public int busquedaCantidad(String producto,int noSilla, String observaciones)
    {
        for(int i=0;i < MercanciaPendiente.size();i++)
            if(producto.equals(MercanciaPendiente.get(i).getProducto())
                    && MercanciaPendiente.get(i).getSilla() == noSilla && MercanciaPendiente.get(i).getObservaciones().equals(observaciones) )
                return i;

        return -1;
    }
 /*
    public void guardarTipo(String elemento,String tipo,int noSilla)
    {
        for (int i = 0; i < MercanciaPendiente.size(); i++)
        {
            if(MercanciaPendiente.get(i).getTipo()== TIPO_ALIMENTOS)
            {
                if (elemento.equals(MercanciaPendiente.get(i).getProducto()) &&
                        MercanciaPendiente.get(i).getSilla() == noSilla)
                {
                    MercanciaPendiente.get(i).setCarne(tipo);
                    i = MercanciaPendiente.size();
                }
            }
        }
    }
    */

    public boolean verificarTipo(String elemento,int noSilla)
    {
        for (int i = 0; i < MercanciaPendiente.size(); i++)
            if(MercanciaPendiente.get(i).getSilla() == noSilla
                    && MercanciaPendiente.get(i).getTipo() == TIPO_ALIMENTOS)
                if (elemento.equals(MercanciaPendiente.get(i).getProducto()))
                    return true;

        return false;
    }

    public String enviarPedido(String numeroEmpleado,short noMesa)
    {
        Log.d("mesa", noMesa + "");
        int estado = 1;
        //borra toda la informacion de esa mesa
        //httpHandler.enviarGet(noMesa, 0, "borrar", estado, 0, "", 0,0);
        estado = 0;
        int comodin = 100;
        if(venta!= null) {
            venta.setMercancia(MercanciaPendiente);
            venta.setPin(numeroEmpleado);
            venta.setMesa(noMesa);

        }
        else
        {
            venta = new Venta(numeroEmpleado,noMesa,MercanciaPendiente,11);
        }


        String json = new Gson().toJson(venta);

        //conectarSocketServidor(noMesa);

        return   httpHandler.doInBackground_2(json, GUARDAR_PEDIDO);
    }

    private void conectarSocketServidor(short mesa )
    {
        try
        {
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);
            out.println(mesa);
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
                boolean surt = false;
                //si es igual a borrar no lo tomamos en cuenta ya que ese registro solo es temporal
                //y es para control de las mesas abiertas en los diferente dispositivos
                if (!json.getJSONObject(i).getString(CAMPO_PRODUCTO).toString().equals("borrar"))
                {
                    if( json.getJSONObject(i).getInt(CAMPO_SURTIDO) == 1)
                        surt = true;

                    Log.d("json2", "1"+ json.getJSONObject(i).getString(CAMPO_PRODUCTO) + "2");
                    Mercancia.add(new Mercancia(
                            (float)json.getJSONObject(i).getInt(CAMPO_CANTIDAD),
                            (short)json.getJSONObject(i).getInt(CAMPO_NO_SILLA),
                            /* json.getJSONObject(i).getBoolean(CAMPO_TIPO),*/
                            json.getJSONObject(i).getString(CAMPO_PRODUCTO),
                           surt,
                            json.getJSONObject(i).getString(CAMPO_OBERVACION),
                            json.getJSONObject(i).getInt(CAMPO_ID)

                    ));
                }
            }
            return true;
        }
        catch (Exception e)
        {
            Log.d("ejemplo", "1"+ e.toString() + "2");
            return false;
        }
    }
}
