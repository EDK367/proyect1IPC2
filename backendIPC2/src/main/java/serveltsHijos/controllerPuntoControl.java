/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package serveltsHijos;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataBase.conexionData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import models.puntoControl;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@WebServlet("/api/point")
public class controllerPuntoControl extends HttpServlet {

    conexionData data = new conexionData();
    Connection connection = null;
    Gson gson = new Gson();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet controllerPuntoControl</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet controllerPuntoControl at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS, PUT");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, X-Auth-Token, Origin, Authorization");
    }

//metodo CRUD para ver un nuevo punto de control
    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doOptions(request, response);
        String op = (request.getParameter("op") != null) ? request.getParameter("op") : "list";

        if (op.equals("list")) {
            ArrayList<puntoControl> listPuntoControl = new ArrayList<>();
            connection = data.conectar();
            try {

                String sql = "select * from punto_control";
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    puntoControl puntoControl = new puntoControl();
                    puntoControl.setIdControl(rs.getInt("idControl"));
                    puntoControl.setNombre(rs.getString("nombreControl"));
                    puntoControl.setCuiOperador(rs.getLong("cui_operador"));
                    listPuntoControl.add(puntoControl);
                }
                String json = gson.toJson(listPuntoControl);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
            } catch (Exception ex) {
                response.getWriter().print("Error al leer la base de datos punto control " + ex);
            } finally {
                data.desconectar();
            }
        }
    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doOptions(request, response);
        JsonObject objeto = gson.fromJson(request.getReader(), JsonObject.class);

        int idControl = objeto.get("IdControl").getAsInt();
        String nombre = objeto.get("nombre").getAsString();
        long cuiOperador = objeto.get("cuiOperador").getAsLong();

        puntoControl newPoint = new puntoControl();
        newPoint.setIdControl(idControl);
        newPoint.setNombre(nombre);
        newPoint.setCuiOperador(cuiOperador);

        ResultSet rsVerification = null;

        try{
            connection = data.conectar();
            String sqlVerification = "SELECT * FROM operador WHERE cui_operador = ?";
            PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
            psVerification.setLong(1, cuiOperador);
            rsVerification = psVerification.executeQuery();
            if(rsVerification.next()){
                String sql = "INSERT INTO punto_control (idControl, nombreControl, cui_operador) VALUES (?, ?, ?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, idControl);
                ps.setString(2, nombre);
                ps.setLong(3,cuiOperador);
                ps.executeUpdate();
                response.getWriter().print("SI se inserto " + newPoint);
            }else{
                response.getWriter().print("No existe el operador en la base de datos " + cuiOperador);
            }
        }catch(Exception ex){
                response.getWriter().print("Duplicate data " + ex);
        }finally {
            if (rsVerification != null) {
                rsVerification.close();
            }
            data.desconectar();
        }
    }

    @SneakyThrows
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        doOptions(request, response);
        JsonObject objeto = gson.fromJson(request.getReader(), JsonObject.class);
        int idControl = objeto.get("IdControl").getAsInt();

        }
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
