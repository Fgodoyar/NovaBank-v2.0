package service;

import novabank.model.cliente.Cliente;
import novabank.repository.cliente.ClienteRepository;
import novabank.service.clientes.ClienteService;
import novabank.service.clientes.ClienteServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Long clienteId;
    private String nombre;
    private String apellidos;
    private String dni;
    private String email;
    private String telefono;

    @BeforeEach
    void setUp() {
        clienteId = 1L;
        nombre = "Pepillo";
        apellidos = "grillo";
        dni = "76543210A";
        email = "pepeergrillo@email.com";
        telefono = "654789345";
    }

    @Test
    void guardar_camposVacios_debeSalirExcepcion(){

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                clienteService.guardar(" ", " ", " ", " ", " ")
        );

        verify(clienteRepository, never()).guardar(any());
    }

    @Test
    void guardar_dniIncorrecto_debeSalirExcepcion(){
        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                clienteService.guardar(nombre, apellidos, "3", email, telefono)
        );

        verify(clienteRepository, never()).guardar(any());
    }

    @Test
    void guardar_emailIncorrecto_debeSalirExcepcion(){
        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                clienteService.guardar(nombre, apellidos, dni, "patata", telefono)
        );

        verify(clienteRepository, never()).guardar(any());
    }

    @Test
    void guardar_telefonoIncorrecto_debeSalirExcepcion(){
        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                clienteService.guardar(nombre, apellidos, email, email, "123")
        );

        verify(clienteRepository, never()).guardar(any());
    }

    @Test
    void guardar_correctamente(){

        clienteService.guardar(nombre, apellidos, dni, email, telefono);

        verify(clienteRepository, atLeast(1)).guardar(any());
    }

    @Test
    void buscarPorId_existente_devuelveCliente() {
        Cliente cliente = Cliente.builder()
                .id(clienteId)
                .nombre(nombre)
                .apellidos(apellidos)
                .dni(dni)
                .email(email)
                .telefono(telefono)
                .fecha_creacion(LocalDateTime.now())
                .build();

        when(clienteRepository.buscarPorId(clienteId))
                .thenReturn(Optional.of(cliente));

        Cliente resultado = clienteService.buscarPorId(clienteId);

        assertEquals(cliente, resultado);
        verify(clienteRepository).buscarPorId(clienteId);
    }

    @Test
    void buscarPorId_noExistente_lanzaExcepcion() {
        when(clienteRepository.buscarPorId(clienteId))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> clienteService.buscarPorId(1L));

        verify(clienteRepository).buscarPorId(1L);
    }

    @Test
    void buscarPorDni_existente_devuelveCliente() {
        Cliente cliente = Cliente.builder()
                .id(clienteId)
                .nombre(nombre)
                .apellidos(apellidos)
                .dni(dni)
                .email(email)
                .telefono(telefono)
                .fecha_creacion(LocalDateTime.now())
                .build();

        when(clienteRepository.buscarPorDni(dni))
                .thenReturn(Optional.of(cliente));

        Cliente resultado = clienteService.buscarPorDni(dni);

        assertEquals(cliente, resultado);
        verify(clienteRepository).buscarPorDni(dni);
    }

    @Test
    void buscarPorDni_noExistente_lanzaExcepcion() {
        when(clienteRepository.buscarPorDni(dni))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> clienteService.buscarPorDni(dni));

        verify(clienteRepository).buscarPorDni(dni);
    }

    @Test
    void listarTodos_devuelveListaClientes() {
        Cliente cliente1 = Cliente.builder()
                .id(clienteId)
                .nombre(nombre)
                .apellidos(apellidos)
                .dni(dni)
                .email(email)
                .telefono(telefono)
                .fecha_creacion(LocalDateTime.now())
                .build();

        Cliente cliente2 = Cliente.builder()
                .id(2L)
                .nombre("Pepilla")
                .apellidos("pili")
                .dni("77654321L")
                .email("pepilla54@gmail.com")
                .telefono("654789345")
                .fecha_creacion(LocalDateTime.now())
                .build();
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(cliente1);
        clientes.add(cliente2);

        when(clienteRepository.listarTodos())
                .thenReturn(clientes);

        List<Cliente> resultado = clienteService.listarTodos();

        assertEquals(2, resultado.size());
        verify(clienteRepository).listarTodos();
    }

    @Test
    void eliminar_clienteExistente_llamaRepositorio() {
        Cliente cliente = Cliente.builder()
                .id(clienteId)
                .nombre(nombre)
                .apellidos(apellidos)
                .dni(dni)
                .email(email)
                .telefono(telefono)
                .fecha_creacion(LocalDateTime.now())
                .build();

        when(clienteRepository.buscarPorId(1L))
                .thenReturn(Optional.of(cliente));

        clienteService.eliminar(1L);

        verify(clienteRepository).eliminar(1L);
    }

    @Test
    void eliminar_clienteNoExistente_lanzaExcepcion() {
        when(clienteRepository.buscarPorId(1L))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> clienteService.eliminar(1L));

        verify(clienteRepository, never()).eliminar(anyLong());
    }

}
