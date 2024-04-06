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
import models.paquetes;



import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/api/paquetes")
public class controllerPaquetes extends HttpServlet {

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
            }

            @SneakyThrows
            @Override
            protected void doGet(HttpServletRequest request, HttpServletResponse response)
                    throws ServletException, IOException {
                doOptions(request, response);
                String op = (request.getParameter("op") != null) ? request.getParameter("op") : "list";
                if (op.equals("list")) {
                    ArrayList<paquetes> listPaquetes = new ArrayList<>();
                    conexionData data = new conexionData();
                    connection = data.conectar();
                    try {
                        String sql = "select * from paquetes";
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            paquetes paquete = new paquetes();
                            paquete.setPaquete(rs.getInt("Paquete"));
                            paquete.setNIT(rs.getInt("NIT"));
                            paquete.setNoPedido(rs.getInt("NoPedido"));
                            paquete.setNoBodega(rs.getInt("NoBodega"));
                            paquete.setPeso(rs.getFloat("peso"));
                            paquete.setTarifaGlobal(rs.getBoolean("tarifaGlobal"));
                            paquete.setTotal(rs.getFloat("total"));
                            paquete.setDestino(rs.getString("destino"));
                            paquete.setCuiRecepcionista(rs.getLong("cui_recepcionista"));
                            paquete.setCuiOperador(rs.getLong("cui_operador"));
                            listPaquetes.add(paquete);
                        }
                        String json = gson.toJson(listPaquetes);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        PrintWriter out = response.getWriter();
                        out.print(json);
                        out.flush();
                    }catch(SQLException e){
                        response.getWriter().println("Error al leer el contenido de paquestes " +e);
                    }finally {
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
                int NIT = jsonObject.get("NIT").getAsInt();
                int NoPedido = jsonObject.get("NoPedido").getAsInt();
                int NoBodega = jsonObject.get("NoBodega").getAsInt();
                float peso = jsonObject.get("peso").getAsFloat();
                boolean tarifaGlobal = jsonObject.get("tarifaGlobal").getAsBoolean();
                String destino = jsonObject.get("destino").getAsString();
                long cuiRecepcionista = jsonObject.get("cuiRecepcionista").getAsLong();
                long cuiOperador = jsonObject.get("cuiOperador").getAsLong();

                paquetes newPaquete = new paquetes();
                newPaquete.setNIT(NIT);
                newPaquete.setNoPedido(NoPedido);
                newPaquete.setNoBodega(NoBodega);
                newPaquete.setPeso(peso);
                newPaquete.setTarifaGlobal(tarifaGlobal);
                newPaquete.setDestino(destino);
                newPaquete.setCuiRecepcionista(cuiRecepcionista);
                newPaquete.setCuiOperador(cuiOperador);

                ResultSet rsVerification = null;

                try{
                    connection = data.conectar();
                    String sqlVerification = "SELECT * FROM cliente WHERE NIT = ?";
                    PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
                    psVerification.setInt(1, NIT);
                    rsVerification = psVerification.executeQuery();

                    if(rsVerification.next()){

                        String sqlPedido = "SELECT * FROM pedido WHERE  NoPedido = ? AND NoBodega = ?";
                        PreparedStatement psPedido = connection.prepareStatement(sqlPedido);
                        psPedido.setInt(1, NoPedido);
                        psPedido.setInt(2, NoBodega);
                        rsVerification = psPedido.executeQuery();
                        if(rsVerification.next()){
                            String sqlOperador = "SELECT * FROM operador WHERE cui_operador = ?";
                            PreparedStatement psOperador = connection.prepareStatement(sqlOperador);
                            psOperador.setLong(1, cuiOperador);
                            rsVerification = psOperador.executeQuery();
                            if(rsVerification.next()){
                                String sqlRece = "SELECT * FROM recepcionista WHERE cui_recepcionista = ?";
                                PreparedStatement psRece = connection.prepareStatement(sqlRece);
                                psRece.setLong(1, cuiRecepcionista);
                                rsVerification = psRece.executeQuery();

                                if(rsVerification.next()){

                                    if (tarifaGlobal) {
                                        // Obtener el valor de tarifaGlobal de la última fila en la tabla tarifaGlobal
                                        String sqlTarifaGlobal = "SELECT tarifaglobal FROM tarifaglobal ORDER BY IdTarifaGloblal DESC LIMIT 1";
                                        PreparedStatement psTarifaGlobal = connection.prepareStatement(sqlTarifaGlobal);
                                        ResultSet rsTarifaGlobal = psTarifaGlobal.executeQuery();

                                        float tarifaGlobalValue = 0.0f;
                                        if (rsTarifaGlobal.next()) {
                                            tarifaGlobalValue = rsTarifaGlobal.getFloat("tarifaglobal");
                                        }

                                        // Calcular el total multiplicando la tarifa global por el peso
                                        float total = tarifaGlobalValue * peso;

                                        // Insertar el paquete en la tabla paquetes con el total calculado
                                        String sqlInsert = "INSERT INTO paquetes(NIT, NoPedido, NoBodega, peso, tarifaGlobal, total, destino, cui_recepcionista, cui_operador) VALUES(?,?,?,?,?,?,?,?,?)";
                                        PreparedStatement psInsert = connection.prepareStatement(sqlInsert);
                                        psInsert.setInt(1, NIT);
                                        psInsert.setInt(2, NoPedido);
                                        psInsert.setInt(3, NoBodega);
                                        psInsert.setFloat(4, peso);
                                        psInsert.setBoolean(5, tarifaGlobal);
                                        psInsert.setFloat(6, total);
                                        psInsert.setString(7, destino);
                                        psInsert.setLong(8, cuiRecepcionista);
                                        psInsert.setLong(9, cuiOperador);
                                        psInsert.executeUpdate();
                                        response.getWriter().println("Paquete insertado correctamente");
                                    } else {
                                        response.getWriter().println("El paquete no tiene tarifa global");
                                    }

                                }else{
                                    response.getWriter().println("El recepcionista no existe, en la base de datos");
                                }
                            }else{
                                response.getWriter().println("El operador no existe, en la base de datos");
                            }
                        }else{
                            response.getWriter().println("Datos del pedido incorrectos");
                        }
                    }else{
                        response.getWriter().println("El cliente no existe, cree antes el cliente");
                    }

                }catch(SQLException e) {
                    response.getWriter().println("Error al insertar el paquete " + e);
                }finally {
                    if(rsVerification != null) {
                        rsVerification.close();
                    }
                    data.desconectar();
                }
            }//final del post

    @Override
    public String getServletInfo() {

        return "Short description";
    }// </editor-fold>

}//final de la clase



