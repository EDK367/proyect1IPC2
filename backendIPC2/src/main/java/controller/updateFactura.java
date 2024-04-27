package controller;

import dataBase.conexionData;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class updateFactura extends HttpServlet {
    conexionData data = new conexionData();
    Connection connection = null;

    public void updateFactura(HttpServletResponse response, int NoPedido, int idRuta)
            throws SQLException, IOException {

        try{
            connection = data.conectar();
            String sqlFactura = "SELECT * FROM facturas WHERE NoPedido = ?";
            PreparedStatement psFactura = connection.prepareStatement(sqlFactura);
            psFactura.setInt(1, NoPedido);
            ResultSet rsFactura = psFactura.executeQuery();
            if(rsFactura.next()){
                String sql = "UPDATE facturas SET ruta = ? WHERE NoPedido = ?";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, idRuta);
                ps.setInt(2, NoPedido);
                ps.executeUpdate();
                response.getWriter().println("Factura actualizada");
            }else{
                response.getWriter().println("Error al actualizar la factura");
            }

        }catch(SQLException | ClassNotFoundException e){
            response.getWriter().println("Error al conectar con la base de datos" + e);
        }finally{
            data.desconectar();
        }
    }



}//fin de la clase
