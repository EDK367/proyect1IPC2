package models;

import lombok.Getter;
import lombok.Setter;

public class paquetes {

    @Getter
    @Setter
    private int paquete;

    @Getter
    @Setter
    private int NIT;

    @Getter
    @Setter
    private int NoPedido;

    @Getter
    @Setter
    private int NoBodega;

    @Getter
    @Setter
    private float peso;

    @Getter
    @Setter
    private boolean tarifaGlobal;

    @Getter
    @Setter
    private float total;

    @Getter
    @Setter
    private String destino;

    @Getter
    @Setter
    private long cuiRecepcionista;

    @Getter
    @Setter
    private long cuiOperador;


    public paquetes(int paquete, int NIT, int NoPedido, int NoBodega, float peso, boolean tarifaGlobal, float total, String destino, long cuiRecepcionista, long cuiOperador) {
        this.paquete = paquete;
        this.NIT = NIT;
        this.NoPedido = NoPedido;
        this.NoBodega = NoBodega;
        this.peso = peso;
        this.tarifaGlobal = tarifaGlobal;
        this.total = total;
        this.destino = destino;
        this.cuiRecepcionista = cuiRecepcionista;
        this.cuiOperador = cuiOperador;
    }

    public paquetes(){

    }

}
