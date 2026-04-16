package novabank.model.cliente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Clase Cliente
 * @author fgodoyar
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    /**
     * Atributos de la clase.
     */
    private Long id;
    private String nombre;
    private String apellidos;
    private String dni;
    private String email;
    private String telefono;
    private LocalDateTime fecha_creacion;

}
