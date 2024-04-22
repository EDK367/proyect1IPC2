package sevelts;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataBase.conexionData;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import models.cliente;
import models.pedido;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/api/pedidos")
public class controllerPedido extends HttpServlet {
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
            ArrayList<pedido> listPedidos = new ArrayList<>();
            try {
                conexionData data = new conexionData();
                Connection connection = data.conectar();
                String sql = "select * from pedido";
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    pedido pedido = new pedido();
                    pedido.setNoPedido(rs.getInt("NoPedido"));
                    pedido.setBodegaActual(rs.getInt("BodegaActual"));
                    pedido.setEstado(String.valueOf(rs.getString("Estado").charAt(0)));
                    pedido.setDestinoController(rs.getInt("DestinoController"));
                    pedido.setRutaTomada(rs.getInt("RutaTomada"));
                    listPedidos.add(pedido);
                }
                String json = gson.toJson(listPedidos);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
            } catch (SQLException e) {
                response.getWriter().print("Error en la lectura" + e);
                System.out.println("Error en la lectura");
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
        JsonObject jsonObject = gson.fromJson(request.getReader(), JsonObject.class);
        int bodegaActual = jsonObject.get("bodegaActual").getAsInt();
        int destinoController = jsonObject.get("destinoController").getAsInt();

        pedido newPedido = new pedido();
        newPedido.setBodegaActual(bodegaActual);
        newPedido.setDestinoController(destinoController);
        ResultSet rsVerification = null;
        try {
            connection = data.conectar();
            String sql = "INSERT INTO pedido (BodegaActual, DestinoController) VALUES (?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, newPedido.getBodegaActual());
            ps.setInt(2, newPedido.getDestinoController());
            ps.executeUpdate();
            response.getWriter().print("pedido registrado" + newPedido);
        } catch (SQLException e) {
            response.getWriter().print("Error al crear");
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
        response.getWriter().print("Opcion invalida");
    }

    @SneakyThrows
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doOptions(request, response);
        JsonObject jsonObject = gson.fromJson(request.getReader(), JsonObject.class);
        int noPedido = jsonObject.get("noPedido").getAsInt();
        int bodegaActual = jsonObject.get("bodegaActual").getAsInt();
        String estado = jsonObject.get("estado").getAsString();
        int destinoController = jsonObject.get("destinoController").getAsInt();
        int rutaTomada = jsonObject.get("rutaTomada").getAsInt();
        ResultSet rsVerification = null;
        try {
            connection = data.conectar();
            String sqlVerification = "SELECT * FROM pedido WHERE NoPedido = ?";
            PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
            psVerification.setInt(1, noPedido);
            rsVerification = psVerification.executeQuery();

            if (rsVerification.next()) {
                String sql = "UPDATE pedido SET bodegaActual = ?, estado = ?, destinoController = ?, rutaTomada = ? WHERE NoPedido = ?";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, bodegaActual);
                ps.setString(2, estado);
                ps.setInt(3, destinoController);
                ps.setInt(4, rutaTomada);
                ps.setInt(5, noPedido);
                ps.executeUpdate();
                response.getWriter().print("pedido actualizado" + ps);
            } else {
                response.getWriter().print("pedido no existe");
            }
        } catch (SQLException e) {
            response.getWriter().print("Error al actualizar");
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

