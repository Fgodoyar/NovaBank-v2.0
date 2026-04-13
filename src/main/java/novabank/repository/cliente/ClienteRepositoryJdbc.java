package novabank.repository.cliente;

import novabank.config.DatabaseConnectionManager;
import novabank.model.cliente.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteRepositoryJdbc implements ClienteRepository {

    private Connection conn;

    private static final String INSERT = """
            INSERT INTO Clientes (nombre, apellidos, dni, email, telefono, fecha_creacion) 
            VALUES (?,?,?,?,?,?)
            """;
    private static final String SEARCH_BY_ID = "SELECT * FROM Clientes WHERE id_cliente = ?";

    private static final String SEARCH_BY_DNI = "SELECT * FROM Clientes WHERE dni = ?";

    private static final String SEARCH_ALL = "SELECT * FROM Clientes";

    private static final String DELETE = "DELETE FROM Clientes WHERE id_cliente = ?";

    public ClienteRepositoryJdbc(Connection conn) {
        this.conn = conn;
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        try(Connection conn = DatabaseConnectionManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getApellidos());
            stmt.setString(3, cliente.getDni());
            stmt.setString(4, cliente.getEmail());
            stmt.setString(5, cliente.getTelefono());
            stmt.setDate(6, Date.valueOf(cliente.getFecha_creacion()));

            stmt.executeUpdate();

            try(ResultSet resultSet = stmt.getGeneratedKeys()){
                if (resultSet.next()){
                    Long generatedKey = resultSet.getLong(1);
                    cliente.setId(generatedKey);
                }
            }

            return cliente;
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar el cliente", e);
        }
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SEARCH_BY_ID)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearCliente(rs));
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cliente con id " + id, e);
        }
    }

    // Método de mapeo: convierte una fila del ResultSet en un objeto Cliente
    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getLong("id_cliente"),
                rs.getString("nombre"),
                rs.getString("apellidos"),
                rs.getString("dni"),
                rs.getString("email"),
                rs.getString("telefono"),
                rs.getDate("fecha_creacion").toLocalDate()
        );
    }

        @Override
    public Optional<Cliente> buscarPorDni(String dni) {
            try (Connection conn = DatabaseConnectionManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(SEARCH_BY_DNI)) {

                stmt.setString(1, dni);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return Optional.of(mapearCliente(rs));
                }

                return Optional.empty();
            } catch (SQLException e) {
                throw new RuntimeException("Error al buscar cliente con dni " + dni, e);
            }
    }

    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();

        try(Connection conn = DatabaseConnectionManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(SEARCH_ALL);
            ResultSet resultSet = stmt.executeQuery()){

            while (resultSet.next()){
                Cliente cliente = mapearCliente(resultSet);
                clientes.add(cliente);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return clientes;
    }

    @Override
    public void eliminar(Long id) {
        try(Connection conn = DatabaseConnectionManager.getConnection();
            PreparedStatement stmt = conn.prepareStatement(DELETE)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar cliente con id " + id, e);
        }
    }
}
