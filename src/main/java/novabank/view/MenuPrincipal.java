package novabank.view;

import java.util.Scanner;

import static novabank.view.MenuCuenta.menuCuenta;

public class MenuPrincipal {

    /**
     * Menú de cada parte del proyecto
     */
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
        int opcion;
        try {
            do{
                System.out.println(MOSTRARTEXTOMENU);
                opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion){
                    case 1:
                        //menuCliente();
                        break;

                    case 2:
                        menuCuenta();
                        break;

                    case 3:
                        //menuOperaciones();
                        break;

                    case 4:
                        //menuConsultas();
                        break;

                    case 5:
                        System.out.println("Bye Bye!");
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }

            }while (opcion != 5);
        }catch (NumberFormatException e){
            System.out.println("ERROR: Se ha introducido un valor inválido.");
        }
    }
}
