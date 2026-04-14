package novabank.service.operaciones.strategy;

import novabank.config.DatabaseConnectionManager;
import novabank.model.cuenta.Cuenta;
import novabank.model.movimiento.MovimientoFactory;
import novabank.repository.cuenta.CuentaRepository;
import novabank.repository.movimiento.MovimientoRepository;
import novabank.service.operaciones.OperacionFinanciera;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class DepositoStrategy implements OperacionFinanciera {

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;

    public DepositoStrategy(CuentaRepository cuentaRepository,
                            MovimientoRepository movimientoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Override
    public void ejecutar(String numeroCuenta, BigDecimal monto) {

    }
}