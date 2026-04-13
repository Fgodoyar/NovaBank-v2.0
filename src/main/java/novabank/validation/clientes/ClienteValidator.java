package novabank.validation.clientes;

public final class ClienteValidator {

    private ClienteValidator() {
    }

    public static boolean confirmarDNI(String dni) {
        if (dni == null || dni.isEmpty()) {
            return false;
        }
        String dniPattern = "\\d{8}[A-Z]";
        return dni.matches(dniPattern);
    }

    public static boolean confirmarTelefono(String telefono) {
        if (telefono == null || telefono.isEmpty()) {
            return false;
        }
        String phonePattern = "^(?:\\+34|0034)?\\s?(?:6\\d{8}|7\\d{8}|8\\d{8}|9\\d{8})$";
        return telefono.matches(phonePattern);
    }

    public static boolean confirmarEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailPattern);
    }
}
