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

@WebServlet("/api/operador")
public class controllerOperador extends HttpServlet {

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
            out.println("<title>Servlet controllerOperador</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet controllerOperador at " + request.getContextPath() + "</h1>");
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
        String op = (request.getParameter("op") != null) ? request.getParameter("op") : "list";

        if (op.equals("list")) {
            ArrayList<operadores> listOP = new ArrayList<>();

            connection = data.conectar();
            try {
                String sql = "select * from operador";
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    operadores operador = new operadores();
                    operador.setCuiOperador(rs.getInt("cui_operador"));
                    operador.setNombre(rs.getString("nombre"));
                    operador.setApellido(rs.getString("apellido"));
                    operador.setCorreo(rs.getString("correo"));
                    operador.setContraseña(rs.getString("contraseña"));

                    listOP.add(operador);
                }

                // Convertir la lista a JSON
                String json = gson.toJson(listOP);

                // Responde con el Json
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
            } catch (SQLException ex) {
                response.getWriter().print("Error en la lista" + ex);
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
        Gson gson = new Gson();
        JsonObject objeto = gson.fromJson(request.getReader(), JsonObject.class);

        // Acceder a los valores del JSON
        int cuiOperador = objeto.get("cuiOperador").getAsInt();

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
            String sqlVerification = "SELECT * FROM administrador WHERE cui_admin = ?";
            PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
            psVerification.setInt(1, cuiOperador);
            rsVerification = psVerification.executeQuery();

            if (rsVerification.next()) {
                response.getWriter().print(rsVerification + "si existe");
            } else {

                String sql = "INSERT INTO operador(cui_operador, nombre, apellido, correo, contraseña) VALUES (?,?,?,?,?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, newOperador.getCuiOperador());
                ps.setString(2, newOperador.getNombre());
                ps.setString(3, newOperador.getApellido());
                ps.setString(4, newOperador.getCorreo());
                ps.setString(5, newOperador.getContraseña());
                ps.executeUpdate();
                response.getWriter().print("SI se actualizo" + newOperador);
            }
        } catch (SQLException ex) {
            response.getWriter().print("No se pudo crear por saber que error ");
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
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doOptions(request, response);
        Gson gson = new Gson();
        JsonObject objeto = gson.fromJson(request.getReader(), JsonObject.class);
        
        int cuiOperador = objeto.get("cuiOperador").getAsInt();
        try {
            connection = data.conectar();
            String sql = "DELETE FROM operador WHERE cui_operador = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, cuiOperador);
            ps.executeUpdate();
            response.getWriter().print("SI se elimino");
        } catch (SQLException ex) {
            response.getWriter().print("que hubo");
        } catch (ClassNotFoundException e) {
            response.getWriter().print("try de fount");
        } finally {
            data.desconectar();
        }
        
    }
    
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
