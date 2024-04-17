package sevelts;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataBase.conexionData;
import lombok.SneakyThrows;
import models.tarifaGlobal;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/api/tarifaGlobal")
public class controllerTarifaG extends HttpServlet {


    conexionData data = new conexionData();
    Connection connection = null;
    Gson gson = new Gson();
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet controllerTarifaG</title>");
            out.println("</head>");
            out.println("<body>");
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
                    ArrayList<tarifaGlobal> listTarifaGlobal = new ArrayList<>();
                    connection = data.conectar();
                    try {
                        String sql = "select * from tarifaglobal";
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            tarifaGlobal global = new tarifaGlobal();
                            global.setTarifaGlobalId(rs.getInt("IdTarifaGloblal"));
                            global.setTarifaG(rs.getFloat("tarifaGlobal"));
                            global.setFechaInicio(rs.getString("fechaCreacion"));
                            global.setCuiAdmin(rs.getLong("cui_admin"));
                            listTarifaGlobal.add(global);
                        }
                        String json = gson.toJson(listTarifaGlobal);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        PrintWriter out = response.getWriter();
                        out.print(json);
                        out.flush();
                    }catch(SQLException e){
                        response.getWriter().print("Erro en la lectura" + e);
                    }finally {
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

        float tarifaGlobal = objeto.get("tarifaG").getAsFloat();
        String fechaCreacion = objeto.get("fechaInicio").getAsString();

        long cuiAdmin = objeto.get("cuiAdmin").getAsLong();

        try {
            connection = data.conectar();
            String sqlVerificationAdmin = "SELECT * FROM administrador WHERE cui_admin = ?";
            PreparedStatement psVerificationAdmin = connection.prepareStatement(sqlVerificationAdmin);
            psVerificationAdmin.setLong(1, cuiAdmin);
            ResultSet rsVerification = psVerificationAdmin.executeQuery();
            if (rsVerification.next()) {
                // Usar la sentencia SQL con parámetros para evitar inyección SQL
                String sql = "INSERT INTO tarifaglobal(tarifaGlobal, fechaCreacion, cui_admin) VALUES (?, STR_TO_DATE(?, '%Y-%m-%d'), ?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setFloat(1, tarifaGlobal);
                ps.setString(2, fechaCreacion);
                ps.setLong(3, cuiAdmin);
                ps.executeUpdate();
                response.getWriter().print(objeto);
            } else {
                objeto = new JsonObject();
                response.getWriter().print(objeto);
            }
        } catch (SQLException e) {
            objeto = new JsonObject();
            response.getWriter().print(objeto);
            //response.getWriter().print("Error en la inserción: " + e.getMessage());
            System.out.println("Error en la inserción: " + e.getMessage());
        } finally {
            data.desconectar();
        }
    }


protected void doDelete(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    doOptions(request, response);
    JsonObject objeto = gson.fromJson(request.getReader(), JsonObject.class);
    int IdTarifaGloblal = objeto.get("tarifaGlobalId").getAsInt();
    try {
        connection = data.conectar();
        String sql = "DELETE FROM tarifaglobal WHERE IdTarifaGloblal = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, IdTarifaGloblal);
        ps.executeUpdate();

        response.getWriter().print("Se ha eliminado el dato " + IdTarifaGloblal);
    } catch (SQLException | ClassNotFoundException ex) {
        response.getWriter().print("Error al eliminar " + ex);
    } finally {
        data.desconectar();
    }
        }
//    @SneakyThrows
//    @Override
//            protected void doPut(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//                    doOptions(request, response);
//                JsonObject objeto = gson.fromJson(request.getReader(), JsonObject.class);
//
//                int IdTarifaGloblal = objeto.get("IdTarifaGloblal").getAsInt();
//                float tarifaGlobal = objeto.get("tarifaG").getAsFloat();
//                String fechaCreacion = objeto.get("fechaInicio").getAsString();
//                long cuiAdmin = objeto.get("cuiAdmin").getAsLong();
//
//                tarifaGlobal newGlobal = new tarifaGlobal();
//                newGlobal.setTarifaGlobalId(IdTarifaGloblal);
//                newGlobal.setTarifaG(tarifaGlobal);
//                newGlobal.setFechaInicio(fechaCreacion);
//                newGlobal.setCuiAdmin(cuiAdmin);
//                    ResultSet rsVerification = null;
//                try{
//                    connection = data.conectar();
//
//                    String sqlVerification = "SELECT * FROM administrador WHERE cui_admin = ?";
//                    PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
//                    psVerification.setLong(1, cuiAdmin);
//                    rsVerification = psVerification.executeQuery();
//                    if(!rsVerification.next()){
//                        response.getWriter().print("No existe el admin en la base de datos " + cuiAdmin);
//                    }else {
//                        String sql = "UPDATE tarifaglobal SET tarifaGlobal = ?, fechaCreacion = ?, cui_admin = ? WHERE IdTarifaGloblal = ?";
//                        PreparedStatement ps = connection.prepareStatement(sql);
//                        ps.setFloat(1, (float) newGlobal.getTarifaG());
//                        ps.setString(2, newGlobal.getFechaInicio());
//                        ps.setLong(3, newGlobal.getCuiAdmin());
//                        ps.setInt(4, newGlobal.getTarifaGlobalId());
//                        ps.executeUpdate();
//                        response.getWriter().print("Tarifa global actualizada");
//                    }
//                }catch(SQLException | ClassNotFoundException e){
//                    response.getWriter().print("Error al actualizar " + e);
//                }finally {
//                    if(rsVerification != null){
//                        rsVerification.close();
//                    }
//                    data.desconectar();
//                }
//            }

    @Override
    public String getServletInfo() {

        return "Short description";
    }// </editor-fold>

}//fin de la clase
