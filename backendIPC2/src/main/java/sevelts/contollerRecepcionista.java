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
import models.recepcionista;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/api/recepcion")
public class contollerRecepcionista extends HttpServlet {
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
            out.println("<title>Servlet contollerRecepcionista</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet contollerRecepcionista at " + request.getContextPath() + "</h1>");
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
            ArrayList<recepcionista> listRecepcionista = new ArrayList<>();
            conexionData data = new conexionData();
            Connection connection = data.conectar();

            try {
                String sql = "select * from recepcionista";
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    recepcionista recepcionista = new recepcionista();
                    recepcionista.setCuiRecepcionista(rs.getLong("cui_recepcionista"));
                    recepcionista.setNombre(rs.getString("nombre"));
                    recepcionista.setApellido(rs.getString("apellido"));
                    recepcionista.setCorreo(rs.getString("correo"));
                    recepcionista.setContraseña(rs.getString("contraseña"));
                    recepcionista.setCuiOperador(rs.getLong("cui_operador"));
                    listRecepcionista.add(recepcionista);
                }
                String json = gson.toJson(listRecepcionista);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
            } catch (SQLException e) {
                response.getWriter().print("Error en la lista" + e);
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

        // Acceder a los valores del JSON
        long cuiRecepcionista = objeto.get("cuiRecepcionista").getAsLong();
        String nombre = objeto.get("nombre").getAsString();
        String apellido = objeto.get("apellido").getAsString();
        String correo = objeto.get("correo").getAsString();
        String contraseña = objeto.get("contraseña").getAsString();
        long cuiOperador = objeto.get("cuiOperador").getAsLong();

        // Crear un objeto recepcionista y agregarlo a la lista
        recepcionista newRece = new recepcionista();
        newRece.setCuiRecepcionista(cuiRecepcionista);
        newRece.setNombre(nombre);
        newRece.setApellido(apellido);
        newRece.setCorreo(correo);
        newRece.setContraseña(contraseña);
        newRece.setCuiOperador(cuiOperador);

        ResultSet rsVerification = null;
        try {
            connection = data.conectar();
            String sqlVerification = "SELECT * FROM recepcionista WHERE cui_recepcionista = ? OR correo = ?";
            PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
            psVerification.setLong(1, cuiRecepcionista);
            psVerification.setString(2, correo);
            rsVerification = psVerification.executeQuery();
            if (rsVerification.next()) {
                //response.getWriter().print("ya existe este cui o correo en recepcionista");
                objeto = new JsonObject();
                response.getWriter().print(objeto);
            } else {
                String sqlAdmin = "SELECT * FROM administrador WHERE cui_admin = ? OR correo = ?";
                PreparedStatement psAdmin = connection.prepareStatement(sqlAdmin);
                psAdmin.setLong(1, cuiRecepcionista);
                psAdmin.setString(2, correo);
                rsVerification = psAdmin.executeQuery();
                if (rsVerification.next()) {
                    //response.getWriter().print("ya existe este cui o correo en admin");
                    objeto = new JsonObject();
                    response.getWriter().print(objeto);
                } else {
                    String sqlOperador = "SELECT * FROM operador WHERE cui_operador = ? OR correo = ?";
                    PreparedStatement psOperador = connection.prepareStatement(sqlOperador);
                    psOperador.setLong(1, cuiOperador);
                    psOperador.setString(2, correo);
                    rsVerification = psOperador.executeQuery();
                    if (rsVerification.next()) {
                        long cuiOperadorVerification = rsVerification.getLong("cui_operador");
                        if (cuiRecepcionista != cuiOperadorVerification) {

                            String sql = "INSERT INTO recepcionista (cui_recepcionista, nombre, apellido, correo, contraseña, cui_operador) VALUES (?, ?, ?, ?, ?, ?)";
                            PreparedStatement ps = connection.prepareStatement(sql);
                            ps.setLong(1, cuiRecepcionista);
                            ps.setString(2, nombre);
                            ps.setString(3, apellido);
                            ps.setString(4, correo);
                            ps.setString(5, contraseña);
                            ps.setLong(6, cuiOperador);
                            ps.executeUpdate();
                            response.getWriter().print("Todo salio bien");
                        } else {
                            //response.getWriter().print("no existe este operador");
                            objeto = new JsonObject();
                            response.getWriter().print(objeto);
                        }
                    } else {
                        //response.getWriter().print("Este cui o correo ya existe en operador");
                        objeto = new JsonObject();
                        response.getWriter().print(objeto);
                    }
                }
            }
        } catch (SQLException e) {
            response.getWriter().print("Error en la lista" + e);
        } catch (ClassNotFoundException e) {
            response.getWriter().print("Error inesperado " + e);
        } finally {
            if (rsVerification != null) {
                rsVerification.close();
            }
            data.desconectar();
        }

    }//fin de doPos

    @SneakyThrows
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doOptions(request, response);
        JsonObject objeto = gson.fromJson(request.getReader(), JsonObject.class);

        long cuiRecepcionista = objeto.get("cuiRecepcionista").getAsLong();
        connection = data.conectar();
        try {
            // Verificar si hay registros en la tabla cliente que dependen del recepcionista a eliminar
            String sqlVerification = "SELECT * FROM cliente WHERE cui_recepcionista = ?";
            PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
            psVerification.setLong(1, cuiRecepcionista);
            ResultSet rsVerification = psVerification.executeQuery();
            if (rsVerification.next()) {
                // Si hay registros en la tabla cliente, no se puede eliminar el recepcionista
                response.getWriter().print("No se puede eliminar el recepcionista porque hay registros en la tabla cliente que dependen de él");
            } else {
                // No hay registros en la tabla cliente que dependan del recepcionista, proceder con la eliminación
                String sql = "DELETE FROM recepcionista WHERE cui_recepcionista = ?";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setLong(1, cuiRecepcionista);
                ps.executeUpdate();
                response.getWriter().print("Recepcionista eliminado");
            }
        } catch (SQLException e) {
            response.getWriter().print("Error inesperado " + e);
        } finally {
            data.desconectar();
        }
    }


    @SneakyThrows
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doOptions(request, response);
        JsonObject objeto = gson.fromJson(request.getReader(), JsonObject.class);

        long cuiRecepcionista = objeto.get("cuiRecepcionista").getAsLong();
        String nombre = objeto.get("nombre").getAsString();
        String apellido = objeto.get("apellido").getAsString();
        String correo = objeto.get("correo").getAsString();
        String contraseña = objeto.get("contraseña").getAsString();
        long nuevoCuiOperador = objeto.get("cuiOperador").getAsLong();

        ResultSet rsVerification = null;
        try {
            connection = data.conectar();
            // Verificar si el nuevo cuiOperador existe en la tabla operador
            String sqlVerification = "SELECT * FROM operador WHERE cui_operador = ?";
            PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
            psVerification.setLong(1, nuevoCuiOperador);
            rsVerification = psVerification.executeQuery();

            if (rsVerification.next()) {
                String sqlUpdate = "UPDATE recepcionista SET nombre = ?, apellido = ?, correo = ?, contraseña = ?, cui_operador = ? WHERE cui_recepcionista = ?";
                PreparedStatement ps = connection.prepareStatement(sqlUpdate);
                ps.setString(1, nombre);
                ps.setString(2, apellido);
                ps.setString(3, correo);
                ps.setString(4, contraseña);
                ps.setLong(5, nuevoCuiOperador);
                ps.setLong(6, cuiRecepcionista);
                ps.executeUpdate();
                response.getWriter().print("recepcionista actualizado");
            } else {
                // El nuevo cuiOperador no existe en la tabla operador
                response.getWriter().print("El nuevo cuiOperador no existe en la tabla operador");
            }
        } catch (SQLException ex) {
            response.getWriter().print("Error Este cui Operador existe en otra dependencias " + ex);
        } finally {
            if (rsVerification != null) {
                rsVerification.close();
            }
            data.desconectar();
        }
    }//fin del put



    @Override
    public String getServletInfo() {

        return "Short description";
    }// </editor-fold>
}//final de clase
