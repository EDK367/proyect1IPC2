package models;

import lombok.Getter;
import lombok.Setter;

public class operadores {

    @Getter
    @Setter
    private long cuiOperador;

    @Getter
    @Setter
    private int idTrabajador;

    @Getter
    @Setter
    private String nombre;

    @Getter
    @Setter
    private String apellido;

    @Getter
    @Setter
    private String correo;

    @Getter
    @Setter
    private String contraseña;


    public operadores(long cuiOperador, String nombre, String apellido, String correo, String contraseña, int idTrabajador) {
        this.cuiOperador = cuiOperador;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contraseña = contraseña;
        this.idTrabajador = idTrabajador;
    }

    public operadores(){

    }


    
}
