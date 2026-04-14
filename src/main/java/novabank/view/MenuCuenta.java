package novabank.view;

import novabank.repository.cliente.ClienteRepositoryJdbc;
import novabank.repository.cuenta.CuentaRepositoryJdbc;
import novabank.service.cuentas.CuentaService;
import novabank.service.cuentas.CuentaServiceImpl;

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
    public static void menuCuenta(){
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do{
            System.out.println(MOSTRARTEXTOCUENTAS);
            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion){
                case 1:
                    try{
                        System.out.println("Introduce el ID del cliente: ");
                        Long id_cliente = Long.parseLong(scanner.nextLine());
                        cuentaService.guardar(id_cliente);

                    }catch (NumberFormatException e){
                        System.out.println("Valor no válido");
                    }
                    break;
                case 2:
                    try{
                        System.out.println("Introduce el ID del cliente: ");
                        Long id_cliente = Long.parseLong(scanner.nextLine());
                        cuentaService.buscarPorClienteId(id_cliente);

                    } catch (Exception e) {
                        System.out.println("Valor no válido");
                    }
                    break;
                case 3:
                    System.out.println("Introduce el número de la cuenta del cliente: ");
                    String numero_cuenta = scanner.nextLine();
                    cuentaService.buscarPorNumero(numero_cuenta);
                    break;
                case 4:
                    menuPrincipal();
                default:
                    System.out.println("Opción no válida.");
            }

        }while(opcion != 4);
    }
}
