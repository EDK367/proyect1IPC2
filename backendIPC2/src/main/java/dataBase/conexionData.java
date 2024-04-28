package dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class conexionData {

    private static final String URL = "jdbc:mysql://localhost:3306/proyecto";
    private static final String USER = "Denilson";
    private static final String PASSWORD = "Denilson22";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    public Connection connection;

    public conexionData() {
    }

    public Connection conectar() throws SQLException, ClassNotFoundException {
        try {
            Class.forName(DRIVER);
            //esto es lo que sirve para hacer la conexion
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
           // System.out.println("Conexion exitosa");
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error al conectar con la base de datos");
            throw ex;
        }
        return connection;
    }

    public void desconectar() {
        if (connection != null) {
            try {
                connection.close();
              //  System.out.println("Desconexion exitosa");
            } catch (SQLException ex) {
                Logger.getLogger(conexionData.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) {
        try {
            conexionData conexion = new conexionData();
            Connection connection = conexion.conectar();
            conexion.desconectar();
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
