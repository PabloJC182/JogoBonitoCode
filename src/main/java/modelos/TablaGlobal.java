package modelos;

import java.util.ArrayList;
import java.util.List;

public class TablaGlobal {

    public static List<Token> tokens = new ArrayList<>();
    public static List<ErrorCompilador> errores = new ArrayList<>();

    public static void limpiar() {
        tokens.clear();
        errores.clear();
    }

    public static void agregarError(String tipo, String desc, int linea) {
        errores.add(new ErrorCompilador(tipo, desc, linea));
    }
}