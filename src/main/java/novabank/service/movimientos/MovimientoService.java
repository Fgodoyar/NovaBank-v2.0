package novabank.service.movimientos;

import novabank.model.movimiento.Movimiento;
import novabank.model.movimiento.TipoMovimiento;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoService {
    Movimiento guardar(Movimiento movimiento);
    List<Movimiento> buscarPorCuentaId(Long cuentaId);
    List<Movimiento> buscarPorCuentaIdYFechas(Long cuentaId, LocalDateTime inicio, LocalDateTime fin);

    Movimiento guardar(Movimiento movimiento, Connection conn);
}
