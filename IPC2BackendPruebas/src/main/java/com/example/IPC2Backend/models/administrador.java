package com.example.IPC2Backend.models;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import java.util.Collection;

@Entity
@Table(name = "administrador", uniqueConstraints = @UniqueConstraint(columnNames = "correo"))
public class administrador {

    @Id
    @Column
    private Long cuiAdmin;

    @Column
    private String nombre;

    @Column
    private String apellido;

    @Column
    private String correo;

    @Column
    private String contraseña;



    public administrador() {

    }

    public administrador(Long cuiAdmin, String nombre, String apellido, String correo, String contraseña) {
        this.cuiAdmin = cuiAdmin;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contraseña = contraseña;

    }




    public Long getCuiAdmin() {

        return cuiAdmin;
    }

    public void setCuiAdmin(Long cuiAdmin) {

        this.cuiAdmin = cuiAdmin;
    }

    public String getNombre() {

        return nombre;
    }

    public void setNombre(String nombre) {

        this.nombre = nombre;
    }

    public String getApellido() {

        return apellido;
    }

    public void setApellido(String apellido) {

        this.apellido = apellido;
    }

    public String getCorreo() {

        return correo;
    }

    public void setCorreo(String correo) {

        this.correo = correo;
    }

    public String getContraseña() {

        return contraseña;
    }

    public void setContraseña(String contraseña) {

        this.contraseña = contraseña;
    }

}
