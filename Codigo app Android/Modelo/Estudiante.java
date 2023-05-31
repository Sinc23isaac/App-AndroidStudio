package com.example.ejerciciobd.modelo;

import android.os.Parcel;
import android.os.Parcelable;

public class Estudiante implements Parcelable {

    private String nombre;
    private String apellidos;
    private String correoElectronico;

    // Constructor sin argumentos
    public Estudiante() {
        // Constructor sin argumentos requerido por Firebase Realtime Database
    }

   //Constructor
    public Estudiante( String nombre, String apellidos, String correoElectronico) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correoElectronico = correoElectronico;
    }

//Getters

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }
    //Setters

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

//TO string
    @Override
    public String toString() {
        return "ModeloEstudiante{" +
                ", nombre='" + nombre + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", correoElectronico='" + correoElectronico + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nombre);
        dest.writeString(this.apellidos);
        dest.writeString(this.correoElectronico);
    }

    public void readFromParcel(Parcel source) {
        this.nombre = source.readString();
        this.apellidos = source.readString();
        this.correoElectronico = source.readString();
    }

    protected Estudiante(Parcel in) {
        this.nombre = in.readString();
        this.apellidos = in.readString();
        this.correoElectronico = in.readString();
    }

    public static final Parcelable.Creator<Estudiante> CREATOR = new Parcelable.Creator<Estudiante>() {
        @Override
        public Estudiante createFromParcel(Parcel source) {
            return new Estudiante(source);
        }

        @Override
        public Estudiante[] newArray(int size) {
            return new Estudiante[size];
        }
    };
}
