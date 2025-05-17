package py.edison.megasoftappv2.utils;

public class ValidationUtils {

    public static void validarPositivo(double valor, String nombreCampo) {
        if (valor < 0) {
            throw new IllegalArgumentException(nombreCampo + " no puede ser negativo");
        }
    }

    public static void validarNoNulo(Object objeto, String nombreCampo) {
        if (objeto == null) {
            throw new IllegalArgumentException(nombreCampo + " no puede ser nulo");
        }
    }

    public static void validarStringNoVacio(String valor, String nombreCampo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(nombreCampo + " no puede estar vacío");
        }
    }
}