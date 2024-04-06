package models;

import lombok.Getter;
import lombok.Setter;

public class pedido {

    @Getter
    @Setter
    private int NoPedido;

    @Getter
    @Setter
    private int NoBodega;

    @Getter
    @Setter
    private long cuiOperador;

    @Getter
    @Setter
    private char estado;

    public pedido(int NoPedido, int NoBodega, long cuiOperador, char estado) {
        this.NoPedido = NoPedido;
        this.NoBodega = NoBodega;
        this.cuiOperador = cuiOperador;
        this.estado = estado;
    }

    public pedido() {

    }
}
