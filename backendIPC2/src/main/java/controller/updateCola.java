package controller;

import dataBase.conexionData;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class updateCola {
    conexionData data = new conexionData();
    Connection connection = null;


    public void actualizarCola(HttpServletResponse response, int idRuta, int NoPedido)
            throws SQLException, IOException {
        char estado = 'R';
        try {
            connection = data.conectar();
            String sqlTotal = "SELECT * FROM ruta WHERE IDRuta = ?";
            PreparedStatement psTotal = connection.prepareStatement(sqlTotal);
            psTotal.setInt(1, idRuta);
            ResultSet rsTotal = psTotal.executeQuery();
            if (rsTotal.next()) {
                int total = rsTotal.getInt("totalPaquetes");
                total++;
                String enviar = "UPDATE ruta SET totalPaquetes = ? WHERE IDRuta = ?";
                PreparedStatement ps = connection.prepareStatement(enviar);
                ps.setInt(1, total);
                ps.setInt(2, idRuta);
                ps.executeUpdate();
            }

            String sql = "UPDATE pedido SET Estado = ?, RutaTomada = ?  WHERE NoPedido = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, String.valueOf(estado));
            ps.setInt(2, idRuta);
            ps.setInt(3, NoPedido);
            ps.executeUpdate();
            response.getWriter().print("Pedido actualizado");
        } catch (SQLException | ClassNotFoundException e) {
            response.getWriter().print("Error de lectura " + e);
        } finally {
            data.desconectar();
        }
    }

    public void actualizarRuta(HttpServletResponse response, int idRuta, int idControl, int NoPedido, int posicion)
            throws SQLException, IOException {
       // response.getWriter().println("NoPedido: " + NoPedido + " - Ruta tomada: " + idRuta + " - Estara en la bodega: " + idControl
         //       + " - Posicion: " + posicion);
        try {
            connection = data.conectar();
            String sql = "UPDATE pedido SET BodegaActual = ? WHERE NoPedido = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, idControl);
            ps.setInt(2, NoPedido);
            ps.executeUpdate();
            response.getWriter().println  ("Pedido actualizado");
        } catch (SQLException | ClassNotFoundException e) {
            response.getWriter().print("Error de lectura " + e);
        } finally {
            data.desconectar();
        }
    }
}//fin de la clase updateCola
