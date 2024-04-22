package modelsServices;

import lombok.Getter;
import lombok.Setter;

public class trayecto {

    @Getter
    @Setter
    private int idRuta;

    @Getter
    @Setter
    private int idControl;

    @Getter
    @Setter
    private int posicion;

    @Getter
    @Setter
    private boolean activete;

    public trayecto(int idRuta, int idControl, int posicion, boolean activete) {
        this.idRuta = idRuta;
        this.idControl = idControl;
        this.posicion = posicion;
        this.activete = activete;
    }

    public trayecto() {

    }
}
