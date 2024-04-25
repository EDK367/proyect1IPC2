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
import models.paquete;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.lang.System.out;

@WebServlet("/api/paquetes")
public class controllerPaquete extends HttpServlet {

    conexionData data = new conexionData();
    Gson gson = new Gson();
    Connection connection = null;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet controllerPaquetes</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet controllerPaquetes at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS, PUT");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, X-Auth-Token, Origin, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doOptions(request, response);
        String op = (request.getParameter("op") != null) ? request.getParameter("op") : "list";
        if (op.equals("list")) {
            ArrayList<paquete> listPaquetes = new ArrayList<>();
            try {
                connection = data.conectar();
                String sql = "select * from paquetes";
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    paquete paquete = new paquete();
                    paquete.setPaquete(rs.getInt("paquete"));
                    paquete.setCuiOperador(rs.getLong("cui_operador"));
                    paquete.setCuiRecepcionista(rs.getLong("cui_recepcionista"));
                    paquete.setNoPedido(rs.getInt("NoPedido"));
                    paquete.setBodegaInicial(rs.getInt("BodegaInicial"));
                    paquete.setCliente(rs.getInt("NIT"));
                    paquete.setPeso(rs.getFloat("peso"));
                    paquete.setTarifaGlobal(rs.getBoolean("tarifaGlobal"));
                    paquete.setTotal(rs.getFloat("total"));
                    listPaquetes.add(paquete);

                }
                String json = gson.toJson(listPaquetes);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
            } catch (SQLException | ClassNotFoundException e) {
                response.getWriter().println("Error al leer el contenido de paquestes " + e);
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

        long cuiOperador = jsonObject.get("cuiOperador").getAsLong();
        long cuiRecepcionista = jsonObject.get("cuiRecepcionista").getAsLong();
        int noPedido = jsonObject.get("noPedido").getAsInt();
        int bodegaIncial = jsonObject.get("bodegaInicial").getAsInt();
        int cliente = jsonObject.get("cliente").getAsInt();
        float peso = jsonObject.get("peso").getAsFloat();
        boolean tarifaGlobal = jsonObject.get("tarifaGlobal").getAsBoolean();

        paquete newPaquete = new paquete();
        newPaquete.setCuiOperador(cuiOperador);
        newPaquete.setCuiRecepcionista(cuiRecepcionista);
        newPaquete.setNoPedido(noPedido);
        newPaquete.setBodegaInicial(bodegaIncial);
        newPaquete.setCliente(cliente);
        newPaquete.setPeso(peso);
        newPaquete.setTarifaGlobal(tarifaGlobal);

        ResultSet rsVerification = null;

        try {
            connection = data.conectar();
            String sqlVerification = "SELECT * FROM cliente WHERE NIT = ?";
            PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
            psVerification.setInt(1, cliente);
            rsVerification = psVerification.executeQuery();
            if (rsVerification.next()) {
                String sqlPedido = "SELECT * FROM pedido WHERE NoPedido = ? AND BodegaActual = ?";
                PreparedStatement psPedido = connection.prepareStatement(sqlPedido);
                psPedido.setInt(1, noPedido);
                psPedido.setInt(2, bodegaIncial);
                rsVerification = psPedido.executeQuery();
                if (rsVerification.next()) {
                    String sqlOperador = "SELECT * FROM operador WHERE cui_operador = ?";
                    PreparedStatement psOperador = connection.prepareStatement(sqlOperador);
                    psOperador.setLong(1, cuiOperador);
                    rsVerification = psOperador.executeQuery();
                    if (rsVerification.next()) {
                        String sqlRecepcionista = "SELECT * FROM recepcionista WHERE cui_recepcionista = ?";
                        PreparedStatement psRecepcionista = connection.prepareStatement(sqlRecepcionista);
                        psRecepcionista.setLong(1, cuiRecepcionista);
                        rsVerification = psRecepcionista.executeQuery();
                        if (rsVerification.next()) {

                            if (tarifaGlobal) {
                                String sqlTarifaGlobal = "SELECT tarifaglobal FROM tarifaglobal ORDER BY IdTarifaGloblal DESC LIMIT 1";
                                PreparedStatement psTarifaGlobal = connection.prepareStatement(sqlTarifaGlobal);
                                ResultSet rsTarifaGlobal = psTarifaGlobal.executeQuery();

                                float tarifaGlobalValue = 0.0f;
                                if (rsTarifaGlobal.next()) {
                                    tarifaGlobalValue = rsTarifaGlobal.getFloat("tarifaglobal");
                                }
                                float total = tarifaGlobalValue * peso;

                                String sqlInsert = "INSERT INTO paquetes( cui_operador, cui_recepcionista, NoPedido, BodegaInicial, NIT, peso, tarifaGlobal, total) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
                                PreparedStatement psInsert = connection.prepareStatement(sqlInsert);
                                psInsert.setLong(1, cuiOperador);
                                psInsert.setLong(2, cuiRecepcionista);
                                psInsert.setInt(3, noPedido);
                                psInsert.setInt(4, bodegaIncial);
                                psInsert.setInt(5, cliente);
                                psInsert.setFloat(6, peso);
                                psInsert.setBoolean(7, tarifaGlobal);
                                psInsert.setFloat(8, total);
                                psInsert.executeUpdate();
                                response.getWriter().println("Paquete insertado correctamente con global");
                            } else {
                                String sqlTarifaLocal = "SELECT * FROM tarifalocal ORDER BY IdTarifa AND IDControl DESC LIMIT 1";
                                PreparedStatement psTarifaLocal = connection.prepareStatement(sqlTarifaLocal);
                                ResultSet rsTarifaLocal = psTarifaLocal.executeQuery();

                                float tarifaLocalValue = 0.0f;
                                if (rsTarifaLocal.next()) {
                                    tarifaLocalValue = rsTarifaLocal.getFloat("tarifalocal");
                                }
                                float total = tarifaLocalValue * peso;
                                String sqlInsert = "INSERT INTO paquetes( cui_operador, cui_recepcionista, NoPedido, BodegaInicial, NIT, peso, tarifaGlobal, total) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
                                PreparedStatement psInsert = connection.prepareStatement(sqlInsert);
                                psInsert.setLong(1, cuiOperador);
                                psInsert.setLong(2, cuiRecepcionista);
                                psInsert.setInt(3, noPedido);
                                psInsert.setInt(4, bodegaIncial);
                                psInsert.setInt(5, cliente);
                                psInsert.setFloat(6, peso);
                                psInsert.setBoolean(7, tarifaGlobal);
                                psInsert.setFloat(8, total);
                                psInsert.executeUpdate();
                                response.getWriter().println("Paquete insertado correctamente con local");
                            }
                        } else {
                            response.getWriter().println("Error el recepcionista no existe en la base de datos");
                        }
                    } else {
                        response.getWriter().println("Error el operador no existe en la base de datos");
                    }
                } else {
                    response.getWriter().println("Error el pedido no existe en la base de o no coincide con la bodega");
                }
            } else {
                response.getWriter().println("Error el cliente no existe en la base de datos");
            }
        } catch (SQLException | ClassNotFoundException e) {
            response.getWriter().println("Error al crear un paquete " + e);
        } finally {
            if (rsVerification != null) {
                rsVerification.close();
            }
            data.desconectar();
        }

    }//fin del post

    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doOptions(request, response);
        int idPaquete = Integer.parseInt(request.getParameter("paquete"));
        try {
            connection = data.conectar();
            String sql = "DELETE FROM paquetes WHERE paquete = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, idPaquete);
            ps.executeUpdate();
            response.getWriter().println("Paquete eliminado correctamente");
        } catch (SQLException | ClassNotFoundException e) {
            response.getWriter().println("Error al eliminar el paquete " + e);
        } finally {
            data.desconectar();
        }
    }

}//fin de la clase
