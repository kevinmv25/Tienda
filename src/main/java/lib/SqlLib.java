
package lib;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author juego
 */
public class SqlLib {
    private static SqlLib instance;
    private Connection connection;
    private final String url;
    private String username;
    private String password;

    
    public SqlLib(String url, String username, String password) throws SQLException {
        this.url = String.format(url, username, password);
        connect();
    }

    
    private void connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();

            this.connection = DriverManager.getConnection(url, username, password);
            System.out.println("Conexión establecida exitosamente.");
        } catch (SQLException e) {
            System.err.println("Error de SQL: " + e.getMessage());

            close();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            System.err.println("Error de reflexión: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Argumento ilegal: " + e.getMessage());
        } catch (InvocationTargetException e) {
        }
    }

    public static SqlLib getInstance(String url, String username, String password) throws SQLException {
        if (instance == null) {
            instance = new SqlLib(url, username, password);
        }
        return instance;
    }

   
    
    public String generateHash(String password) {
        String salt = BCrypt.gensalt(12);
        return BCrypt.hashpw(password, salt);
    }

    
    public boolean isValidCredentials(String username, String password) throws SQLException {
        String query = "SELECT contrasena FROM usuario WHERE nombre = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String storedHash = resultSet.getString("contrasena");
                if (BCrypt.checkpw(password, storedHash)) {
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Contraseña incorrecta.", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog(null, "Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
    }

    
    public boolean createUser(int par, String role, String username, String password) {
        String hashedPassword = generateHash(password);
        System.out.println(hashedPassword);

        String query = "{ CALL AgregarUsuario(?, ?, ?) }";
        try (PreparedStatement statement = connection.prepareCall(query)) {
            statement.setString(1, role);
            statement.setString(2, username);
            statement.setString(3, hashedPassword);
            statement.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    public List<String[]> cargarUsuariosDesdeBD() throws SQLException {
        List<String[]> usuarios = new ArrayList<>();
        String query = "SELECT id_usuario, nombre, contrasena, rol FROM usuario WHERE is_deleted = 0";

        // Ejemplo de conexión y consulta a la base de datos
        try (PreparedStatement statement = connection.prepareStatement(query); 
                ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                String[] usuario = new String[4];
                usuario[0] = rs.getString("id_usuario");
                usuario[1] = rs.getString("nombre");
                usuario[2] = rs.getString("contrasena");
                usuario[3] = rs.getString("rol");
                usuarios.add(usuario);
            }
        }

        return usuarios;
    }
    
    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
            System.out.println("Conexión cerrada.");
        }
    }

}
