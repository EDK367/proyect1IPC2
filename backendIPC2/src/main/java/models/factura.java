package models;

import lombok.Getter;
import lombok.Setter;

public class factura {

    @Getter
    @Setter
    private int noFactura;

    @Getter
    @Setter
    private long cuiOperador;

    @Getter
    @Setter
    private long cuiRecepcionista;

    @Getter
    @Setter
    private int noPedido;

    @Getter
    @Setter
    private int noBodega;

    @Getter
    @Setter
    private int ruta;

    @Getter
    @Setter
    private float total;

    public factura() {
    }

    public factura(int noFactura, long cuiOperador, long cuiRecepcionista, int noPedido, int noBodega, int ruta, float total) {
        this.noFactura = noFactura;
        this.cuiOperador = cuiOperador;
        this.cuiRecepcionista = cuiRecepcionista;
        this.noPedido = noPedido;
        this.noBodega = noBodega;
        this.ruta = ruta;
        this.total = total;
    }
}
