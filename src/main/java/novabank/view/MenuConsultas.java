package novabank.view;

import novabank.model.movimiento.Movimiento;
import novabank.repository.cliente.ClienteRepositoryJdbc;
import novabank.repository.cuenta.CuentaRepository;
import novabank.repository.cuenta.CuentaRepositoryJdbc;
import novabank.repository.movimiento.MovimientoRepositoryJdbc;
import novabank.service.cuentas.CuentaService;
import novabank.service.cuentas.CuentaServiceImpl;
import novabank.service.movimientos.MovimientoService;
import novabank.service.movimientos.MovimientoServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static novabank.view.MenuPrincipal.menuPrincipal;

public class MenuConsultas {

    private static CuentaService cuentaService =
            new CuentaServiceImpl(new CuentaRepositoryJdbc(), new ClienteRepositoryJdbc());

    private static MovimientoService movimientoService =
            new MovimientoServiceImpl(new MovimientoRepositoryJdbc());

    private static final String MOSTRARTEXTOCONSULTAS = """
            --- CONSULTAS ---
            1. Consultar saldo
            2. Historial de movimientos
            3. Movimientos por rango de fechas
            4. Volver
            """;

    /**
     * Menú consultas, encargado de
     */
    public static void menuConsultas(Scanner scanner) {
        int opcion;

        do{
            System.out.println(MOSTRARTEXTOCONSULTAS);
            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion){
                case 1:
                    System.out.println("Introduzca el id de la cuenta: ");
                    Long cuentaId = Long.parseLong(scanner.nextLine());

                    BigDecimal saldo = cuentaService.consultarSaldo(cuentaId);

                    System.out.println("\n========== CONSULTA DE SALDO ==========");
                    System.out.println("Saldo actual: " + saldo + " €");
                    System.out.println("=======================================\n");
                    break;
                case 2:
                    System.out.println("Introduzca el id de la cuenta: ");
                    Long cuenta_Id = Long.parseLong(scanner.nextLine());

                    List<Movimiento> movimientos = movimientoService.buscarPorCuentaId(cuenta_Id);

                    if (movimientos.isEmpty()) {
                        System.out.println("No hay movimientos para esta cuenta.");
                    } else {

                        System.out.printf("%-20s %-15s %-15s%n", "Fecha", "Tipo", "Importe");
                        System.out.println("-----------------------------------------------------------");

                        movimientos.forEach(movimiento -> {
                            String tipo = movimiento.getTipo().toString();
                            String importeFormateado;

                            if ("INGRESO".equalsIgnoreCase(tipo)) {
                                importeFormateado = "+ " + movimiento.getCantidad() + " €";
                            } else {
                                importeFormateado = "- " + movimiento.getCantidad() + " €";
                            }

                            System.out.printf(
                                    "%-20s %-15s %-15s%n",
                                    movimiento.getFecha(),
                                    tipo,
                                    importeFormateado
                            );
                        });

                    }
                    break;

                case 3:
                    try{
                        System.out.println("Introduzca el id de la cuenta: ");
                        Long idCuenta = Long.parseLong(scanner.nextLine());

                        System.out.println("Introduzca una fecha de inicio (yyyy-MM-dd): ");
                        String fechaInicio = scanner.nextLine();

                        System.out.println("Introduzca una fecha de fin (yyyy-MM-dd): ");
                        String fechaFin = scanner.nextLine();

                        List<Movimiento> movimientoList =
                                movimientoService.buscarPorCuentaIdYFechas(idCuenta, fechaInicio, fechaFin);

                        if (movimientoList.isEmpty()) {
                            System.out.println("No hay movimientos en ese rango de fechas.");
                        } else {

                            System.out.printf("%-20s %-15s %-15s%n", "Fecha", "Tipo", "Importe");
                            System.out.println("-----------------------------------------------------------");

                            movimientoList.forEach(movimiento ->{
                                String tipo = movimiento.getTipo().toString();
                                String importeFormateado;

                                if ("INGRESO".equalsIgnoreCase(tipo)) {
                                    importeFormateado = "+ " + movimiento.getCantidad() + " €";
                                } else {
                                    importeFormateado = "- " + movimiento.getCantidad() + " €";
                                }

                                System.out.printf(
                                        "%-20s %-15s %-15s%n",
                                        movimiento.getFecha(),
                                        tipo,
                                        importeFormateado
                                );
                            });

                        }
                    }catch(NumberFormatException e){
                        System.out.println("Valor no válido");
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case 4:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        }while (opcion != 4);
    }
}
