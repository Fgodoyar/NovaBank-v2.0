package novabank.service.cuentas;

import novabank.model.cliente.Cliente;
import novabank.model.cuenta.Cuenta;
import novabank.repository.cliente.ClienteRepository;
import novabank.repository.cuenta.CuentaRepository;
import novabank.validation.cuentas.CuentaValidator;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Clase CuentaServiceImpl que implementa los métodos de CuentaService.
 * Esta clase se encarga de toda la lógica relacionada a las cuentas.
 */
public class CuentaServiceImpl implements CuentaService{

    /**
     * Atributos de la clase
     */
    private CuentaRepository cuentaRepository;
    private ClienteRepository clienteRepository;

    /**
     * Constructor de la clase
     * @param cuentaRepository
     * @param clienteRepository
     */
    public CuentaServiceImpl(CuentaRepository cuentaRepository, ClienteRepository clienteRepository) {
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
    }

    /**
     * Método guardar que se encarga de guardar una cuenta a través del id del cliente.
     * Primero, comprueba que el cliente existe y si existe, se genera un iban para la cuenta,
     * se guarda la fecha de creación de la cuenta y se obtienen todos los datos relacionados con el titular.
     * Por último, llama al método guardar de su repositorio y guarda la cuenta.
     * @param id_titular
     * @return la cuenta guardada
     */
    @Override
    public Cuenta guardar(Long id_titular) {
        Cliente cliente = clienteRepository.buscarPorId(id_titular)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        LocalDateTime fecha_creacion = LocalDateTime.now();
        String iban = CuentaValidator.generarIBAN(cliente.getId());
        Cuenta cuenta = Cuenta.builder()
                .fecha_creacion(fecha_creacion)
                .numero_cuenta(iban)
                .titular(cliente.getNombre() + " " + cliente.getApellidos())
                .cliente_id(cliente.getId())
                .saldo(BigDecimal.ZERO)
                .build();

        return cuentaRepository.guardar(cuenta);
    }

    /**
     * Método que busca una cuenta por su id.
     * Realiza una llamada al método del repositorio y en caso de que exista, retorna la cuenta.
     * @param id
     * @return la cuenta encontrada
     */
    @Override
    public Cuenta buscarPorId(Long id) {
        return cuentaRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
    }

    /**
     * Método que busca una cuenta por su iban.
     * Realiza una llamada al método del repositorio y en caso de que exista, retorna la cuenta.
     * @param numeroCuenta
     * @return la cuenta encontrada
     */
    @Override
    public Cuenta buscarPorNumero(String numeroCuenta) {
        return cuentaRepository.buscarPorNumero(numeroCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
    }

    /**
     * Método que busca una cuenta por el id de cliente.
     * Realiza una llamada al método del repositorio y en caso de que exista, retorna la cuenta.
     * @param clienteId
     * @return la cuenta encontrada
     */
    @Override
    public List<Cuenta> buscarPorClienteId(Long clienteId) {
        return cuentaRepository.buscarPorClienteId(clienteId);
    }

    /**
     * Método que actualiza el saldo de una cuenta.
     * Llama al método del repositorio y actualiza el saldo.
     * @param cuentaId
     * @param nuevoSaldo
     * @return el saldo actualizado
     */
    @Override
    public Cuenta actualizarSaldo(Long cuentaId, BigDecimal nuevoSaldo) {
        return cuentaRepository.actualizarSaldo(cuentaId, nuevoSaldo);
    }

    /**
     * Método para consultar el saldo de una cuenta.
     * Llama al método del repositorio y devuelve el saldo actual de la cuenta.
     * @param cuentaId
     * @return el saldo de la cuenta
     */
    @Override
    public BigDecimal consultarSaldo(Long cuentaId) {
        return cuentaRepository.consultarSaldo(cuentaId);
    }

    /**
     * Método que busca una cuenta por su iban.
     * Realiza una llamada al método del repositorio y en caso de que exista, retorna la cuenta.
     * Recibe la conexión como parámetro para que las operaciones usen la misma transacción.
     * @param numeroCuenta
     * @param conn
     * @return la cuenta encontrada
     */
    @Override
    public Cuenta buscarPorNumero(String numeroCuenta, Connection conn) {
        return cuentaRepository.buscarPorNumero(numeroCuenta, conn)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
    }

    /**
     * Método que actualiza el saldo de una cuenta.
     * Llama al método del repositorio y actualiza el saldo.
     * Recibe la conexión como parámetro para que las operaciones usen la misma transacción.
     * @param cuentaId
     * @param nuevoSaldo
     * @param conn
     * @return el saldo actualizado
     */
    @Override
    public Cuenta actualizarSaldo(Long cuentaId, BigDecimal nuevoSaldo, Connection conn) {
        return cuentaRepository.actualizarSaldo(cuentaId, nuevoSaldo, conn);
    }
}