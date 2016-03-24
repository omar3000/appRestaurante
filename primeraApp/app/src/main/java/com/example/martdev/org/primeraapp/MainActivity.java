package com.example.martdev.org.primeraapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.os.StrictMode;



public class MainActivity extends Activity {

    //declaro los botones imagen para cada mesa del menu
    ImageButton imgButton,imgButton2,imgButton3,imgButton4,imgButton5,imgButton6,imgButton7,imgButton8;
    ImageButton imgButton9,imgButton10,imgButton11,imgButton12,imgButton13,imgButton14,imgButton15,imgButton16;
    //direccion ip del servidor y puerto actual
    String servidor = "http://192.168.1.96:80";
    //saber si la mesa esta activa en algun otro dispositivo
    String comparar = "abierto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //hacer funcionar el http handler
        StrictMode.ThreadPolicy p = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(p);

        //evento para los cliks de las imagenes boton
        addButtonListener();
    }

    public void addButtonListener()
    {
        //inicializamos cada imagen boton
        imgButton = (ImageButton) findViewById(R.id.imageButton1);
        imgButton2 = (ImageButton) findViewById(R.id.imageButton2);
        imgButton3 = (ImageButton) findViewById(R.id.imageButton3);
        imgButton4 = (ImageButton) findViewById(R.id.imageButton4);
        imgButton5 = (ImageButton) findViewById(R.id.imageButton5);
        imgButton6 = (ImageButton) findViewById(R.id.imageButton6);
        imgButton7 = (ImageButton) findViewById(R.id.imageButton7);
        imgButton8 = (ImageButton) findViewById(R.id.imageButton8);
        imgButton9 = (ImageButton) findViewById(R.id.imageButton9);
        imgButton10 = (ImageButton) findViewById(R.id.imageButton10);
        imgButton11 = (ImageButton) findViewById(R.id.imageButton11);
        imgButton12 = (ImageButton) findViewById(R.id.imageButton12);
        imgButton13 = (ImageButton) findViewById(R.id.imageButton13);
        imgButton14 = (ImageButton) findViewById(R.id.imageButton14);
        imgButton15 = (ImageButton) findViewById(R.id.imageButton15);
        imgButton16 = (ImageButton) findViewById(R.id.imageButton16);

        //creamos un intent para abrir la nueva activiad
        final Intent int1 = new  Intent("com.example.martdev.org.Actividad2");

        //evento cuando se le de click a alguna imagen
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                //se le pasa a la siguiente activity el nombre de la actividad
                int1.putExtra("mesa","Mesa 1");
                //se le pasa a la siguiente activity el numero de mesa
                int1.putExtra("no",1);
                //se manda llamar un metodo de la clase httphandler
                HttpHandler handler = new HttpHandler();
                // envio por metodo post a php y su retorno lo guardo en un string
                String txt = handler.post(servidor + "/android/verificar.php?noMesa=1");
                //como el retorno tiene errorer por que es utf o ascci tenemos que eliminar los
                //primeros tres caracteres
                String auxiliar = txt.substring(3);
                //String auxiliar = txt;
                //comparamos si es igual el retorno y el contenido de la variable abierto si
                //son iguales abrimos la mesa porque quiere decir que esta disponible
                if(auxiliar.equals(comparar))
                    startActivity(int1);
                //de lo contrario mandamos llamar a la funcion mensaje que es un mensaje en pantalla
                else
                {
                    mensaje();
                }

                //CON TODOS LOS DEMAS BOTONES IMAGENES ES EL MISMO PROCESO POR LO TANTO NO LO
                //COMENTARE
            }
        });
        imgButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int1.putExtra("mesa","Mesa 2");
                int1.putExtra("no",2);
                HttpHandler handler = new HttpHandler();
                String txt = handler.post(servidor + "/android/verificar.php?noMesa=2");
                String auxiliar = txt.substring(3);
                //String auxiliar = txt;
                if(auxiliar.equals(comparar))
                    startActivity(int1);
                else
                {
                    mensaje();
                }

            }
        });
        imgButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int1.putExtra("mesa","Mesa 3");
                int1.putExtra("no",3);
                HttpHandler handler = new HttpHandler();
                String txt = handler.post(servidor + "/android/verificar.php?noMesa=3");
                String auxiliar = txt.substring(3);
                //String auxiliar = txt;
                if(auxiliar.equals(comparar))
                    startActivity(int1);
                else
                {
                    mensaje();
                }
            }
        });
        imgButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int1.putExtra("mesa","Mesa 4");
                int1.putExtra("no",4);

                HttpHandler handler = new HttpHandler();
                String txt = handler.post(servidor + "/android/verificar.php?noMesa=4");
                String auxiliar = txt.substring(3);
                //String auxiliar = txt;
                if(auxiliar.equals(comparar))
                    startActivity(int1);
                else
                {
                    mensaje();
                }
            }
        });
        imgButton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int1.putExtra("mesa","Mesa 5");
                int1.putExtra("no",5);

                HttpHandler handler = new HttpHandler();
                String txt = handler.post(servidor + "/android/verificar.php?noMesa=5");
                String auxiliar = txt.substring(3);
                //String auxiliar = txt;
                if(auxiliar.equals(comparar))
                    startActivity(int1);
                else
                {
                    mensaje();
                }
            }
        });
        imgButton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                int1.putExtra("mesa","Mesa 6");
                int1.putExtra("no",6);

                HttpHandler handler = new HttpHandler();
                String txt = handler.post(servidor + "/android/verificar.php?noMesa=6");
                String auxiliar = txt.substring(3);
                //String auxiliar = txt;
                if(auxiliar.equals(comparar))
                    startActivity(int1);
                else
                {
                    mensaje();
                }
            }
        });
        imgButton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int1.putExtra("mesa","Mesa 7");
                int1.putExtra("no",7);

                HttpHandler handler = new HttpHandler();
                String txt = handler.post(servidor + "/android/verificar.php?noMesa=7");
                String auxiliar = txt.substring(3);
                //String auxiliar = txt;
                if(auxiliar.equals(comparar))
                    startActivity(int1);
                else
                {
                    mensaje();
                }
            }
        });
        imgButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int1.putExtra("mesa","Mesa 8");
                int1.putExtra("no",8);

                HttpHandler handler = new HttpHandler();
                String txt = handler.post(servidor + "/android/verificar.php?noMesa=8");
                String auxiliar = txt.substring(3);
                //String auxiliar = txt;
                if(auxiliar.equals(comparar))
                    startActivity(int1);
                else
                {
                    mensaje();
                }

            }
        });
        imgButton9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int1.putExtra("mesa","Mesa 9");
                int1.putExtra("no",9);

                HttpHandler handler = new HttpHandler();
                String txt = handler.post(servidor + "/android/verificar.php?noMesa=9");
                String auxiliar = txt.substring(3);
                //String auxiliar = txt;
                if(auxiliar.equals(comparar))
                    startActivity(int1);
                else
                {
                    mensaje();
                }
            }
        });
        imgButton10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int1.putExtra("mesa","Mesa 10");
                int1.putExtra("no",10);

                HttpHandler handler = new HttpHandler();
                String txt = handler.post(servidor + "/android/verificar.php?noMesa=10");
                String auxiliar = txt.substring(3);
                //String auxiliar = txt;
                if(auxiliar.equals(comparar))
                    startActivity(int1);
                else
                {
                    mensaje();
                }
            }
        });
        imgButton11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int1.putExtra("mesa","Mesa 11");
                int1.putExtra("no",11);

                HttpHandler handler = new HttpHandler();
                String txt = handler.post(servidor + "/android/verificar.php?noMesa=11");
                String auxiliar = txt.substring(3);
                //String auxiliar = txt;
                if(auxiliar.equals(comparar))
                    startActivity(int1);
                else
                {
                    mensaje();
                }
            }
        });
        imgButton12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int1.putExtra("mesa","Mesa 12");
                int1.putExtra("no",12);

                HttpHandler handler = new HttpHandler();
                String txt = handler.post(servidor + "/android/verificar.php?noMesa=12");
                String auxiliar = txt.substring(3);
                //String auxiliar = txt;
                if(auxiliar.equals(comparar))
                    startActivity(int1);
                else
                {
                    mensaje();
                }
            }
        });
        imgButton13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int1.putExtra("mesa","Mesa 13");
                int1.putExtra("no",13);

                HttpHandler handler = new HttpHandler();
                String txt = handler.post(servidor + "/android/verificar.php?noMesa=13");
                String auxiliar = txt.substring(3);
                //String auxiliar = txt;
                if(auxiliar.equals(comparar))
                    startActivity(int1);
                else
                {
                    mensaje();
                }
            }
        });
        imgButton14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int1.putExtra("mesa","Mesa 14");
                int1.putExtra("no",14);

                HttpHandler handler = new HttpHandler();
                String txt = handler.post(servidor + "/android/verificar.php?noMesa=14");
                String auxiliar = txt.substring(3);
                //String auxiliar = txt;
                if(auxiliar.equals(comparar))
                    startActivity(int1);
                else
                {
                    mensaje();
                }
            }
        });
        imgButton15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int1.putExtra("mesa","Mesa 15");
                int1.putExtra("no",15);

                HttpHandler handler = new HttpHandler();
                String txt = handler.post(servidor + "/android/verificar.php?noMesa=15");
                String auxiliar = txt.substring(3);
                //String auxiliar = txt;
                if(auxiliar.equals(comparar))
                    startActivity(int1);
                else
                {
                    mensaje();
                }
            }
        });
        imgButton16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int1.putExtra("mesa","Mesa 16");
                int1.putExtra("no",16);

                HttpHandler handler = new HttpHandler();
                String txt = handler.post(servidor + "/android/verificar.php?noMesa=16");
                String auxiliar = txt.substring(3);
                //String auxiliar = txt;
                if(auxiliar.equals(comparar))
                    startActivity(int1);
                else
                {
                    mensaje();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
        builder.setMessage("Esta mesa no esta disponile, es probable que este abierta en otro dispositivo, verifique por favor");

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
