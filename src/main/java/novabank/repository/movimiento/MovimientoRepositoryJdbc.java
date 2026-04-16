package novabank.repository.movimiento;

import novabank.config.DatabaseConnectionManager;
import novabank.model.movimiento.Movimiento;
import novabank.model.movimiento.TipoMovimiento;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase MovimientoRepositoryJdbc que implementa la interfaz MovimientoRepository
 * Esta clase es la capa de acceso a datos relacionado con los movimientos
 */
public class MovimientoRepositoryJdbc implements MovimientoRepository {

    /**
     * Consultas de la clase
     */
    private static final String INSERT = """
            INSERT INTO Movimientos (id_cuenta, tipo, cantidad, descripcion)
            VALUES (?,?,?,?)
            """;

    private static final String SEARCH_BY_ID_CUENTA = "SELECT * FROM Movimientos WHERE id_cuenta = ?";

    private static final String SEARCH_BY_ID_CUENTA_FECHAS = """
            SELECT *
            FROM Movimientos
            WHERE id_cuenta = ? 
            AND fecha BETWEEN ? AND ?
            ORDER BY fecha ASC
            """;

    /**
     * Método guardar que se encarga de realizar la consulta INSERT en la base de datos.
     * Asignamos los valores con PreparedStatement para evitar inyecciones SQL y ejecutamos
     * la consulta, después, obtenemos la clave autogenerada de la base de datos con el ResultSet
     * y devolvemos el movimiento guardado.
     * @param movimiento
     * @return el movimiento guardado
     */
    @Override
    public Movimiento guardar(Movimiento movimiento) {
        try(Connection conn = DatabaseConnectionManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)){
            stmt.setLong(1, movimiento.getId_cuenta());
            stmt.setString(2, movimiento.getTipo().toString());
            stmt.setBigDecimal(3, movimiento.getCantidad());
            stmt.setString(4, movimiento.getDescripcion());

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

    /**
     * Método para buscar los movimientos de una cuenta por su id a través de la consulta SEARCH_BY_ID_CUENTA.
     * Asignamos los valores con PreparedStatement para evitar inyecciones SQL y ejecutamos
     * la consulta, después, añadimos los movimientos a la lista, leemos
     * los datos con el ResultSet utilizando el método mapearMovimientos y devolvemos la lista.
     * @param cuentaId
     * @return movimientos encontrados
     */
    @Override
    public List<Movimiento> buscarPorCuentaId(Long cuentaId) {
        List<Movimiento> movimientos = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SEARCH_BY_ID_CUENTA)) {

            stmt.setLong(1, cuentaId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
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
                TipoMovimiento.valueOf(rs.getString("tipo")),
                rs.getBigDecimal("cantidad"),
                rs.getString("descripcion"),
                rs.getObject("fecha", LocalDateTime.class)
        );
    }

    /**
     * Método para buscar los movimientos de una cuenta por su id dentro de un rango de fechas a través de la consulta SEARCH_BY_ID_CUENTA_FECHAS.
     * Asignamos los valores con PreparedStatement para evitar inyecciones SQL y ejecutamos
     * la consulta, después, añadimos los movimientos a la lista, leemos
     * los datos con el ResultSet utilizando el método mapearMovimientos y devolvemos la lista.
     * @param cuentaId
     * @return movimientos encontrados
     */
    @Override
    public List<Movimiento> buscarPorCuentaIdYFechas(Long cuentaId, LocalDate inicio, LocalDate fin) {
        List<Movimiento> movimientos = new ArrayList<>();
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SEARCH_BY_ID_CUENTA_FECHAS)) {

            stmt.setLong(1, cuentaId);
            stmt.setDate(2, Date.valueOf(inicio));
            stmt.setDate(3, Date.valueOf(fin));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Movimiento movimiento = mapearMovimiento(rs);
                movimientos.add(movimiento);
            }

            return movimientos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
            // "Error al buscar la cuenta con id " + cuentaId,
        }
    }

    /**
     * Método guardar que se encarga de realizar la consulta INSERT en la base de datos.
     * Asignamos los valores con PreparedStatement para evitar inyecciones SQL y ejecutamos
     * la consulta, después, obtenemos la clave autogenerada de la base de datos con el ResultSet
     * y devolvemos el movimiento guardado.
     * Recibe la conexión como parámetro para que las operaciones usen la misma transacción.
     * @param movimiento
     * @param conn
     * @return el movimiento guardado
     */
    @Override
    public Movimiento guardar(Movimiento movimiento, Connection conn) {
        try(PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)){
            stmt.setLong(1, movimiento.getId_cuenta());
            stmt.setString(2, movimiento.getTipo().toString());
            stmt.setBigDecimal(3, movimiento.getCantidad());
            stmt.setString(4, movimiento.getDescripcion());

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
