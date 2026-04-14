package novabank.view;

import novabank.repository.cuenta.CuentaRepositoryJdbc;
import novabank.repository.movimiento.MovimientoRepositoryJdbc;
import novabank.service.operaciones.OperacionService;
import novabank.service.operaciones.OperacionServiceImpl;

import java.math.BigDecimal;
import java.util.Scanner;

import static novabank.view.MenuPrincipal.menuPrincipal;

public class MenuOperaciones {

    private static OperacionService operacionService =
            new OperacionServiceImpl(new MovimientoRepositoryJdbc(), new CuentaRepositoryJdbc());

    private static final String MOSTRARTEXTOOPERACIONES = """
            --- OPERACIONES FINANCIERAS --- 
            1. Depositar dinero 
            2. Retirar dinero 
            3. Transferencia entre cuentas 
            4. Volver
            """;

    /**
     * Menú operaciones, encargado de llamar a los métodos de GestorCuenta
     */
    public static void menuOperaciones(){
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do{
            System.out.println(MOSTRARTEXTOOPERACIONES);
            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion){
                case 1:
                    System.out.println("Introduzca su número de cuenta: ");
                    String numeroCuenta = scanner.nextLine();
                    System.out.println("Introduzca la cantidad a depositar: ");
                    BigDecimal cantidad_ingresar = new BigDecimal(scanner.nextLine());
                    operacionService.depositar(numeroCuenta, cantidad_ingresar);

                    System.out.println("\nDepósito realizado correctamente.\n");
                    break;
                case 2:
                    System.out.println("Introduzca su número de cuenta: ");
                    String numero_cuenta = scanner.nextLine();
                    System.out.println("Introduzca la cantidad a retirar: ");
                    BigDecimal cantidad_retirar = new BigDecimal(scanner.nextLine());
                    operacionService.retirar(numero_cuenta, cantidad_retirar);

                    System.out.println("\nRetirada realizada correctamente.\n");
                    break;
                case 3:
                    System.out.println("Introduzca su número de cuenta: ");
                    String numeroCuentaOrigen = scanner.nextLine();
                    System.out.println("Introduzca el número de cuenta de la persona a la que desea depositar: ");
                    String numeroCuentaDestino = scanner.nextLine();
                    System.out.println("Introduzca la cantidad que desea transferir: ");
                    BigDecimal cantidad_transaccion = new BigDecimal(scanner.nextLine());
                    operacionService.transferir(numeroCuentaOrigen, numeroCuentaDestino, cantidad_transaccion);

                    System.out.println("\nTransferencia realizada correctamente.\n");
                    break;
                case 4:
                    menuPrincipal();
                default:
                    System.out.println("Opción no válida.");
            }

        }while(opcion != 4);
    }
}
