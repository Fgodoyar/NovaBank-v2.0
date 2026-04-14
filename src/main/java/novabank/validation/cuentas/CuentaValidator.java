package novabank.validation.cuentas;

public final class CuentaValidator {

    public static String generarIBAN(Long cliente_id){
        String numero = String.format("%012d", cliente_id);

        String iban = "ES91210000" + numero;

        return iban;
    }
}
