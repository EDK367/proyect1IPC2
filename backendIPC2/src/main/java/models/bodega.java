package models;

import lombok.Getter;
import lombok.Setter;

public class bodega {

    @Getter
    @Setter
    private int idBodega;

    @Getter
    @Setter
    private int punto;

    public bodega() {

    }
    public bodega(int idBodega, int punto) {
        this.idBodega = idBodega;
        this.punto = punto;
    }
}
