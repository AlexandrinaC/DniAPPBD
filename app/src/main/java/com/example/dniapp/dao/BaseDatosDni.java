package com.example.dniapp.dao;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.dniapp.actividades.MainActivity;
import com.example.dniapp.beans.Dni;

import java.util.ArrayList;
import java.util.List;

//extends SGLOpenHelper porque este es el que me va a ayudar a crear mi base de datos para los Dnis.
public class BaseDatosDni extends SQLiteOpenHelper {

    //Mejor declaro una constante con el nombre de mi base de datos, para que en la posterioridad, NO ME EQUIVOQUE Y PETE TODO. :)

    public final static String NOMBRE_BD = "MiDB";

    //Creo la forma que va a tener mi lista de Dnis.
    private final String SQL_CREACION_TABLA_DNI = "CREATE TABLE DNI (id INTEGER PRIMARY KEY AUTOINCREMENT, numero INTEGER, letra TEXT)"; //El id siempre hay que ponerlo.

    public BaseDatosDni(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Ejecuta el crear mi tabla de DNI.
        db.execSQL(SQL_CREACION_TABLA_DNI);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Esto se utilizaría para cambiar, si vamos a editar la lista que ya tenemos.

    }

    private void cerrarBaseDatos (SQLiteDatabase database) {
        database.close();
    }

    public void insertarDni (Dni dni) {
        try{
            //Metodo para escribir en la base de datos
            SQLiteDatabase database = this.getWritableDatabase();
            database.execSQL("INSERT INTO DNI (numero, letra) VALUES (" + dni.getNumero() + " , '" + dni.getLetra() + "')");
            this.cerrarBaseDatos(database);
        }
        //Estas son las excepciones. En caso de que no funcione al insertar algo, o por si se inserta dos veces,etc.
        catch (NullPointerException n)
        {
            Log.e("MIAPP", "Null ha dejado de funcionar insertando un dni", n);
        }
        catch (Exception e)
        {
            Log.e("MIAPP", "Ha dejado de funcionar insertando un dni" + dni.toString(), e);
        }
    }

    //Para borrar de la base de datos.


    public void borrarDeBaseDatos (int numero){
        try{
            SQLiteDatabase database = this.getWritableDatabase();
            database.execSQL ("DELETE FROM DNI WHERE numero = " + numero );
        }catch (Exception e){
            Log.e("MIAPP", "Ha dejado de funcionar borrando" + numero);

        }
    }

   /*
    //Este metodo solo lo utilizo si voy a asociar este Dni a algo.
      public Dni buscarDni (Dni dni)
    {
        //Crear un dni vacío, para decirle qué forma va a tener para cuando busque.
        Dni dni_vacio = null;
        int aux_numero = -1;
        String letra_aux;

        //Crear mi consultor para cuando vayamos a acceder a la base de datos.
        String consulta = "SELECT numero, letra FROM DNI WHERE numero LIKE %"+dni+"%;";

        //Accedemos a la base de datos.
        SQLiteDatabase basedatos = this.getReadableDatabase();
        //Con el cursor nos dice dónde está mi dni. Para hacer la consulta
        //utilizo mi consultor.
        Cursor cursor = basedatos.rawQuery(consulta, null);

        if ( cursor != null && cursor.getCount() >0)
        {
            //El cursor se va a la posicion 1.
            cursor.moveToFirst();

            //Coge los datos que encuentra.
            aux_numero = cursor.getInt(0); //La posicion primera, que es donde esta mi numero.
            letra_aux = cursor.getString(1); //La posicion dos porque es donde esta mi letra.
            dni = new Dni(aux_numero,letra_aux.charAt(0));
        }


    }*/

    /**
     *
     * @return null si no existen dnis, una lista en caso contrario
     */

   public List<Dni> buscarDnis ()
   {
       //Digo la forma que va a tener mi lista de dnis.
       List<Dni> lista_dnis = null;
       Dni dni = null;
       int numero = -1;
       String letra = "";

       //Creo mi consulta.
       String consulta = "SELECT numero, letra FROM DNI";

       //Accedo a mi base de datos.
       SQLiteDatabase basedatos = this.getReadableDatabase();
       //Con mi cursor accedo a la base de datos.
       Log.d(MainActivity.TAG_APP, "Ejecutando sql "+ consulta);
       Cursor cursor = basedatos.rawQuery(consulta, null);


       if ((cursor != null) && (cursor.getCount()>0))
       {
           //Si tenemos por lo menos un dato en nuestra lista, nos vamos a la posicion 1.
           Log.d(MainActivity.TAG_APP, "La consulta recuperó "+ cursor.getCount() + " registros");
           cursor.moveToFirst();
           //Creo una lista con la cantidad de datos que tenga (haya contado) el cursor.
           lista_dnis = new ArrayList<Dni>(cursor.getCount());

           do {
               numero = cursor.getInt(0); //La posicio 1 porque el numero va primero.
               letra = cursor.getString(1); //LA posicion 2 porque va despues del numero.
               dni = new Dni(numero,letra.charAt(0));
               lista_dnis.add(dni);

           } while (cursor.moveToNext()); //Nos vamos a la próxima posición.
            cursor.close();//Si ya no hay más datos, se para el cursor.
       } else {
           Log.d(MainActivity.TAG_APP, "La consulta recuperó 0 registros");
       }
       this.cerrarBaseDatos(basedatos);
       return lista_dnis;
   }

}
