package sevelts;


import com.google.gson.Gson;
import dataBase.conexionData;
import lombok.SneakyThrows;
import models.tarifaGlobal;

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
import java.util.ArrayList;

@WebServlet("/api/tarifaGlobal")
public class controllerTarifaG extends HttpServlet {


    conexionData data = new conexionData();
    Connection connection = null;
    Gson gson = new Gson();
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet controllerTarifaG</title>");
            out.println("</head>");
            out.println("<body>");
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
                    ArrayList<tarifaGlobal> listTarifaGlobal = new ArrayList<>();
                    connection = data.conectar();
                    try {
                        String sql = "select * from tarifaglobal";
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ResultSet rs = ps.executeQuery();
                        while (rs.next()) {
                            tarifaGlobal global = new tarifaGlobal();
                            global.setTarifaGlobalId(rs.getInt("IdTarifaGloblal"));
                            global.setTarifaG(rs.getFloat("tarifaGlobal"));
                            global.setFechaInicio(rs.getString("fechaCreacion"));
                            global.setCuiAdmin(rs.getLong("cui_admin"));
                            listTarifaGlobal.add(global);
                        }
                        String json = gson.toJson(listTarifaGlobal);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        PrintWriter out = response.getWriter();
                        out.print(json);
                        out.flush();
                    }catch(SQLException e){
                        response.getWriter().print("Erro en la lectura" + e);
                    }finally {
                        data.desconectar();
                    }
                }
            }

    @Override
    public String getServletInfo() {

        return "Short description";
    }// </editor-fold>

}//fin de la clase
