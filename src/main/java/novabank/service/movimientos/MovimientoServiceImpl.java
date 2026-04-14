package novabank.service.movimientos;

import novabank.model.movimiento.Movimiento;
import novabank.repository.movimiento.MovimientoRepository;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public class MovimientoServiceImpl implements MovimientoService {

    private MovimientoRepository movimientoRepository;

    public MovimientoServiceImpl(MovimientoRepository movimientoRepository) {
        this.movimientoRepository = movimientoRepository;
    }


    @Override
    public Movimiento guardar(Movimiento movimiento) {
        return movimientoRepository.guardar(movimiento);
    }

    @Override
    public List<Movimiento> buscarPorCuentaId(Long cuentaId) {
        return movimientoRepository.buscarPorCuentaId(cuentaId);
    }

    @Override
    public List<Movimiento> buscarPorCuentaIdYFechas(Long cuentaId, LocalDateTime inicio, LocalDateTime fin) {
        return movimientoRepository.buscarPorCuentaIdYFechas(cuentaId, inicio, fin);
    }

    @Override
    public Movimiento guardar(Movimiento movimiento, Connection conn) {
        return movimientoRepository.guardar(movimiento, conn);
    }
}
