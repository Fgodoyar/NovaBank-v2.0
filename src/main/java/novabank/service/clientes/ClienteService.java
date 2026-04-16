package novabank.service.clientes;

import novabank.model.cliente.Cliente;

import java.util.List;

/**
 * Interfaz que define los métodos que debe tener su futura implementación
 */
public interface ClienteService {
    Cliente guardar(String nombre, String apellidos, String dni, String email, String telefono);
    Cliente buscarPorId(Long id);
    Cliente buscarPorDni(String dni);
    List<Cliente> listarTodos();
    void eliminar(Long id);
}
