package com.example.smartbin;

public class MainModel {
    String Id;
    String Direccion;
    String Peso;
    String Estado;
    String Nombre;

    public MainModel() {
    }

    public MainModel(String id, String direccion, String peso, String estado, String nombre) {
        Id = id;
        Direccion = direccion;
        Peso = peso;
        Estado = estado;
        Nombre = nombre;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getPeso() {
        return Peso;
    }

    public void setPeso(String peso) {
        Peso = peso;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }
}
