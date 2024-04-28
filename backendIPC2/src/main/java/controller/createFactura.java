package controller;

import dataBase.conexionData;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class createFactura extends HttpServlet {
    conexionData data = new conexionData();
    Connection connection = null;

    public void createFactura(HttpServletResponse response, int noPedido, long cuiOperador, long cuiRecepcionista, int bodegaIncial) {

        System.out.println("NoPedido: " + noPedido);
        try {
            connection = data.conectar();
            String sqlExisteFac = "SELECT * FROM facturas WHERE NoPedido = ?";
            PreparedStatement psExisteFac = connection.prepareStatement(sqlExisteFac);
            psExisteFac.setInt(1, noPedido);
            ResultSet rsExisteFac = psExisteFac.executeQuery();
            if (rsExisteFac.next()) {
                System.out.println("Este pedido ya tiene factura");
            }else{
                System.out.println("NO  tiene factura  ");
                String sqlExiste = "SELECT * FROM facturas WHERE NoPedido";
                PreparedStatement psExiste = connection.prepareStatement(sqlExiste);
                ResultSet rsExiste = psExiste.executeQuery();
                String sqlFactura = "INSERT INTO facturas(cui_operador, cui_recepcionista, NoPedido, NoBodega) VALUES(?,?,?,?)";
                PreparedStatement psFactura = connection.prepareStatement(sqlFactura);
                psFactura.setLong(1, cuiOperador);
                psFactura.setLong(2, cuiRecepcionista);
                psFactura.setInt(3, noPedido);
                psFactura.setInt(4, bodegaIncial);
                psFactura.executeUpdate();
            }
        } catch (SQLException | ClassNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }finally {
            data.desconectar();
        }
    }
}
