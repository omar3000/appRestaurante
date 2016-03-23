package com.example.martdev.org.primeraapp;

import android.os.StrictMode;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;


/**
 * Created by omar on 30/05/2015.
 */
public class httpHandler {
    static String post(String postUrl)
    {
        StrictMode.ThreadPolicy p = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(p);

           HttpClient httpclient = new DefaultHttpClient();
            HttpContext contexto = new BasicHttpContext();
            HttpGet httpget = new HttpGet(postUrl);
            String resultado= "";
            try
            {
                HttpResponse response = httpclient.execute(httpget,contexto);
                HttpEntity entity = response.getEntity();
                resultado = EntityUtils.toString(entity);

            }
            catch (Exception e)
            {
            }
        return resultado;
    }

    //envia por get a php una consulta para eliminar todos los elementos de una mesa
    static void enviarGet_eliminar(int noMesa)
    {
        int numeroMesa = noMesa;
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        String parametros = "?numeroMesa=" + numeroMesa;

        HttpGet httpget = new HttpGet(
                MainActivity.SERVIDOR + "/android/eliminar.php" + parametros);
        try {
            httpClient.execute(httpget, localContext);

        } catch (Exception e) {

        }
    }

    //envio por get a un archivo php
    static String leer2(int noMesa)
    {
        HttpClient cliente = new DefaultHttpClient();
        HttpContext contexto = new BasicHttpContext();
        HttpGet httpget = new HttpGet(MainActivity.SERVIDOR +
                "/android/recuperarPedido.php?numeroMesa=" + noMesa);
        String resultado = null;
        HttpResponse response;
        HttpEntity entity;
        try {
            response = cliente.execute(httpget, contexto);
            entity = response.getEntity();
            resultado = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            // TODO: handle exception
            resultado ="ERROR";
        }
        return resultado;
    }

    //metodo que envia la informacion a traves de get a archivo php
    static String leer()
    {
        HttpClient cliente = new DefaultHttpClient();
        HttpContext contexto = new BasicHttpContext();
        HttpGet httpget = new HttpGet(MainActivity.SERVIDOR + "/android/consulta.php");
        String resultado = null;
        HttpResponse response;
        HttpEntity entity;
        try {
            response = cliente.execute(httpget, contexto);
            entity = response.getEntity();
            resultado = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return resultado;
    }

    //envio por get a un archivo php
    static void enviarGet(int numeroMesa, int cantidad, String producto, int verificar,
                          int noSilla,String tipo,int noEmpleado,int tipoPro)
    {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        String producto2 = producto.replace(" ", "%20");
        String parametros = "?numeroMesa=" + numeroMesa + "&cantidad=" + cantidad
                + "&producto=" + producto2 + "&estado=" + verificar  + "&numeroSilla="
                + noSilla + "&tipo=" + tipo + "&empleado=" + noEmpleado + "&tipoPro=" + tipoPro;

        HttpGet httpget = new HttpGet(MainActivity.SERVIDOR + "/android/guardarPedido.php"
                + parametros);
        try
        {
            httpClient.execute(httpget, localContext);
        }
        catch (Exception e)
        {

        }

    }
}
