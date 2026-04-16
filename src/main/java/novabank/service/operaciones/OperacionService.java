package novabank.service.operaciones;

import java.math.BigDecimal;

/**
 * Interfaz OperacionService que especifica los métodos que debe tener su futura implementación
 */
public interface OperacionService {
    void depositar(String numeroCuenta, BigDecimal monto);
    void retirar(String numeroCuenta, BigDecimal monto);
    void transferir(String numeroCuentaOrigen, String numeroCuentaDestino, BigDecimal importe);
}
