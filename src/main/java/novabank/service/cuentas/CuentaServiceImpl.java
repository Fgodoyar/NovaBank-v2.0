package novabank.service.cuentas;

import novabank.model.cuenta.Cuenta;
import novabank.repository.cuenta.CuentaRepository;
import novabank.repository.cuenta.CuentaRepositoryJdbc;

import java.math.BigDecimal;
import java.util.List;

public class CuentaServiceImpl implements CuentaService{

    private CuentaRepository cuentaRepository;

    public CuentaServiceImpl(CuentaRepository cuentaRepository) {
        this.cuentaRepository = new CuentaRepositoryJdbc();
    }

    @Override
    public Cuenta guardar(Long id_titular) {
        return null;
    }

    @Override
    public Cuenta buscarPorId(Long id) {
        return null;
    }

    @Override
    public Cuenta buscarPorNumero(String numeroCuenta) {
        return null;
    }

    @Override
    public List<Cuenta> buscarPorClienteId(Long clienteId) {
        return List.of();
    }

    @Override
    public Cuenta actualizarSaldo(Long cuentaId, BigDecimal nuevoSaldo) {
        return null;
    }
}
