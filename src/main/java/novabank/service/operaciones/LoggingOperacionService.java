package novabank.service.operaciones;

import java.math.BigDecimal;

public class LoggingOperacionService implements OperacionService {

    private final OperacionService delegate; // el servicio real envuelto

    public LoggingOperacionService(OperacionService delegate) {
        this.delegate = delegate;
    }

    @Override
    public void depositar(String numeroCuenta, BigDecimal monto) {
        System.out.println("[LOG] Inicio deposito: cuenta=" + numeroCuenta);
        delegate.depositar(numeroCuenta, monto); // delega en el servicio real
        System.out.println("[LOG] Deposito completado.");
    }

    @Override
    public void retirar(String numeroCuenta, BigDecimal monto) {
        System.out.println("[LOG] Inicio retirada: cuenta=" + numeroCuenta);
        delegate.retirar(numeroCuenta, monto);
        System.out.println("[LOG] Retirada completada.");
    }

    @Override
    public void transferir(String numeroCuentaOrigen, String numeroCuentaDestino, BigDecimal importe) {
        System.out.println("[LOG] Inicio transferencia: cuenta=" + numeroCuentaOrigen + " - " + numeroCuentaDestino);
        delegate.transferir(numeroCuentaOrigen, numeroCuentaDestino, importe);
        System.out.println("[LOG] Transferencia completada.");
    }
}

