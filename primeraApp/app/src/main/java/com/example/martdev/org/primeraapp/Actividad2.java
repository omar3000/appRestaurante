package com.example.martdev.org.primeraapp;

/**
 * Created by omar franco alvarez on 03/06/2015.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.ToggleButton;
import org.json.JSONArray;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Actividad2 extends Activity implements View.OnClickListener
{
    //servidro donde esta alojado los archivos php

    private final int MAXIMO_PRODUCTO  = 216;
    private final int LIMITE_ALIMENTOS = 54;
    private final int LIMITE_BEBIDAS = 108;
    private final int LIMITE_CERVEZA = 162;
    private final int CANTIDAD_SILLAS = 15;
    private final int TIPO_ALIMENTOS = 12;
    private final int TIPO_BEBIDAS = 13;
    private final int TIPO_CERVEZAS = 14;
    private final int TIPO_PAQUETES = 78;
    private final int LISTA_MERCANCIA_SURTIDA = 1;
    private final int LISTA_MERCANCIA_PENDIENTE = 2;



    private Bitmap loadedImage;
    private LayoutInflater inflater;

    private Button btnGuardar;
    //botones para las productos
    private Button btnProductos[] = new Button[MAXIMO_PRODUCTO];
    //botones psts las sillas
    private Button btnsillas[] = new Button[CANTIDAD_SILLAS];

    //lista de elementos para cargar el listview
    private ListView listaItems;
    private List<String> pedido = new ArrayList<String>();

    //objeto para los metodos de la lista
    funcionesMercancia funcionesMerca = new funcionesMercancia();

    //arreglosp ara cargar todos los productos activos cantidad,nombre y foto
    private String rutaImagen[] = new String[MAXIMO_PRODUCTO];

    //boton para agregar o quitar productos
    private ToggleButton agregar;
    //numero de mesa activa
    private int noMesa = 0;
    //saber si estan agregando o quitando productos
    private int estado = 1;
    //saber numero de mesa
    private int noSilla = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad2);

        new Thread(new ClientThread()).start();
        new Thread(new ClientThread_2()).start();

        //para que acepte la conexion con http post
        StrictMode.ThreadPolicy p = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(p);

        //recuperamos la informacion mandada por la activity anterios
        Bundle bundle = getIntent().getExtras();
        EditText et = (EditText) findViewById(R.id.editText2);
        et.setText(bundle.getString("mesa"));
        noMesa = (bundle.getInt("no"));

        inicializar();
        agregarQuitarProducto();
        ConectarMetodosHTTP();
    }

    private void inicializar()
    {
        configurarPestañas();

        //configuramos la lista del pedido
        listaItems = (ListView) findViewById(R.id.ListView1);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (Actividad2.this, android.R.layout.simple_list_item_1, pedido);
        listaItems.setAdapter(adapter);
        listaItems.setOnItemClickListener(new ListClickHandler());

        inicializarBotonesProductos();
        inicializarBotonesSillas();

        for (int i = 0; i < MAXIMO_PRODUCTO; i++)
            rutaImagen[i] = "";

        noSilla = 1;
    }

    private void configurarPestañas()
    {
        //configuramos las pestañas de las tres categorias
        TabHost th = (TabHost) findViewById(R.id.tabHost);

        th.setup();
        TabHost.TabSpec ts1 = th.newTabSpec("Tab1");
        ts1.setIndicator("Alimentos");
        ts1.setContent(R.id.tab1);
        th.addTab(ts1);

        th.setup();
        TabHost.TabSpec ts2 = th.newTabSpec("Tab2");
        ts2.setIndicator("Bebidas");
        ts2.setContent(R.id.tab2);
        th.addTab(ts2);

        th.setup();
        TabHost.TabSpec ts3 = th.newTabSpec("Tab3");
        ts3.setIndicator("Cerveza");
        ts3.setContent(R.id.tab3);
        th.addTab(ts3);

        th.setup();
        TabHost.TabSpec ts4 = th.newTabSpec("Tab4");
        ts4.setIndicator("Paquetes");
        ts4.setContent(R.id.tab4);
        th.addTab(ts4);
    }

    private void inicializarBotonesProductos()
    {
        //iniciamos las listas de los botones declaradas arriba
        listasBotones.btnListaProductos = new ArrayList<Button>();

        //activamos los botones mediante la lista anterios
        int h = 0;
        for (int id : listasBotones.BUTTON_IDS)
        {
            btnProductos[h] = (Button) findViewById(id);
            btnProductos[h].setOnClickListener(this);
            listasBotones.btnListaProductos.add(btnProductos[h]);
            h++;
        }
    }

    private void inicializarBotonesSillas()
    {
        //iniciamos las listas de los botones declaradas arriba
        listasBotones.btnListaSillas = new ArrayList<Button>();

        //activamos los botones mediante la lista anterios
        int h = 0;
        for (int id : listasBotones.BUTTON_IDS_SILLAS) {
            btnsillas[h] = (Button) findViewById(id);
            btnsillas[h].setOnClickListener(this);
            listasBotones.btnListaSillas.add(btnsillas[h]);
            h++;
        }

        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(this);
    }

    private void agregarQuitarProducto()
    {
        agregar = (ToggleButton) findViewById(R.id.toggleButton);
        //programamos el boton de agregar o quitar y le asignamos el valor a la variable estado
        agregar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                    estado = -1;
                else
                    estado = 1;
            }
        });
    }

    private void ConectarMetodosHTTP()
    {
        //enviamos y recuperamos un arreglo json por php
        Thread tr = new Thread()
        {
            @Override
            public void run()
            {
                final String resultado = httpHandler.leer();
                runOnUiThread(
                        new Runnable()
                        {

                            @Override
                            public void run()
                            {
                                obtDatosJSON(resultado);
                            }
                        });
            }
        };
        tr.start();

        Thread tr2 = new Thread()
        {
            @Override
            public void run()
            {
                final String resultado2 = httpHandler.leer2(noMesa);
                if(resultado2.equals("ERROR"))
                {
                    errorLeer();
                }
                else
                {
                    runOnUiThread(
                            new Runnable() {

                                @Override
                                public void run() {
                                    if(!funcionesMerca.obtDatosJSON_2(resultado2))
                                        errorCargar();
                                    else
                                    {
                                        funcionesMerca.actualizar();
                                        cambioSilla("-");
                                        colorBotonSilla(0);
                                    }
                                }
                            });
                }
            }
        };
        tr2.start();
    }

    //cargamos las imagenes
    private void downloadFile()
    {
        Drawable foto;

        for (int i = 0; i < LIMITE_ALIMENTOS; i++)
        {
            if (rutaImagen[i].compareTo("") != 0)
            {
                //guardamos la imagen que retorna la funcion
                foto = cargarImagen(i);
                //se la asignamos al boton correspondiente
                btnProductos[i].setCompoundDrawablesWithIntrinsicBounds(foto, null, null, null);
            }
            if (rutaImagen[i + LIMITE_ALIMENTOS].compareTo("") != 0)
            {
                //guardamos la imagen que retorna la funcion
                foto = cargarImagen(i+LIMITE_ALIMENTOS);
                //se la asignamos al boton correspondiente
                btnProductos[i+LIMITE_ALIMENTOS].setCompoundDrawablesWithIntrinsicBounds(foto,
                        null, null, null);
            }
            if (rutaImagen[i + LIMITE_BEBIDAS].compareTo("") != 0)
            {
                //guardamos la imagen que retorna la funcion
                foto = cargarImagen(i+LIMITE_BEBIDAS);
                //se la asignamos al boton correspondiente
                btnProductos[i+LIMITE_BEBIDAS].setCompoundDrawablesWithIntrinsicBounds
                        (foto, null, null, null);
            }
            if (rutaImagen[i + LIMITE_CERVEZA].compareTo("") != 0)
            {
                //guardamos la imagen que retorna la funcion
                foto = cargarImagen(i+LIMITE_CERVEZA);
                //se la asignamos al boton correspondiente
                btnProductos[i+LIMITE_CERVEZA].setCompoundDrawablesWithIntrinsicBounds
                        (foto, null, null, null);
            }
        }
    }

    private Drawable cargarImagen(int i)
    {
        Drawable img;
        try
        {
            URL imageUrl = null;
            imageUrl = new URL(MainActivity.SERVIDOR + "/android/" + rutaImagen[i].toString());
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.connect();
            loadedImage = BitmapFactory.decodeStream(conn.getInputStream());
        }
        catch (IOException e)
        {
            Toast.makeText(getApplicationContext(), "Error cargando la imagen: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        img = new BitmapDrawable(getResources(), loadedImage);
        return img;
    }

    //programamos el boton de atras
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            salirSinGuardar(noMesa);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



    //configuramos todos los clicks de los botones en la app
    public void onClick(View arg0)
    {
        //volvemos a declarar el listview para poderlo manejar dentro de la funcion
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (Actividad2.this, android.R.layout.simple_list_item_1, pedido);

        String espacio = "-";
        String nombre;
        Resources res = getResources();
        String producto;
        int boton;

        listaItems.setAdapter(adapter);

        //for para revisar todos los botones de las sillas
        for (int i = 1; i <= CANTIDAD_SILLAS ; i++)
        {
            nombre = "btnSilla" + i;
            boton = res.getIdentifier(nombre, "id", getPackageName());
            //si el boton que se presiono es igual al del ciclo entra
            if (arg0.getId() == boton)
            {
                noSilla = i;
                cambioSilla(espacio);
                colorBotonSilla(i-1);
            }
        }

        //si se presiona este boton
        if (R.id.btnGuardar == arg0.getId())
            activityNumeroEpleado();

        //for para revisar todos los botones de alimentos
        for (int i = 1; i < MAXIMO_PRODUCTO; i++)
        {
            nombre = "btnProducto" + i;
            boton = res.getIdentifier(nombre, "id", getPackageName());
            //si el boton que se presiono es igual al del ciclo entra
            if (arg0.getId() == boton)
            {
                producto =   btnProductos[i-1].getText().toString();
                if(i <= TIPO_ALIMENTOS)
                    accionesProductos(espacio, producto, TIPO_ALIMENTOS);
                else if(i > TIPO_ALIMENTOS && i <= TIPO_BEBIDAS)
                    accionesProductos(espacio, producto, TIPO_BEBIDAS);
                else if(i > TIPO_BEBIDAS && i <= TIPO_CERVEZAS)
                    accionesProductos(espacio, producto, TIPO_CERVEZAS);
                else
                    accionesProductos(espacio, producto, TIPO_PAQUETES);
            }
        }
        //actualiza la lista
        adapter.notifyDataSetChanged();
    }

    private void accionesProductos(String espacio,String producto,int tipo)
    {
        int pos = funcionesMerca.busquedaCantidad(producto, noSilla);

        if(pos >= 0)
        {
            //actualiza el producto eliminando el actual de la lista
            pedido.remove(String.format("%02d",funcionesMerca.dameCantidad(pos,
                    LISTA_MERCANCIA_PENDIENTE))+ espacio + producto);

            //le aumenta o disminuye uno depende del estado del boton toggle
            funcionesMerca.fijaCantidad(funcionesMerca.dameCantidad(pos,LISTA_MERCANCIA_PENDIENTE)
                    + estado,pos,LISTA_MERCANCIA_PENDIENTE);

            //se añade el nuevo registro  a la lista ya actualizado
            pedido.add(String.format("%02d",funcionesMerca.dameCantidad(pos,
                    LISTA_MERCANCIA_PENDIENTE)) + espacio + producto);

            //en caso de ser negativo la cantidad elimina el producto de la lista
            if (funcionesMerca.dameCantidad(pos,LISTA_MERCANCIA_PENDIENTE)  <= 0)
            {
                pedido.remove(String.format("%02d", funcionesMerca.dameCantidad(pos
                        ,LISTA_MERCANCIA_PENDIENTE)) + espacio + producto);
                funcionesMerca.eliminarElemento(pos);
            }
        }
        else if(estado > 0)
        {
            funcionesMerca.agregarElemento(tipo,estado,producto,"Carnitas",0,noSilla,
                    LISTA_MERCANCIA_PENDIENTE);

            //se añade el nuevo registro  a la lista ya actualizado
            pedido.add(String.format("%02d",estado) + espacio + producto);
        }
    }

    private void cambioSilla(String espacio)
    {
        //si salen de la mesa borra todos los elementos de la lista
        pedido.clear();

        for (int i = 0; i < funcionesMerca.tamanioLista(LISTA_MERCANCIA_SURTIDA);i++)
            if(funcionesMerca.dameSurtido(i,LISTA_MERCANCIA_SURTIDA) == 1
                    && funcionesMerca.dameSilla(i, LISTA_MERCANCIA_SURTIDA) == noSilla)
                pedido.add("✔ " + String.format("%02d", funcionesMerca.dameCantidad(i,
                        LISTA_MERCANCIA_SURTIDA)) + espacio + funcionesMerca.dameNombre(i,
                        LISTA_MERCANCIA_SURTIDA).toString());

        for (int j = 0; j < funcionesMerca.tamanioLista(LISTA_MERCANCIA_PENDIENTE); j++)
            if (funcionesMerca.dameSilla(j, LISTA_MERCANCIA_PENDIENTE) == noSilla )
                  pedido.add(String.format("%02d",funcionesMerca.dameCantidad(j,
                          LISTA_MERCANCIA_PENDIENTE)) + espacio +
                          funcionesMerca.dameNombre(j, LISTA_MERCANCIA_PENDIENTE).toString());
    }

    private void colorBotonSilla(int pos)
    {
        btnsillas[pos].getBackground().setColorFilter(0xFFFF0000, PorterDuff.Mode.MULTIPLY);
        limpiarColorSillas(pos);
    }

    private void limpiarColorSillas(int pos)
    {
        for(int i=0;i<CANTIDAD_SILLAS;i++)
            if(i!=pos)
                btnsillas[i].getBackground().setColorFilter(0xffcccccc, PorterDuff.Mode.MULTIPLY);
    }

    //funcion para recuperar los productos e imagenes atraves de un arreglo json
    private void obtDatosJSON(String response)
    {
        JSONArray json;
        int contador1 = 0, contador2 = LIMITE_ALIMENTOS, contador3 = LIMITE_BEBIDAS, contador4 = LIMITE_CERVEZA;
        try
        {
            json = new JSONArray(response);
            for (int i = 0; i < json.length(); i++)
            {
                if (
                        json.getJSONObject(i).getInt(CAMPO_TIPO) == TIPO_ALIMENTOS)
                {
                    btnProductos[contador1].setText(json.getJSONObject(i)
                            .getString(CAMPO_DESCRIPCION));
                    btnProductos[contador1].setVisibility(View.VISIBLE);
                    rutaImagen[contador1] = json.getJSONObject(i)
                            .getString(CAMPO_IMAGEN).toString();
                    contador1++;
                }
                else if (json.getJSONObject(i).getInt(CAMPO_TIPO) == TIPO_BEBIDAS)
                {
                    btnProductos[contador2].setText(json.getJSONObject(i)
                            .getString(CAMPO_DESCRIPCION));
                    btnProductos[contador2].setVisibility(View.VISIBLE);
                    rutaImagen[contador2] = json.getJSONObject(i).getString(CAMPO_IMAGEN)
                            .toString();
                    contador2++;
                }
                else if (json.getJSONObject(i).getInt(CAMPO_TIPO) == TIPO_CERVEZAS)
                {
                    btnProductos[contador3].setText(json.getJSONObject(i)
                            .getString(CAMPO_DESCRIPCION));
                    btnProductos[contador3].setVisibility(View.VISIBLE);
                    rutaImagen[contador3] = json.getJSONObject(i)
                            .getString(CAMPO_IMAGEN).toString();
                    contador3++;
                }
                else if (json.getJSONObject(i).getInt(CAMPO_TIPO) == TIPO_PAQUETES)
                {
                    btnProductos[contador4].setText(json.getJSONObject(i)
                            .getString(CAMPO_DESCRIPCION));
                    btnProductos[contador4].setVisibility(View.VISIBLE);
                    rutaImagen[contador4] = json.getJSONObject(i)
                            .getString(CAMPO_IMAGEN).toString();
                    contador4++;
                }
            }
        }
        catch (Exception e)
        {

        }
        //llamamos al metodo para cargar las imagenes
        downloadFile();
    }

    private void errorCargar()
    {
        AlertDialog.Builder builder;
        AlertDialog alert;

        builder = new AlertDialog.Builder(this);

        builder.setTitle("Error");
        builder.setMessage("Hubo un problema al cargar la informacion intentelo de " +
                "nuevo mas tarde");
        builder.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        httpHandler.enviarGet_eliminar(noMesa);
                        finish();
                    }
                });

        alert = builder.create();
        alert.setIcon(R.drawable.ic_launcher);
        alert.show();
    }

    private void activityCarne(final String elemento)
    {
        inflater = getLayoutInflater();
        final View dialoglayout = inflater.inflate(R.layout.tipo_carne, null);
        final RadioButton carnitas = (RadioButton) dialoglayout.findViewById(R.id.rbCarnitas);
        final RadioButton lengua = (RadioButton) dialoglayout.findViewById(R.id.rbLengua);
        final RadioButton buche = (RadioButton) dialoglayout.findViewById(R.id.rbBuche);
        final RadioButton panza = (RadioButton) dialoglayout.findViewById(R.id.rbPanza);
        final Dialog dialog = new Dialog(Actividad2.this);

        Window window;

        carnitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (carnitas.isChecked()) {
                    funcionesMerca.guardarTipo(elemento, "Carnitas", noSilla);
                    dialog.dismiss();
                }
            }
        });
        lengua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lengua.isChecked()) {
                    funcionesMerca.guardarTipo(elemento, "Lengua", noSilla);
                    dialog.dismiss();
                }
            }
        });
        buche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buche.isChecked()) {
                    funcionesMerca.guardarTipo(elemento, "Buche", noSilla);
                    dialog.dismiss();
                }
            }
        });
        panza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (panza.isChecked()) {
                    funcionesMerca.guardarTipo(elemento, "Panza", noSilla);
                    dialog.dismiss();
                }
            }
        });

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable
                (android.graphics.Color.TRANSPARENT));
        dialog.setContentView(dialoglayout);
        dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);
        window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCancelable(false);
        dialog.show();
    }

    private void activityNumeroEpleado()
    {
        inflater = getLayoutInflater();
        final View  dialoglayout2 =  inflater.inflate(R.layout.confirmar_envio, null);
        final Button  btnEnviar = (Button) dialoglayout2.findViewById(R.id.Enviar);
        final Button regresar = (Button) dialoglayout2.findViewById(R.id.Regresar);
        final EditText  noEmpleado = (EditText) dialoglayout2.findViewById(R.id.etEmpleado);
        final Dialog  dialog2 = new Dialog(Actividad2.this);

        btnEnviar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int valido = noEmpleado.getText().toString()!=null &&
                        !noEmpleado.getText().toString().equals("")
                        ? Integer.parseInt(noEmpleado.getText().toString()) : -1;

                if (valido > 0)
                {
                    final int numeroEmpleado = Integer.parseInt(noEmpleado.getText().toString());
                    if (numeroEmpleado > 0 && numeroEmpleado < 10)
                    {
                        try
                        {
                            if(funcionesMerca.tamanioLista(LISTA_MERCANCIA_PENDIENTE) > 0)
                            {
                                funcionesMerca.enviarPedido(numeroEmpleado,noMesa);
                                // cierra la actividad
                                dialog2.dismiss();
                                finish();
                            }
                            else
                                errorLeer();
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getApplicationContext(),"error,error,error",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    else
                        Toast.makeText(getApplicationContext(), "¡Numero de empleado inmcorrecto " +
                                "intentelo de nuevo!", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "¡Numero de empleado inmcorrecto " +
                            "intentelo de nuevo!", Toast.LENGTH_SHORT).show();
            }
        });

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2.dismiss();

            }
        });

        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog2.setContentView(dialoglayout2);
        dialog2.getWindow().setFormat(PixelFormat.TRANSLUCENT);
        final Window window = dialog2.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog2.show();
    }

    private void errorLeer()
    {
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Error");
        builder.setMessage("Pedido vacio,No hay nada que enviar,Verifique el pedido por favor!");

        builder.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        alert = builder.create();
        alert.setIcon(R.drawable.ic_launcher);
        alert.show();
    }

    private void salirSinGuardar(final int noMesa)
    {
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Alerta");
        builder.setMessage("Esta apunto de salir de la mesa y se perderan los cambios " +
                "no enviados ¿desea continuar?");

        builder.setPositiveButton("SI",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //si salen de la mesa borra todos los elementos de la lista
                        pedido.clear();
                        funcionesMerca.limpiarLista(LISTA_MERCANCIA_SURTIDA);
                        funcionesMerca.limpiarLista(LISTA_MERCANCIA_PENDIENTE);
                        //manda llamar al metodo elimina que envia parametros get a un archivo
                        //php en el servidor
                        httpHandler.enviarGet_eliminar(noMesa);
                        //por ultimo cierra la actividad y regresa al menu principal
                        finish();
                    }
                });

        builder.setNegativeButton("NO",
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });

        alert = builder.create();
        alert.setCancelable(false);
        alert.setIcon(R.drawable.ic_launcher);
        alert.show();
    }

    public class ListClickHandler implements AdapterView.OnItemClickListener
    {
        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3)
        {
            // TODO Auto-generated method stub
            String elemento =  adapter.getItemAtPosition(position).toString().substring(3);
            if(funcionesMerca.verificarTipo(elemento, noSilla))
                activityCarne(elemento);
        }
    }

    public class ClientThread implements Runnable
    {
        @Override
        public void run()
        {
            try
            {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                funcionesMerca.socket = new Socket(serverAddr, SERVERPORT);

            }
            catch (UnknownHostException e1)
            {
                e1.printStackTrace();
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
        }
    }

    public class ClientThread_2 implements Runnable
    {
        @Override
        public void run()
        {
            try
            {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                funcionesMerca.socket = new Socket(serverAddr, SERVERPORT2);

            }
            catch (UnknownHostException e1)
            {
                e1.printStackTrace();
            }
            catch (IOException e1)
            {
                e1.printStackTrace();
            }
        }
    }
}