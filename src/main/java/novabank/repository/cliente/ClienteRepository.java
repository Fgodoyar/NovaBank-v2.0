package novabank.repository.cliente;

import novabank.model.cliente.Cliente;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz ClienteRepository que define los métodos que debe tener su futura implementación
 */
public interface ClienteRepository {
    Cliente guardar(Cliente cliente);
    Optional<Cliente> buscarPorId(Long id);
    Optional<Cliente> buscarPorDni(String dni);
    List<Cliente> listarTodos();
    void eliminar(Long id);
}
