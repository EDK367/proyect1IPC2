package models;

import lombok.Getter;
import lombok.Setter;

public class rutas {

    @Getter
    @Setter
    private int idRuta;

    @Getter
    @Setter
    private int cantidadPuntos;

    @Getter
    @Setter
    private String start;

    @Getter
    @Setter
    private String end;

    public rutas(int idRuta, int cantidadPuntos, String start, String end) {
        this.idRuta = idRuta;
        this.cantidadPuntos = cantidadPuntos;
        this.start = start;
        this.end = end;
    }
    public rutas(){

    }

   
}
