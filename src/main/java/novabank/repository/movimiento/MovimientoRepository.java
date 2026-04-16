package novabank.repository.movimiento;

import novabank.model.movimiento.Movimiento;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interfaz que define los métodos que tendrá su futura implementación
 */
public interface MovimientoRepository {
    Movimiento guardar(Movimiento movimiento);
    List<Movimiento> buscarPorCuentaId(Long cuentaId);
    List<Movimiento> buscarPorCuentaIdYFechas(Long cuentaId, LocalDate inicio, LocalDate fin);

    // Variante transaccional: recibe la Connection activa
    Movimiento guardar(Movimiento movimiento, Connection conn);
}
