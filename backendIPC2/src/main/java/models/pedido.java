package models;

import lombok.Getter;
import lombok.Setter;

public class pedido {

    @Getter
    @Setter
    private int noPedido;

    @Getter
    @Setter
    private int bodegaActual;

    @Getter
    @Setter
    private char estado;

    @Getter
    @Setter
    private int destinoController;

    @Getter
    @Setter
    private int rutaTomada;

    public pedido(int noPedido, int bodegaActual, char estado, int destinoController, int rutaTomada) {
        this.noPedido = noPedido;
        this.bodegaActual = bodegaActual;
        this.estado = estado;
        this.destinoController = destinoController;
        this.rutaTomada = rutaTomada;
    }

    public pedido(){

    }
}
