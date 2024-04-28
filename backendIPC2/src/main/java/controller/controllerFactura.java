package controller;

import com.google.gson.Gson;
import dataBase.conexionData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import models.factura;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/api/facturas")
public class controllerFactura extends HttpServlet {
    factura getFactura = new factura();
    conexionData data = new conexionData();
    Connection connection = null;
    Gson  gson = new Gson();

    protected void processRequest(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet controllerPrincipal</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet controllerFacturas at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS, PUT");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, X-Auth-Token, Origin, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }


    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{
        doOptions(request, response);
        String op = (request.getParameter("op") != null) ? request.getParameter("op") : "list";

        if(op.equals("list")){
            ArrayList<factura> listFactura = new ArrayList<>();
            try {
               connection = data.conectar();
               String sql = "SELECT * FROM facturas";
               PreparedStatement ps = connection.prepareStatement(sql);
               ResultSet rs = ps.executeQuery();

               while(rs.next()){
                   factura factura = new factura();
                   factura.setNoFactura(rs.getInt("NoFactura"));
                   factura.setCuiOperador(rs.getLong("cui_operador"));
                   factura.setCuiRecepcionista(rs.getLong("cui_recepcionista"));
                   factura.setNoPedido(rs.getInt("NoPedido"));
                   factura.setNoBodega(rs.getInt("NoBodega"));
                   factura.setRuta(rs.getInt("ruta"));
                   factura.setTotal(rs.getFloat("total"));
                   listFactura.add(factura);
               }
               String json = gson.toJson(listFactura);
               response.setContentType("application/json");
               response.setCharacterEncoding("UTF-8");
               PrintWriter out = response.getWriter();
               out.print(json);
               out.flush();
            } catch (SQLException e) {
               response.getWriter().print("No se pudo listar" + e);
            }finally {
                data.desconectar();
            }

        }else{
            response.getWriter().print("No se pudo listar");
        }
    }
}//fin de la clase
