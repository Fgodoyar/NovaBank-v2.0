package novabank.repository.movimiento;

import novabank.config.DatabaseConnectionManager;
import novabank.model.Movimiento;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovimientoRepositoryJdbc implements MovimientoRepository {

    private static final String INSERT = """
            INSERT INTO Movimientos (id_cuenta, tipo, cantidad, fecha)
            VALUES (?,?,?,?)
            """;

    private static final String SEARCH_BY_ID_CUENTA = "SELECT * FROM Movimientos WHERE id_cuenta = ?";

    private static final String SEARCH_BY_ID_CUENTA_FECHAS = """
            SELECT *
            FROM Movimientos
            WHERE id_cuenta = ? AND fecha BETWEEN ? AND ?
            ORDER BY fecha ASC
            """;

    @Override
    public Movimiento guardar(Movimiento movimiento) {
        try(Connection conn = DatabaseConnectionManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)){
            stmt.setLong(1, movimiento.getId_cuenta());
            stmt.setString(2, movimiento.getTipo());
            stmt.setBigDecimal(3, movimiento.getCantidad());
            stmt.setDate(4, Date.valueOf(movimiento.getFecha()));

            stmt.executeUpdate();

            try(ResultSet resultSet = stmt.getGeneratedKeys()){
                if (resultSet.next()){
                    Long generatedKey = resultSet.getLong(1);
                    movimiento.setId(generatedKey);
                }
            }

            return movimiento;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Movimiento> buscarPorCuentaId(Long cuentaId) {
        List<Movimiento> movimientos = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SEARCH_BY_ID_CUENTA)) {

            stmt.setLong(1, cuentaId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                movimientos.add(mapearMovimiento(rs));
            }

            return movimientos;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la cuenta con id " + cuentaId, e);
        }
    }

    // Método de mapeo: convierte una fila del ResultSet en un objeto Movimiento
    private Movimiento mapearMovimiento(ResultSet rs) throws SQLException {
        return new Movimiento(
                rs.getLong("id_movimiento"),
                rs.getLong("id_cuenta"),
                rs.getString("tipo"),
                rs.getBigDecimal("cantidad"),
                rs.getDate("fecha").toLocalDate()
        );
    }

    @Override
    public List<Movimiento> buscarPorCuentaIdYFechas(Long cuentaId, LocalDateTime inicio, LocalDateTime fin) {
        List<Movimiento> movimientos = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SEARCH_BY_ID_CUENTA_FECHAS)) {

            stmt.setLong(1, cuentaId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                movimientos.add(mapearMovimiento(rs));
            }

            return movimientos;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar la cuenta con id " + cuentaId, e);
        }
    }

    @Override
    public Movimiento guardar(Movimiento movimiento, Connection conn) {
        try(PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)){
            stmt.setLong(1, movimiento.getId_cuenta());
            stmt.setString(2, movimiento.getTipo());
            stmt.setBigDecimal(3, movimiento.getCantidad());
            stmt.setDate(4, Date.valueOf(movimiento.getFecha()));

            stmt.executeUpdate();

            try(ResultSet resultSet = stmt.getGeneratedKeys()){
                if (resultSet.next()){
                    Long generatedKey = resultSet.getLong(1);
                    movimiento.setId(generatedKey);
                }
            }

            return movimiento;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
