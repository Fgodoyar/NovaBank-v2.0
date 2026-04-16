package novabank.service.movimientos;

import novabank.model.movimiento.Movimiento;
import novabank.model.movimiento.TipoMovimiento;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interfaz MovimientoService que define los métodos que tendrá su futura implementación.
 */
public interface MovimientoService {
    Movimiento guardar(Movimiento movimiento);
    List<Movimiento> buscarPorCuentaId(Long cuentaId);
    List<Movimiento> buscarPorCuentaIdYFechas(Long cuentaId, String inicio, String fin);

    Movimiento guardar(Movimiento movimiento, Connection conn);
}
