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

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import models.administrador;
import models.operadores;

import static javax.swing.text.html.parser.DTDConstants.ID;

@WebServlet("/api/operador")
public class controllerOperador extends HttpServlet {

    conexionData data = new conexionData();
    Connection connection = null;

    Gson gson = new Gson();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet controllerOperador</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet controllerOperador at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS, PUT");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, X-Auth-Token, Origin, Authorization");
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doOptions(request, response);
        String op = (request.getParameter("op") != null) ? request.getParameter("op") : "list";

        if (op.equals("list")) {
            ArrayList<operadores> listOperador = new ArrayList<>();
            conexionData data = new conexionData();
            Connection connection = data.conectar();

            try {
                String sql = "select * from operador";
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    operadores operadores = new operadores();
                    operadores.setCuiOperador(rs.getLong("cui_operador"));
                    operadores.setNombre(rs.getString("nombre"));
                    operadores.setApellido(rs.getString("apellido"));
                    operadores.setCorreo(rs.getString("correo"));
                    operadores.setContraseña(rs.getString("contraseña"));
                    listOperador.add(operadores);
                }

                // Convertir la lista a JSON
                Gson gson = new Gson();
                String json = gson.toJson(listOperador);

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

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doOptions(request, response);
        JsonObject objeto = gson.fromJson(request.getReader(), JsonObject.class);

        // Acceder a los valores del JSON
        long cuiOperador = objeto.get("cuiOperador").getAsLong();
        String nombre = objeto.get("nombre").getAsString();
        String apellido = objeto.get("apellido").getAsString();
        String correo = objeto.get("correo").getAsString();
        String contraseña = objeto.get("contraseña").getAsString();

        operadores newOperador = new operadores();
        newOperador.setCuiOperador(cuiOperador);
        newOperador.setNombre(nombre);
        newOperador.setApellido(apellido);
        newOperador.setCorreo(correo);
        newOperador.setContraseña(contraseña);


        ResultSet rsVerification = null;

        try {
            connection = data.conectar();
            String sqlVerification = "SELECT * FROM operador WHERE cui_operador = ? OR correo = ?";
            PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
            psVerification.setLong(1, cuiOperador);
            psVerification.setString(2, correo);
            rsVerification = psVerification.executeQuery();

            if (rsVerification.next()) {
                //response.getWriter().print("este cui o correo ya existe en esta base de datos");
                objeto = new JsonObject();
                response.getWriter().print(objeto);
            } else {
                String sqlAdmin = "SELECT * FROM administrador WHERE cui_admin = ? OR correo = ?";
                PreparedStatement psAdmin = connection.prepareStatement(sqlAdmin);
                psAdmin.setLong(1, cuiOperador);
                psAdmin.setString(2, correo);
                rsVerification = psAdmin.executeQuery();

                if (rsVerification.next()) {
                    //response.getWriter().print("Ya existe el cui o correo en la base de datos de administradores");
                    objeto = new JsonObject();
                    response.getWriter().print(objeto);
                } else {
                    String sqlRece = "SELECT * FROM recepcionista WHERE cui_recepcionista = ? OR correo = ?";
                    PreparedStatement psRece = connection.prepareStatement(sqlRece);
                    psRece.setLong(1, cuiOperador);
                    psRece.setString(2, correo);
                    rsVerification = psRece.executeQuery();

                    if (rsVerification.next()) {
                        //response.getWriter().print("este cui o correo ya existe en la tabla recepcionista");
                        objeto = new JsonObject();
                        response.getWriter().print(objeto);
                    } else {
                        String sql = "INSERT INTO operador(cui_operador, nombre, apellido, correo, contraseña) VALUES (?,?,?,?,?)";
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setLong(1, newOperador.getCuiOperador());
                        ps.setString(2, newOperador.getNombre());
                        ps.setString(3, newOperador.getApellido());
                        ps.setString(4, newOperador.getCorreo());
                        ps.setString(5, newOperador.getContraseña());
                        ps.executeUpdate();
                        response.getWriter().print("SI se actualizo" + newOperador);
                    }
                }
            }
        } catch (SQLException ex) {
            objeto = new JsonObject();
            response.getWriter().print(objeto);
            System.out.println("Correo duplicado " + ex);
        } catch (ClassNotFoundException ex) {
            response.getWriter().print("pura mamada del java");
        } finally {
            if (rsVerification != null) {
                rsVerification.close();
            }
            data.desconectar();
        }
    }

    @SneakyThrows
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doOptions(request, response);

        JsonObject objeto = gson.fromJson(request.getReader(), JsonObject.class);

        long cuiOperador = objeto.get("cuiOperador").getAsLong();
        try {
            connection = data.conectar();
            String sql = "DELETE FROM operador WHERE cui_operador = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, cuiOperador);
            ps.executeUpdate();
            response.getWriter().print("SI se elimino");
        } catch (SQLException ex) {
            response.getWriter().print("Este dato es muy probable que ya ese enlazado " + ex);
        } catch (ClassNotFoundException e) {
            response.getWriter().print("try de fount");
        } finally {
            data.desconectar();
        }

    }

    @SneakyThrows
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doOptions(request, response);
        Gson gson = new Gson();
        JsonObject objeto = gson.fromJson(request.getReader(), JsonObject.class);

        long cuiOperador = objeto.get("cuiOperador").getAsLong();
        String nombre = objeto.get("nombre").getAsString();
        String apellido = objeto.get("apellido").getAsString();
        String correo = objeto.get("correo").getAsString();
        String contraseña = objeto.get("contraseña").getAsString();

        ResultSet rsVerification = null;
        try {
            connection = data.conectar();
            //verificacion si existe
            String sqlVerification = "SELECT * FROM operador WHERE cui_operador = ?";
            PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
            psVerification.setLong(1, cuiOperador);
            rsVerification = psVerification.executeQuery();

            if (rsVerification.next()) {

                String sql = "UPDATE operador SET  nombre = ?, apellido = ?, correo = ?, contraseña = ? WHERE cui_operador = ?";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, nombre);
                ps.setString(2, apellido);
                ps.setString(3, correo);
                ps.setString(4, contraseña);
                ps.setLong(5, cuiOperador);
                ps.executeUpdate();
                response.getWriter().print("Todo salio bien");

            } else {
                objeto = new JsonObject();
                response.getWriter().print(objeto);
                //response.getWriter().print("No existe el cui");
            }

        } catch (SQLException ex) {
            objeto = new JsonObject();
            response.getWriter().print(objeto);
            //response.getWriter().print("No se pudo realizar la actualizacion, muy probablemente tenga datos repetidos " + ex);
            System.out.println("No se pudo realizar la actualizacion, muy probablemente tenga datos repetidos " + ex);
        } catch (ClassNotFoundException ex) {
            response.getWriter().print("QUe hubo");
        } finally {
            if (rsVerification != null) {
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
