package com.example.ejerciciobd.controlador;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ejerciciobd.MainActivity;
import com.example.ejerciciobd.modelo.Estudiante;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Controlador {


    //metodos xd
    public static boolean comrpobacion(Context context, String  txt1, String txt2, String txt3){
        boolean comprueba;
        if (!txt1.isEmpty()){
            if(!txt2.isEmpty()){
                if(!txt3.isEmpty()){
                    comprueba=true;

                }else{
                    Toast.makeText(context, "El CORREO debe estar lleno", Toast.LENGTH_SHORT).show();
                    comprueba=false;
                }
            }else{
                Toast.makeText(context, "El APELLIDO debe estar lleno", Toast.LENGTH_SHORT).show();
                comprueba=false;
            }

        }else{
            Toast.makeText(context, "El NOMBRE debe estar lleno", Toast.LENGTH_SHORT).show();
            comprueba=false;
        }

        return  comprueba;

    }

    //metodo2
    public static boolean comprobacionBuscador(Context context, String nomb){
        boolean boolbusca;
        if(!nomb.isEmpty()){
          boolbusca=true;

        }else{
            Toast.makeText(context, "El NOMBRE debe estar lleno del BUSCADOR", Toast.LENGTH_SHORT).show();
            boolbusca=false;
        }
        return boolbusca;
    }

    //metodo 3
    public static boolean comprobacionEliminar(Context context, String nomb){
        boolean boolbusca;
        if(!nomb.isEmpty()){
            boolbusca=true;

        }else{
            Toast.makeText(context, "El NOMBRE debe estar lleno del buscador para ELIMINAR", Toast.LENGTH_SHORT).show();
            boolbusca=false;
        }
        return boolbusca;
    }

    //metodo 4
    public static boolean comrpobacionModificar(Context context, String nomb,String apelli, String correo){
        boolean boolbusca;
        if(!nomb.isEmpty()){
            if(!apelli.isEmpty()){
                if(!correo.isEmpty()){
                    boolbusca=true;

                }else{
                    Toast.makeText(context, "El CCORREO ELECTRONICO debe estar lleno del buscador para Modificar", Toast.LENGTH_SHORT).show();
                    boolbusca=false;
                }
            }else{
                Toast.makeText(context, "El Apellido debe estar lleno del buscador para Modificar", Toast.LENGTH_SHORT).show();
                boolbusca=false;
            }
        }else{
            Toast.makeText(context, "El NOMBRE debe estar lleno del buscador para Modificar", Toast.LENGTH_SHORT).show();
            boolbusca=false;
        }
        return boolbusca;
    }


}
