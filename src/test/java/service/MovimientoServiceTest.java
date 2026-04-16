package service;

import novabank.model.movimiento.Movimiento;
import novabank.repository.movimiento.MovimientoRepository;
import novabank.service.movimientos.MovimientoServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovimientoServiceTest {

    @Mock
    private MovimientoRepository movimientoRepository;

    @InjectMocks
    private MovimientoServiceImpl movimientoService;

    @Test
    void buscarPorCuentaId_ok() {
        List<Movimiento> lista = List.of(mock(Movimiento.class));
        when(movimientoRepository.buscarPorCuentaId(1L)).thenReturn(lista);

        List<Movimiento> result = movimientoService.buscarPorCuentaId(1L);

        assertEquals(lista, result);
        verify(movimientoRepository).buscarPorCuentaId(1L);
    }

    @Test
    void buscarPorCuentaIdYFechas_ok() {
        List<Movimiento> lista = List.of(mock(Movimiento.class));
        when(movimientoRepository.buscarPorCuentaIdYFechas(eq(1L), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(lista);

        List<Movimiento> result = movimientoService.buscarPorCuentaIdYFechas(1L, "2024-01-01", "2024-12-31");

        assertEquals(lista, result);
        verify(movimientoRepository).buscarPorCuentaIdYFechas(eq(1L),
                eq(LocalDate.of(2024, 1, 1)),
                eq(LocalDate.of(2024, 12, 31)));
    }

    @Test
    void buscarPorCuentaIdYFechas_formatoIncorrecto() {
        assertThrows(IllegalArgumentException.class,
                () -> movimientoService.buscarPorCuentaIdYFechas(1L, "01-01-2024", "31-12-2024"));
    }

    @Test
    void buscarPorCuentaIdYFechas_inicioPosteriorAFin() {
        assertThrows(IllegalArgumentException.class,
                () -> movimientoService.buscarPorCuentaIdYFechas(1L, "2024-12-31", "2024-01-01"));
    }
}
