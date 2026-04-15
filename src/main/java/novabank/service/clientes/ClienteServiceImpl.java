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
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente guardar(String nombre, String apellidos, String dni, String email, String telefono) {

        if (nombre == null || nombre.trim().isEmpty() ||
                apellidos == null || apellidos.trim().isEmpty() ||
                dni == null || dni.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                telefono == null || telefono.trim().isEmpty()) {
            System.out.println("ERROR: Por favor, rellene todos los campos.");
        }

        String mensajes = "";

        if (!ClienteValidator.confirmarEmail(email)){
            mensajes += "ERROR: El email proporcionado es inválido.\n";
        }
        if (!ClienteValidator.confirmarDNI(dni)){
            mensajes += "ERROR: El DNI proporcionado es inválido.\n";
        }
        if (!ClienteValidator.confirmarTelefono(telefono)){
            mensajes += "ERROR: El teléfono proporcionado es inválido.";
        }
        if(!mensajes.isEmpty()){
            throw new RuntimeException(mensajes);
        }

        Cliente cliente = Cliente.builder()
                .nombre(nombre)
                .apellidos(apellidos)
                .dni(dni)
                .email(email)
                .telefono(telefono)
                .fecha_creacion(LocalDate.now())
                .build();

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
