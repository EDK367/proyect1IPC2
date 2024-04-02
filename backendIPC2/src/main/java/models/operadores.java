package models;

import lombok.Getter;
import lombok.Setter;

public class operadores {

    @Getter
    @Setter
    private int cuiOperador;

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


    public operadores(int cuiOperador, String nombre, String apellido, String correo, String contraseña) {
        this.cuiOperador = cuiOperador;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contraseña = contraseña;
    }

    public operadores(){

    }


    
}
