package modelsServices;

import lombok.Getter;
import lombok.Setter;

public class reportePopular {

    @Getter
    @Setter
    private int noFactura;

    @Getter
    @Setter
    private int ruta;

    @Getter
    @Setter
    private int noPedido;

    @Getter
    @Setter
    private float total;

    @Getter
    @Setter
    private String nombre;

    @Getter
    @Setter
    private int NIT;

    @Getter
    @Setter
    private int telefono;

    public reportePopular() {
    }

    public reportePopular(int noFactura, int ruta, int noPedido, float total, String nombre, int NIT, int telefono) {
        this.noFactura = noFactura;
        this.ruta = ruta;
        this.noPedido = noPedido;
        this.total = total;
        this.nombre = nombre;
        this.NIT = NIT;
        this.telefono = telefono;
    }

}
