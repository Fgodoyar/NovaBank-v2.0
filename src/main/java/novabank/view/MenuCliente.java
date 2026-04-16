package novabank.view;

import novabank.model.cliente.Cliente;
import novabank.repository.cliente.ClienteRepositoryJdbc;
import novabank.service.clientes.ClienteService;
import novabank.service.clientes.ClienteServiceImpl;

import java.util.List;
import java.util.Scanner;

import static novabank.view.MenuPrincipal.menuPrincipal;

public class MenuCliente {

    private static ClienteService clienteService = new ClienteServiceImpl(new ClienteRepositoryJdbc());


    private static final String MOSTRARTEXTOCLIENTES = """
            --- GESTIÓN DE CLIENTES --- 
            1. Crear cliente 
            2. Buscar cliente 
            3. Listar clientes 
            4. Eliminar cliente
            5. Volver
            """;

    private static final String BUSQUEDA = """
            --- Seleccione una de las opciones de búsqueda ---
            1. Por ID de Cliente.
            2. Por DNI de Cliente.
            """;

    /**
     * Menú que llama a los métodos de GestorClientes
     */
    public static void menuCliente(Scanner scanner){
        int opcion;

        do{
            System.out.println(MOSTRARTEXTOCLIENTES);
            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion){
                case 1:
                    try{
                        System.out.println("Introduzca el nombre del cliente: ");
                        String nombre = scanner.nextLine();
                        System.out.println("Introduzca los apellidos del cliente: ");
                        String apellidos = scanner.nextLine();
                        System.out.println("Introduzca el DNI del cliente: ");
                        String dni = scanner.nextLine();
                        System.out.println("Introduzca el email del cliente: ");
                        String email = scanner.nextLine();
                        System.out.println("Introduzca el teléfono del cliente: ");
                        String telefono = scanner.nextLine();

                        clienteService.guardar(nombre, apellidos, dni, email, telefono);
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;

                case 2:
                    try {
                        System.out.println(BUSQUEDA);
                        int tipoBusqueda = Integer.parseInt(scanner.nextLine());

                        if (tipoBusqueda == 1) {

                            System.out.println("Introduzca el ID del cliente: ");
                            Long buscarId = Long.parseLong(scanner.nextLine());

                            Cliente cliente = clienteService.buscarPorId(buscarId);

                            System.out.println("\n===== CLIENTE ENCONTRADO =====");
                            System.out.println(cliente);
                            System.out.println("==============================\n");

                        } else if (tipoBusqueda == 2) {

                            System.out.println("Introduzca el DNI del cliente: ");
                            String buscarDni = scanner.nextLine();

                            Cliente cliente = clienteService.buscarPorDni(buscarDni);

                            System.out.println("\n===== CLIENTE ENCONTRADO =====");
                            System.out.println(cliente);
                            System.out.println("==============================\n");

                        } else {
                            System.out.println("ERROR: Opción incorrecta.");
                        }

                    } catch (NumberFormatException e) {
                        System.out.println("Valor no válido.");
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 3:
                    List<Cliente> clientes = clienteService.listarTodos();

                    if (clientes.isEmpty()) {
                        System.out.println("No hay clientes registrados.");
                    } else {

                        System.out.printf("%-5s %-15s %-20s %-15s %-25s %-15s%n",
                                "ID", "Nombre", "Apellidos", "DNI", "Email", "Teléfono");
                        System.out.println("--------------------------------------------------------------------------------------------------------------------");

                        clientes.forEach(cliente -> {
                            System.out.printf("%-5d %-15s %-20s %-15s %-25s %-15s%n",
                                    cliente.getId(),
                                    cliente.getNombre(),
                                    cliente.getApellidos(),
                                    cliente.getDni(),
                                    cliente.getEmail(),
                                    cliente.getTelefono());
                        });
                    }
                    break;
                case 4:
                    System.out.println("Introduzca el ID del cliente a eliminar: ");
                    Long id = Long.parseLong(scanner.nextLine());
                    System.out.println("Para confirmar su eliminación, por favor escriba DELETE en mayúscula: ");
                    String eliminar = scanner.nextLine();

                    if(eliminar.equals("DELETE")){
                        clienteService.eliminar(id);
                        System.out.println("Cliente eliminado.");
                    }else {
                        System.out.println("ERROR: No se ha podido eliminar el cliente.");
                    }
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        }while(opcion != 5);
    }
}
