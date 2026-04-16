package service;

import novabank.model.cliente.Cliente;
import novabank.model.cuenta.Cuenta;
import novabank.repository.cliente.ClienteRepository;
import novabank.repository.cuenta.CuentaRepository;
import novabank.service.cuentas.CuentaService;
import novabank.service.cuentas.CuentaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CuentaServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private CuentaServiceImpl cuentaService;

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
    void guarda_idNoExiste_lanzaExcepcion() {
        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                cuentaService.guardar(2L)
        );

        verify(cuentaRepository, never()).guardar(any());
    }

    @Test
    void guarda_idExiste_guarda_cuenta() {

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

        cuentaService.guardar(clienteId);

        verify(clienteRepository).buscarPorId(clienteId);
        verify(cuentaRepository).guardar(any(Cuenta.class));
    }

    @Test
    void buscarPorId_ok() {
        Cuenta cuenta = mock(Cuenta.class);
        when(cuentaRepository.buscarPorId(1L)).thenReturn(Optional.of(cuenta));
        assertEquals(cuenta, cuentaService.buscarPorId(1L));
    }

    @Test
    void buscarPorId_noExiste() {
        when(cuentaRepository.buscarPorId(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> cuentaService.buscarPorId(1L));
    }

    @Test
    void buscarPorNumero_ok() {
        Cuenta cuenta = mock(Cuenta.class);
        when(cuentaRepository.buscarPorNumero("IBAN")).thenReturn(Optional.of(cuenta));
        assertEquals(cuenta, cuentaService.buscarPorNumero("IBAN"));
    }

    @Test
    void buscarPorNumero_noExiste() {
        when(cuentaRepository.buscarPorNumero("IBAN")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> cuentaService.buscarPorNumero("IBAN"));
    }

    @Test
    void buscarPorClienteId_ok() {
        List<Cuenta> cuentas = List.of(mock(Cuenta.class));
        when(cuentaRepository.buscarPorClienteId(1L)).thenReturn(cuentas);
        assertEquals(cuentas, cuentaService.buscarPorClienteId(1L));
    }

    @Test
    void actualizarSaldo_ok() {
        Cuenta cuenta = mock(Cuenta.class);
        when(cuentaRepository.actualizarSaldo(1L, BigDecimal.TEN)).thenReturn(cuenta);
        assertEquals(cuenta, cuentaService.actualizarSaldo(1L, BigDecimal.TEN));
    }

    @Test
    void consultarSaldo_ok() {
        when(cuentaRepository.consultarSaldo(1L)).thenReturn(BigDecimal.TEN);
        assertEquals(BigDecimal.TEN, cuentaService.consultarSaldo(1L));
    }
}
