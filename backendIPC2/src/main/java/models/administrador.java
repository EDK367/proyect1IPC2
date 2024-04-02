package models;


import jakarta.ws.rs.GET;
import lombok.Getter;
import lombok.Setter;

public class administrador {

    @Getter
    @Setter
    private int cuiAdmin;

    @Getter
    @Setter
    private String nombre;

    @Getter
    @Setter
    private String apellido;

    @Getter
    @Setter
    private String correo;

    @Getter
    @Setter
    private String contraseña;

    public administrador(int cuiAdmin, String nombre, String apellido, String correo, String contraseña) {
        this.cuiAdmin = cuiAdmin;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contraseña = contraseña;
    }

    public administrador() {

    }
}
