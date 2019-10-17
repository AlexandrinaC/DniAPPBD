package com.example.dniapp.actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.dniapp.R;
import com.example.dniapp.beans.Dni;
import com.example.dniapp.beans.DniX;
import com.example.dniapp.beans.DniY;
import com.example.dniapp.beans.DniZ;
import com.example.dniapp.dao.BaseDatosDni;
import com.example.dniapp.util.Preferencias;
import com.google.gson.Gson;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG_APP = "DNI_APP";
    private RadioButton radioButtonSeleccionado;
    private EditText caja_dni;

    private BaseDatosDni baseDatosDni;

    private void mostrarListaDnis (List<Dni> list)
    {
        Log.d(TAG_APP, "Mostrando lista de Dnis");
        for (Dni dni : list)
        {
            Log.d(TAG_APP, dni.getNumero() + " " + dni.getLetra());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.caja_dni = findViewById(R.id.dni);
        int id_radio = Preferencias.obtenerRadioActivo(this);
        //Creo mi base de datos para mis Dnis.
        baseDatosDni = new BaseDatosDni(this, "MiDB", null, 1);
        if (id_radio==0)//no había guardado
        {
            this.radioButtonSeleccionado = findViewById(R.id.radio1);
        }
        else{
            this.radioButtonSeleccionado = findViewById(id_radio);
            this.radioButtonSeleccionado.setChecked(true);

        }

        /*String ultimo_dni = Preferencias.obtenerUltimoDNI(this);
        this.caja_dni.setText(ultimo_dni);

        List<Dni> dniList = Preferencias.cargarFicheroDni(this);*/

        //Creo mi Dni List.
        List<Dni> dniList = baseDatosDni.buscarDnis();
        mostrarListaDnis(dniList);

        //Ahora leo de mi base de datos, no de preferencias.


    }


    private Dni crearObjetoDni (int num_dni)
    {
        Dni dni = null;

        switch (this.radioButtonSeleccionado.getId())
        {
            case R.id.radio1: dni = new Dni(num_dni);//nacional
                break;
            case R.id.radio2: dni = new DniX(num_dni);//X
                break;
            case R.id.radio3: dni = new DniY(num_dni);//Y
                break;
            case R.id.radio4: dni = new DniZ(num_dni);//Z
                break;
        }

        return dni;
    }

    public void calcularLetraDni(View view) {
        Log.d(TAG_APP, "Ha tocado el botón de calcular");
        //TODO
        //1 obtener el número de dni introducido por el usario
        EditText caja_dni = findViewById(R.id.dni);
        String sdni = caja_dni.getText().toString();
        //2 Casting de String a Int //NO ESTOY VALIDANDO LA ENTRADA
        int num_dni = Integer.parseInt(sdni);
        //2.1 Creo el objeto DNI nuevo
        Dni dni = crearObjetoDni(num_dni);
        //calcular letra
        char letra_dni = dni.calculaLetra();
        Log.d(TAG_APP, "Letra calculada = "+letra_dni);
        //4 Lanzo la actividad del resultado pasándole la letra
        Intent intent = new Intent(this, AnimacionLetraActivity.class);
        intent.putExtra("LETRA", letra_dni);
        startActivity(intent);
        caja_dni.setText("");

        //guadarmos el objeto DNI
        dni.setLetra(letra_dni);
        /*Gson gson = new Gson();
        String dni_json = gson.toJson(dni);
        Log.d(TAG_APP, "DNI json " + dni_json);
        Preferencias.guardarDNIJson(this, dni_json);*/

        //Insertamos nuestros Dnis en la base de datos que hemos creado
        baseDatosDni.insertarDni(dni);

    }


    private String obtenerDni ()
    {
        String dni_actual = null;

            EditText editText = findViewById(R.id.dni);
            dni_actual = editText.getText().toString();

        return dni_actual;
    }

    //este método se invoca cuando el usuario pulsa el  botón de hacia atrás
    @Override
    public void onBackPressed() {
        Log.d(TAG_APP, "El usuario le ha dado para atrás");
        mostrarDialogoGuardar();
    }


    public void tocadoRadio(View view) {
        Log.d(TAG_APP, "Tocó RadioButton");
        this.radioButtonSeleccionado = (RadioButton)view;
    }


    private void mostrarDialogoGuardar ()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Desea guardar los cambios?");

        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d(TAG_APP, "Tocó SÍ");
                dialog.dismiss();
                String dni_guardar = MainActivity.this.obtenerDni();
                Preferencias.guardarDNI(MainActivity.this, dni_guardar);
                Preferencias.guardarRadioActivo(MainActivity.this, MainActivity.this.radioButtonSeleccionado.getId());
                MainActivity.this.finish();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                Log.d(TAG_APP, "Tocó NO");
                dialog.cancel();
                Preferencias.guardarDNI(MainActivity.this, "");
                Preferencias.guardarRadioActivo(MainActivity.this, 0);
                MainActivity.this.finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.ver_lista_dnis:
                Log.d(TAG_APP, "Quiere ver la lista de dnis");
                Intent intent = new Intent(this, ListaDnisActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
