package controller;

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

@WebServlet("/api/colas")
public class contollerColas extends HttpServlet {

    //llamados importantes
    conexionData data = new conexionData();
    Connection connection = null;
    Gson gson = new Gson();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html");
            out.println("<html");
            out.println("<head>");
            out.println("<title>Servlet contollerColas</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet contollerColas at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html");
        }
    }

    protected void doOptions(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4000");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS, PUT");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, X-Auth-Token, Origin, Authorization");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doOptions(request, response);
        response.getWriter().print("Desabilitado");
    }

    @SneakyThrows
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doOptions(request, response);
        JsonObject jsonObject = gson.fromJson(request.getReader(), JsonObject.class);
        long identificador = jsonObject.get("ID").getAsLong();
        String password = jsonObject.get("pass").getAsString();
        int bodegaOperador = jsonObject.get("bodegaOperador").getAsInt();
        String flag = jsonObject.get("flag").getAsString();
        ResultSet rsVerification = null;
        try {
            connection = data.conectar();
            String sqlVerification = "SELECT * FROM operador WHERE cui_operador = ? AND contraseña = ?";
            PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
            psVerification.setLong(1, identificador);
            psVerification.setString(2, password);
            rsVerification = psVerification.executeQuery();
            if (rsVerification.next()) {
                String sqlVericationBodega = "SELECT * FROM punto_control WHERE IDControl = ? AND cui_operador = ?";
                PreparedStatement psVerificationB = connection.prepareStatement(sqlVericationBodega);
                psVerificationB.setInt(1, bodegaOperador);
                psVerificationB.setLong(2, identificador);
                rsVerification = psVerificationB.executeQuery();
                if (rsVerification.next()) {
                    String sqlActual = "SELECT * FROM pedido WHERE BodegaActual = ? AND estado = ?";
                    PreparedStatement psActual = connection.prepareStatement(sqlActual);
                    psActual.setInt(1, bodegaOperador);
                    psActual.setString(2, flag);
                    rsVerification = psActual.executeQuery();

                    //aca empieza lo bueno
                    if (flag.equals("C")) {
                        while (rsVerification.next()) {
                            int NoPedido = rsVerification.getInt("NoPedido");
                            int destino = rsVerification.getInt("DestinoController");
                            updateRuta(response, NoPedido, destino);
                        }


                    } else if (flag.equals("R")) {
                        while (rsVerification.next()) {
                            response.getWriter().println("Ruta");
                            int NoPedido = rsVerification.getInt("NoPedido");
                            int rutaTomada = rsVerification.getInt("RutaTomada");
                            int destino = rsVerification.getInt("DestinoController");
                            updateTraslado(response, NoPedido, rutaTomada, bodegaOperador, destino);
                        }
                    }

                } else {
                    response.getWriter().print("El operador no tiene poder en esta bodega");
                }
            } else {
                response.getWriter().print("No existen los permiso necesarios para realizar tal accion ");
            }


        } catch (SQLException | ClassNotFoundException e) {
            response.getWriter().print("Error de lectura " + e);
        } finally {
            if (rsVerification != null) {
                rsVerification.close();
            }
            data.desconectar();
        }
    }//fin del post

    //meetodo para meter a la ruta
    private void updateRuta(HttpServletResponse response, int NoPedido, int destino)
            throws SQLException, IOException {

        ResultSet rsRuta = null;
        try {
            connection = data.conectar();
            boolean activate = true;
            boolean rutaDisponible = false;
            //verifica si ya se paso por ese punto en esa ruta
            String sqlRuta = "SELECT * FROM trayecto WHERE IDControl = ? AND activete = ?";
            PreparedStatement psRuta = connection.prepareStatement(sqlRuta);
            psRuta.setInt(1, destino);
            psRuta.setBoolean(2, activate);
            rsRuta = psRuta.executeQuery();

            while (rsRuta.next()) {
                rutaDisponible = true;
                //obtengo los datos de la ruta y la posicion en la que va el paquete
                int idRuta = rsRuta.getInt("IDRuta");
                int posicion = rsRuta.getInt("posicion");
                response.getWriter().println("NoPedido: " + NoPedido + " - Destino: " + destino +
                        " - La ruta " + idRuta + " está disponible en la posición " + posicion);
                updateCola update = new updateCola();
                update.actualizarCola(response, idRuta, NoPedido);
                break;
            }
            if (!rutaDisponible) {
                response.getWriter().println("NoPedido: " + NoPedido + " - Destino: " + destino +
                        " - No hay ruta disponible para este destino");
            }
        } catch (SQLException | ClassNotFoundException e) {
            response.getWriter().print("Error de lectura " + e);
        } finally {
            if (rsRuta != null) {
                rsRuta.close();
            }
            data.desconectar();
        }

    }

    private void updateTraslado(HttpServletResponse response, int NoPedido, int rutaTomada, int bodegaOperador, int destino)
            throws SQLException, IOException {
        ResultSet rsEntregado = null;
        try {
            connection = data.conectar();
            response.getWriter().println("NoPedido: " + NoPedido + " - Ruta tomada: " + rutaTomada + " - Esta en la bodega: " + bodegaOperador);
            String sqlEntregado = "SELECT * FROM pedido WHERE NoPedido = ? AND DestinoController = ?";
            PreparedStatement psEntregado = connection.prepareStatement(sqlEntregado);
            psEntregado.setInt(1, NoPedido);
            psEntregado.setInt(2, bodegaOperador);
            rsEntregado = psEntregado.executeQuery();
            if (rsEntregado.next()) {
                String sqlCompletado = "SELECT * FROM pedido WHERE NoPedido = ?";
                PreparedStatement psCompletado = connection.prepareStatement(sqlCompletado);
                psCompletado.setInt(1, NoPedido);
                rsEntregado = psCompletado.executeQuery();
                if (rsEntregado.next()) {
                    String sqlTotal = "SELECT * FROM ruta WHERE IDRuta = ?";
                    PreparedStatement psTotal = connection.prepareStatement(sqlTotal);
                    psTotal.setInt(1, rutaTomada);
                    rsEntregado = psTotal.executeQuery();
                    if (rsEntregado.next()) {
                        int total = rsEntregado.getInt("totalPaquetes");
                        total--;
                        String sqlUpdate = "UPDATE ruta SET totalPaquetes = ? WHERE IDRuta = ?";
                        PreparedStatement psUpdate = connection.prepareStatement(sqlUpdate);
                        psUpdate.setInt(1, total);
                        psUpdate.setInt(2, rutaTomada);
                        psUpdate.executeUpdate();
                    }

                    char estado = 'E';
                    String sql = "UPDATE pedido SET estado = ? WHERE NoPedido = ?";
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setString(1, String.valueOf(estado));
                    ps.setInt(2, NoPedido);
                    ps.executeUpdate();
                    response.getWriter().println("Pedido entregado");
                } else {
                    response.getWriter().println("Pedido no encontrado");
                }
            } else {
                response.getWriter().println("No entregado el pedido");
            }
        } catch (SQLException | ClassNotFoundException e) {
            response.getWriter().print("Error de lectura " + e);
        } finally {
            data.desconectar();
        }

    }

}//fin de la clase