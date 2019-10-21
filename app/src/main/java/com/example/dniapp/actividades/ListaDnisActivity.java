package com.example.dniapp.actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.dniapp.R;
import com.example.dniapp.adapter.DniAdapter;
import com.example.dniapp.beans.Dni;
import com.example.dniapp.dao.BaseDatosDni;
import com.example.dniapp.util.ComparatorDni;
import com.example.dniapp.util.ComparatorNumero;
import com.example.dniapp.util.Preferencias;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListaDnisActivity extends AppCompatActivity {

    private BaseDatosDni baseDatosDni;
    private List<Dni> dniList;
    private DniAdapter dniAdapter;
    private  RecyclerView recyclerView;

    private ColorDrawable color_fondo; //Para ponerle color cuando arrastramos.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_dnis);

        //Esto solo si recupero mis datos de las Preferences.
       // List<Dni> dniList = Preferencias.cargarFicheroDni(this);

        //Creo mi base de datos. La cual va a ser una primera versión.
        baseDatosDni = new BaseDatosDni( this , BaseDatosDni.NOMBRE_BD, null, 1);
        //Buscamos los Dnis.
        dniList = baseDatosDni.buscarDnis();



        //Si la lista es diferente de null y tiene un tamaño mayor que 0:
        if ((dniList!=null) && (dniList.size() > 0))
        {
            //Cojo la caja que voy a inflar y que luego rellenare con los datos.
            findViewById(R.id.caja_no_resultado).setVisibility(View.GONE);
            //Creo mi Adapter, al cual ,le voy a pasar mi dniList.
            this.dniAdapter = new DniAdapter(dniList);
            //Con el recycler, inflo la fila de datos.
            this.recyclerView = findViewById(R.id.recycler_view);
            //Al recycler le digo que Adapter tiene que utilizar.
            recyclerView.setAdapter(dniAdapter);

            //ESTILO
            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        }

        color_fondo = new ColorDrawable(Color.YELLOW);

        //Método para implementar el swipe.

        ItemTouchHelper.SimpleCallback callback =
                //El 0 significa que no se reazilan movimientos ni para arriba ni para abajo. si lo queremos implementar seria: ItemTouchHleper.down
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {

                    @Override//onMove se va a provocar cuando haya un movimiento vertical. O para arriba o para abajo.
                    public boolean onMove
                            (@NonNull RecyclerView recyclerView,
                             //La cajita donde esta mi dato.
                             @NonNull RecyclerView.ViewHolder viewHolder,
                             //El target es el origen de destino. Es decir, a donde arrastro la cajita.
                             @NonNull RecyclerView.ViewHolder target) {

                        //Para cambiar la posicion de las cajas al arrastrarlas hacia arriba o abajo

                        //Esta es la posicion inicial que tiene la caja que he seleccionado
                        int pos_elemento_seleccionado = viewHolder.getAdapterPosition();

                        //Esta es la posicion a la que quiero llevarmi caja de texto
                        int pos_elemento_destino = target.getAdapterPosition();

                        //COMPLETAR ESTO CON LO ESCRITO EN EL CUADERNO.//

                        //Creo mi dni auxiliar. Este dni va a ser el que se encuentra en la posicion de abajo, la caja de la posicion 1 que es a donde llevamos nuestra caja.
                        //El dni de la caja, la cual tiene la posicion 1, se va a transformar en dni_aux, al insertar el dni de la posicion 0, para que asi no se queden los dos valores en la misma caja.
                        Dni dni_aux = dniList.get(pos_elemento_destino);

                        //A la posicion de destino (el dni de la posicion 1), le paso de la dniList el dni que se encuentra en la posicion 0
                        // que es mi dni seleccionado.
                        dniList.set(pos_elemento_destino, dniList.get(pos_elemento_seleccionado));
                        //Una vez que le he pasado a la posicion 1 el valor del dni que estaba en la posicion 0, la caja de la posicion 0 ha quedado vacia,
                        //Entonces, le paso el valor del Dni que estaba en la posicion 1 al principio, el cual posteriormente, paso a ser el dni_aux.
                        dniList.set(pos_elemento_seleccionado, dni_aux);

                        //Avisamos al adapter de los cambios.´´.,
                        dniAdapter.notifyDataSetChanged();


                        return false;
                    }


                    @Override///onSwiped se invoca al  movimiento lateral.
                    //El viewHolder es la cajita. Direction es la direccion de desplazamiento.
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        Log.d("MIAPP", "direccion " + direction);
                        //Me da la posicion en la lista, devuelve un int.
                        int posicion = viewHolder.getAdapterPosition();
                        Log.d("MIAPP", "posicion " + posicion);

                        //Para eliminar al realizar el movimiento hacia la izquierda. Se quita, pero sigue estando en la BD.
                        //ListaDnisActivity.this.dniList;

                        //Esta instruccion sirve para coger de la posicion que se ha tocado, la informacion que tiene dentro. El Dni en si.
                        Dni dni_seleccionado = dniList.get(posicion);
                        //Con la posicio, puedo borrar visualmente el Dni que por ejemplo se encuentre en la posicion 3.
                        //Pero para borrar de la BD es necesario coger los datos que tiene dentro esa posicion.
                        dniList.remove(posicion);
                        //Aviso al adapter.
                        dniAdapter.notifyDataSetChanged();

                        //TODO borrar de la BD tambien cuando lo borremos de la pantalla
                        baseDatosDni.borrarDeBaseDatos(dni_seleccionado.getNumero());

                    }


                    @Override//onChildDraw se invoca cada vez que se mueve un poco, tanto vertical como horizontal.
                    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                        Log.d("MIAPP", "dX " + dX);
                        Log.d("MIAPP", "dY " + dY);
                        Log.d("MIAPP", "dX " + dX);
                        Log.d("MIAPP", "actionState " + actionState);
                        Log.d("MIAPP", "isCurrentlyActive " + isCurrentlyActive);
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                        //Para ponerle el color de fondo, tengo que ponerlo en el Canvas. (El lienzo del recycler)
                        color_fondo = new ColorDrawable(Color.YELLOW);
                        //Decirle los limites de donde quiero que me pinte. LEFT-TOP-RIGHT-BOTTOM.
                        //Le voy restando la dX que se crea cuando muevo hacia la izquierda.
                        color_fondo.setBounds(viewHolder.itemView.getRight()+ (int)+dX,
                                viewHolder.itemView.getTop(),
                                viewHolder.itemView.getRight(),
                                viewHolder.itemView.getBottom());
                        color_fondo.draw(c);
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                    }

                };

        //Creamos el método ItemTouchHelper.
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        //Lo adjuntamos con el recycler view.
        touchHelper.attachToRecyclerView(recyclerView);


    }

    //Para ordenar por el numero
    public void ordenarPorDni (View view)
    {

        ComparatorNumero comparatorNumero = new ComparatorNumero();
        Collections.sort(dniList, comparatorNumero);

        dniAdapter.notifyDataSetChanged();

        String text = "Ordenado de menor a mayor";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this, text, duration); //This porque es el contexto en el que lo utilizamos.
        toast.show();

    }
    //Para ordenar por la letra
    public void ordenarLetraDni(View view) {


        ComparatorDni comparatorDni = new ComparatorDni();
        Collections.sort(dniList, comparatorDni);
        dniAdapter.notifyDataSetChanged();

        String text = "Ordenado alfabéticamente";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }

    //Para que darle al boton de +, cambiar las ventanas.

    public void sumarDni (View view)
    {
        //TODO 1cerrar esta ventana 2abrir la de main

        //Creo el intent, al que le digo que la darle al boton comience la MainActivity, la cual es la de introducir Dni.
        Intent intent = new Intent(this, MainActivity.class);
        //Le digo que comience la actividad que he puesto arriba.
        startActivity(intent);
        //Finalizo la actividad actual, la de ListasDni.
        this.finish();

    }





}
