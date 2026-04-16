package novabank.service.operaciones;


import novabank.config.DatabaseConnectionManager;
import novabank.model.cuenta.Cuenta;
import novabank.model.movimiento.MovimientoFactory;
import novabank.repository.cuenta.CuentaRepository;
import novabank.repository.movimiento.MovimientoRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Clase OperacionServiceImpl clase que implementa la interfaz OperaciónService.
 * Contiene toda la lógica de las operaciones bancarias (depositar, retirar y transacción).
 *
 * @author fgodoyar
 */
public class OperacionServiceImpl implements OperacionService {

    /**
     * Atributos de la clase
     */
    private MovimientoRepository movimientoRepository;
    private CuentaRepository cuentaRepository;

    /**
     * Constructor de la clase
     * @param movimientoRepository
     * @param cuentaRepository
     */
    public OperacionServiceImpl(MovimientoRepository movimientoRepository, CuentaRepository cuentaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
    }

    /**
     * Método depositar, controla que la cuenta exista y que el valor a depositar no sea menor o igual a 0.
     * Si pasa las verificaciones, actualiza el saldo, guarda el movimiento y guarda los cambios.
     * Si la transacción llega a fallar, realizará un rollback y se revertirán los cambios.
     *
     * @param numeroCuenta
     * @param monto
     */
    @Override
    public void depositar(String numeroCuenta, BigDecimal monto) {
        try(Connection conn = DatabaseConnectionManager.getConnection()){
            conn.setAutoCommit(false);

            try{
                Cuenta cuenta = cuentaRepository
                        .buscarPorNumero(numeroCuenta, conn)
                        .orElseThrow(() -> new RuntimeException("No existe el cuenta con el numero: " + numeroCuenta));

                if (monto.compareTo(BigDecimal.ZERO) <= 0){
                    throw new RuntimeException("Monto no puede ser menor que cero");
                }

                cuentaRepository.actualizarSaldo(
                        cuenta.getId(),
                        cuenta.getSaldo().add(monto),
                        conn
                );

                movimientoRepository.guardar(
                        MovimientoFactory.crearDeposito(
                                cuenta.getId(),
                                monto
                        ),
                        conn
                );

                conn.commit(); // todo ha ido bien: confirmar

            } catch (Exception e) {
                conn.rollback(); // algo ha fallado: deshacer todos los cambios
                throw new RuntimeException(
                        "Error en la transferencia: " + e.getMessage(),
                        e
                );
            }
        }catch(SQLException e){
            throw new RuntimeException("Error de conexión", e);
        }
    }


    /**
     * Método retirar, controla que la cuenta exista, que el valor a depositar no sea menor o igual a 0 y
     * que no se pueda retirar si la cantidad a retirar es mayor al saldo de la cuenta.
     * Si pasa las verificaciones, actualiza el saldo, guarda el movimiento y guarda los cambios.
     * Si la transacción llega a fallar, realizará un rollback y se revertirán los cambios.
     * @param numeroCuenta
     * @param monto
     */
    @Override
    public void retirar(String numeroCuenta, BigDecimal monto) {
        try(Connection conn = DatabaseConnectionManager.getConnection()){
            conn.setAutoCommit(false);

            try{
                Cuenta cuenta = cuentaRepository
                        .buscarPorNumero(numeroCuenta, conn)
                        .orElseThrow(() -> new RuntimeException("No existe el cuenta con el numero: " + numeroCuenta));

                if (cuenta.getSaldo().compareTo(monto) < 0) {
                    throw new RuntimeException("Saldo insuficiente");
                }

                if (monto.compareTo(BigDecimal.ZERO) <= 0){
                    throw new RuntimeException("Monto no puede ser menor que zero");
                }

                cuentaRepository.actualizarSaldo(
                        cuenta.getId(),
                        cuenta.getSaldo().subtract(monto),
                        conn
                );

                movimientoRepository.guardar(
                        MovimientoFactory.crearRetiro(
                                cuenta.getId(),
                                monto
                        ),
                        conn
                );

                conn.commit(); // todo ha ido bien: confirmar

            } catch (Exception e) {
                conn.rollback(); // algo ha fallado: deshacer todos los cambios
                throw new RuntimeException(
                        "Error en la transferencia: " + e.getMessage(),
                        e
                );
            }
        }catch(SQLException e){
            throw new RuntimeException("Error de conexión", e);
        }
    }


    @Override
    public void transferir(String numeroCuentaOrigen, String numeroCuentaDestino, BigDecimal importe) {
        try (Connection conn = DatabaseConnectionManager.getConnection()) {
            conn.setAutoCommit(false); // desactivar autocommit: inicio de transacción

            try {
                // Paso 1: verificar saldo suficiente
                // Se pasa conn para que use la misma conexión/transacción
                Cuenta origen = cuentaRepository
                        .buscarPorNumero(numeroCuentaOrigen, conn)
                        .orElseThrow(() -> new RuntimeException("No se encontró la cuenta origen con número " + numeroCuentaOrigen));

                if (origen.getNumero_cuenta().equals(numeroCuentaDestino)) {
                    throw new RuntimeException("El origen no puede ser igual al destino");
                }

                if (origen.getSaldo().compareTo(importe) < 0) {
                    throw new RuntimeException("Saldo insuficiente");
                }

                // Paso 2: actualizar saldos — ambos en la misma transacción
                cuentaRepository.actualizarSaldo(
                        origen.getId(),
                        origen.getSaldo().subtract(importe),
                        conn
                );

                Cuenta destino = cuentaRepository
                        .buscarPorNumero(numeroCuentaDestino, conn)
                        .orElseThrow(() -> new RuntimeException("No se encontró la cuenta origen con número " + numeroCuentaDestino));

                cuentaRepository.actualizarSaldo(
                        destino.getId(),
                        destino.getSaldo().add(importe),
                        conn
                );

                // Paso 3: registrar movimientos — usando MovimientoFactory
                movimientoRepository.guardar(
                        MovimientoFactory.crearTransferenciaSaliente(
                                origen.getId(),
                                importe,
                                numeroCuentaDestino
                        ),
                        conn
                );

                movimientoRepository.guardar(
                        MovimientoFactory.crearTransferenciaEntrante(
                                destino.getId(),
                                importe,
                                numeroCuentaOrigen
                        ),
                        conn
                );

                conn.commit(); // todo ha ido bien: confirmar

            } catch (Exception e) {
                conn.rollback(); // algo ha fallado: deshacer todos los cambios
                throw new RuntimeException(
                        "Error en la transferencia: " + e.getMessage(),
                        e
                );
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error de conexión", e);
        }
}


}