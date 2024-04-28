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
                System.out.println("EL pedido se manda como" + NoPedido);
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

    public void updateTotal(HttpServletResponse response, int NoPedido)
        throws SQLException, IOException{

        try{
            connection = data.conectar();
            System.out.println("EL pedido recibido es " + NoPedido);
            String sqlTotalPedido =  "SELECT * FROM paquetes WHERE NoPedido = ?";
            PreparedStatement psTotalPedido = connection.prepareStatement(sqlTotalPedido);
            psTotalPedido.setInt(1, NoPedido);
            ResultSet rsTotalPedido = psTotalPedido.executeQuery();
            float total = 0;
            while(rsTotalPedido.next()){
                total += rsTotalPedido.getFloat("total");
                System.out.println("El total es" + total);
            }
            String sqlTotalFactura = "SELECT * FROM facturas WHERE NoPedido = ?";
            PreparedStatement psTotalFactura = connection.prepareStatement(sqlTotalFactura);
            psTotalFactura.setInt(1, NoPedido);
            ResultSet rsTotalFactura = psTotalFactura.executeQuery();
            float totalActual = 0;
            if (rsTotalFactura.next()) {
                totalActual = rsTotalFactura.getFloat("total");
                System.out.println("El total actual es " + totalActual);
            }
            float nuevoTotal = totalActual + total;
            System.out.println("El nuevo total es " + nuevoTotal);
            String sqlUpdateTotal = "UPDATE facturas SET total = ? WHERE NoPedido = ?";
            PreparedStatement psUpdateTotal = connection.prepareStatement(sqlUpdateTotal);
            psUpdateTotal.setFloat(1, nuevoTotal);
            psUpdateTotal.setInt(2, NoPedido);
            psUpdateTotal.executeUpdate();

            response.getWriter().println("Total actualizado exitosamente");
        }catch(SQLException | ClassNotFoundException e){
            response.getWriter().println("Error al conectar con la base de datos" + e);
        }finally{
            data.desconectar();
        }

    }

}
