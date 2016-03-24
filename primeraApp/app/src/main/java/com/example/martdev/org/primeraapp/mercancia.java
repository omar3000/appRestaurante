package com.example.martdev.org.primeraapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import org.json.JSONArray;

/**
 * Created by User01 on 01/09/2015.
 */
public class Mercancia extends Actividad2
{
    final short TIPO_ALIMENTOS = 12;
    final short TIPO_BEBIDAS = 13;
    final short TIPO_CERVEZAS = 14;

    final String DESCRIPCION = "vchDescripcion";
    final String IMAGEN = "img";
    final String TIPO = "tynTipo";
    final String SURTIDO = "surtido";
    final String CANTIDAD = "cantidad";
    final String PRODUCTO = "producto";
    final String SILLA = "numeroSilla";

    final int LIMITE_BEBIDAS = 54;
    final int LIMITE_TIPO = 54;
    final int LIMITE_CERVEZAS = 108;
    final int TAMANIO_OBJETO = 200;



    private String rutaImagen;
    private int arregloEstado;
    private int arregloCantidad;
    private int arregloSilla;
    private String arregloCargar;
    private String arregloTipo;

    public String getRutaImagen()
    {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen)
    {
        this.rutaImagen = rutaImagen;
    }

    public int getArregloEstado()
    {
        return arregloEstado;
    }

    public void setArregloEstado(int arregloEstado)
    {
        this.arregloEstado = arregloEstado;
    }

    public int getArregloCantidad()
    {
        return arregloCantidad;
    }

    public void setArregloCantidad(int arregloCantidad)
    {
        this.arregloCantidad = arregloCantidad;
    }

    public int getArregloSilla()
    {
        return arregloSilla;
    }

    public void setArregloSilla(int arregloSilla)
    {
        this.arregloSilla = arregloSilla;
    }

    public String getArregloCargar()
    {
        return arregloCargar;
    }

    public void setArregloCargar(String arregloCargar)
    {
        this.arregloCargar = arregloCargar;
    }

    public String getArregloTipo()
    {
        return arregloTipo;
    }

    public void setArregloTipo(String arregloTipo)
    {
        this.arregloTipo = arregloTipo;
    }

    public void inicializa()
    {
        setArregloCantidad(0);
        setArregloCargar("");
        setArregloEstado(0);
        setArregloSilla(0);
        setArregloTipo("");
        setRutaImagen("");
    }

    //funcion para recuperar los productos e imagenes atraves de un arreglo json
    public void obtDatosJSON(String response)
    {
        short contador1,contador2, contador3;
        JSONArray json;
        try
        {

            contador1 =0;
            contador2 =0;
            contador3 =0;

            json = new JSONArray(response);
            for (short i = 0; i < json.length(); i++)
            {
                if (json.getJSONObject(i).getInt(TIPO) == TIPO_ALIMENTOS)
                {
                    btnAlimentos[contador1].setText(json.getJSONObject(i).getString(DESCRIPCION));
                    btnAlimentos[contador1].setVisibility(View.VISIBLE);
                    merca[contador1].setRutaImagen(json.getJSONObject(i).getString(IMAGEN).toString());
                    contador1++;
                }
                else if (json.getJSONObject(i).getInt(TIPO) == TIPO_BEBIDAS)
                {
                    btnBebidas[contador2].setText(json.getJSONObject(i).getString(DESCRIPCION));
                    btnBebidas[contador2].setVisibility(View.VISIBLE);
                    merca[contador2 + LIMITE_BEBIDAS].setRutaImagen(json.getJSONObject(i).getString(IMAGEN).toString());
                    contador2++;
                }
                else if (json.getJSONObject(i).getInt(TIPO) == TIPO_CERVEZAS) {
                    btnCervezas[contador3].setText(json.getJSONObject(i).getString(DESCRIPCION));
                    btnCervezas[contador3].setVisibility(View.VISIBLE);
                    merca[contador3+ LIMITE_CERVEZAS].setRutaImagen(json.getJSONObject(i).getString(IMAGEN).toString());
                    contador3++;
                }
            }
        }
        catch (Exception e)
        {

        }
        //llamamos al metodo para cargar las imagenes
        downloadFile();
    }

    //recuperar un arreglo json desde php
    public void obtDatosJSON_2(String response) {
        JSONArray json;
        try
        {
            json = new JSONArray(response);

            //este primer for son para los productos que ya se surtieron
            for (short i = 0; i < json.length(); i++)
            {
                merca[i].setArregloEstado(json.getJSONObject(i).getInt(SURTIDO));
                merca[i].setArregloCantidad(json.getJSONObject(i).getInt(CANTIDAD));
                merca[i].setArregloCargar(json.getJSONObject(i).getString(PRODUCTO));
                merca[i].setArregloSilla(arregloSilla = json.getJSONObject(i).getInt(SILLA));
            }

            //este for añade a la lista los productos que no se han surtido y se pueden modificar
            for (short i = 0; i < json.length(); i++) {
                //si es igual a borrar no lo tomamos en cuenta ya que ese registro solo es temporal
                //y es para control de las mesas abiertas en los diferente dispositivos
                if (!json.getJSONObject(i).getString(PRODUCTO).toString().equals("borrar"))
                {
                    merca[i].setArregloEstado(json.getJSONObject(i).getInt(SURTIDO));
                    merca[i].setArregloCantidad(json.getJSONObject(i).getInt(CANTIDAD));
                    merca[i].setArregloCargar(json.getJSONObject(i).getString(PRODUCTO));
                    merca[i].setArregloSilla(arregloSilla = json.getJSONObject(i).getInt(SILLA));

                    //si surtido es false guarda el tipo de carne de la comida
                    if (merca[i].getArregloEstado() == 0)
                        merca[i].setArregloTipo(json.getJSONObject(i).getString("carne"));
                }
            }
        }
        catch (Exception e)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Error");
            builder.setMessage("Hubo un problema al cargar la informacion intentelo de nuevo mas tarde");
            builder.setPositiveButton("Aceptar",
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            enviarHTTP.enviarGet_eliminar(noMesa);
                            finish();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.setIcon(R.drawable.ic_launcher);
            alert.show();
        }
    }

    //recuperamos el pedido
    public void actualizar()
    {
        String alimentos,bebidas,cervezas;
        for (int i = 0; i < TAMANIO_OBJETO; i++)
        {
            if (merca[i].getArregloEstado() == 0)
            {
                for (int j = 0; j < LIMITE_TIPO; j++)
                {
                    alimentos = btnAlimentos[j].getText().toString();
                    if (alimentos.equals(merca[i].getArregloCargar()))
                    {
                        productos[j][merca[i].getArregloSilla()-1] = merca[i].getArregloCantidad();
                        enviar[j] = merca[i].getArregloCargar();
                        arregloCarne[j] = merca[i].getArregloTipo();
                        j = LIMITE_TIPO;
                    }
                }
                for (int j = 0; j < LIMITE_TIPO; j++)
                {
                    bebidas = btnBebidas[j].getText().toString();
                    if (bebidas.equals(merca[i].getArregloCargar()))
                    {
                        productos[j + LIMITE_BEBIDAS][merca[i].getArregloSilla()-1] = merca[i].getArregloCantidad();
                        enviar[j + LIMITE_BEBIDAS] = merca[i].getArregloCargar();
                        j = LIMITE_TIPO;
                    }

                }
                for (int j = 0; j < LIMITE_TIPO; j++)
                {
                    cervezas = btnCervezas[j].getText().toString();
                    if (cervezas.equals(merca[i].getArregloCargar()))
                    {
                        productos[j + LIMITE_CERVEZAS][merca[i].getArregloSilla()-1] = merca[i].getArregloCantidad();
                        enviar[j + LIMITE_CERVEZAS] = merca[i].getArregloCargar();
                        j = LIMITE_TIPO;
                    }
                }
            }
        }
    }
}
