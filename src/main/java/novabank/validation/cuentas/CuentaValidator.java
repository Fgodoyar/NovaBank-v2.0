package novabank.validation.cuentas;

/**
 * Clase CuentaValidator que se encarga de gestionar las validaciones de cuenta.
 */
public final class CuentaValidator {

    /**
     * Método generarIBAN se encarga de generar un iban a través del id del cliente.
     * @param cliente_id
     * @return iban
     */
    public static String generarIBAN(Long cliente_id){
        String numero = String.format("%012d", cliente_id);

        String iban = "ES91210000" + numero;

        return iban;
    }
}
