package controller;

import com.google.gson.Gson;
import dataBase.conexionData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import modelsServices.trayecto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/api/trayecto")
public class controllerTrayecto extends HttpServlet {

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
            out.println("<title>Servlet controllerTrayecto</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet controllerTrayecto at " + request.getContextPath() + "</h1>");
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doOptions(request, response);
        String op = (request.getParameter("op") != null) ? request.getParameter("op") : "list";

        if (op.equals("list")) {
            ArrayList<trayecto> listTrayectos = new ArrayList<>();
            try {
                connection = data.conectar();
                String sql = "select * from trayecto";
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    trayecto trayecto = new trayecto();
                    trayecto.setIdRuta(rs.getInt("IDRuta"));
                    trayecto.setIdControl(rs.getInt("IDControl"));
                    trayecto.setPosicion(rs.getInt("posicion"));
                    trayecto.setActivete(rs.getBoolean("activate"));
                    listTrayectos.add(trayecto);
                }
                String json = gson.toJson(listTrayectos);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
            }catch (SQLException | ClassNotFoundException e) {
                response.getWriter().print("Error de lectura " + e);
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

