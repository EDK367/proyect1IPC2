package controller;

import com.google.gson.Gson;
import dataBase.conexionData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import lombok.SneakyThrows;
import models.administrador;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelsServices.reportePopular;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/api/reportes")
public class controllerReportes extends HttpServlet {
    reportePopular reporte = new reportePopular();
    conexionData data = new conexionData();
    Connection connection = null;
    ArrayList<reportePopular> listReporte = new ArrayList<>();
    Gson gson = new Gson();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
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
            out.println("<h1>Servlet ControllerReportes at " + request.getContextPath() + "</h1>");
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
    protected void doGet(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response)
            throws ServletException, IOException {
        doOptions(request, response);
        String op = (request.getParameter("op") != null) ? request.getParameter("op") : "list";
        switch (op) {
            case "list":
                lista(request, response);
                break;
            case "popular":
                mostrarPopular(request, response);
                break;
            case "pagado":
                mostrarMejorPagados(request, response);
                break;
            case "ganancia":
                ganancia(request, response);
                break;
            case "cliente":
                mejoresClientes(request, response);
                break;
            case "rutas":
                rutas(request, response);
                break;
            default:
                response.getWriter().println("No se encontro la opcion " + op);
                break;
        }//fin del switch

    }

    @SneakyThrows
    private void lista(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        doOptions(request, response);
        try {
            connection = data.conectar();
            String sql = "SELECT * FROM reportesRutas";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                reportePopular reporte = new reportePopular();
                reporte.setNoFactura(rs.getInt("NoFactura"));
                reporte.setRuta(rs.getInt("ruta"));
                reporte.setNoPedido(rs.getInt("NoPedido"));
                reporte.setTotal(rs.getInt("total_factura"));
                reporte.setNombre(rs.getString("nombre_cliente"));
                reporte.setNIT(rs.getInt("NIT"));
                reporte.setTelefono(rs.getInt("telefono"));
                listReporte.add(reporte);
            }
            String json = gson.toJson(listReporte);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
            listReporte.clear();
        }catch(SQLException e){
            response.getWriter().print("Error al conectar a la base de datos " + e);
        }finally{
            data.desconectar();
        }
    }

    @SneakyThrows
    private void mostrarPopular(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        doOptions(request, response);
        try {
            connection = data.conectar();
            String sql = "SELECT ruta, COUNT(ruta) AS total FROM reportesRutas GROUP BY ruta ORDER BY total DESC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                reportePopular reporte = new reportePopular();
                reporte.setRuta(rs.getInt("ruta"));
                reporte.setTotal(rs.getInt("total"));
                listReporte.add(reporte);
            }

            String json = gson.toJson(listReporte);
            // Responde con el Json
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
            listReporte.clear();
        }catch(SQLException e){
            response.getWriter().print("Error al conectar a la base de datos " + e);
        }finally{
            data.desconectar();
        }
    }

    @SneakyThrows
    private void mostrarMejorPagados(HttpServletRequest request, HttpServletResponse response)
    throws SQLException, IOException{
        doOptions(request, response);

        try{
            connection = data.conectar();
            String sql = "SELECT ruta, SUM(total_factura) as total from reportesRutas GROUP BY ruta ORDER BY total DESC LIMIT 3";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                reportePopular reporte = new reportePopular();
                reporte.setRuta(rs.getInt("ruta"));
                reporte.setTotal(rs.getInt("total"));
                listReporte.add(reporte);
            }

            String json = gson.toJson(listReporte);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
            listReporte.clear();
        }catch(SQLException e){
            response.getWriter().print("Error al conectar a la base de datos " + e);
        }finally{
            data.desconectar();
        }
    }

    @SneakyThrows
    private void ganancia(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{
        doOptions(request, response);
        try {
            connection = data.conectar();
            String sql = "SELECT SUM(total_factura) as total FROM reportesRutas";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                reporte.setTotal(rs.getInt("total"));
                listReporte.add(reporte);
            }
            String json = gson.toJson(listReporte);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
            listReporte.clear();
        }catch(SQLException e){
            response.getWriter().print("Error al conectar a la base de datos " + e);
        }finally {
            data.desconectar();
        }
    }

    @SneakyThrows
    private void mejoresClientes(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{
        doOptions(request, response);
        try {
            connection = data.conectar();
            String sql = "SELECT NIT, COUNT(NIT) AS total FROM reportesrutas GROUP BY NIT ORDER BY total DESC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                reportePopular reporte = new reportePopular();
                reporte.setNIT(rs.getInt("NIT"));
                reporte.setTotal(rs.getInt("total"));
                listReporte.add(reporte);
            }
            String json = gson.toJson(listReporte);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
            listReporte.clear();
        }catch(SQLException e){
            response.getWriter().print("Error al conectar a la base de datos " + e);
        }finally {
            data.desconectar();
        }
    }

    @SneakyThrows
    private void rutas(HttpServletRequest request, HttpServletResponse response)
        throws SQLException, IOException {
        doOptions(request, response);
        try {
            connection = data.conectar();
            String sql = "SELECT ruta, COUNT(ruta) AS total FROM reportesRutas GROUP BY ruta ORDER BY total ASC";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                reportePopular reporte = new reportePopular();
                reporte.setRuta(rs.getInt("ruta"));
                reporte.setTotal(rs.getInt("total"));
                listReporte.add(reporte);
            }
            String json = gson.toJson(listReporte);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
            listReporte.clear();
        }finally{
            data.desconectar();
        }
    }
}//fin de la clase
