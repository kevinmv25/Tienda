package lib;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class SqlLib {

    private static SqlLib instance;
    private Connection connection;
    private String url;
    private String username;
    private String password;

    // CONSTRUCTOR
    private SqlLib(String url, String username, String password) throws SQLException {
        this.url = url;
        this.username = username;
        this.password = password;
        connect();
    }

    // CONECTAR A LA BD
    private void connect() throws SQLException {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver")
                    .getDeclaredConstructor()
                    .newInstance();

            connection = DriverManager.getConnection(url, username, password);

            System.out.println("Conexión establecida exitosamente.");

        } catch (SQLException e) {

            System.err.println("Error de SQL: " + e.getMessage());
            close();

        } catch (ClassNotFoundException | NoSuchMethodException |
                 InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {

            System.err.println("Error cargando el driver: " + e.getMessage());
        }
    }

    // CREAR INSTANCIA (SOLO UNA VEZ)
    public static SqlLib getInstance(String url, String username, String password) throws SQLException {

        if (instance == null) {
            instance = new SqlLib(url, username, password);
        }

        return instance;
    }

    // OBTENER INSTANCIA EXISTENTE
    public static SqlLib getInstance() {
        return instance;
    }

    // HASH DE CONTRASEÑA
    public String generateHash(String password) {

        String salt = BCrypt.gensalt(12);
        return BCrypt.hashpw(password, salt);

    }

    // VALIDAR LOGIN
    public boolean isValidCredentials(String username, String password) throws SQLException {

        String sql = "SELECT contrasena FROM usuario WHERE nombre = ?";

        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, username);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            String storedPassword = rs.getString("contrasena");

            return storedPassword.equals(password);
        }

        return false;
    }


    // CREAR USUARIO
    public boolean crearUsuario(int par, String role, String username, String password) {

        String hashedPassword = generateHash(password);

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

    // CARGAR USUARIOS
    public List<String[]> cargarUsuariosDesdeBD() throws SQLException {

        List<String[]> usuarios = new ArrayList<>();

        String query = "SELECT idUsuario, nombre, contrasena, rol FROM usuario";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {

                String[] usuario = new String[4];

                usuario[0] = rs.getString("idUsuario");
                usuario[1] = rs.getString("nombre");
                usuario[2] = rs.getString("contrasena");
                usuario[3] = rs.getString("rol");

                usuarios.add(usuario);
            }
        }

        return usuarios;
    }

    // BUSCAR PRODUCTO POR NOMBRE
    public String[] buscarProductoPorNombre(String nombre) {

        String sql = "SELECT idProducto, nombreProducto, precio FROM producto WHERE nombreProducto = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, nombre);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return new String[]{
                    rs.getString("idProducto"),
                    rs.getString("nombreProducto"),
                    rs.getString("precio")
                };
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }

    // CARGAR PRODUCTOS
    public List<String[]> cargarProductosDesdeBD() throws SQLException {

        List<String[]> productos = new ArrayList<>();

        String query = "SELECT idProducto, nombreProducto, precio, tipoProducto, caducidad FROM producto";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {

                String[] producto = new String[5];

                producto[0] = rs.getString("idProducto");
                producto[1] = rs.getString("nombreProducto");
                producto[2] = rs.getString("precio");
                producto[3] = rs.getString("tipoProducto");
                producto[4] = rs.getString("caducidad");

                productos.add(producto);
            }
        }

        return productos;
    }

    // OBTENER ROL
    public String getRole(String username) throws SQLException {

        String sql = "SELECT rol FROM usuario WHERE nombre = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, username);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return rs.getString("rol");
            }
        }

        return "nar";
    }

    // AGREGAR PRODUCTO
    public boolean agregarProducto(String nombre, double precio, String categoria) {

        String sql = "INSERT INTO producto (nombreProducto, precio, tipoProducto) VALUES (?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setDouble(2, precio);
            ps.setString(3, categoria);

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }

    // ELIMINAR PRODUCTO
    public boolean eliminarProducto(int idProducto) {

        String sql = "DELETE FROM producto WHERE idProducto = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, idProducto);

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }

    // CERRAR CONEXION
    public void close() throws SQLException {

        if (connection != null) {

            connection.close();
            System.out.println("Conexión cerrada.");
        }
    }
}

