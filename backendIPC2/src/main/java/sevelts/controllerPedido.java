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
            ArrayList<pedido> listPedido = new ArrayList<>();
            conexionData data = new conexionData();
            Connection connection = data.conectar();
            try {
                String sql = "select * from pedido";
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    pedido pedido = new pedido();
                    pedido.setNoBodega(rs.getInt("NoBodega"));
                    pedido.setNoPedido(rs.getInt("NoPedido"));
                    pedido.setCuiOperador(rs.getLong("cui_operador"));
                    pedido.setEstado(rs.getString("estado").charAt(0));
                    listPedido.add(pedido);

                }
                String json = gson.toJson(listPedido);
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
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            doOptions(request, response);

            JsonObject objeto = gson.fromJson(request.getReader(), JsonObject.class);
            int noBodega = objeto.get("NoBodega").getAsInt();
            long cuiOperador = objeto.get("cuiOperador").getAsLong();
            char estado = objeto.get("estado").getAsCharacter();

            pedido newPedido = new pedido();
            newPedido.setNoBodega(noBodega);
            newPedido.setCuiOperador(cuiOperador);
            newPedido.setEstado(estado);

            ResultSet rsVerification = null;

            try {
                connection = data.conectar();
                String sqlVerification = "SELECT * FROM bodega WHERE NoBodega = ?";
                PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
                psVerification.setInt(1, noBodega);
                rsVerification = psVerification.executeQuery();
                if(rsVerification.next()){
                    String psOperador = "SELECT * FROM operador WHERE cui_operador = ?";
                    PreparedStatement psOp = connection.prepareStatement(psOperador);
                    psOp.setLong(1, cuiOperador);
                    rsVerification = psVerification.executeQuery();
                    if(rsVerification.next()) {

                        String sql = "INSERT INTO pedido (NoBodega, cui_operador, estado) VALUES (?, ?, ?)";
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setInt(1, noBodega);
                        ps.setLong(2, cuiOperador);
                        ps.setCharacterStream(3, new StringReader(String.valueOf(estado)));
                        ps.executeUpdate();
                        response.getWriter().print("Pedido creado exitosamente");
                    }else{
                        response.getWriter().print("El operador no existe");
                    }
                }else{
                    response.getWriter().print("La bodega no existe");
                }
            }catch (SQLException ex) {
                response.getWriter().print("Error en la escritura");
            }finally {
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
        response.getWriter().print("Opcion invalida");
    }

    @Override
    public String getServletInfo() {

        return "Short description";
    }// </editor-fold>
}

