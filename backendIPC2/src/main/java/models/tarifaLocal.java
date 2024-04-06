package models;

import lombok.Getter;
import lombok.Setter;

public class tarifaLocal {

    @Getter
    @Setter
    private int IdTarifa;

    @Getter
    @Setter
    private float tarifaLocal;

    @Getter
    @Setter
    private int IDControl;

    @Getter
    @Setter
    private long cuiOperador;

    public tarifaLocal(int IdTarifa, float tarifaLocal, int IDControl, long cuiOperador) {
        this.IdTarifa = IdTarifa;
        this.tarifaLocal = tarifaLocal;
        this.IDControl = IDControl;
        this.cuiOperador = cuiOperador;
    }

    public tarifaLocal(){

    }
}
