package novabank.repository.cuenta;

import novabank.config.DatabaseConnectionManager;
import novabank.model.cuenta.Cuenta;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Clase CuentaRepositoryJdbc que implementa los métodos de CuentaRepository.
 * Esta clase es la capa de acceso a datos relacionada con Cuentas.
 */
public class CuentaRepositoryJdbc implements CuentaRepository{

    /**
     * Consultas de la clase
     */
    private static final String SEARCH_BY_ID = "SELECT * FROM Cuentas WHERE id_cuenta = ?";

    private static final String SEARCH_BY_IBAN = "SELECT * FROM Cuentas WHERE numero_cuenta = ?";

    private static final String SEARCH_BY_ID_CLIENTE = "SELECT * FROM Cuentas WHERE id_cliente = ?";

    private static final String CONSULTA_SALDO = "SELECT saldo FROM Cuentas WHERE id_cuenta = ?";

    private static final String INSERT = """
            INSERT INTO Cuentas (numero_cuenta, titular, id_cliente, saldo, fecha_creacion)
            VALUES (?,?,?,?,?)
            """;

    private static final String UPDATE_SALDO = "UPDATE Cuentas SET saldo = ? WHERE id_cuenta = ?";

    /**
     * Método guardar que se encarga de realizar la consulta INSERT en la base de datos.
     * Asignamos los valores con PreparedStatement para evitar inyecciones SQL y ejecutamos
     * la consulta, después, obtenemos la clave autogenerada de la base de datos con el ResultSet
     * y devolvemos la cuenta guardada.
     * @param cuenta
     * @return la cuenta guardada
     */
    @Override
    public Cuenta guardar(Cuenta cuenta) {
        try(Connection conn = DatabaseConnectionManager.getConnection();
                PreparedStatement stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)){
            stmt.setString(1, cuenta.getNumero_cuenta());
            stmt.setString(2, cuenta.getTitular());
            stmt.setLong(3, cuenta.getCliente_id());
            stmt.setBigDecimal(4, cuenta.getSaldo());
            stmt.setObject(5, cuenta.getFecha_creacion());

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

    /**
     * Método para buscar una cuenta por su id a través de la consulta SEARCH_BY_ID.
     * Asignamos los valores con PreparedStatement para evitar inyecciones SQL y
     * después, ejecutamos la consulta, leemos los datos con el ResultSet utilizando
     * el método mapearCuenta y devolvemos la cuenta.
     * @param id
     * @return cuenta encontrada
     */
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
                rs.getObject("fecha_creacion", LocalDateTime.class)
        );
    }

    /**
     * Método para buscar una cuenta por su numero de cuenta a través de la consulta SEARCH_BY_IBAN.
     * Asignamos los valores con PreparedStatement para evitar inyecciones SQL y
     * después, ejecutamos la consulta, leemos los datos con el ResultSet utilizando
     * el método mapearCuenta y devolvemos la cuenta.
     * @param numeroCuenta
     * @return cuenta encontrada
     */
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

    /**
     * Método para buscar las cuentas de un cliente por su id a través de la consulta SEARCH_BY_ID_CLIENTE.
     * Asignamos los valores con PreparedStatement para evitar inyecciones SQL y ejecutamos
     * la consulta, después, añadimos las cuentas a la lista, leemos
     * los datos con el ResultSet utilizando el método mapearCuenta y devolvemos la lista.
     * @param clienteId
     * @return cuentas encontradas
     */
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

    /**
     * Método que actualiza el saldo de una cuenta a través de la consulta UPDATE_SALDO
     * Asignamos los valores con PreparedStatement para evitar inyecciones SQL, ejecutamos
     * la consulta y devolvemos el saldo actualizado.
     * @param cuentaId
     * @param nuevoSaldo
     * @return saldo actualizado
     */
    @Override
    public Cuenta actualizarSaldo(Long cuentaId, BigDecimal nuevoSaldo) {
        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_SALDO)) {
            stmt.setBigDecimal(1, nuevoSaldo);
            stmt.setLong(2, cuentaId);
            stmt.executeUpdate();

            return buscarPorId(cuentaId)
                    .orElseThrow(() -> new RuntimeException("Cuenta no encontrada tras actualización"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Método que se encarga de mostrar el saldo actual de una cuenta a través de la consulta CONSULTA_SALDO
     * Asignamos los valores con PreparedStatement para evitar inyecciones SQL, ejecutamos
     * la consulta y devolvemos el saldo actual.
     * @param cuentaId
     * @return
     */
    @Override
    public BigDecimal consultarSaldo(Long cuentaId) {

        try (Connection conn = DatabaseConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(CONSULTA_SALDO)) {

            stmt.setLong(1, cuentaId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBigDecimal("saldo");
            } else {
                throw new RuntimeException("Cuenta no encontrada con id: " + cuentaId);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar el saldo de la cuenta con id: " + cuentaId, e);
        }
    }


    /**
     * Método para buscar una cuenta por su numero de cuenta a través de la consulta SEARCH_BY_IBAN.
     * Asignamos los valores con PreparedStatement para evitar inyecciones SQL y
     * después, ejecutamos la consulta, leemos los datos con el ResultSet utilizando
     * el método mapearCuenta y devolvemos la cuenta.
     * Recibe la conexión como parámetro para que las operaciones usen la misma transacción.
     * @param numeroCuenta
     * @param conn
     * @return cuenta encontrada
     */
    @Override
    public Optional<Cuenta> buscarPorNumero(String numeroCuenta, Connection conn) {
        try (PreparedStatement stmt = conn.prepareStatement(SEARCH_BY_IBAN)) {

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

    /**
     * Método que actualiza el saldo de una cuenta a través de la consulta UPDATE_SALDO
     * Asignamos los valores con PreparedStatement para evitar inyecciones SQL, ejecutamos
     * la consulta y devolvemos el saldo actualizado.
     * Recibe la conexión como parámetro para que las operaciones usen la misma transacción.
     * @param cuentaId
     * @param nuevoSaldo
     * @param conn
     * @return saldo actualizado
     */
    @Override
    public Cuenta actualizarSaldo(Long cuentaId, BigDecimal nuevoSaldo, Connection conn) {
        try (PreparedStatement stmt = conn.prepareStatement(UPDATE_SALDO)) {
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
