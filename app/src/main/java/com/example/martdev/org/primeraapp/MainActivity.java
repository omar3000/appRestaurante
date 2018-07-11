package com.example.martdev.org.primeraapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.os.StrictMode;
import java.util.ArrayList;

public class MainActivity extends Activity  implements View.OnClickListener {

    //declaro los botones imagen para cada mesa del menu

    ImageButton btnMesas[] = new ImageButton[16];
    //direccion ip del servidor y puerto actual
    static String SERVIDOR = "http://localhost";

    static String GUARDAR_PEDIDO = "/modrest/guardarPedido.php";
    static String RECUPERAR_PEDIDO = "/modrest/recuperarPedido.php";
    static String TRAER_PRODUCTOS = "/modrest/consulta.php";
    static String VERIFICAR = "/modrest/verificar.php";
    static String ELIMINAR = "/modrest/eliminar.php";
    //saber si la mesa esta activa en algun otro dispositivo
    String comparar = "abierto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hacer funcionar el http handler
        StrictMode.ThreadPolicy p = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(p);

        inicializarListaBotones();

    }

    private void inicializarListaBotones()
    {
        //iniciamos las listas de los botones declaradas arriba
        listasBotones.btnListaMesas = new ArrayList<ImageButton>();

        //activamos los botones mediante la lista anterior
        int h = 0;
        for (int id :listasBotones. BUTTON_IDMESAS) {
            btnMesas[h] = (ImageButton) findViewById(id);
            btnMesas[h].setOnClickListener(this);
            listasBotones.btnListaMesas.add(btnMesas[h]);
            h++;
        }
    }

    //configuramos todos los clicks de los botones en la app
    public void onClick(View arg0)
    {
        String nombre;
        Resources res;
        String txt;
        String auxiliar;
        String mesa;
        //creamos un intent para abrir la nueva activiad
        final Intent int1 = new  Intent("com.example.martdev.org.Actividad2");
        int boton;

        //for para revisar todos los botones de alimentos
        for (short i = 1; i < 17; i++)
        {
            nombre = "btnMesa" + i;
            res = getResources();
            boton = res.getIdentifier(nombre, "id", getPackageName());
            //si el boton que se presiono es igual al del ciclo entra
            if (arg0.getId() == boton)
            {
                //se le pasa a la siguiente activity el nombre de la actividad
                mesa = "Mesa " + i;
                int1.putExtra("mesa",mesa);
                //se le pasa a la siguiente activity el numero de mesa
                int1.putExtra("no", i);

                // envio por metodo post a php y su retorno lo guardo en un string
                txt = (String)(httpHandler.doInBackground_2(String.valueOf(i), VERIFICAR));
                txt = txt.replace("\uFEFF", "");
                Log.d("verificar", txt + " " + comparar);
                //comparamos si es igual el retorno y el contenido de la variable abierto si
                //son iguales abrimos la mesa porque quiere decir que esta disponible
                if(txt.equals(comparar))
                    startActivity(int1);
                //de lo contrario mandamos llamar a la funcion mensaje que es un mensaje en pantalla
                else
                    mensaje();
            }

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //Metodo del mensaje de error
    void mensaje()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Error");
        builder.setMessage("Esta mesa no esta disponile, es probable que este abierta en otro " +
                "dispositivo, verifique por favor");

        builder.setPositiveButton("Aceptar",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                });

        AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.setIcon(R.drawable.ic_launcher);
        alert.show();
    }
}
