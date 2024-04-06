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
import models.cliente;

import javax.json.Json;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/api/cliente")
public class controllerCliente extends HttpServlet {



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
            out.println("<title>Servlet controllerCliente</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet controllerCliente at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "XXXXXXXXXXXXXXXXXXXXX");
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
            ArrayList<cliente> listCliente = new ArrayList<>();
            conexionData data = new conexionData();
            Connection connection = data.conectar();
            try {
                String sql = "select * from cliente";
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    cliente cliente = new cliente();
                    cliente.setNIT(rs.getInt("Nit"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setApellido(rs.getString("apellido"));
                    cliente.setTelefono(rs.getInt("telefono"));
                    cliente.setCuiOperador(rs.getLong("cui_operador"));
                    cliente.setCuiRecepcionista(rs.getLong("cui_recepcionista"));
                    listCliente.add(cliente);

                }
                String json = gson.toJson(listCliente);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
            } catch (SQLException ex) {
                response.getWriter().print("Error en la lectura");
            } finally {
                data.desconectar();
            }
        }
    }


    @SneakyThrows
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doOptions(request, response);
        JsonObject objeto = gson.fromJson(request.getReader(), JsonObject.class);
        int nit = objeto.get("NIT").getAsInt();
        String nombre = objeto.get("nombre").getAsString();
        String apellido = objeto.get("apellido").getAsString();
        int telefono = objeto.get("telefono").getAsInt();
        long cuiOperador = objeto.get("cuiOperador").getAsLong();
        long cuiRecepcionista = objeto.get("cuiRecepcionista").getAsLong();

        cliente newCliente = new cliente();
        newCliente.setNIT(nit);
        newCliente.setNombre(nombre);
        newCliente.setApellido(apellido);
        newCliente.setTelefono(telefono);
        newCliente.setCuiOperador(cuiOperador);
        newCliente.setCuiRecepcionista(cuiRecepcionista);

        ResultSet rsVerification = null;

        try {
            connection = data.conectar();
            String sqlVerification = "SELECT * FROM cliente WHERE nit = ?";
            PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
            psVerification.setInt(1, nit);
            rsVerification = psVerification.executeQuery();

            if(rsVerification.next()){
                response.getWriter().print("Error, el cliente ya existe en la base de datos");

            }else{
                String sqlOperador = "SELECT * FROM operador WHERE cui_operador = ?";
                PreparedStatement psOperador = connection.prepareStatement(sqlOperador);
                psOperador.setLong(1, cuiOperador);
                ResultSet rsOperador = psOperador.executeQuery();

                if( rsOperador.next()){
                    String sqlRecepcionista = "SELECT * FROM recepcionista WHERE cui_recepcionista = ?";
                    PreparedStatement psRecepcionista = connection.prepareStatement(sqlRecepcionista);
                    psRecepcionista.setLong(1, cuiRecepcionista);
                    ResultSet rsRecepcionista = psRecepcionista.executeQuery();

                    if( rsRecepcionista.next()){
                        String sql = "INSERT INTO cliente (nit, nombre, apellido, telefono, cui_operador, cui_recepcionista) VALUES (?, ?, ?, ?, ?, ?)";
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setInt(1, nit);
                        ps.setString(2, nombre);
                        ps.setString(3, apellido);
                        ps.setInt(4, telefono);
                        ps.setLong(5, cuiOperador);
                        ps.setLong(6, cuiRecepcionista);
                        ps.executeUpdate();
                        response.getWriter().print("Cliente creado exitosamente");
                    }else{
                        response.getWriter().print("Error, el recepcionista no existe en la base de datos");
                    }

                }else{
                    response.getWriter().print("Error, el operador no existe en la base de datos");
                }
            }

        } catch (SQLException ex) {
            response.getWriter().print("Error en la creacion, posiblemente hay un dato duplicado" + ex);
    } finally{
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
            int nit = objeto.get("NIT").getAsInt();
            try {
                connection = data.conectar();
                String sql = "DELETE FROM cliente WHERE nit = ?";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, nit);
                ps.executeUpdate();
                response.getWriter().print("SI se elimino");
            } catch (SQLException ex) {
                response.getWriter().print("Error en la eliminacion, probablemente no existe el cliente en la base de datos");
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
    int nit = objeto.get("NIT").getAsInt();
    String nombre = objeto.get("nombre").getAsString();
    String apellido = objeto.get("apellido").getAsString();
    int telefono = objeto.get("telefono").getAsInt();
    long cuiOperador = objeto.get("cuiOperador").getAsLong();
    long cuiRecepcionista = objeto.get("cuiRecepcionista").getAsLong();

    cliente newCliente = new cliente();
    newCliente.setNIT(nit);
    newCliente.setNombre(nombre);
    newCliente.setApellido(apellido);
    newCliente.setTelefono(telefono);
    newCliente.setCuiOperador(cuiOperador);
    newCliente.setCuiRecepcionista(cuiRecepcionista);

    ResultSet rsVerification = null;
    try {
        connection = data.conectar();
       String sqlVerification = "SELECT * FROM cliente WHERE nit = ?";
        PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
        psVerification.setInt(1, nit);
        rsVerification = psVerification.executeQuery();

        if(rsVerification.next()){
            String sqlOperador = "SELECT * FROM operador WHERE cui_operador = ?";
            PreparedStatement psOperador = connection.prepareStatement(sqlOperador);
            psOperador.setLong(1, cuiOperador);
            rsVerification = psOperador.executeQuery();

            if(rsVerification.next()){
                String sqlRecepcionista = "SELECT * FROM recepcionista WHERE cui_recepcionista = ?";
                PreparedStatement psRecepcionista = connection.prepareStatement(sqlRecepcionista);
                psRecepcionista.setLong(1, cuiRecepcionista);
                rsVerification = psRecepcionista.executeQuery();

                if(rsVerification.next()) {
                    String sql = "UPDATE cliente SET  nombre = ?, apellido = ?, telefono = ?, cui_operador = ?, cui_recepcionista = ? WHERE nit = ?";
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setString(1, nombre);
                    ps.setString(2, apellido);  
                    ps.setInt(3, telefono);
                    ps.setLong(4, cuiOperador);
                    ps.setLong(5, cuiRecepcionista);
                    ps.setInt(6, nit);
                    ps.executeUpdate();
                    response.getWriter().print("Cliente actualizado exitosamente");
                }else{
                    response.getWriter().print("Error, el recepcionista no existe en la base de datos");
                }

            }else {
                response.getWriter().print("Error, el operador no existe en la base de datos");
            }
        }else{
            response.getWriter().print("Error, el cliente no existe en la base de datos");
        }

    } catch(SQLException | ClassNotFoundException ex) {
        response.getWriter().print("Error en la actualizacion, posiblemente hay un dato duplicado" +ex);
    }finally{
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
}//final de la clase
