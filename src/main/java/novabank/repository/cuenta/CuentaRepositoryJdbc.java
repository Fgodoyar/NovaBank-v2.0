package novabank.repository.cuenta;

import novabank.config.DatabaseConnectionManager;
import novabank.model.Cuenta;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CuentaRepositoryJdbc implements CuentaRepository{

    private static final String SEARCH_BY_ID = "SELECT * FROM Cuentas WHERE id_cuenta = ?";

    private static final String SEARCH_BY_IBAN = "SELECT * FROM Cuentas WHERE numero_cuenta = ?";

    private static final String SEARCH_BY_ID_CLIENTE = "SELECT * FROM Cuentas WHERE id_cliente = ?";

    private static final String INSERT = """
            INSERT INTO Cuentas (numero_cuenta, titular, cliente_id, saldo, fecha_creacion)
            VALUES (?,?,?,?,?,?)
            """;

    private static final String UPDATE = "UPDATE Cuentas SET saldo = ? WHERE id_cuenta = ?";

    @Override
    public Cuenta guardar(Cuenta cuenta) {
        try(Connection conn = DatabaseConnectionManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, cuenta.getNumero_cuenta());
            stmt.setString(2, cuenta.getTitular());
            stmt.setLong(3, cuenta.getCliente_id());
            stmt.setBigDecimal(4, cuenta.getSaldo());
            stmt.setDate(5, Date.valueOf(cuenta.getFecha_creacion()));

            stmt.executeUpdate();

            try(ResultSet resultSet = stmt.getGeneratedKeys()){
                if (resultSet.next()){
                    Long generatedKey = resultSet.getLong(1);
                    cuenta.setId(generatedKey);
                }
            }

            return cuenta;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Cuenta> buscarPorId(Long id) {
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SEARCH_BY_ID)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearCuenta(rs));
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la cuenta con id " + id, e);
        }
    }

    // Método de mapeo: convierte una fila del ResultSet en un objeto Cuenta
    private Cuenta mapearCuenta(ResultSet rs) throws SQLException {
        return new Cuenta(
                rs.getLong("id_cuenta"),
                rs.getString("numero_cuenta"),
                rs.getString("titular"),
                rs.getLong("id_cliente"),
                rs.getBigDecimal("saldo"),
                rs.getDate("fecha_creacion").toLocalDate()
        );
    }

    @Override
    public Optional<Cuenta> buscarPorNumero(String numeroCuenta) {
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SEARCH_BY_IBAN)) {

            stmt.setString(1, numeroCuenta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(mapearCuenta(rs));
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la cuenta con el número de cuenta " + numeroCuenta, e);
        }
    }

    @Override
    public List<Cuenta> buscarPorClienteId(Long clienteId) {
        List<Cuenta> cuentas = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SEARCH_BY_ID_CLIENTE)) {

            stmt.setLong(1, clienteId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cuentas.add(mapearCuenta(rs));
            }

            return cuentas;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la cuenta con id " + clienteId, e);
        }
    }

    @Override
    public Cuenta actualizarSaldo(Long cuentaId, BigDecimal nuevoSaldo) {
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {
            stmt.setBigDecimal(1, nuevoSaldo);
            stmt.setLong(2, cuentaId);
            stmt.executeUpdate();

            return buscarPorId(cuentaId)
                    .orElseThrow(() -> new RuntimeException("Cuenta no encontrada tras actualización"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
