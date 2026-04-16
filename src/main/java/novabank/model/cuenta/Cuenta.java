package novabank.model.cuenta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Clase Cuenta
 * @author fgodoyar
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cuenta {
    /**
     * Atributos de la clase
     */
    private Long id;
    private String numero_cuenta;
    private String titular;
    private long cliente_id;
    private BigDecimal saldo;
    private LocalDateTime fecha_creacion;

}