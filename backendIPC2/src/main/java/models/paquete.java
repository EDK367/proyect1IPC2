package models;

import lombok.Getter;
import lombok.Setter;

public class paquete {

    @Getter
    @Setter
    private int paquete;

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
    private int bodegaInicial;

    @Getter
    @Setter
    private int cliente;

    @Getter
    @Setter
    private float peso;

    @Getter
    @Setter
    private boolean tarifaGlobal;

    @Getter
    @Setter
    private float total;

    public paquete(int paquete, long cuiOperador, long cuiRecepcionista, int noPedido, int bodegaInicial, int cliente, float peso, boolean tarifaGlobal, float total) {

        this.paquete = paquete;
        this.cuiOperador = cuiOperador;
        this.cuiRecepcionista = cuiRecepcionista;
        this.noPedido = noPedido;
        this.bodegaInicial = bodegaInicial;
        this.cliente = cliente;
        this.peso = peso;
        this.tarifaGlobal = tarifaGlobal;
        this.total = total;
    }
    public paquete(){

    }
}
