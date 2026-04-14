package novabank.service.cuentas;

import novabank.model.cuenta.Cuenta;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

public interface CuentaService {
    Cuenta guardar(Long id_titular);
    Cuenta buscarPorId(Long id);
    Cuenta buscarPorNumero(String numeroCuenta);
    List<Cuenta> buscarPorClienteId(Long clienteId);
    Cuenta actualizarSaldo(Long cuentaId, BigDecimal nuevoSaldo);
    BigDecimal consultarSaldo(Long cuentaId);
    Cuenta buscarPorNumero(String numeroCuenta, Connection conn);
    Cuenta actualizarSaldo(Long cuentaId, BigDecimal nuevoSaldo, Connection conn);
}
