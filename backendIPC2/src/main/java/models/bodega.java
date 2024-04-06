package models;

import lombok.Getter;
import lombok.Setter;

public class bodega {

    @Getter
    @Setter
    private int IdBodega;

    @Getter
    @Setter
    private long cuiOperador;

    public bodega() {

    }
    public bodega(int IdBodega, long cuiOperador) {
        this.IdBodega = IdBodega;
        this.cuiOperador = cuiOperador;
    }
}
