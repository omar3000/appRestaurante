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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import static android.view.View.VISIBLE;
import static android.view.View.generateViewId;
import static com.example.martdev.org.primeraapp.MainActivity.ELIMINAR;
import static com.example.martdev.org.primeraapp.MainActivity.RECUPERAR_PEDIDO;
import static com.example.martdev.org.primeraapp.MainActivity.TRAER_PRODUCTOS;

public class Actividad2 extends Activity implements View.OnClickListener
{
    //servidro donde esta alojado los archivos php
    private final int SERVERPORT = 8050;
    private final int SERVERPORT2 = 8051;
    private final String SERVER_IP = "192.168.1.70:80";
    private final int MAXIMO_PRODUCTO  = 216;
    private final int LIMITE_ALIMENTOS = 108;
    private final short CANTIDAD_SILLAS = 15;
    private final short CANTIDAD_RB = 5;
    private final String TIPO_ALIMENTOS = "Desayunos";
    private final String TIPO_BEBIDAS = "Bebidas";
    private final boolean LISTA_MERCANCIA_SURTIDA = true;
    private final boolean LISTA_MERCANCIA_PENDIENTE = false;



    private final String CAMPO_DESCRIPCION = "Descripcion";
    private final String CAMPO_IMAGEN = "img";
    private final String CAMPO_TIPO = "nombre";

    private Bitmap loadedImage;
    private LayoutInflater inflater;
    private Dialog  dialog2;

    private Button btnGuardar;
    //botones para las productos
    private Button btnProductos[] = new Button[MAXIMO_PRODUCTO];
    //botones psts las sillas
    private Button btnsillas[] = new Button[CANTIDAD_SILLAS];
    //radio puntos para los subproductos
    private RadioButton rbSub[] = new RadioButton[CANTIDAD_RB];

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
    private short noMesa = 0;
    //saber si estan agregando o quitando productos
    private short estado = 1;
    //saber numero de mesa
    private short noSilla = 1;
    //nombre del campo subproducto que viee en el json
    private final String CAMPO_SUB = "subproducto";
    //contador para los radio button en la activity subproducto
    private int contadorRB = 0;

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
        noMesa = (bundle.getShort("no"));

        inicializar();

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
        //inicializarBotonesSubProductos();

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



    private void ConectarMetodosHTTP()
    {
        //enviamos y recuperamos un arreglo json por php
        Thread tr = new Thread()
        {
            @Override
            public void run()
            {

                final String resultado = httpHandler.doInBackground_2("", TRAER_PRODUCTOS);
                //final String resultado = httpHandler.leer();
                Log.d("EJEMPLO REVISAR", resultado);
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
                //final String resultado2 = httpHandler.leer2(noMesa);

                final String resultado2 = (String) httpHandler.doInBackground_2(String.valueOf(noMesa), RECUPERAR_PEDIDO);

                Log.d("leer2", "1"+ resultado2 + "2");

                if(resultado2.equals("vacio"))
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
                                        //errorCargar();
                                        ;
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

        for (int i = 0; i < 20; i++)
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

        }
    }

    private Drawable cargarImagen(int i)
    {
        Drawable img;
        try
        {
            URL imageUrl = null;
            //imageUrl = new URL(MainActivity.SERVIDOR + "/android/" + rutaImagen[i].toString());
            imageUrl = new URL(MainActivity.SERVIDOR + "/androd/default.bmp");
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
    public void onClick(View arg0) {
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
        for (short i = 1; i <= CANTIDAD_SILLAS; i++) {
            nombre = "btnSilla" + i;
            boton = res.getIdentifier(nombre, "id", getPackageName());
            //si el boton que se presiono es igual al del ciclo entra
            if (arg0.getId() == boton) {
                noSilla = i;
                cambioSilla(espacio);
                colorBotonSilla(i - 1);
            }
        }

        //si se presiona este boton
        if (R.id.btnGuardar == arg0.getId())
        {
            if (funcionesMerca.tamanioLista(LISTA_MERCANCIA_PENDIENTE) > 0 || funcionesMerca.tamanioLista(LISTA_MERCANCIA_SURTIDA) > 0) {
                activityNumeroEpleado();
            } else
                errorLeer();
        }

        //for para revisar todos los botones de alimentos
        for (int i = 1; i < MAXIMO_PRODUCTO; i++)
        {
            nombre = "btnProducto" + i;
            boton = res.getIdentifier(nombre, "id", getPackageName());
            //si el boton que se presiono es igual al del ciclo entra
            if (arg0.getId() == boton)
            {

                producto =   btnProductos[i-1].getText().toString();
                ArrayList<String> subpro = funcionesMerca.dameSubproductos(producto);
                if(subpro.size() > 1 )
                    activitySubProducto(producto, subpro);
                else
                    activityObservaciones(producto,"");
            }
        }
        //actualiza la lista
        adapter.notifyDataSetChanged();
    }

    private void accionesProductos(String espacio,String producto,boolean tipo, String observaciones)
    {
        int pos = funcionesMerca.busquedaCantidad(producto, noSilla, observaciones);

        Log.d("pos", "1"+ pos + "2");

        //volvemos a declarar el listview para poderlo manejar dentro de la funcion
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (Actividad2.this, android.R.layout.simple_list_item_1, pedido);

        listaItems.setAdapter(adapter);

        if(pos >= 0)
        {
            //actualiza el producto eliminando el actual de la lista
            pedido.remove(funcionesMerca.dameCantidad(pos,
                    LISTA_MERCANCIA_PENDIENTE)+ espacio + producto + espacio + observaciones);

            //le aumenta o disminuye uno depende del estado del boton toggle
            int cantidad =   (funcionesMerca.dameCantidad(pos,LISTA_MERCANCIA_PENDIENTE)  + estado);
            funcionesMerca.fijaCantidad(cantidad
                     ,pos,LISTA_MERCANCIA_PENDIENTE);

            //se añade el nuevo registro  a la lista ya actualizado
            pedido.add(funcionesMerca.dameCantidad(pos,
                    LISTA_MERCANCIA_PENDIENTE) + espacio + producto + espacio + observaciones);

            //en caso de ser negativo la cantidad elimina el producto de la lista
            if (funcionesMerca.dameCantidad(pos,LISTA_MERCANCIA_PENDIENTE)  <= 0)
            {
                pedido.remove(funcionesMerca.dameCantidad(pos
                        ,LISTA_MERCANCIA_PENDIENTE) + espacio + producto + espacio + observaciones);
                funcionesMerca.eliminarElemento(pos);
            }
        }
        else if(estado > 0)
        {
            funcionesMerca.agregarElemento(tipo,estado,producto,noSilla,
                    LISTA_MERCANCIA_PENDIENTE,false, observaciones,0);


            //se añade el nuevo registro  a la lista ya actualizado
            pedido.add(estado + espacio + producto + espacio + observaciones);
        }

        //actualiza la lista
        adapter.notifyDataSetChanged();
    }

    private void cambioSilla(String espacio)
    {
        //si salen de la mesa borra todos los elementos de la lista
        pedido.clear();

        for (int i = 0; i < funcionesMerca.tamanioLista(LISTA_MERCANCIA_SURTIDA);i++)
            if(funcionesMerca.dameSurtido(i,LISTA_MERCANCIA_SURTIDA)
                    && funcionesMerca.dameSilla(i, LISTA_MERCANCIA_SURTIDA) == noSilla)
                pedido.add("✔ " + funcionesMerca.dameCantidad(i,
                        LISTA_MERCANCIA_SURTIDA) + espacio + funcionesMerca.dameNombre(i,
                        LISTA_MERCANCIA_SURTIDA) + espacio + funcionesMerca.dameObservaciones(i, LISTA_MERCANCIA_SURTIDA));

        for (int j = 0; j < funcionesMerca.tamanioLista(LISTA_MERCANCIA_PENDIENTE); j++)
            if (funcionesMerca.dameSilla(j, LISTA_MERCANCIA_PENDIENTE) == noSilla )
                  pedido.add(funcionesMerca.dameCantidad(j,
                          LISTA_MERCANCIA_PENDIENTE) + espacio +
                          funcionesMerca.dameNombre(j, LISTA_MERCANCIA_PENDIENTE) + espacio + funcionesMerca.dameObservaciones(j,LISTA_MERCANCIA_PENDIENTE));

        Log.d("cambio", "1"+ "llego aqui" + "2");
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
        int contador1 = 0, contador2 = LIMITE_ALIMENTOS; // contador3 = LIMITE_BEBIDAS, contador4 = LIMITE_CERVEZA;
        try
        {
            json = new JSONArray(response);
            Log.d("EJEMPLO TAMAÑO",json.toString());
            String descripcion;
            for (int i = 0; i < json.length(); i++)
            {
                descripcion = json.getJSONObject(i).getString(CAMPO_DESCRIPCION);
                Log.d("compare", json.getJSONObject(i).getString(CAMPO_TIPO) + " " +  TIPO_ALIMENTOS);
                  if (json.getJSONObject(i).getString(CAMPO_TIPO).toString().equals(TIPO_ALIMENTOS))
                  {
                    btnProductos[contador1].setText(descripcion);
                    btnProductos[contador1].setVisibility(VISIBLE);
                    //rutaImagen[contador1] = json.getJSONObject(i)
                           // .getString(CAMPO_IMAGEN).toString();
                    rutaImagen[contador1] = "default.bmp";
                    contador1++;
                  }

                else if (json.getJSONObject(i).getString(CAMPO_TIPO).toString().equals(TIPO_BEBIDAS))
                {
                    btnProductos[contador2].setText(descripcion);
                    btnProductos[contador2].setVisibility(VISIBLE);
                    //rutaImagen[contador2] = json.getJSONObject(i).getString(CAMPO_IMAGEN)
                      //      .toString();

                    contador2++;
                }

                agregarSubproducto(descripcion,json.getJSONObject(i).getString(CAMPO_SUB).split(","));
            }
        }
        catch (Exception e)
        {
            Log.d("error",e.toString());

        }
        //llamamos al metodo para cargar las imagenes
        //downloadFile();
    }

    private void agregarSubproducto(String descripcion,String subProductos[])
    {
        for (int i =0; i< subProductos.length; i++) {
            funcionesMerca.agregarSubProductos(descripcion,subProductos[i]);
        }
    }

    private void errorLeer()
    {
        AlertDialog alert;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Advertenia");
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


    private void errorPin()
    {
        AlertDialog.Builder builder;
        AlertDialog alert;

        builder = new AlertDialog.Builder(this);

        builder.setTitle("Error");
        builder.setMessage("Hubo un problema con su pin verifiquelo por favor");
        builder.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });

        alert = builder.create();
        alert.setIcon(R.drawable.ic_launcher);
        alert.show();
    }


    private void activitySubProducto(final String elemento, final ArrayList<String> subpro)
    {
        contadorRB = 0;
        final Dialog dialog = new Dialog(Actividad2.this);
        final  View v = getLayoutInflater().inflate(R.layout.subproducto, null);
        listasBotones.btnListaSubProducto = new ArrayList<RadioButton>();
        final RadioGroup  group = (RadioGroup) v.findViewById(R.id.rdgGrupo);
        Window window;
        for ( String x: subpro)
        {
            Resources res = getResources();
            String nombre = "rb" + (contadorRB + 1);
            int boton = res.getIdentifier(nombre, "id", getPackageName());
            rbSub[contadorRB] = (RadioButton) v.findViewById(boton);
            rbSub[contadorRB].setVisibility(v.VISIBLE);
            rbSub[contadorRB].setText(x);
            listasBotones.btnListaSubProducto.add(rbSub[contadorRB]);
            contadorRB++;
        }

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                RadioButton rb=(RadioButton)v.findViewById(checkedId);
                String observaciones = rb.getText().toString();
                //accionesProductos(espacio, elemento, true, observaciones);
                dialog.dismiss();
                activityObservaciones(elemento, observaciones);

            }
        });


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable
                (android.graphics.Color.TRANSPARENT));
        dialog.setContentView(v);
        dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);
        window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCancelable(false);
        dialog.show();
    }

    private void activityObservaciones(final String elemento,final String observaciones)
    {
        //desactivarRadioButtonSubcategorias(elemento);
        estado = 1;
        inflater = getLayoutInflater();
        final View  dialoglayout2 =  inflater.inflate(R.layout.observaciones, null);
        final Button  btnEnviar = (Button) dialoglayout2.findViewById(R.id.Enviar);
        final Button regresar = (Button) dialoglayout2.findViewById(R.id.Regresar);
        final EditText  etObservaciones = (EditText) dialoglayout2.findViewById(R.id.etObservaciones);
        final Dialog  dialog2 = new Dialog(Actividad2.this);
        final String espacio = "-";

        etObservaciones.setText(observaciones);
        etObservaciones.setSelection(observaciones.length());

        etObservaciones.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.d("tecla", event.getKeyCode() + " " + keyCode + " " + KeyEvent.KEYCODE_ENTER);
                // If the event is a key-down event on the "enter" button
                if (keyCode == KeyEvent.KEYCODE_ENTER)
                {
                    String observacione = etObservaciones.getText().toString();

                    try
                    {
                        accionesProductos(espacio, elemento, true, observacione);
                        //accionesProductos(espacio, elemento, true, observaciones);
                        //funcionesMerca.enviarPedido(numeroEmpleado,noMesa);
                        // cierra la actividad
                        dialog2.dismiss();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getApplicationContext(),"error,error,error",
                                Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
                return false;
            }
        });


        btnEnviar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String observacione = etObservaciones.getText().toString();

                try
                {
                    accionesProductos(espacio, elemento, true, observacione);
                        //accionesProductos(espacio, elemento, true, observaciones);
                        //funcionesMerca.enviarPedido(numeroEmpleado,noMesa);
                        // cierra la actividad
                        dialog2.dismiss();
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"error,error,error",
                            Toast.LENGTH_LONG).show();
                }
                    /*
                    else
                        Toast.makeText(getApplicationContext(), "¡Numero de empleado inmcorrecto " +
                                "intentelo de nuevo!", Toast.LENGTH_SHORT).show();

                        "intentelo de nuevo!", Toast.LENGTH_SHORT).show();*/
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


    private void activityNumeroEpleado()
    {
        inflater = getLayoutInflater();
        final View  dialoglayout2 =  inflater.inflate(R.layout.confirmar_envio, null);
        dialog2= new Dialog(Actividad2.this);
        final Button regresar = (Button) dialoglayout2.findViewById(R.id.Regresar);



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


    public void ClickPin(View view)
    {
        String pin ="0";
        if(view.getId() == R.id.btnPin1)
        {
            pin = "1";
        }
        else if(view.getId() == R.id.btnPin2)
        {
            pin = "2";
        }
        else if(view.getId() == R.id.btnPin3)
        {
            pin = "3";
        }
        else if(view.getId() == R.id.btnPin4)
        {
            pin = "4";
        }
        else if(view.getId() == R.id.btnPin5)
        {
            pin = "5";
        }

        try
        {

            String validacionPin = funcionesMerca.enviarPedido(pin, noMesa);

            if (validacionPin.equals("pinInvalido")) {
                errorPin();
            } else {
                // cierra la actividad
                dialog2.dismiss();
                finish();
            }

            /*
            final String pim = pin;
            Thread tr1 = new Thread()
            {
                @Override
                public void run()
                {

                    final String validacionPin = funcionesMerca.enviarPedido(pim, noMesa);

                    Log.d("leer2", "1"+ validacionPin + "2");

                    if(validacionPin.equals("pinInvalido"))
                    {
                        errorPin();
                    }
                }
            };
            tr1.start();

            // cierra la actividad
            dialog2.dismiss();
            finish();
            */


        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"error,error,error",
                    Toast.LENGTH_LONG).show();
        }
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
                        //httpHandler.enviarGet_eliminar(noMesa);
                        httpHandler.doInBackground_2(String.valueOf(noMesa), ELIMINAR);
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
            String elemento =  adapter.getItemAtPosition(position).toString().substring(2);
            String[] parts = elemento.split("-");
            elemento = parts[0];
            final String espacio = "-";


            //if(funcionesMerca.verificarTipo(elemento, noSilla))
              //  activityCarne(elemento);

            estado = -1;
            Log.d("elemento", elemento + "");
            if(parts.length  > 1)
                accionesProductos(espacio, elemento, true, parts[1]);
            else
                accionesProductos(espacio, elemento, true, "");

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