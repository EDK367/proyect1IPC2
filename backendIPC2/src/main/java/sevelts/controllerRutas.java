package sevelts;

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
import models.rutas;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/api/router")
public class controllerRutas extends HttpServlet {
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
            out.println("<title>Servlet controllerRutas</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet controllerRutas at " + request.getContextPath() + "</h1>");
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

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doOptions(request, response);
        String op = (request.getParameter("op") != null) ? request.getParameter("op") : "list";

        if (op.equals("list")) {
            ArrayList<rutas> listRutas = new ArrayList<>();
            conexionData data = new conexionData();
            Connection connection = data.conectar();
            try {
                String sql = "select * from ruta";
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    rutas ruta = new rutas();
                    ruta.setIdRuta(rs.getInt("idRuta"));
                    ruta.setCantidadPuntos(rs.getInt("cantidadPuntoControl"));
                    ruta.setStart(rs.getString("puntoInicio"));
                    ruta.setEnd(rs.getString("puntoFin"));
                    listRutas.add(ruta);
                }
                String json = gson.toJson(listRutas);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
            } catch (SQLException e) {
                response.getWriter().print("Error inesperado " + e);
            } finally {
                data.desconectar();
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doOptions(request, response);
        JsonObject objeto = gson.fromJson(request.getReader(), JsonObject.class);
        int idRuta = objeto.get("idRuta").getAsInt();
        int cantidadPuntos = objeto.get("cantidadPuntos").getAsInt();
        String puntoInicio = objeto.get("start").getAsString();
        String puntoFinal = objeto.get("end").getAsString();

        rutas newRuta = new rutas();
        newRuta.setIdRuta(idRuta);
        newRuta.setCantidadPuntos(cantidadPuntos);
        newRuta.setStart(puntoInicio);
        newRuta.setEnd(puntoFinal);
        ResultSet rsVerification = null;

        try {
            connection = data.conectar();
            //verificacion si existe
            String sqlVerification = "SELECT * FROM ruta WHERE idRuta = ?";
            PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
            psVerification.setInt(1, idRuta);
            rsVerification = psVerification.executeQuery();

            if (rsVerification.next()) {
                response.getWriter().print("ya existe esta ruta");
            } else {
                String sql = "INSERT INTO ruta(idRuta, cantidadPuntoControl, puntoInicio, puntoFin) VALUES (?,?,?,?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, newRuta.getIdRuta());
                ps.setInt(2, newRuta.getCantidadPuntos());
                ps.setString(3, newRuta.getStart());
                ps.setString(4, newRuta.getEnd());
                ps.executeUpdate();
                response.getWriter().print("ruta registrada");
            }
        } catch (SQLException e) {
            response.getWriter().print("Error inesperado " + e);
        } catch (ClassNotFoundException e) {
            response.getWriter().print("Error inesperado " + e);
        } finally {
            data.desconectar();
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doOptions(request, response);
        JsonObject objeto = gson.fromJson(request.getReader(), JsonObject.class);
        int idRuta = objeto.get("idRuta").getAsInt();
        int cantidadPuntos = objeto.get("cantidadPuntos").getAsInt();
        String puntoInicio = objeto.get("start").getAsString();
        String puntoFinal = objeto.get("end").getAsString();

        ResultSet rsVerification = null;
        try {
            connection = data.conectar();
            //verificacion si existe
            String sqlVerification = "SELECT * FROM ruta WHERE idRuta = ?";
            PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
            psVerification.setInt(1, idRuta);
            rsVerification = psVerification.executeQuery();

            if (rsVerification.next()) {
                String sql = "UPDATE ruta SET cantidadPuntoControl = ?, puntoInicio = ?, puntoFin = ? WHERE idRuta = ?";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, cantidadPuntos);
                ps.setString(2, puntoInicio);
                ps.setString(3, puntoFinal);
                ps.setInt(4, idRuta);
                ps.executeUpdate();
                response.getWriter().print("ruta actualizada");
            } else {
                response.getWriter().print("ruta no existe");
            }
        } catch (SQLException e) {
            response.getWriter().print("Error inesperado " + e);
        } catch (ClassNotFoundException e) {
            response.getWriter().print("Error inesperado " + e);
        } finally {
            data.desconectar();
        }
    }

    @SneakyThrows
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doOptions(request, response);
        int idRuta = Integer.parseInt(request.getParameter("idRuta"));
        ResultSet rsVerification = null;
        try {
            connection = data.conectar();
            String sqlVerification = "SELECT * FROM ruta WHERE idRuta = ?";
            PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
            psVerification.setInt(1, idRuta);
            rsVerification = psVerification.executeQuery();
            if(rsVerification.next()){
                String sqlTrayecto = "SELECT * FROM trayecto WHERE idRuta = ?";
                PreparedStatement psTrayecto = connection.prepareStatement(sqlTrayecto);
                psTrayecto.setInt(1, idRuta);
                rsVerification = psTrayecto.executeQuery();
                if(rsVerification.next()){
                    String sqlTrayectoActivate = "SELECT * FROM trayecto WHERE idRuta = ? AND activate = 1";
                    PreparedStatement psTrayectoActivate = connection.prepareStatement(sqlTrayectoActivate);
                    psTrayectoActivate.setInt(1, idRuta);
                    rsVerification = psTrayectoActivate.executeQuery();
                    if(rsVerification.next()){
                        response.getWriter().print("ruta activa");
                    }else{
                        String sqlTrayectoDelete = "DELETE FROM trayecto WHERE idRuta = ?";
                        PreparedStatement psTrayectoDelete = connection.prepareStatement(sqlTrayectoDelete);
                        psTrayectoDelete.setInt(1, idRuta);
                        psTrayectoDelete.executeUpdate();

                        String sql = "DELETE FROM ruta WHERE idRuta = ?";
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setInt(1, idRuta);
                        ps.executeUpdate();
                        response.getWriter().print("ruta eliminada");
                    }
                }else{
                    String sql = "DELETE FROM ruta WHERE idRuta = ?";
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setInt(1, idRuta);
                    ps.executeUpdate();
                    response.getWriter().print("ruta eliminada");
                }
            }else{
                response.getWriter().print("ruta no existe");
            }

        } catch (SQLException e) {
            response.getWriter().print("Error inesperado " + e);
        } finally {
            if(rsVerification != null){
                rsVerification.close();
            }
            data.desconectar();
        }
    }

    @Override
    public String getServletInfo() {

        return "Short description";
    }// </editor-fold>

}
