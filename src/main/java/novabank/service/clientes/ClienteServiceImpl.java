package novabank.service.clientes;

import novabank.model.cliente.Cliente;
import novabank.repository.cliente.ClienteRepository;
import novabank.repository.cliente.ClienteRepositoryJdbc;
import novabank.validation.clientes.ClienteValidator;

import java.time.LocalDate;
import java.util.List;

public class ClienteServiceImpl implements ClienteService {

    private ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = new ClienteRepositoryJdbc();
    }



    @Override
    public Cliente guardar(String nombre, String apellidos, String dni, String email, String telefono) {
        if (!ClienteValidator.confirmarEmail(email)){
            throw new IllegalArgumentException("ERROR: El email proporcionado es inválido.");
        }
        if (!ClienteValidator.confirmarDNI(dni)){
            throw new IllegalArgumentException("ERROR: El DNI proporcionado es inválido.");
        }
        if (!ClienteValidator.confirmarTelefono(telefono)){
            throw new IllegalArgumentException("ERROR: El teléfono proporcionado es inválido.");
        }

        LocalDate fecha_creacion = LocalDate.now();
        Cliente cliente = new Cliente();
        cliente.setFecha_creacion(fecha_creacion);
        cliente.setNombre(nombre);
        cliente.setApellidos(apellidos);
        cliente.setDni(dni);
        cliente.setEmail(email);
        cliente.setTelefono(telefono);

        return clienteRepository.guardar(cliente);
    }

    @Override
    public Cliente buscarPorId(Long id) {
        return clienteRepository
                .buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ERROR: El cliente no existe"));
    }

    @Override
    public Cliente buscarPorDni(String dni) {
        return clienteRepository
                .buscarPorDni(dni)
                .orElseThrow(() -> new IllegalArgumentException("ERROR: El cliente no existe"));
    }

    @Override
    public List<Cliente> listarTodos() {
        return clienteRepository.listarTodos();
    }

    @Override
    public void eliminar(Long id) {
        buscarPorId(id);
        clienteRepository.eliminar(id);
    }
}
