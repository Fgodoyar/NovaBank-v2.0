package novabank.service.movimientos;

import novabank.model.movimiento.Movimiento;
import novabank.repository.movimiento.MovimientoRepository;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase MovimientoServiceImpl que implementa los métodos de la interfaz MovimientoService.
 * Se encarga de toda la lógica relacionada a los movimientos bancarios.
 */
public class MovimientoServiceImpl implements MovimientoService {

    /**
     * Atributos de la clase
     */
    private MovimientoRepository movimientoRepository;

    /**
     * Constructor de la clase
     * @param movimientoRepository
     */
    public MovimientoServiceImpl(MovimientoRepository movimientoRepository) {
        this.movimientoRepository = movimientoRepository;
    }


    /**
     * Método guardar que se encarga de recibir un movimiento y guardarlo.
     * Llama al método guardar del repositorio y guarda el movimiento.
     * @param movimiento
     * @return el movimiento guardado
     */
    @Override
    public Movimiento guardar(Movimiento movimiento) {
        return movimientoRepository.guardar(movimiento);
    }

    /**
     * Método que se encarga de llamar al método buscarPorCuentaId del repositorio y busca que la cuenta exista.
     * @param cuentaId
     * @return la cuenta encontrada
     */
    @Override
    public List<Movimiento> buscarPorCuentaId(Long cuentaId) {
        return movimientoRepository.buscarPorCuentaId(cuentaId);
    }

    /**
     * Método que llama al método buscarPorCuentaIdYFechas y devuelve las cuentas dentro de un rango de fechas.
     * @param cuentaId
     * @param inicio
     * @param fin
     * @return la cuenta encontrada
     */
    @Override
    public List<Movimiento> buscarPorCuentaIdYFechas(Long cuentaId, String inicio, String fin) {
        LocalDate fechaInicio;
        LocalDate fechaFin;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            fechaInicio = LocalDate.parse(inicio, formatter);
            fechaFin = LocalDate.parse(fin, formatter);

        } catch (Exception e) {
            throw new IllegalArgumentException("El formato de las fechas es incorrecto.");
        }

        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser anterior a la fecha de fin.");
        }

        List<Movimiento> movFechas = movimientoRepository.buscarPorCuentaIdYFechas(cuentaId, fechaInicio, fechaFin);

        return movFechas;
    }

    /**
     * Método guardar que recibe la conexión como parámetro para que las operaciones usen la misma transacción.
     * @param movimiento
     * @param conn
     * @return el movimiento guardado
     */
    @Override
    public Movimiento guardar(Movimiento movimiento, Connection conn) {
        return movimientoRepository.guardar(movimiento, conn);
    }
}
