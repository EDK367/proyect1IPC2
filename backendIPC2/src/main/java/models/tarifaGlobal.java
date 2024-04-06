package models;

import lombok.Getter;
import lombok.Setter;

public class tarifaGlobal {

    @Getter
    @Setter
    private int tarifaGlobalId;

    @Getter
    @Setter
    private double tarifaG;

    @Getter
    @Setter
    private String fechaInicio;

    @Getter
    @Setter
    private long cuiAdmin;

    public tarifaGlobal(int tarifaGlobalId, double tarifaG, String fechaInicio, long cuiAdmin) {
        this.tarifaGlobalId = tarifaGlobalId;
        this.tarifaG = tarifaG;
        this.fechaInicio = fechaInicio;
        this.cuiAdmin = cuiAdmin;
    }

    public tarifaGlobal() {

    }
}
