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
import models.tarifaLocal;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/api/tarifaLocal")
public class controllerTarifaLocal extends HttpServlet {

    conexionData data = new conexionData();
    Gson gson = new Gson();
    Connection connection = null;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet controllerTarifaLocal</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet controllerTarifaLocal at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "XXXXXXXXXXXXXXXXXXXXX");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS, PUT");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, X-Auth-Token, Origin, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }

    @SneakyThrows
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doOptions(request, response);
        String op = (request.getParameter("op") != null) ? request.getParameter("op") : "list";
        if (op.equals("list")) {
            ArrayList<tarifaLocal> listTarifaLocal = new ArrayList<>();
            conexionData data = new conexionData();
            Connection connection = data.conectar();
            try {
                String sql = "select * from tarifaLocal";
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    tarifaLocal tarifaLocal = new tarifaLocal();
                    tarifaLocal.setIdTarifa(rs.getInt("IdTarifa"));
                    tarifaLocal.setTarifaLocal(rs.getFloat("tarifaLocal"));
                    tarifaLocal.setIDControl(rs.getInt("IDControl"));
                    tarifaLocal.setCuiOperador(rs.getLong("cui_operador"));
                    listTarifaLocal.add(tarifaLocal);
                }
                String json = gson.toJson(listTarifaLocal);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
            } catch (SQLException e) {

            } finally {
                data.desconectar();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doOptions(request, response);
        JsonObject objeto = gson.fromJson(request.getReader(), JsonObject.class);
        float tarifaLocal = objeto.get("tarifaLocal").getAsFloat();
        int IDControl = objeto.get("IDControl").getAsInt();
        long cuiOperador = objeto.get("cuiOperador").getAsLong();

        tarifaLocal newTarifa = new tarifaLocal();

        newTarifa.setTarifaLocal(tarifaLocal);
        newTarifa.setIDControl(IDControl);
        newTarifa.setCuiOperador(cuiOperador);

        ResultSet rsVerification = null;

        try {
            connection = data.conectar();
            String sqlVerification = "SELECT * FROM punto_control WHERE IDControl = ? AND cui_operador = ?";
            PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
            psVerification.setInt(1, IDControl);
            psVerification.setLong(2, cuiOperador);
            rsVerification = psVerification.executeQuery();
            if (rsVerification.next()) {
                String sql = "INSERT INTO tarifaLocal (tarifaLocal, IDControl, cui_operador) VALUES (?,?,?)";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setFloat(1, tarifaLocal);
                ps.setInt(2, IDControl);
                ps.setLong(3, cuiOperador);
                ps.executeUpdate();
                response.getWriter().print("SI se inserto " + IDControl);
            } else {
                response.getWriter().print("No existe el operador  o el Punto de Control en la base de datos ");
            }
        } catch (SQLException | ClassNotFoundException e) {
            response.getWriter().print("Error inesperado " + e);
        } finally {
            data.desconectar();
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doOptions(request, response);
        JsonObject objeto = gson.fromJson(request.getReader(), JsonObject.class);
        int IdTarifa = objeto.get("IdTarifa").getAsInt();
        try {
            connection = data.conectar();
            String sql = "DELETE FROM tarifaLocal WHERE IdTarifa = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, IdTarifa);
            ps.executeUpdate();
            response.getWriter().print("Se ha eliminado el dato " + IdTarifa);
        } catch (SQLException | ClassNotFoundException ex) {
            response.getWriter().print("Error al eliminar " + ex);
        } finally {
            data.desconectar();
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doOptions(request, response);
        JsonObject objeto = gson.fromJson(request.getReader(), JsonObject.class);
        int IdTarifa = objeto.get("IdTarifa").getAsInt();
        float tarifaLocal = objeto.get("tarifaLocal").getAsFloat();
        long cuiOperador = objeto.get("cuiOperador").getAsLong();
        try {
            connection = data.conectar();
            String sql = "UPDATE tarifaLocal SET tarifaLocal = ?, cui_operador = ? WHERE IdTarifa = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setFloat(1, tarifaLocal);
            ps.setLong(2, cuiOperador);
            ps.setInt(3, IdTarifa);
            ps.executeUpdate();
            response.getWriter().print("Se actualizó correctamente la tarifa con ID " + IdTarifa);
        } catch (SQLException | ClassNotFoundException ex) {
            response.getWriter().print("Error inesperado: " + ex.getMessage());
        } finally {
            data.desconectar();
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}//fin de la clase
