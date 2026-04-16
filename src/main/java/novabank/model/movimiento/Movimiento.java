package novabank.model.movimiento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Clase Movimiento
 */
public class Movimiento {
    /**
     * Atributos de la clase
     */

    private Long id;
    private Long id_cuenta;
    private TipoMovimiento tipo;
    private BigDecimal cantidad;
    private String descripcion;
    private LocalDateTime fecha;

    /**
     * Constructor vacío
     */
    public Movimiento() {
    }

    public Movimiento(Long id_cuenta, TipoMovimiento tipo, BigDecimal cantidad, String descripcion, LocalDateTime fecha) {
        this.id_cuenta = id_cuenta;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    public Movimiento(Long id, Long id_cuenta, TipoMovimiento tipo, BigDecimal cantidad, String descripcion, LocalDateTime fecha) {
        this.id = id;
        this.id_cuenta = id_cuenta;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.fecha = fecha;
    }

    /**
     * Obtiene el ID del movimiento.
     * @return ID del movimiento
     */
    public Long getId() {
        return id;
    }

    /**
     * Inserta el ID del movimiento.
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el ID de la cuenta.
     * @return ID de la cuenta
     */
    public Long getId_cuenta() {
        return id_cuenta;
    }

    /**
     * Inserta el ID de la cuenta.
     * @param id_cuenta
     */
    public void setId_cuenta(Long id_cuenta) {
        this.id_cuenta = id_cuenta;
    }

    /**
     * Obtiene el tipo de movimiento.
     * @return tipo de movimiento
     */
    public TipoMovimiento getTipo() {
        return tipo;
    }

    /**
     * Inserta el tipo de movimiento
     * @param tipo
     */
    public void setTipo(TipoMovimiento tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtiene la cantidad del movimiento.
     * @return cantidad del movimiento
     */
    public BigDecimal getCantidad() {
        return cantidad;
    }

    /**
     * Inserta la cantidad del movimiento.
     * @param cantidad
     */
    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtener la descripción del movimiento
     * @return descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Insertar la descripción del movimiento
     * @param descripcion
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la fecha en la que se realizó el movimiento.
     * @return fecha de movimiento
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * Inserta la fecha del movimiento.
     * @param fecha
     */
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}