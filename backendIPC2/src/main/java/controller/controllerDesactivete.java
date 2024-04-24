package controller;

import dataBase.conexionData;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class controllerDesactivete {
    conexionData data = new conexionData();
    Connection connection = null;

    public void desactiveteController(HttpServletResponse response, int rutaTomada, int bodegaOperador, String flag, int contador, int total, int NoPedido)
            throws SQLException, IOException {
        response.getWriter().println(rutaTomada + " " + bodegaOperador + " " + flag + " " + contador);
        try {
            connection = data.conectar();
                if(total == contador){
                   String sqlResultado = "SELECT * FROM pedido WHERE NoPedido = ?";
                   PreparedStatement ps = connection.prepareStatement(sqlResultado);
                   ps.setInt(1, NoPedido);
                   ResultSet rsResultado = ps.executeQuery();
                    if(rsResultado.next()){
                        boolean activete = false;
                        int bodegaActual = rsResultado.getInt("BodegaActual");
                        String sqlUpdate = "UPDATE trayecto SET activete = ? WHERE IDRuta = ? AND IDControl = ?";
                        PreparedStatement psUpdate = connection.prepareStatement(sqlUpdate);
                        psUpdate.setBoolean(1, activete);
                        psUpdate.setInt(2, rutaTomada);
                        psUpdate.setInt(3, bodegaActual);
                        psUpdate.executeUpdate();
                        response.getWriter().println("Trayecto desactivado");
                    }else{
                        response.getWriter().println("Problemas");
                    }
            }
        } catch (SQLException | ClassNotFoundException e) {
            response.getWriter().println("Error de lectura");
        } finally {
            data.desconectar();
        }
    }

}//finally de la clase

