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

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            } catch (SQLException ex) {
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

        try {
            connection = data.conectar();
            String sqlVerification = "SELECT * FROM operador WHERE cui_operador = ?";
            PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
            psVerification.setLong(1, cuiOperador);
            rsVerification = psVerification.executeQuery();
            if (rsVerification.next()) {
                String sqlVerificationBodega = "SELECT * FROM bodega WHERE NoBodega = ?";
                PreparedStatement psVerificationBodega = connection.prepareStatement(sqlVerificationBodega);
                psVerificationBodega.setInt(1, idControl);
                rsVerification = psVerificationBodega.executeQuery();
                if (rsVerification.next()) {
                    objeto = new JsonObject();
                    response.getWriter().print(objeto);
                    System.out.println("No se puede insertar el punto de control " + idControl + " ya que existe una bodega enlazada");
                } else {
                    String sqlConnection = "SELECT * FROM punto_control";
                    PreparedStatement psConnection = connection.prepareStatement(sqlConnection);
                    psConnection.executeQuery();

                    String sqlBodega = "INSERT INTO bodega (NoBodega, IDControl) VALUES (?, ?)";
                    PreparedStatement psBodega = connection.prepareStatement(sqlBodega);
                    psBodega.setInt(1, idControl);
                    psBodega.setLong(2, idControl);


                    String sql = "INSERT INTO punto_control (idControl, nombreControl, cui_operador) VALUES (?, ?, ?)";
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setInt(1, idControl);
                    ps.setString(2, nombre);
                    ps.setLong(3, cuiOperador);
                    ps.executeUpdate();
                    psBodega.executeUpdate();

                    objeto = new JsonObject();
                    objeto.addProperty("IdControl", idControl);
                    objeto.addProperty("nombre", nombre);
                    objeto.addProperty("cuiOperador", cuiOperador);
                    response.getWriter().print(objeto);

                }
            } else {
                objeto = new JsonObject();
                response.getWriter().print(objeto);
                //response.getWriter().print("No existe el operador en la base de datos " + cuiOperador);
            }
        } catch (SQLException ex) {
            //response.getWriter().print("Duplicate data " + ex);
            objeto = new JsonObject();
            response.getWriter().print(objeto);
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
        JsonObject objeto = gson.fromJson(request.getReader(), JsonObject.class);
        int IdControl = objeto.get("IdControl").getAsInt();
        try {
            connection = data.conectar();
            String sqlBodega = "DELETE FROM bodega WHERE NoBodega = ?";
            PreparedStatement psBodega = connection.prepareStatement(sqlBodega);
            psBodega.setInt(1, IdControl);
            String sql = "DELETE FROM punto_control WHERE IdControl = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, IdControl);
            psBodega.executeUpdate();

            ps.executeUpdate();

            response.getWriter().print("Se ha eliminado el dato " + IdControl);
        } catch (SQLException ex) {
            response.getWriter().print("Error al eliminar " + ex);
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

        int IdControl = objeto.get("IdControl").getAsInt();
        String nombre = objeto.get("nombre").getAsString();
        long cuiOperador = objeto.get("cuiOperador").getAsLong();

        try {
            connection = data.conectar();
            String sqlVerification = "SELECT * FROM operador WHERE cui_operador = ?";
            PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
            psVerification.setLong(1, cuiOperador);
            ResultSet rsVerification = psVerification.executeQuery();

            if (rsVerification.next()) {
                String sql = "UPDATE punto_control SET  nombreControl = ?, cui_operador = ? WHERE IdControl = ?";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, nombre);
                ps.setLong(2, cuiOperador);
                ps.setInt(3, IdControl);
                ps.executeUpdate();
                //response.getWriter().print("SI se actualizo " + IdControl);
                objeto = new JsonObject();
                objeto.addProperty("IdControl", IdControl);
                response.getWriter().print(objeto);

            } else {
                objeto = new JsonObject();
                response.getWriter().print(objeto);
                // response.getWriter().print("No existe el operador en la base de datos " + cuiOperador);
            }
        } catch (SQLException ex) {
            objeto = new JsonObject();
            response.getWriter().print(objeto);
            //response.getWriter().print("Error al actualizar " + ex);
        } finally {
            data.desconectar();
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
