package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataBase.conexionData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import lombok.SneakyThrows;
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
                    trayecto.setActivete(rs.getBoolean("activete"));
                    listTrayectos.add(trayecto);
                }
                String json = gson.toJson(listTrayectos);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
            } catch (SQLException | ClassNotFoundException e) {
                response.getWriter().print("Error de lectura " + e);
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
        int idRuta = jsonObject.get("idRuta").getAsInt();
        int idControl = jsonObject.get("idControl").getAsInt();
        int posicion = jsonObject.get("posicion").getAsInt();

        trayecto newTrayecto = new trayecto();
        newTrayecto.setIdRuta(idRuta);
        newTrayecto.setIdControl(idControl);
        newTrayecto.setPosicion(posicion);

        ResultSet rsVerification = null;

        try {
            connection = data.conectar();
            String sqlVerification = "SELECT * FROM ruta WHERE IDRuta = ?";
            PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
            psVerification.setInt(1, idRuta);
            rsVerification = psVerification.executeQuery();

            if (rsVerification.next()) {
                String sqlControllerActivate = "SELECT * FROM trayecto WHERE IDRuta = ? AND IDControl = ?";
                PreparedStatement psControllerActivate = connection.prepareStatement(sqlControllerActivate);
                psControllerActivate.setInt(1, idRuta);
                psControllerActivate.setInt(2, idControl);
                rsVerification = psControllerActivate.executeQuery();
                if (rsVerification.next()) {
                   // response.getWriter().print(idControl + " el control ya existe en esta ruta " + idRuta);
                } else {
                    String sqlController = "SELECT * FROM punto_control WHERE IDControl = ?";
                    PreparedStatement psController = connection.prepareStatement(sqlController);
                    psController.setInt(1, idControl);
                    rsVerification = psController.executeQuery();
                    if (!rsVerification.next()) {
                        //response.getWriter().print(idControl + " no existe en punto de control");
                    } else {
                        String sqlPosicion = "SELECT * FROM trayecto WHERE IDRuta = ? AND posicion = ?";
                        PreparedStatement psPosicion = connection.prepareStatement(sqlPosicion);
                        psPosicion.setInt(1, idRuta);
                        psPosicion.setInt(2, posicion);
                        rsVerification = psPosicion.executeQuery();
                        if (rsVerification.next()) {
                            //response.getWriter().print(posicion + " posicion ya existente en esta ruta " + idRuta + " asignado al control " + idControl);
                        } else {
                            String sql = "INSERT INTO trayecto (IDRuta, IDControl, posicion, activete) VALUES (?,?,?,?)";
                            PreparedStatement ps = connection.prepareStatement(sql);
                            ps.setInt(1, idRuta);
                            ps.setInt(2, idControl);
                            ps.setInt(3, posicion);
                            ps.setBoolean(4, true);
                            ps.executeUpdate();
                            response.getWriter().print(newTrayecto);
                        }
                    }
                }
            } else {
               // response.getWriter().print(idRuta + " no existe esa ruta");
            }
        } catch (SQLException | ClassNotFoundException e) {
            response.getWriter().print("Error de lectura " + e);
        } finally {
            if (rsVerification != null) {
                rsVerification.close();
            }
            data.desconectar();
        }
    }

    @SneakyThrows
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doOptions(request, response);
        JsonObject jsonObject = gson.fromJson(request.getReader(), JsonObject.class);
        int idRuta = jsonObject.get("idRuta").getAsInt();
        int idControl = jsonObject.get("idControl").getAsInt();
        boolean activete = jsonObject.get("activete").getAsBoolean();

        if(jsonObject.has("posicion")){
            int posicion = jsonObject.get("posicion").getAsInt();
            updatePosicion(response, idRuta, idControl, posicion, activete);
        }else{
            updateActivete(response,idRuta, idControl, activete);
        }
    }
    //metodo parea poder actualizar con la posicion
    @SneakyThrows
    private void updatePosicion ( HttpServletResponse response, int idRuta, int idControl, int posicion, boolean activete)
            throws  SQLException,IOException {
        ResultSet rsVerification = null;

        try {
            connection = data.conectar();
            String sqlVerification = "SELECT * FROM trayecto WHERE IDRuta = ? AND IDControl = ?";
            PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
            psVerification.setInt(1, idRuta);
            psVerification.setInt(2, idControl);
            rsVerification = psVerification.executeQuery();
            if(rsVerification.next()){
                String sqlPosicion = "SELECT * FROM trayecto WHERE IDRuta = ? AND posicion = ?";
                PreparedStatement psPosicion = connection.prepareStatement(sqlPosicion);
                psPosicion.setInt(1, idRuta);
                psPosicion.setInt(2, posicion);
                rsVerification = psPosicion.executeQuery();
                if(rsVerification.next()){
                    response.getWriter().print(posicion + " posicion ya existente en esta ruta " + idRuta + " asignado al control " + idControl);
                }else{
                    String sql = "UPDATE trayecto SET posicion = ?, activete = ? WHERE IDRuta = ? AND IDControl = ?";
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setInt(1, posicion);
                    ps.setBoolean(2, activete);
                    ps.setInt(3, idRuta);
                    ps.setInt(4, idControl);
                    ps.executeUpdate();
                    response.getWriter().print(idRuta + " se actualizo correctamente " + idControl);
                }
            }else{
                response.getWriter().print(idControl + " no existe en esta ruta " + idRuta);
            }
        }catch(SQLException e) {
            response.getWriter().print("Error de lectura " + e);
        }finally{
            if(rsVerification != null) {
                rsVerification.close();
            }
            data.desconectar();
        }
    }
    //si solo queremos actualizar el estado de activado
    private void updateActivete (HttpServletResponse response, int idRuta, int idControl, boolean activete)
            throws  SQLException,IOException {
        ResultSet rsVerification = null;
        try{
            connection = data.conectar();
            String sqlVerification = "SELECT * FROM trayecto WHERE IDRuta = ? AND IDControl = ?";
            PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
            psVerification.setInt(1, idRuta);
            psVerification.setInt(2, idControl);
            rsVerification = psVerification.executeQuery();
            if(rsVerification.next()){
                String sql = "UPDATE trayecto SET activete = ? WHERE IDRuta = ? AND IDControl = ?";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setBoolean(1, activete);
                ps.setInt(2, idRuta);
                ps.setInt(3, idControl);
                ps.executeUpdate();
                response.getWriter().print(idControl + " se actualizo correctamente el estado en la ruta " + idRuta);
            }else{
                response.getWriter().print(idControl + " no existe en esta ruta " + idRuta);
            }
        }catch(SQLException | ClassNotFoundException e) {
            response.getWriter().print("Error de lectura " + e);
        }finally{
            if(rsVerification != null) {
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
        int idRuta = Integer.parseInt(request.getParameter("idRuta"));
        int idControl = Integer.parseInt(request.getParameter("idControl"));
        ResultSet rsVerification = null;
        try {
            connection = data.conectar();
            String sqlVerification = "SELECT * FROM trayecto WHERE IDRuta = ? AND IDControl = ?";
            PreparedStatement psVerification = connection.prepareStatement(sqlVerification);
            psVerification.setInt(1, idRuta);
            psVerification.setInt(2, idControl);
            rsVerification = psVerification.executeQuery();
            if(rsVerification.next()) {
                String sql = "DELETE FROM trayecto WHERE IDRuta = ? AND IDControl = ?";
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, idRuta);
                ps.setInt(2, idControl);
                ps.executeUpdate();
                response.getWriter().print( idRuta +" Se elimino correctamente " + idControl);
            }else{
                response.getWriter().println("Error en la eliminacion ");
            }
        }catch(SQLException | ClassNotFoundException e) {
            response.getWriter().print("Error de lectura " + e);
        }finally{
            if(rsVerification != null) {
                rsVerification.close();
            }
            data.desconectar();
        }
    }

    @Override
    public String getServletInfo() {

        return "Short description";
    }// </editor-fold>
}//fin de la clase