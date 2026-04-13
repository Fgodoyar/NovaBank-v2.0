package novabank.validation.cuentas;

public final class CuentaValidator {

    private static int contador = 0;

    public static String generarIBAN(){
        String numero = String.format("%012d", contador);
        contador++;

        String iban = "ES91210000" + numero;

        return iban;
    }
}
