package models;

import lombok.Getter;
import lombok.Setter;

public class cliente {

    @Getter
    @Setter
    private int NIT;

    @Getter
    @Setter
    private String nombre;

    @Getter
    @Setter
    private String apellido;

    @Getter
    @Setter
    private int telefono;

    @Getter
    @Setter
    private long cuiOperador;

    @Getter
    @Setter
    private long cuiRecepcionista;

    public cliente(int NIT, String nombre, String apellido, int telefono, long cuiOperador, long cuiRecepcionista) {
        this.NIT = NIT;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.cuiOperador = cuiOperador;
        this.cuiRecepcionista = cuiRecepcionista;
    }

    public cliente(){

    }
}
