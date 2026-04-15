package novabank.view;

import java.util.Scanner;

import static novabank.view.MenuConsultas.menuConsultas;
import static novabank.view.MenuCliente.menuCliente;
import static novabank.view.MenuCuenta.menuCuenta;

import static novabank.view.MenuOperaciones.menuOperaciones;

/**
 * Menú de cada parte del proyecto
 */
public class MenuPrincipal {

    private static final String MOSTRARTEXTOMENU = """
            ==================================== 
              NOVABANK - SISTEMA DE OPERACIONES 
            ==================================== 
            1. Gestión de clientes
            2. Gestión de cuentas
            3. Operaciones financieras
            4. Consultas
            5. Salir
            """;

    /**
     * Menú principal, redirige a los diversos menús según la opción
     */
    public static void menuPrincipal(){
        Scanner scanner = new Scanner(System.in);
        int opcion = 0;

        do{
            try {
                System.out.println(MOSTRARTEXTOMENU);
                opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion){
                    case 1:
                        menuCliente(scanner);
                        break;

                    case 2:
                        menuCuenta(scanner);
                        break;

                    case 3:
                        menuOperaciones(scanner);
                        break;

                    case 4:
                        menuConsultas(scanner);
                        break;

                    case 5:
                        System.out.println("Bye Bye!");
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }
            }catch (NumberFormatException e){
                System.out.println("ERROR: Se ha introducido un valor inválido.");
            }catch (Exception e){
                System.out.println(e.getMessage());
            }

        }while (opcion != 5);
    }
}
