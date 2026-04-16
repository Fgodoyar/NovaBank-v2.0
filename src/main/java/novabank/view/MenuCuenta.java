package novabank.view;

import novabank.model.cuenta.Cuenta;
import novabank.repository.cliente.ClienteRepositoryJdbc;
import novabank.repository.cuenta.CuentaRepositoryJdbc;
import novabank.service.cuentas.CuentaService;
import novabank.service.cuentas.CuentaServiceImpl;

import java.util.List;
import java.util.Scanner;

import static novabank.view.MenuPrincipal.menuPrincipal;

public class MenuCuenta {

    private static CuentaService cuentaService =
            new CuentaServiceImpl(new CuentaRepositoryJdbc(), new ClienteRepositoryJdbc());

    private static final String MOSTRARTEXTOCUENTAS = """
            --- GESTIÓN DE CUENTAS --- 
            1. Crear cuenta 
            2. Listar cuentas de cliente 
            3. Ver información de cuenta 
            4. Volver
            """;

    /**
     * Menú que llama a los métodos de cuenta
     */
    public static void menuCuenta(Scanner scanner) {
        int opcion;

        do{
            System.out.println(MOSTRARTEXTOCUENTAS);
            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion){
                case 1:
                    try {
                        System.out.println("Introduce el ID del cliente: ");
                        Long idCliente = Long.parseLong(scanner.nextLine());

                        cuentaService.guardar(idCliente);

                        System.out.println("\nCuenta creada correctamente.\n");

                    } catch (NumberFormatException e) {
                        System.out.println("Valor no válido.");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    try {
                        System.out.println("Introduce el ID del cliente: ");
                        Long idCliente = Long.parseLong(scanner.nextLine());

                        List<Cuenta> cuentas = cuentaService.buscarPorClienteId(idCliente);

                        if (cuentas.isEmpty()) {
                            System.out.println("El cliente no tiene cuentas.");
                        } else {
                            System.out.printf("%-5s %-20s %-15s %-15s%n",
                                    "ID", "Número Cuenta", "Saldo", "Fecha creación");
                            System.out.println("-------------------------------------------------------------");

                            cuentas.forEach(cuenta -> {
                                System.out.printf("%-5d %-20s %-15s %-15s%n",
                                        cuenta.getId(),
                                        cuenta.getNumero_cuenta(),
                                        cuenta.getSaldo() + " €",
                                        cuenta.getFecha_creacion());
                            });

                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Valor no válido.");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 3:
                    try {
                        System.out.println("Introduce el número de la cuenta del cliente: ");
                        String numeroCuenta = scanner.nextLine();

                        Cuenta cuenta = cuentaService.buscarPorNumero(numeroCuenta);

                        System.out.println("ID: " + cuenta.getId());
                        System.out.println("Número: " + cuenta.getNumero_cuenta());
                        System.out.println("Titular: " + cuenta.getTitular());
                        System.out.println("Saldo: " + cuenta.getSaldo() + " €");
                        System.out.println("Fecha creación: " + cuenta.getFecha_creacion());

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 4:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        }while(opcion != 4);
    }
}
