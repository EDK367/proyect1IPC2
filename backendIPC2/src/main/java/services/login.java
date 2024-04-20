package services;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataBase.conexionData;
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

//clase que se encarga de evaluar el login de los usuarios
@WebServlet("/api/login")
public class login extends HttpServlet {
    conexionData data = new conexionData();
    Connection connection = null;
    Gson gson = new Gson();
    ResultSet rs = null;
    JsonObject objetoLogin = new JsonObject();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Login</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet login at " + request.getContextPath() + "</h1>");
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

    //metodo post para detectar login
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doOptions(request, response);
        JsonObject objeto = gson.fromJson(request.getReader(), JsonObject.class);

        //formato del json que viene
        long identificador = objeto.get("identificador").getAsLong();
        String password = objeto.get("password").getAsString();

        //validaciones
        try {
            //veriifca si el existe en la tabla admin
            if (validarAdmin(identificador, password)) {
                connection = data.conectar();
                String admin = "SELECT * FROM administrador WHERE cui_admin = ?";
                PreparedStatement ps = connection.prepareStatement(admin);
                ps.setLong(1, identificador);
                rs = ps.executeQuery();
                //selecciona el admin y lo manda en un json
                if (rs.next()) {
                    objetoLogin.addProperty("cuiAdmin", rs.getLong("cui_admin"));
                    objetoLogin.addProperty("nombre", rs.getString("nombre"));
                    objetoLogin.addProperty("apellido", rs.getString("apellido"));
                    objetoLogin.addProperty("correo", rs.getString("correo"));
                    objetoLogin.addProperty("contraseña", rs.getString("contraseña"));
                    System.out.println(objetoLogin);
                    response.getWriter().print(gson.toJson(objetoLogin));
                    objetoLogin = new JsonObject();
                }
                //valida el operador si existe
            } else if (validarOperador(identificador, password)) {
                connection = data.conectar();
                String operador = "SELECT * FROM operador WHERE cui_operador = ?";
                PreparedStatement ps = connection.prepareStatement(operador);
                ps.setLong(1, identificador);
                rs = ps.executeQuery();
                if (rs.next()) {
                    //verifica que exista y si es asi responde con el JSON
                    objetoLogin.addProperty("cuiOperador", rs.getLong("cui_operador"));
                    objetoLogin.addProperty("nombre", rs.getString("nombre"));
                    objetoLogin.addProperty("apellido", rs.getString("apellido"));
                    objetoLogin.addProperty("correo", rs.getString("correo"));
                    objetoLogin.addProperty("contraseña", rs.getString("contraseña"));
                    System.out.println(objetoLogin);
                    response.getWriter().print(gson.toJson(objetoLogin));
                    objetoLogin = new JsonObject();
                }
                //validar recepcionista
            } else if (validarRecepcionista(identificador, password)) {
                connection = data.conectar();
                String recepcionista = "SELECT * FROM recepcionista WHERE cui_recepcionista = ?";
                PreparedStatement ps = connection.prepareStatement(recepcionista);
                ps.setLong(1, identificador);
                rs = ps.executeQuery();
                if (rs.next()) {
                    //valida y obtiene el Json
                    objetoLogin.addProperty("cuiRecepcionista", rs.getLong("cui_recepcionista"));
                    objetoLogin.addProperty("nombre", rs.getString("nombre"));
                    objetoLogin.addProperty("apellido", rs.getString("apellido"));
                    objetoLogin.addProperty("correo", rs.getString("correo"));
                    objetoLogin.addProperty("contraseña", rs.getString("contraseña"));
                    System.out.println(objetoLogin);
                    response.getWriter().print(gson.toJson(objetoLogin));
                    objetoLogin = new JsonObject();
                }
                //si no cumple con ninguna de estas tres es que el usuario no existe por lo tanto manda
                //un JSON vacio para decir que no existe
            } else {
                response.getWriter().print(objetoLogin);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            response.getWriter().print("Error" + ex);
        } finally {
            data.desconectar();
        }
    }

    //verifica que el usuario exista en admin
    private boolean validarAdmin(long identificador, String password) {
        try {
            connection = data.conectar();
            String sql = "SELECT 1 FROM administrador WHERE cui_admin = ? AND contraseña = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, identificador);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Si valido el admin");
                return true;
            } else {
                System.out.println("hubo un fallo en la validacion");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("Error en la validacion admin");
        } finally {
            data.desconectar();
        }
        return false;
    }

    //Validar el usuario operador
    private boolean validarOperador(long identificador, String password) {
        try {
            connection = data.conectar();
            String sql = "SELECT 1 FROM operador WHERE cui_operador = ? AND contraseña = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, identificador);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Si valido el operador");
                return true;
            } else {
                System.out.println("hubo un fallo");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("Error en la validacion operador");
        } finally {
            data.desconectar();
        }
        return false;
    }

    //validar el usuario recepcionista
    private boolean validarRecepcionista(long identificador, String password) {
        try {
            connection = data.conectar();
            String sql = "SELECT 1 FROM recepcionista WHERE cui_recepcionista = ? AND contraseña = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setLong(1, identificador);
            ps.setString(2, password);
            rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Si valido el recepcionista");
                return true;
            } else {
                System.out.println("hubo un fallo");
            }
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("Error en la validacion recepcionista");
        } finally {
            data.desconectar();
        }
        return false;
    }


    @Override
    public String getServletInfo() {
        return "Short description";
    }

}//end of class