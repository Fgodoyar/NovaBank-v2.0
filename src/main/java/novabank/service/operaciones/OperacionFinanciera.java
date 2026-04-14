package novabank.service.operaciones;

import java.math.BigDecimal;

public interface OperacionFinanciera {
    void ejecutar(String numeroCuenta, BigDecimal monto);
}
