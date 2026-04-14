package novabank.service.operaciones;

import java.math.BigDecimal;

public interface OperacionService {
    void depositar(String numeroCuenta, BigDecimal monto);
    void retirar(String numeroCuenta, BigDecimal monto);
}
