package sevelts;

import dataBase.conexionData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import models.administrador;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

//@WebServlet(name = "controllerPrincipal", urlPatterns = {"/controllerPrincipal"})
@WebServlet("/api/admin")
public class controllerPrincipal extends HttpServlet {

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
            out.println("<h1>Servlet controllerPrincipal at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    @Override
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
            ArrayList<administrador> listAdmin = new ArrayList<>();
            conexionData data = new conexionData();
            Connection connection = data.conectar();
            
            try {
                String sql = "select * from administrador";
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                
                while (rs.next()) {
                    administrador admin = new administrador();
                    admin.setCuiAdmin(rs.getLong("cui_admin"));
                    admin.setNombre(rs.getString("nombre"));
                    admin.setApellido(rs.getString("apellido"));
                    admin.setCorreo(rs.getString("correo"));
                    admin.setContraseña(rs.getString("contraseña"));
                    listAdmin.add(admin);
                }

                // Convertir la lista a JSON
                Gson gson = new Gson();
                String json = gson.toJson(listAdmin);
                
                // Responde con el Json
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
            } catch (SQLException ex) {
                response.getWriter().print("Error en la lista");
            } finally {
                data.desconectar();
            }
        }
    }

    //creacion de nuevos usuarios
    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doOptions(request, response);
        Gson gson = new Gson();
        JsonObject objeto = gson.fromJson(request.getReader(), JsonObject.class);

        // Acceder a los valores del JSON
        long cuiAdmin = objeto.get("cuiAdmin").getAsLong();
        String nombre = objeto.get("nombre").getAsString();
        String apellido = objeto.get("apellido").getAsString();
        String correo = objeto.get("correo").getAsString();
        String contraseña = objeto.get("contraseña").getAsString();

        administrador newAdmin = new administrador();
        newAdmin.setCuiAdmin(cuiAdmin);
        newAdmin.setNombre(nombre);
        newAdmin.setApellido(apellido);
        newAdmin.setCorreo(correo);
        newAdmin.setContraseña(contraseña);
        //aca se guarda con mysql
        conexionData data = new conexionData();
        Connection connection = null;

        ResultSet rsVerification = null;
        try {
            connection = data.conectar();

            String sqlVerification = "SELECT * FROM administrador WHERE cui_admin = ? OR correo = ?";
            PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
            psVerification.setLong(1,  cuiAdmin);
            psVerification.setString(2, correo);
            rsVerification = psVerification.executeQuery();

            if (rsVerification.next()) {
                //response.getWriter().print("este cui o correo ya existe en admin ");
                objeto = new JsonObject();
                response.getWriter().print(objeto);
            } else {
                String sqlOperador = "SELECT * FROM operador WHERE cui_operador = ? OR correo = ?";
                PreparedStatement psOperador = connection.prepareStatement(sqlOperador);
                psOperador.setLong(1,  cuiAdmin);
                psOperador.setString(2,  correo);
                rsVerification = psOperador.executeQuery();

                if(rsVerification.next()){
                   // response.getWriter().print("Este cui o correo ya existe en operador ");
                    objeto = new JsonObject();

                    response.getWriter().print(objeto);
                }else {
                    String sqlRecepcion = "SELECT * FROM recepcionista WHERE cui_recepcionista = ? OR correo = ?";
                    PreparedStatement psRece = connection.prepareStatement(sqlRecepcion);
                    psRece.setLong(1,  cuiAdmin);
                    psRece.setString(2,  correo);
                    rsVerification = psRece.executeQuery();

                    if(rsVerification.next()){
                       // response.getWriter().print("Este cui o correo ya existe en recepcionista ");
                        objeto = new JsonObject();
                        response.getWriter().print(objeto);

                    }else {
                        String sql = "INSERT INTO administrador(cui_admin, nombre, apellido, correo, contraseña) VALUES (?,?,?,?,?)";
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setLong(1, newAdmin.getCuiAdmin());
                        ps.setString(2, newAdmin.getNombre());
                        ps.setString(3, newAdmin.getApellido());
                        ps.setString(4, newAdmin.getCorreo());
                        ps.setString(5, newAdmin.getContraseña());
                        ps.executeUpdate();
                        //response.getWriter().print("SI se actualizo ");
                        objeto = new JsonObject();
                        objeto.addProperty("cuiAdmin", newAdmin.getCuiAdmin());
                        response.getWriter().print(objeto);
                        
                    }
                }
            }
        } catch (SQLException ex) {
            response.getWriter().print("El cui o el correo esta duplicado" +ex);
        } catch (ClassNotFoundException ex) {
            response.getWriter().print("pura mamada del java");
        } finally {
            if(rsVerification != null){
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
        Gson gson = new Gson();
        JsonObject objeto = gson.fromJson(request.getReader(), JsonObject.class);
        
        int cuiAdmin = objeto.get("cuiAdmin").getAsInt();
        
        conexionData data = new conexionData();
        Connection connection = null;
        
        try {
            connection = data.conectar();
            String sql = "DELETE FROM administrador WHERE cui_admin = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, cuiAdmin);
            ps.executeUpdate();
            response.getWriter().print("SI se elimino");
        } catch (SQLException ex) {
            response.getWriter().print("no se pudo eliminar " + ex);
        } catch (ClassNotFoundException e) {
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
        Gson gson = new Gson();
        JsonObject objeto = gson.fromJson(request.getReader(), JsonObject.class);

        long cuiAdmin = objeto.get("cuiAdmin").getAsLong();
        String nombre = objeto.get("nombre").getAsString();
        String apellido = objeto.get("apellido").getAsString();
        String correo = objeto.get("correo").getAsString();
        String contraseña = objeto.get("contraseña").getAsString();

        conexionData data = new conexionData();
        Connection connection = null;

        try {
            connection = data.conectar();
            //verificacion si existe
            String sqlVerification = "SELECT * FROM administrador WHERE cui_admin = ?";
            PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
            psVerification.setLong(1, cuiAdmin);
            ResultSet rs = psVerification.executeQuery();

            if(rs.next()){
                String sql = "UPDATE administrador SET nombre = ?, apellido = ?, correo = ?, contraseña = ? WHERE cui_admin = ?";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, nombre);
                ps.setString(2, apellido);
                ps.setString(3, correo);
                ps.setString(4, contraseña);
                ps.setLong(5, cuiAdmin);
                ps.executeUpdate();
                objeto = new JsonObject();
                objeto.addProperty("cuiAdmin", cuiAdmin);
                response.getWriter().print(objeto);
            }else{
                //response.getWriter().print("No existe el cui");
                objeto = new JsonObject();
                response.getWriter().print(objeto);
            }

        } catch (SQLException ex) {
            objeto = new JsonObject();
            response.getWriter().print(objeto);
            System.out.println("No se pudo realizar la actualizacion, muy probablemente tenga datos repetidos \" + ex");
        } catch (ClassNotFoundException ex) {
            response.getWriter().print("QUe hubo");
        } finally {
            data.desconectar();
        }
    }

    @SneakyThrows
    @Override
    public String getServletInfo() {
        return "Short description";
    }
    
}
