package novabank.model.movimiento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class MovimientoFactory {

    public static Movimiento crearDeposito(Long cuentaId, BigDecimal monto) {
        return new Movimiento(
                cuentaId,
                TipoMovimiento.DEPOSITO,
                monto,
                "Deposito en cuenta",
                LocalDateTime.now()
        );
    }

    public static Movimiento crearRetiro(Long cuentaId, BigDecimal monto) {
        return new Movimiento(
                cuentaId,
                TipoMovimiento.RETIRO,
                monto,
                "Retirada de fondos",
                LocalDateTime.now()
        );
    }

    public static Movimiento crearTransferenciaSaliente(
            Long cuentaId,
            BigDecimal monto,
            String cuentaDestino) {

        String descripcion = "Transferencia enviada a " + cuentaDestino;

        return new Movimiento(
                cuentaId,
                TipoMovimiento.TRANSFERENCIA_SALIENTE,
                monto,
                descripcion,
                LocalDateTime.now()

        );
    }

    public static Movimiento crearTransferenciaEntrante(
            Long cuentaId,
            BigDecimal monto,
            String cuentaOrigen) {

        String descripcion = "Transferencia recibida de " + cuentaOrigen;

        return new Movimiento(
                cuentaId,
                TipoMovimiento.TRANSFERENCIA_ENTRANTE,
                monto,
                descripcion,
                LocalDateTime.now()
        );
    }
}
