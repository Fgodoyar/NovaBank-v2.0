package novabank.service.cuentas;

import novabank.model.cuenta.Cuenta;

import java.math.BigDecimal;
import java.util.List;

public interface CuentaService {
    Cuenta guardar(Long id_titular);
    Cuenta buscarPorId(Long id);
    Cuenta buscarPorNumero(String numeroCuenta);
    List<Cuenta> buscarPorClienteId(Long clienteId);
    Cuenta actualizarSaldo(Long cuentaId, BigDecimal nuevoSaldo);
}
