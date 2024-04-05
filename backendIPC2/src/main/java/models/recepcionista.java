package models;

import lombok.Getter;
import lombok.Setter;

public class recepcionista {

    @Getter
    @Setter
    private  long cuiRecepcionista;

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

    @Getter
    @Setter
    private long cuiOperador;

    public recepcionista(long cuiRecepcionista, String nombre, String apellido, String correo, String contraseña, long cuiOperador) {
        this.cuiRecepcionista = cuiRecepcionista;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contraseña = contraseña;
        this.cuiOperador = cuiOperador;
    }

    public recepcionista(){

    }

}
