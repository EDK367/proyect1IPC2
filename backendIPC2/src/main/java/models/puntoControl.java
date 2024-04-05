package models;

import lombok.Getter;
import lombok.Setter;

public class puntoControl {

    @Getter
    @Setter
    private int IdControl;

    @Getter
    @Setter
    private String nombre;

    @Getter
    @Setter
    private  long cuiOperador;


    public puntoControl(int IdControl, String nombre, long cuiOperador) {
        this.IdControl = IdControl;
        this.nombre = nombre;
        this.cuiOperador = cuiOperador;
    }

    public puntoControl(){

    }


}
