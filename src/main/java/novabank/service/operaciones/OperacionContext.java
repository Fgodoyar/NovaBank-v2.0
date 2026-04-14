package novabank.service.operaciones;

import java.math.BigDecimal;

// Contexto que delega en la estrategia configurada
public class OperacionContext {

    private OperacionFinanciera estrategia;

    public void setEstrategia(OperacionFinanciera estrategia) {
        this.estrategia = estrategia;
    }

    public void ejecutar(String numeroCuenta, BigDecimal monto) {
        estrategia.ejecutar(numeroCuenta, monto);
    }
}
