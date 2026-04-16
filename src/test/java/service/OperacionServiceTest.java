package service;

import novabank.model.cuenta.Cuenta;
import novabank.repository.cuenta.CuentaRepository;
import novabank.repository.cuenta.CuentaRepositoryJdbc;
import novabank.repository.movimiento.MovimientoRepository;
import novabank.repository.movimiento.MovimientoRepositoryJdbc;
import novabank.service.operaciones.OperacionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OperacionServiceTest {
    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private MovimientoRepository movimientoRepository;

    @InjectMocks
    private OperacionServiceImpl operacionService;

    @Test
    void depositar_saldoMenorQue0_debeLanzarExcepcion(){
        // Arrange
        Cuenta cuenta = new Cuenta(
                1L,
                "ES91210000000000000001",
                "Pepillo",
                1001L,
                new BigDecimal("100.00"),
                LocalDateTime.now()
        );

        when(cuentaRepository.buscarPorNumero("ES91210000000000000001"))
                .thenReturn(Optional.of(cuenta));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                operacionService.depositar(
                        "ES91210000000000000001",
                        new BigDecimal("0")
                )
        );

        // Verificar que no se actualizó el saldo ni se guardó ningún movimiento
        verify(cuentaRepository, never()).actualizarSaldo(any(), any());
        verify(movimientoRepository, never()).guardar(any());
    }

    @Test
    void retirar_conSaldoInsuficiente_debeLanzarExcepcion() {
        // Arrange
        Cuenta cuenta = new Cuenta(
                1L,
                "ES91210000000000000001",
                "Pepillo",
                1001L,
                new BigDecimal("100.00"),
                LocalDateTime.now()
        );

        when(cuentaRepository.buscarPorNumero("ES91210000000000000001"))
                .thenReturn(Optional.of(cuenta));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                operacionService.retirar(
                        "ES91210000000000000001",
                        new BigDecimal("500.00")
                )
        );

        // Verificar que no se actualizó el saldo ni se guardó ningún movimiento
        verify(cuentaRepository, never()).actualizarSaldo(any(), any());
        verify(movimientoRepository, never()).guardar(any());
    }

    @Test
    void retirar_saldoMenorQue0_debeLanzarExcepcion(){
        // Arrange
        Cuenta cuenta = new Cuenta(
                1L,
                "ES91210000000000000001",
                "Pepillo",
                1001L,
                new BigDecimal("100.00"),
                LocalDateTime.now()
        );

        when(cuentaRepository.buscarPorNumero("ES91210000000000000001"))
                .thenReturn(Optional.of(cuenta));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                operacionService.retirar(
                        "ES91210000000000000001",
                        new BigDecimal("0")
                )
        );

        // Verificar que no se actualizó el saldo ni se guardó ningún movimiento
        verify(cuentaRepository, never()).actualizarSaldo(any(), any());
        verify(movimientoRepository, never()).guardar(any());
    }

    @Test
    void transaccion_mismoNumeroCuenta_debeLanzarExcepcion(){
        // Arrange
        Cuenta cuenta = new Cuenta(
                1L,
                "ES91210000000000000001",
                "Pepillo",
                1001L,
                new BigDecimal("100.00"),
                LocalDateTime.now()
        );

        when(cuentaRepository.buscarPorNumero("ES91210000000000000001"))
                .thenReturn(Optional.of(cuenta));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
                operacionService.transferir(
                        "ES91210000000000000001",
                        "ES91210000000000000001",
                        new BigDecimal("50.00")
                )
        );

        verify(cuentaRepository, never()).actualizarSaldo(any(), any());
        verify(movimientoRepository, never()).guardar(any());
    }

    @Test
    void transferencia_correcta(){
        // Arrange
        Cuenta origen = new Cuenta(
                1L,
                "ES91210000000000000001",
                "Pepillo",
                1001L,
                new BigDecimal("100.00"),
                LocalDateTime.now()
        );

        Cuenta destino = new Cuenta(
                2L,
                "ES91210000000000000002",
                "papilla",
                1002L,
                new BigDecimal("100.00"),
                LocalDateTime.now()
        );

        when(cuentaRepository.buscarPorNumero(eq("ES91210000000000000001"), any(Connection.class)))
                .thenReturn(Optional.of(origen));

        when(cuentaRepository.buscarPorNumero(eq("ES91210000000000000002"), any(Connection.class)))
                .thenReturn(Optional.of(destino));

        // Act & Assert
        operacionService.transferir(
                "ES91210000000000000001",
                "ES91210000000000000002",
                 new BigDecimal("50.00")
        );

        verify(cuentaRepository, atLeast(1)).actualizarSaldo(anyLong(), any(BigDecimal.class), any(Connection.class));
        verify(movimientoRepository, atLeast(1)).guardar(any(), any(Connection.class));
    }

    @Test
    void depositar_correcta(){
        // Arrange
        Cuenta cuenta = new Cuenta(
                1L,
                "ES91210000000000000001",
                "Pepillo",
                1001L,
                new BigDecimal("100.00"),
                LocalDateTime.now()
        );

        when(cuentaRepository.buscarPorNumero(eq("ES91210000000000000001"), any(Connection.class)))
                .thenReturn(Optional.of(cuenta));

        // Act & Assert
        operacionService.depositar(
                "ES91210000000000000001",
                new BigDecimal("50.00")
        );

        verify(cuentaRepository, atLeast(1)).actualizarSaldo(anyLong(), any(BigDecimal.class), any(Connection.class));
        verify(movimientoRepository, atLeast(1)).guardar(any(), any(Connection.class));
    }

    @Test
    void retirar_correcta(){
        // Arrange
        Cuenta cuenta = new Cuenta(
                1L,
                "ES91210000000000000001",
                "Pepillo",
                1001L,
                new BigDecimal("100.00"),
                LocalDateTime.now()
        );

        when(cuentaRepository.buscarPorNumero(eq("ES91210000000000000001"), any(Connection.class)))
                .thenReturn(Optional.of(cuenta));

        // Act & Assert
        operacionService.retirar(
                "ES91210000000000000001",
                new BigDecimal("50.00")
        );

        verify(cuentaRepository, atLeast(1)).actualizarSaldo(anyLong(), any(BigDecimal.class), any(Connection.class));
        verify(movimientoRepository, atLeast(1)).guardar(any(), any(Connection.class));
    }
}
