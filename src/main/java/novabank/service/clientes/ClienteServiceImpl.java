package novabank.service.clientes;

import novabank.model.cliente.Cliente;
import novabank.repository.cliente.ClienteRepository;
import novabank.repository.cliente.ClienteRepositoryJdbc;
import novabank.validation.clientes.ClienteValidator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Clase ClienteServiceImpl que implementa los métodos de ClienteService.
 * Esta clase se encarga de llevar toda la lógica de negocio relacionada con los clientes.
 */
public class ClienteServiceImpl implements ClienteService {

    /**
     * Atributos de la clase
     */
    private ClienteRepository clienteRepository;

    /**
     * Constructor de la clase.
     * @param clienteRepository
     */
    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Métdodo guardar que se encarga de guardar un cliente en la base de datos.
     * Para crear la cuenta, se rige por las siguientes verificaciones.
     * Primero, se encarga de que ningún atributo esté en blanco.
     * Después, llama a la clase ClienteValidator para comprobar que el email, dni y teléfono cumplan
     * su respectivo patrón.
     * Por último, haciendo uso del método builder() de la librería lombok, insertamos los atributos en el objeto cliente,
     * llamamos al repositorio y guardamos al cliente.
     * @param nombre
     * @param apellidos
     * @param dni
     * @param email
     * @param telefono
     * @return
     */
    @Override
    public Cliente guardar(String nombre, String apellidos, String dni, String email, String telefono) {

        if (nombre == null || nombre.trim().isEmpty() ||
                apellidos == null || apellidos.trim().isEmpty() ||
                dni == null || dni.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                telefono == null || telefono.trim().isEmpty()) {
            throw new RuntimeException("ERROR: Por favor, rellene todos los campos.");
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
                .fecha_creacion(LocalDateTime.now())
                .build();

        return clienteRepository.guardar(cliente);
    }

    /**
     * Método que busca una cliente por su id.
     * Realiza una llamada al método del repositorio y en caso de que exista, retorna la cliente.
     * @param id
     * @return el cliente encontrado
     */
    @Override
    public Cliente buscarPorId(Long id) {
        return clienteRepository
                .buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ERROR: El cliente no existe"));
    }

    /**
     * Método que busca una cliente por su dni.
     * Realiza una llamada al método del repositorio y en caso de que exista, retorna la cliente.
     * @param dni
     * @return el cliente encontrado
     */
    @Override
    public Cliente buscarPorDni(String dni) {
        return clienteRepository
                .buscarPorDni(dni)
                .orElseThrow(() -> new IllegalArgumentException("ERROR: El cliente no existe"));
    }

    /**
     * Método que se encarga de imprimir una lista de todos los clientes de la base de datos.
     * Llama al método del repositorio y lista a los clientes
     * @return lista de clientes
     */
    @Override
    public List<Cliente> listarTodos() {
        return clienteRepository.listarTodos();
    }

    /**
     * Método encargado de eliminar a un cliente por su id.
     * Llama al método del repositorio y elimina al cliente.
     * @param id
     */
    @Override
    public void eliminar(Long id) {
        Cliente cliente = buscarPorId(id);
        if (cliente != null) {
            clienteRepository.eliminar(id);
        }
    }
}
