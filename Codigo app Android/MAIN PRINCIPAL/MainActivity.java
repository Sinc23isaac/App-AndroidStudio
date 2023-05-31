package com.example.ejerciciobd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ejerciciobd.controlador.Controlador;
import com.example.ejerciciobd.modelo.Estudiante;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText txti, txnombre,txtapellido, txtCorreo, txtBuscador;
    private Button btnAceptar, modificar,eliminar, buscar, limpiar;
    //listavista xd
    private ListView listaestu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //editext
        txnombre=(EditText)findViewById(R.id.nombre);
        txtapellido=(EditText)findViewById(R.id.apellido);
        txtCorreo=(EditText)findViewById(R.id.correo);
        txtBuscador=(EditText)findViewById(R.id.buscareditx);

        //lista
        listaestu=(ListView)findViewById(R.id.listaEstudianteS);
        //botones
        btnAceptar=(Button)findViewById(R.id.btaceptar);
        modificar=(Button)findViewById(R.id.btnModificar);
        eliminar=(Button)findViewById(R.id.btnEliminar);
        buscar=(Button)findViewById(R.id.btnBuscar);
        limpiar=(Button)findViewById(R.id.btnLimpiar);

        //llamo al metodo listaview xd
        listEstudiantes();
        //funcion del bootn aceptar
        btnAceptar.findViewById(R.id.btaceptar);
        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = txnombre.getText().toString();
                String ape = txtapellido.getText().toString();
                String corr = txtCorreo.getText().toString();

                if (Controlador.comrpobacion(getApplicationContext(), nom, ape, corr)) {
                    //coneccion a la BD
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference dbRef = database.getReference("Estudiante");

                    dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //VALIDANDO QUE NOSE REPITA EL NOMBRE XD
                            boolean resu=false;
                            for (DataSnapshot x:snapshot.getChildren()){
                                //recuerdo lo de las comillas es el atriubuto de la bd
                                if(x.child("nombre").getValue().toString().equals(nom)){
                                    resu=true;
                                    Toast.makeText(MainActivity.this, "Error ese nombre ya existe!!", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }

                            if(resu==false){
                                Estudiante estudiante = new Estudiante(nom, ape, corr);
                                dbRef.push().setValue(estudiante);
                                Toast.makeText(MainActivity.this, "Estudiante guardado correctamente", Toast.LENGTH_SHORT).show();
                                // Limpiar los campos de texto
                                txnombre.setText("");
                                txtapellido.setText("");
                                txtCorreo.setText("");
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this, "Error al guardar el estudiante", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        //boton modficiar
        modificar.findViewById(R.id.btnModificar);
        modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nom = txnombre.getText().toString();
                String ape = txtapellido.getText().toString();
                String corr = txtCorreo.getText().toString();
                String buscador=txtBuscador.getText().toString();

                if (Controlador.comrpobacionModificar(getApplicationContext(), nom, ape, corr)) {
                    //coneccion a la BD
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference dbRef = database.getReference("Estudiante");
                    //preguntamos cuando estamos dentro de la bd
                    dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            //VALIDANDO QUE NOSE REPITA EL NOMBRE XD
                            boolean resu=false;
                            //revisando la bd de firebase
                            for (DataSnapshot x:snapshot.getChildren()){
                                //recuerdo lo de las comillas es el atriubuto de la bd
                                if(x.child("nombre").getValue().toString().equals(buscador)){
                                    resu=true;
                                    //se hace los cambios xd
                                    x.getRef().child("nombre").setValue(nom);
                                    x.getRef().child("apellidos").setValue(ape);
                                    x.getRef().child("correoElectronico").setValue(corr);
                                    txnombre.setText("");
                                    txtapellido.setText("");
                                    txtCorreo.setText("");
                                    Toast.makeText(MainActivity.this, "El ESTUDIANTE("+nom+") Ya se MODIFICO", Toast.LENGTH_SHORT).show();

                                    //por o llamar al metodo de listaview para actualizar xd
                                    listEstudiantes();
                                    //saldra del bucle xd
                                    break;

                                }
                            }
                            //pues si sigue falso no s epodra modificar
                            if(resu==false){
                                Toast.makeText(MainActivity.this, "El ESTUDIANTE("+nom+") NO se pudo MODIFICAR", Toast.LENGTH_SHORT).show();
                                // Limpiar los campos de texto
                                txnombre.setText("");
                                txtapellido.setText("");
                                txtCorreo.setText("");
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this, "Error al guardar el estudiante", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        });

        //btn eliminar
        eliminar.findViewById(R.id.btnEliminar);
        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String busc = txtBuscador.getText().toString();
                String nombre = txnombre.getText().toString();

                //pregunto si la parte del editet debuscar esta lleno si no pues no entrara
                if (Controlador.comprobacionEliminar(getApplicationContext(), busc)) {
                    //entro a la bd
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference dbRef = database.getReference("Estudiante");
                    //hago cosas en la bd
                    dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean encontrado = false;

                            //recorro la lista de datos que hay en el firebase
                            for (DataSnapshot x : snapshot.getChildren()) {
                                //pregunto que tiene el editext y si es igual a lo que hay en el hijo de nombre xd
                                if (busc.equalsIgnoreCase(x.child("nombre").getValue().toString())) {
                                    //mensaje de ventana emergente donde pregunto si quiere eliminar
                                    AlertDialog.Builder a = new AlertDialog.Builder(MainActivity.this);
                                    a.setCancelable(false);
                                    a.setTitle("Pregunta");
                                    a.setMessage("¿Está seguro de querer eliminar este registro del alumno?");
                                    //aqui sera la parte de que pasa i le doy a cancelar xd
                                    a.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Acción al cancelar
                                        }
                                    });
                                    //aqui si le doy a la parte de aceptar se eliminara ese dato de la bd de firebase
                                    a.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            x.getRef().removeValue();
                                            //actualiar la listaview
                                            listEstudiantes();
                                            Toast.makeText(MainActivity.this, "NOMBRE (" + nombre + ") ELIMINADO!!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    a.show();
                                    //sale del bucle forzado xd xq es verdadero
                                    encontrado = true;
                                    break; // Salir del bucle una vez encontrado
                                }
                            }

                            //pues si sigue siendo falso es decir si no existe antes entrara aqui y dira que no existe medinate un mensaje
                            if (!encontrado) {
                                Toast.makeText(MainActivity.this, "NOMBRE (" + busc + ") NO EXISTE!!!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Acción en caso de cancelación
                        }
                    });
                }
            }
        });


        //buscar btn
        buscar.findViewById(R.id.btnBuscar);
        buscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String busc = txtBuscador.getText().toString();
                String nombre=txnombre.getText().toString();

                //preguntar que no este vacio el campo
                if(Controlador.comprobacionBuscador(getApplicationContext(),busc)){
                    //coneccion a la BD
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference dbRef = database.getReference("Estudiante");

                    // establece un "escuchador" o listener para un solo evento de lectura de datos en la
                    // referencia de la base de datos dbRef.
                    dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boolean res=false;
                            //miran los hijos y los lee

                            //para recorrer cada uno de los hijos y ejecutar el código dentro del bloque
                            // del bucle para cada hijo.
                            for(DataSnapshot x:snapshot.getChildren()){
                                //pues compara si esta en mayuscula o minuscula su valor y
                                //si es igual al valor del campo "nombre"que tenemos en la bd mediente el cual recorre algo asi
                                if(busc.equalsIgnoreCase(x.child("nombre").getValue().toString())){

                                    res=true;
                                    txnombre.setText(x.child("nombre").getValue().toString());
                                    txtapellido.setText(x.child("apellidos").getValue().toString());
                                    txtCorreo.setText(x.child("correoElectronico").getValue().toString());
                                    Toast.makeText(MainActivity.this, "NOMBRE ("+busc+")Existe!!", Toast.LENGTH_SHORT).show();
                                    break;
                                }

                            }
                            if(res==false){
                                Toast.makeText(MainActivity.this, "NOMBRE ("+busc+")No encontrado!!!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
        //btn limpiar
        limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txnombre.setText("");
                txtapellido.setText("");
                txtCorreo.setText("");
            }
        });

    }
    //metodo para la lista view xd

    public void listEstudiantes(){
        //conexxion bd firebase
        FirebaseDatabase db= FirebaseDatabase.getInstance();
        DatabaseReference refre=db.getReference(Estudiante.class.getSimpleName());

        //la lista q creamos para mostar al listview xd
        ArrayList<Estudiante>listaEst=new ArrayList<Estudiante>();
        ArrayAdapter<Estudiante>ada=new ArrayAdapter<Estudiante>(MainActivity.this, android.R.layout.simple_list_item_1,listaEst);
        listaestu.setAdapter(ada);

        //detecta los registro nuesvos que se hagan va aactualizar el listview
        refre.addChildEventListener(new ChildEventListener() {
            @Override
            //solo cuando se agregen registros nuevos
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                //nombre refe  xd
                Estudiante estu=snapshot.getValue(Estudiante.class);
                listaEst.add(estu);
                ada.notifyDataSetChanged();
            }

            @Override
            //cuando se produscan cambios xd
            //es decir tipo elimino algo xd
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                ada.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //firebase xd
        //el toque para el listView
        listaestu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                //miramos el modelo y loq  tenemos xd
                Estudiante estudi=listaEst.get(i);
                AlertDialog.Builder a=new AlertDialog.Builder(MainActivity.this);
                a.setCancelable(true);
                a.setTitle("Estuiante Selecionado");

                //saldra una ventna y mostrar los datos
                String msg="Nombre :"+estudi.getNombre()+"\n\n";
                msg+="Apellido :"+estudi.getApellidos()+"\n\n";
                msg+="Correo Electronico :"+estudi.getCorreoElectronico();

                //muestra mensaje xd
                a.setMessage(msg);
                a.show();

                //aqui se pondra los datos en el buscador y el los editext de arriba xd
                txtBuscador.setText(estudi.getNombre());
                txnombre.setText(estudi.getNombre());
                txtapellido.setText(estudi.getApellidos());
                txtCorreo.setText(estudi.getCorreoElectronico());

            }
        });

    }
}
