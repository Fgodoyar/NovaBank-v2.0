package novabank.service.cuentas;

import novabank.model.cliente.Cliente;
import novabank.model.cuenta.Cuenta;
import novabank.repository.cliente.ClienteRepository;
import novabank.repository.cliente.ClienteRepositoryJdbc;
import novabank.repository.cuenta.CuentaRepository;
import novabank.repository.cuenta.CuentaRepositoryJdbc;
import novabank.validation.cuentas.CuentaValidator;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public class CuentaServiceImpl implements CuentaService{

    private CuentaRepository cuentaRepository;
    private ClienteRepository clienteRepository;

    public CuentaServiceImpl(CuentaRepository cuentaRepository) {
        this.cuentaRepository = new CuentaRepositoryJdbc();
        this.clienteRepository = new ClienteRepositoryJdbc();
    }

    @Override
    public Cuenta guardar(Long id_titular) {
        Cliente cliente = clienteRepository.buscarPorId(id_titular)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        LocalDate fecha_creacion = LocalDate.now();
        String iban = CuentaValidator.generarIBAN();
        Cuenta cuenta = new Cuenta();
        cuenta.setFecha_creacion(fecha_creacion);
        cuenta.setNumero_cuenta(iban);
        cuenta.setCliente_id(cliente.getId());
        cuenta.setTitular(cliente.getNombre() + " " + cliente.getApellidos());
        cuenta.setSaldo(BigDecimal.ZERO);

        return cuentaRepository.guardar(cuenta);
    }

    @Override
    public Cuenta buscarPorId(Long id) {
        return cuentaRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
    }

    @Override
    public Cuenta buscarPorNumero(String numeroCuenta) {
        return cuentaRepository.buscarPorNumero(numeroCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
    }

    @Override
    public List<Cuenta> buscarPorClienteId(Long clienteId) {
        return cuentaRepository.buscarPorClienteId(clienteId);
    }

    @Override
    public Cuenta actualizarSaldo(Long cuentaId, BigDecimal nuevoSaldo) {
        return cuentaRepository.actualizarSaldo(cuentaId, nuevoSaldo);
    }

    @Override
    public Cuenta buscarPorNumero(String numeroCuenta, Connection conn) {
        return cuentaRepository.buscarPorNumero(numeroCuenta, conn)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
    }

    @Override
    public Cuenta actualizarSaldo(Long cuentaId, BigDecimal nuevoSaldo, Connection conn) {
        return cuentaRepository.actualizarSaldo(cuentaId, nuevoSaldo, conn);
    }
}
