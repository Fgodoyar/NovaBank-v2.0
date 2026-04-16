package novabank.validation.clientes;

/**
 * Clase ClienteValidator que lleva las validaciones que debe cumplir un cliente al registrarse
 */
public final class ClienteValidator {

    /**
     * Constructor de la clase
     */
    private ClienteValidator() {
    }

    /**
     * Método ConfirmarDNI se encarga de que el dni cumpla un patrón como este: 7654321A
     * @param dni
     * @return true o false
     */
    public static boolean confirmarDNI(String dni) {
        if (dni == null || dni.isEmpty()) {
            return false;
        }
        String dniPattern = "\\d{8}[A-Z]";
        return dni.matches(dniPattern);
    }

    /**
     * Método ConfirmarTelefono se encarga de que el telefono cumpla un patrón como este: 612345678
     * @param telefono
     * @return true o false
     */
    public static boolean confirmarTelefono(String telefono) {
        if (telefono == null || telefono.isEmpty()) {
            return false;
        }
        String phonePattern = "^(?:\\+34|0034)?\\s?(?:6\\d{8}|7\\d{8}|8\\d{8}|9\\d{8})$";
        return telefono.matches(phonePattern);
    }

    /**
     * Método ConfirmarEmail se encarga de que el email cumpla un patrón como este: ejemplo12@gmail.com
     * @param email
     * @return true o false
     */
    public static boolean confirmarEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailPattern);
    }
}
