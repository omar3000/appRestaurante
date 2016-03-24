package com.example.martdev.org.primeraapp;

/**
 * Created by omar on 03/06/2015.
 */

import android.app.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class Actividad2 extends Activity implements View.OnClickListener {

    private Bitmap loadedImage;
    public LayoutInflater inflater;

    //servidro donde esta alojado los archivos php
    String servidor = "http://192.168.1.96:80";
    private static final int SERVERPORT = 8050;
    private static final String SERVER_IP = "192.168.1.96";
    boolean bandera = true;
    boolean comodin = false;

    Button btnGuardar;
    //botones para las diferentes categorias
    Button btnAlimentos[] = new Button[54];
    Button btnBebidas[] = new Button[54];
    Button btnCervezas[] = new Button[54];

    //lista de elementos para cargar el listview
    ListView listaItems;
    List<String> pedido = new ArrayList<String>();

    //arreglosp ara cargar todos los productos activos cantidad,nombre y foto
    int productos[][] = new int[162][15];
    String enviar[] = new String[162];
    String rutaImagen[] = new String[162];
    String arregloCarne[] = new String[162];



    //boton para agregar o quitar productos
    ToggleButton agregar;
    //numero de mesa activa
    int noMesa = 0;
    //saber si estan agregando o quitando productos
    int estado = 1;

    //saber numero de mesa
    int noSilla = 1;


    Button silla1;
    Button silla2;
    Button silla3;
    Button silla4;
    Button silla5;
    Button silla6;
    Button silla7;
    Button silla8;
    Button silla9;
    Button silla10;
    Button silla11;
    Button silla12;
    Button silla13;
    Button silla14;
    Button silla15;

    public HttpHandler enviarHTTP = new HttpHandler();
    public Mercancia merca[] = new Mercancia[200];


    //activar y controlar los botones de alimentos
    private List<Button> btnListaAlimentos;

    private static final int[] BUTTON_IDS =
            {
                    R.id.btnAlimento1,
                    R.id.btnAlimento2,
                    R.id.btnAlimento3,
                    R.id.btnAlimento4,
                    R.id.btnAlimento5,
                    R.id.btnAlimento6,
                    R.id.btnAlimento7,
                    R.id.btnAlimento8,
                    R.id.btnAlimento9,
                    R.id.btnAlimento10,
                    R.id.btnAlimento11,
                    R.id.btnAlimento12,
                    R.id.btnAlimento13,
                    R.id.btnAlimento14,
                    R.id.btnAlimento15,
                    R.id.btnAlimento16,
                    R.id.btnAlimento17,
                    R.id.btnAlimento18,
                    R.id.btnAlimento19,
                    R.id.btnAlimento20,
                    R.id.btnAlimento21,
                    R.id.btnAlimento22,
                    R.id.btnAlimento23,
                    R.id.btnAlimento24,
                    R.id.btnAlimento25,
                    R.id.btnAlimento26,
                    R.id.btnAlimento27,
                    R.id.btnAlimento28,
                    R.id.btnAlimento29,
                    R.id.btnAlimento30,
                    R.id.btnAlimento31,
                    R.id.btnAlimento32,
                    R.id.btnAlimento33,
                    R.id.btnAlimento34,
                    R.id.btnAlimento35,
                    R.id.btnAlimento36,
                    R.id.btnAlimento37,
                    R.id.btnAlimento38,
                    R.id.btnAlimento39,
                    R.id.btnAlimento40,
                    R.id.btnAlimento41,
                    R.id.btnAlimento42,
                    R.id.btnAlimento43,
                    R.id.btnAlimento44,
                    R.id.btnAlimento45,
                    R.id.btnAlimento46,
                    R.id.btnAlimento47,
                    R.id.btnAlimento48,
                    R.id.btnAlimento49,
                    R.id.btnAlimento50,
                    R.id.btnAlimento51,
                    R.id.btnAlimento52,
                    R.id.btnAlimento53,
                    R.id.btnAlimento54,
            };

    //activar o controlas lo botones de bebidas
    private List<Button> btnListaBebidas;
    private static final int[] BUTTON_IDS2 =
            {
                    R.id.btnBebida1,
                    R.id.btnBebida2,
                    R.id.btnBebida3,
                    R.id.btnBebida4,
                    R.id.btnBebida5,
                    R.id.btnBebida6,
                    R.id.btnBebida7,
                    R.id.btnBebida8,
                    R.id.btnBebida9,
                    R.id.btnBebida10,
                    R.id.btnBebida11,
                    R.id.btnBebida12,
                    R.id.btnBebida13,
                    R.id.btnBebida14,
                    R.id.btnBebida15,
                    R.id.btnBebida16,
                    R.id.btnBebida17,
                    R.id.btnBebida18,
                    R.id.btnBebida19,
                    R.id.btnBebida20,
                    R.id.btnBebida21,
                    R.id.btnBebida22,
                    R.id.btnBebida23,
                    R.id.btnBebida24,
                    R.id.btnBebida25,
                    R.id.btnBebida26,
                    R.id.btnBebida27,
                    R.id.btnBebida28,
                    R.id.btnBebida29,
                    R.id.btnBebida30,
                    R.id.btnBebida31,
                    R.id.btnBebida32,
                    R.id.btnBebida33,
                    R.id.btnBebida34,
                    R.id.btnBebida35,
                    R.id.btnBebida36,
                    R.id.btnBebida37,
                    R.id.btnBebida38,
                    R.id.btnBebida39,
                    R.id.btnBebida40,
                    R.id.btnBebida41,
                    R.id.btnBebida42,
                    R.id.btnBebida43,
                    R.id.btnBebida44,
                    R.id.btnBebida45,
                    R.id.btnBebida46,
                    R.id.btnBebida47,
                    R.id.btnBebida48,
                    R.id.btnBebida49,
                    R.id.btnBebida50,
                    R.id.btnBebida51,
                    R.id.btnBebida52,
                    R.id.btnBebida53,
                    R.id.btnBebida54,
            };

    //activar botones de la categoria cervezas
    private List<Button> btnListaCervezas;
    private static final int[] BUTTON_IDS3 =
            {
                    R.id.btnCerveza1,
                    R.id.btnCerveza2,
                    R.id.btnCerveza3,
                    R.id.btnCerveza4,
                    R.id.btnCerveza5,
                    R.id.btnCerveza6,
                    R.id.btnCerveza7,
                    R.id.btnCerveza8,
                    R.id.btnCerveza9,
                    R.id.btnCerveza10,
                    R.id.btnCerveza11,
                    R.id.btnCerveza12,
                    R.id.btnCerveza13,
                    R.id.btnCerveza14,
                    R.id.btnCerveza15,
                    R.id.btnCerveza16,
                    R.id.btnCerveza17,
                    R.id.btnCerveza18,
                    R.id.btnCerveza19,
                    R.id.btnCerveza20,
                    R.id.btnCerveza21,
                    R.id.btnCerveza22,
                    R.id.btnCerveza23,
                    R.id.btnCerveza24,
                    R.id.btnCerveza25,
                    R.id.btnCerveza26,
                    R.id.btnCerveza27,
                    R.id.btnCerveza28,
                    R.id.btnCerveza29,
                    R.id.btnCerveza30,
                    R.id.btnCerveza31,
                    R.id.btnCerveza32,
                    R.id.btnCerveza33,
                    R.id.btnCerveza34,
                    R.id.btnCerveza35,
                    R.id.btnCerveza36,
                    R.id.btnCerveza37,
                    R.id.btnCerveza38,
                    R.id.btnCerveza39,
                    R.id.btnCerveza40,
                    R.id.btnCerveza41,
                    R.id.btnCerveza42,
                    R.id.btnCerveza43,
                    R.id.btnCerveza44,
                    R.id.btnCerveza45,
                    R.id.btnCerveza46,
                    R.id.btnCerveza47,
                    R.id.btnCerveza48,
                    R.id.btnCerveza49,
                    R.id.btnCerveza50,
                    R.id.btnCerveza51,
                    R.id.btnCerveza52,
                    R.id.btnCerveza53,
                    R.id.btnCerveza54,
            };

    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad2);

        new Thread(new ClientThread()).start();

        //para que acepte la conexion con http post
        StrictMode.ThreadPolicy p = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(p);


        //recuperamos la informacion mandada por la activity anterios
        Bundle bundle = getIntent().getExtras();
        EditText et = (EditText) findViewById(R.id.editText2);
        et.setText(bundle.getString("mesa"));
        noMesa = (bundle.getInt("no"));


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


        //configuramos la lista del pedido
        listaItems = (ListView) findViewById(R.id.ListView1);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(Actividad2.this, android.R.layout.simple_list_item_1, pedido);
        listaItems.setAdapter(adapter);
        listaItems.setOnItemClickListener(new ListClickHandler());


        //iniciamos las listas de los botones declaradas arriba
        btnListaAlimentos = new ArrayList<Button>();
        btnListaBebidas = new ArrayList<Button>();
        btnListaCervezas = new ArrayList<Button>();


        //activamos los botones mediante la lista anterios
        int h = 0;
        for (int id : BUTTON_IDS) {
            btnAlimentos[h] = (Button) findViewById(id);
            btnAlimentos[h].setOnClickListener(this);
            btnListaAlimentos.add(btnAlimentos[h]);
            h++;
        }

        h = 0;
        for (int id : BUTTON_IDS2) {
            btnBebidas[h] = (Button) findViewById(id);
            btnBebidas[h].setOnClickListener(this);
            btnListaBebidas.add(btnBebidas[h]);
            h++;
        }

        h = 0;
        for (int id : BUTTON_IDS3) {
            btnCervezas[h] = (Button) findViewById(id);
            btnCervezas[h].setOnClickListener(this);
            btnListaCervezas.add(btnCervezas[h]);
            h++;
        }

        agregar = (ToggleButton) findViewById(R.id.toggleButton);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        silla1 = (Button)findViewById(R.id.silla1);
        silla2 = (Button)findViewById(R.id.silla2);
        silla3 = (Button)findViewById(R.id.silla3);
        silla4 = (Button)findViewById(R.id.silla4);
        silla5 = (Button)findViewById(R.id.silla5);
        silla6 = (Button)findViewById(R.id.silla6);
        silla7 = (Button)findViewById(R.id.silla7);
        silla8 = (Button)findViewById(R.id.silla8);
        silla9 = (Button)findViewById(R.id.silla9);
        silla10 = (Button)findViewById(R.id.silla10);
        silla11 = (Button)findViewById(R.id.silla11);
        silla12 = (Button)findViewById(R.id.silla12);
        silla13 = (Button)findViewById(R.id.silla13);
        silla14 = (Button)findViewById(R.id.silla14);
        silla15 = (Button)findViewById(R.id.silla15);
        silla1.setOnClickListener(this);
        silla2.setOnClickListener(this);
        silla3.setOnClickListener(this);
        silla4.setOnClickListener(this);
        silla5.setOnClickListener(this);
        silla6.setOnClickListener(this);
        silla7.setOnClickListener(this);
        silla8.setOnClickListener(this);
        silla9.setOnClickListener(this);
        silla10.setOnClickListener(this);
        silla11.setOnClickListener(this);
        silla12.setOnClickListener(this);
        silla13.setOnClickListener(this);
        silla14.setOnClickListener(this);
        silla15.setOnClickListener(this);

        btnGuardar.setOnClickListener(this);

        //programamos el boton de agregar o quitar y le asignamos el valor a la variable estado
        agregar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.i("info", "agregar is on!");
                    estado = -1;
                } else {
                    Log.i("info", "agregar is off!");
                    estado = 1;
                }
            }
        });

        //enviamos y recuperamos un arreglo json por php


        Thread tr = new Thread() {
            @Override
            public void run() {
                final String resultado = enviarHTTP.leer();
                runOnUiThread(
                        new Runnable() {

                            @Override
                            public void run() {
                                merca[0].obtDatosJSON(resultado);
                            }
                        });
            }
        };
        tr.start();

        for(int i=0;i<162;i++)
            for(int k=0;k<15;k++)
                productos[i][k]=0;


        for(int i=0;i<54;i++)
            arregloCarne[i] = "Carne1";


        Thread tr2 = new Thread() {
            @Override
            public void run() {
                final String resultado2 = enviarHTTP.leer2(noMesa);
                runOnUiThread(
                        new Runnable() {

                            @Override
                            public void run() {
                                merca[0].obtDatosJSON_2(resultado2);
                            }
                        });
            }
        };
        tr2.start();
    }

    //cargamos las imagenes
    void downloadFile() {
        URL imageUrl = null;
        try {
            for (int i = 0; i < 54; i++) {
                if (rutaImagen[i].compareTo("") != 0) {

                    imageUrl = new URL(servidor + "/android/" + rutaImagen[i].toString());
                    HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                    conn.connect();
                    loadedImage = BitmapFactory.decodeStream(conn.getInputStream());
                    //como la imagen devuelve un mapa de bits la tenemos que pasar a los recursos
                    Drawable d = new BitmapDrawable(getResources(), loadedImage);
                    //se la asignamos al boton correspondiente
                    btnAlimentos[i].setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
                } else {
                    i = 54;
                }
            }

            for (int i = 0; i < 54; i++) {
                if (rutaImagen[i + 54].compareTo("") != 0) {
                    imageUrl = new URL(servidor + "/android/" + rutaImagen[i + 54]);
                    HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                    conn.connect();
                    loadedImage = BitmapFactory.decodeStream(conn.getInputStream());
                    //como la imagen devuelve un mapa de bits la tenemos que pasar a los recursos
                    Drawable d = new BitmapDrawable(getResources(), loadedImage);
                    //se la asignamos al boton correspondiente
                    btnBebidas[i].setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
                } else {
                    i = 54;
                }
            }
            for (int i = 0; i < 54; i++) {
                if (rutaImagen[i + 108].compareTo("") != 0) {
                    imageUrl = new URL(servidor + "/android/" + rutaImagen[i + 108]);
                    HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                    conn.connect();
                    loadedImage = BitmapFactory.decodeStream(conn.getInputStream());
                    //como la imagen devuelve un mapa de bits la tenemos que pasar a los recursos
                    Drawable d = new BitmapDrawable(getResources(), loadedImage);
                    //se la asignamos al boton correspondiente
                    btnCervezas[i].setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
                } else {
                    i = 54;
                }
            }
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Error cargando la imagen: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }


    public class ListClickHandler implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
            // TODO Auto-generated method stub


            //TextView listText = (TextView) view.findViewById(R.id.listText);
            //String text = listText.getText().toString();
            //Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            //intent.putExtra("selected-item", text);
            //startActivity(intent);


            final String elemento =  adapter.getItemAtPosition(position).toString().substring(3);


            if(verificarTipo(elemento))
            {

                inflater = getLayoutInflater();
                final View dialoglayout = inflater.inflate(R.layout.tipo_carne, null);
                final RadioButton carne1 = (RadioButton) dialoglayout.findViewById(R.id.radioButton);
                final RadioButton carne2 = (RadioButton) dialoglayout.findViewById(R.id.radioButton2);
                final RadioButton carne3 = (RadioButton) dialoglayout.findViewById(R.id.radioButton3);
                final Dialog dialog = new Dialog(Actividad2.this);

                carne1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (carne1.isChecked())
                        {
                            guardarTipo(elemento, "carne1");
                            dialog.dismiss ();
                        }
                    }
                });
                carne2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (carne2.isChecked())
                        {
                            guardarTipo(elemento, "carne2");
                            dialog.dismiss ();
                        }
                    }
                });
                carne3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (carne3.isChecked())
                        {
                            guardarTipo(elemento, "carne3");
                            dialog.dismiss ();
                        }
                    }
                });

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(dialoglayout);
                dialog.getWindow().setFormat(PixelFormat.TRANSLUCENT);
                final Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                dialog.setCancelable(false);
                dialog.show();
            }


        }

    }

    //programamos el boton de atras
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Alerta");
            builder.setMessage("Esta apunto de salir de la mesa y se perderan los cambios no enviados ¿desea continuar?");

            builder.setPositiveButton("SI",

                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //si salen de la mesa borra todos los elementos de la lista
                            pedido.clear();
                            //manda llamar al metodo elimina que envia parametros get a un archivo
                            //php en el servidor
                            enviarHTTP.enviarGet_eliminar(noMesa);
                            //por ultimo cierra la actividad y regresa al menu principal
                            finish();
                        }
                    });

            builder.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            AlertDialog alert = builder.create();
            alert.setCancelable(false);
            alert.setIcon(R.drawable.ic_launcher);
            alert.show();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }



    //configuramos todos los clicks de los botones en la app
    public void onClick(View arg0) {
        //volvemos a declarar el listview para poderlo manejar dentro de la funcion
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (Actividad2.this, android.R.layout.simple_list_item_1, pedido);

        listaItems.setAdapter(adapter);
        String espacio = "-";

        //si es la primera vez que entra al evento manda cargar el pedido
        if (bandera) {
            merca[0].actualizar();
            bandera = false;
        }

        switch (arg0.getId()) {
            case R.id.silla1:
                noSilla = 1;
                comodin = true;
                //silla1.setBackgroundColor(getResources().getColor(R.color.actual));
                //silla1.setBackgroundColor(getResources().getColor(R.color.original));
                break;
            case R.id.silla2:
                if(noSilla != 2)
                {
                    noSilla = 2;
                    comodin = true;
                }
                break;
            case R.id.silla3:
                if(noSilla != 3)
                {
                    noSilla = 3;
                    comodin = true;
                }
                break;
            case R.id.silla4:
                if(noSilla != 4)
                {
                    noSilla = 4;
                    comodin = true;
                }
                break;
            case R.id.silla5:
                if(noSilla != 5)
                {
                    noSilla = 5;
                    comodin = true;
                }
                break;
            case R.id.silla6:
                if(noSilla != 6)
                {
                    noSilla = 6;
                    comodin = true;
                }
                break;
            case R.id.silla7:
                if(noSilla != 7)
                {
                    noSilla = 7;
                    comodin = true;
                }
                break;
            case R.id.silla8:
                if(noSilla != 8)
                {
                    noSilla = 8;
                    comodin = true;
                }
                break;
            case R.id.silla9:
                if(noSilla != 9)
                {
                    noSilla = 9;
                    comodin = true;
                }
                break;
            case R.id.silla10:
                if(noSilla != 10)
                {
                    noSilla = 10;
                    comodin = true;
                }
                break;
            case R.id.silla11:
                if(noSilla != 11)
                {
                    noSilla = 11;
                    comodin = true;
                }
                break;
            case R.id.silla12:
                if(noSilla != 12)
                {
                    noSilla = 12;
                    comodin = true;
                }
                break;
            case R.id.silla13:
                if(noSilla != 13)
                {
                    noSilla = 13;
                    comodin = true;
                }
                break;
            case R.id.silla14:
                if(noSilla != 14)
                {
                    noSilla = 14;
                    comodin = true;
                }
                break;
            case R.id.silla15:
                if(noSilla != 15)
                {
                    noSilla = 15;
                    comodin = true;
                }
                break;

            //si se presiona este boton
            case R.id.btnGuardar:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Alerta");
                builder.setMessage("Esta apunto de enviar el pedido ¿Esta Seguro?");

                builder.setPositiveButton("SI",

                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    int estado = 1;
                                    //borra toda la informacion de esa mesa
                                    enviarHTTP.enviarGet(noMesa, 0, "borrar", estado, 0, "");
                                    estado = 0;
                                    int comodin = 100;
                                    for (int i = 0; i < 154; i++)
                                    {
                                        for (int k = 0; k < 15; k++)
                                        {
                                            if (productos[i][k] > 0)
                                            {
                                                //envia por php con el metodo get el pedido registro por registro
                                                if (comodin == 100)
                                                    enviarHTTP.enviarGet(noMesa, productos[i][k], enviar[i].toString(), comodin, k + 1, arregloCarne[i]);
                                                else
                                                    enviarHTTP.enviarGet(noMesa, productos[i][k], enviar[i].toString(), estado, k + 1, arregloCarne[i]);

                                                comodin = 1;
                                            }
                                        }
                                    }
                                    //limpia la lista
                                    pedido.clear();

                                    try {

                                        PrintWriter out = new PrintWriter(new BufferedWriter(
                                                new OutputStreamWriter(socket.getOutputStream())),
                                                true);
                                        out.println(noMesa);
                                    } catch (UnknownHostException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    //cierra la actividad
                                    finish();
                                } catch (Exception e) {

                                }
                            }
                        });
                //boton NO del mensaje confirmar pedido
                builder.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                AlertDialog alert = builder.create();
                alert.setCancelable(false);
                alert.setIcon(R.drawable.ic_launcher);
                alert.show();
                break;

        }

        if(comodin)
        {
            //si salen de la mesa borra todos los elementos de la lista
            pedido.clear();

            for (int i = 0; i < 200; i++)
                if(merca[i].getArregloEstado() == 1 && merca[i].getArregloSilla() == noSilla)
                    pedido.add("✔ " + String.format("%02d", merca[i].getArregloCantidad()) + espacio + merca[i].getArregloCargar().toString());


            for (int j = 0; j < 54; j++)
            {
                if (!btnAlimentos[j].equals("") && productos[j][noSilla - 1] > 0)
                        pedido.add(String.format("%02d", productos[j][noSilla - 1]) + espacio + btnAlimentos[j].getText().toString());
                if (!btnBebidas[j].equals("") && productos[j+54][noSilla - 1] > 0)
                    pedido.add(String.format("%02d", productos[j + 54][noSilla - 1]) + espacio + btnBebidas[j].getText().toString());
                if (!btnCervezas[j].equals("") && productos[j+108][noSilla - 1] > 0)
                    pedido.add(String.format("%02d", productos[j + 108][noSilla - 1]) + espacio + btnCervezas[j].getText().toString());
            }


            /*
            for (int i = 0; i < 200; i++) {
                for (int j = 0; j < 54; j++) {
                    String alimentos = btnAlimentos[j].getText().toString();
                    if (alimentos.equals(arregloCargar[i]))
                    {
                        if (arregloSilla[i] == noSilla) {
                            //se añade el nuevo registro  a la lista ya actualizado
                            pedido.add(String.format("%02d", productos[j][noSilla - 1]) + espacio + btnAlimentos[j].getText().toString());
                        }
                    }
                }

                for (int j = 54; j < 108; j++) {
                    String bebidas = btnBebidas[j-54].getText().toString();
                    if (bebidas.equals(arregloCargar[i])) {
                        if (arregloSilla[i] == noSilla) {
                            //se añade el nuevo registro  a la lista ya actualizado
                            pedido.add(String.format("%02d", productos[j][noSilla - 1]) + espacio + bebidas);
                        }
                    }
                }

                for (int j = 108; j < 161; j++) {
                    String cervezas = btnCervezas[j-108].getText().toString();
                    if (cervezas.equals(arregloCargar[i])) {

                        if (arregloSilla[i] == noSilla) {
                            //se añade el nuevo registro  a la lista ya actualizado
                            pedido.add(String.format("%02d", productos[j][noSilla - 1]) + espacio + cervezas);
                        }
                    }
                }
            }*/

            comodin = false;

            //actualia los elementos de la lista
            adapter.notifyDataSetChanged();

        }

        //for para revisar todos los botones de alimentos
        for (int i = 1; i < 54; i++) {

            String nombre = "btnAlimento" + i;
            Resources res = getResources();
            int boton = res.getIdentifier(nombre, "id", getPackageName());
            //si el boton que se presiono es igual al del ciclo entra
            if (arg0.getId() == boton) {
                //actualiza el producto eliminando el actual de la lista
                pedido.remove(String.format("%02d", productos[i - 1][noSilla-1]) + espacio + btnAlimentos[i - 1].getText().toString());
                //le aumenta o disminuye uno depende del estado del boton toggle
                productos[i - 1][noSilla-1] = productos[i - 1][noSilla-1] + estado;
                //se le asigna el nuevo valor
                enviar[i - 1] = btnAlimentos[i - 1].getText().toString();
                //se le asigna el numero de silla
                merca[i-1].setArregloSilla(noSilla);
                //se añade el nuevo registro  a la lista ya actualizado
                pedido.add(String.format("%02d", productos[i - 1][noSilla-1]) + espacio + btnAlimentos[i - 1].getText().toString());
                //en caso de ser negativo la cantidad elimina el producto de la lista
                if (productos[i - 1][noSilla-1] <= 0) {
                    pedido.remove(String.format("%02d", productos[i - 1][noSilla-1]) + espacio + btnAlimentos[i - 1].getText().toString());
                    productos[i - 1][noSilla-1] = 0;
                }
            }
        }
        //for para revisar todos los botones de bebidas
        for (int i = 1; i < 54; i++) {

            String nombre = "btnBebida" + i;
            Resources res = getResources();
            int boton = res.getIdentifier(nombre, "id", getPackageName());
            //si el boton que se presiono es igual al del ciclo entra
            if (arg0.getId() == boton) {
                //actualiza el producto eliminando el actual de la lista
                pedido.remove(String.format("%02d", productos[i + 53][noSilla-1]) + espacio + btnBebidas[i - 1].getText());
                //le aumenta o disminuye uno depende del estado del boton toggle
                productos[i + 53][noSilla-1] = productos[i + 53][noSilla-1] + estado;
                //se le asigna el nuevo valor
                enviar[i + 53] = btnBebidas[i - 1].getText().toString();
                //se le asigna el numero de silla
                merca[i+53].setArregloSilla(noSilla);
                //se añade el nuevo registro  a la lista ya actualizado
                pedido.add(String.format("%02d", productos[i + 53][noSilla-1]) + espacio + btnBebidas[i - 1].getText());
                //en caso de ser negativo la cantidad elimina el producto de la lista
                if (productos[i + 53][noSilla-1] <= 0) {
                    pedido.remove(String.format("%02d", productos[i + 53][noSilla-1]) + espacio + btnBebidas[i - 1].getText());
                    productos[i + 53][noSilla-1] = 0;
                }
            }
        }
        //for para revisar todos los botones de cervezas
        for (int i = 1; i < 54; i++) {

            String nombre = "btnCerveza" + i;
            Resources res = getResources();
            int boton = res.getIdentifier(nombre, "id", getPackageName());
            //si el boton que se presiono es igual al del ciclo entra
            if (arg0.getId() == boton) {
                //actualiza el producto eliminando el actual de la lista
                pedido.remove(String.format("%02d", productos[i + 107][noSilla-1]) + espacio + btnCervezas[i - 1].getText());
                //le aumenta o disminuye uno depende del estado del boton toggle
                productos[i +107][noSilla-1] = productos[i + 107][noSilla-1] + estado;
                //se le asigna el nuevo valor
                enviar[i + 107] = btnCervezas[i - 1].getText().toString();
                //se le asigna el numero de silla
                merca[i+107].setArregloSilla(noSilla);
                //se añade el nuevo registro  a la lista ya actualizado
                pedido.add(String.format("%02d", productos[i + 107][noSilla-1]) + espacio + btnCervezas[i - 1].getText());
                //en caso de ser negativo la cantidad elimina el producto de la lista
                if (productos[i + 107][noSilla-1] <= 0) {
                    pedido.remove(String.format("%02d", productos[i + 107][noSilla-1]) + espacio + btnCervezas[i - 1].getText());
                    productos[i + 107][noSilla-1] = 0;
                }

            }
        }
        //actualiza la lista
        adapter.notifyDataSetChanged();
    }



    public void guardarTipo(String elemento,String tipo)
    {
        for (int i = 0; i < 200; i++)
        {
            for (int j = 0; j < 54; j++)
            {
                String alimentos = btnAlimentos[j].getText().toString();
                if (alimentos.equals(enviar[i]) && merca[i].getArregloSilla() == noSilla )
                {
                    if(elemento.equals(alimentos))
                    {
                        arregloCarne[j] = tipo;
                        j = 54;
                        i=200;
                    }
                } //else if (alimentos == "Torta ahogada")
                //    j = 54;
            }
        }

    }

    public boolean verificarTipo(String elemento)
    {
        for (int i = 0; i < 162; i++)
        {
            for (int j = 0; j < 54; j++)
            {
                if(merca[i].getArregloSilla() == noSilla)
                {
                    String alimentos = btnAlimentos[j].getText().toString();
                    if (alimentos.equals(enviar[i]))
                    {
                        if (elemento.equals(alimentos))
                        {
                            return true;
                        }
                    }
                }
                else
                {
                    j = 54;
                }
                //else if (alimentos == "Torta ahogada")
                  //  j = 54;
            }
        }
        return false;
    }

    class ClientThread implements Runnable {

        @Override

        public void run() {
            try {

                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

                socket = new Socket(serverAddr, SERVERPORT);

            } catch (UnknownHostException e1) {

                e1.printStackTrace();

            } catch (IOException e1) {

                e1.printStackTrace();

            }
        }
    }
}
