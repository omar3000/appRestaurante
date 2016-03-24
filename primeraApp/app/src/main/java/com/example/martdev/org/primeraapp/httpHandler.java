package com.example.martdev.org.primeraapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Entity;
import android.os.StrictMode;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by omar on 30/05/2015.
 */
public class HttpHandler extends Activity {
    public String servidor = "http://192.168.1.96:80";

    public String post(String postUrl)
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
                resultado = EntityUtils.toString(entity,"ASCII");

            }
            catch (Exception e)
            {

            }
        return resultado;
    }

    //metodo que envia la informacion a traves de get a archivo php
    public String leer() {
        HttpClient cliente = new DefaultHttpClient();
        HttpContext contexto = new BasicHttpContext();
        HttpGet httpget = new HttpGet(servidor+ "/android/consulta.php");
        String resultado = null;
        try {
            HttpResponse response = cliente.execute(httpget, contexto);
            HttpEntity entity = response.getEntity();
            resultado = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            // TODO: handle exception
        }
        return resultado;
    }

    //envio por get a un archivo php
    public void enviarGet(int numeroMesa, int cantidad, String producto, int verificar,int noSilla,String tipo) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        String producto2 = producto.replace(" ", "%20");
        String parametros = "?numeroMesa=" + numeroMesa + "&cantidad=" + cantidad
                + "&producto=" + producto2 + "&estado=" + verificar  + "&numeroSilla=" + noSilla + "&tipo=" + tipo;

        HttpGet httpget = new HttpGet(
                servidor + "/android/guardarPedido.php" + parametros);
        try {
            httpClient.execute(httpget, localContext);

        } catch (Exception e) {

        }

    }

    //envio por get a un archivo php
    public String leer2(int noMesa) {
        HttpClient cliente = new DefaultHttpClient();
        HttpContext contexto = new BasicHttpContext();
        HttpGet httpget = new HttpGet(servidor+ "/android/recuperarPedido.php?numeroMesa=" + noMesa);
        String resultado = null;
        try {
            HttpResponse response = cliente.execute(httpget, contexto);
            HttpEntity entity = response.getEntity();
            resultado = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            // TODO: handle exception
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Error");
            builder.setMessage("Pedido vacio,No hay nada que enviar,Verifique el pedido por favor!");

            builder.setPositiveButton("Aceptar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            AlertDialog alert = builder.create();
            alert.setIcon(R.drawable.ic_launcher);
            alert.show();
        }
        return resultado;
    }

    //envia por get a php una consulta para eliminar todos los elementos de una mesa
    public void enviarGet_eliminar(int noMesa) {
        int numeroMesa = noMesa;
        HttpClient httpClient = new DefaultHttpClient();
        HttpContext localContext = new BasicHttpContext();
        String parametros = "?numeroMesa=" + numeroMesa;

        HttpGet httpget = new HttpGet(
                servidor + "/android/eliminar.php" + parametros);
        try {
            httpClient.execute(httpget, localContext);

        } catch (Exception e) {

        }
    }
}
