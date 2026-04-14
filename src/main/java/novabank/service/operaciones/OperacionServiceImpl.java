package novabank.service.operaciones;


import novabank.config.DatabaseConnectionManager;
import novabank.model.cuenta.Cuenta;
import novabank.model.movimiento.MovimientoFactory;
import novabank.repository.cuenta.CuentaRepository;
import novabank.repository.movimiento.MovimientoRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class OperacionServiceImpl implements OperacionService {

    private MovimientoRepository movimientoRepository;
    private CuentaRepository cuentaRepository;

    @Override
    public void depositar(String numeroCuenta, BigDecimal monto) {
        try(Connection conn = DatabaseConnectionManager.getConnection()){
            conn.setAutoCommit(false);

            try{
                Cuenta cuenta = cuentaRepository
                        .buscarPorNumero(numeroCuenta, conn)
                        .orElseThrow(() -> new RuntimeException("No existe el cuenta con el numero: " + numeroCuenta));

                if (monto.compareTo(BigDecimal.ZERO) <= 0){
                    throw new RuntimeException("Monto no puede ser menor que zero");
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

    public void transferir(String numeroCuentaOrigen, String numeroCuentaDestino, BigDecimal importe) {
        try (Connection conn = DatabaseConnectionManager.getConnection()) {
            conn.setAutoCommit(false); // desactivar autocommit: inicio de transacción

            try {
                // Paso 1: verificar saldo suficiente
                // Se pasa conn para que use la misma conexión/transacción
                Cuenta origen = cuentaRepository
                        .buscarPorNumero(numeroCuentaOrigen, conn)
                        .orElseThrow(() -> new RuntimeException("No se encontró la cuenta origen con número " + numeroCuentaOrigen));

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