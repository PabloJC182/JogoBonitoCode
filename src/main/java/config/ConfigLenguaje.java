package config;

import analizador.sym;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.lang.reflect.Field;

public class ConfigLenguaje {

    public static Map<String, Integer> tokenMap = new HashMap<>();
    // Mantenemos tus mapas para los reportes del IDE
    public static Map<String, String> reservadas = new HashMap<>();
    public static Map<String, String> operadores = new HashMap<>();
    public static Map<String, String> simbolos = new HashMap<>();

    static {
        cargarConfiguracion();
    }

    public static void cargarConfiguracion() {
        tokenMap.clear();
        reservadas.clear();
        operadores.clear();
        simbolos.clear();

        boolean cargado = cargarDesdeArchivo("ConfigLenguaje.properties");

        if (!cargado) {
            System.out.println("⚠ Usando valores por defecto (No se halló .properties)");
            cargarValoresPorDefecto();
        }

        // --- CLASIFICACIÓN DE TOKENS ---
        // Esto es lo que rellena las tablas que usa tu IDE para el HTML
        for (Map.Entry<String, Integer> entry : tokenMap.entrySet()) {
            String lexema = entry.getKey();
            int symId = entry.getValue();
            String nombreToken = getNombreTokenDesdeSym(symId);

            if (esSimbolo(symId)) {
                simbolos.put(lexema, nombreToken);
            } else if (esOperador(symId)) {
                operadores.put(lexema, nombreToken);
            } else {
                reservadas.put(lexema, nombreToken);
            }
        }
    }

    private static void cargarValoresPorDefecto() {
        // Estructura Principal
        tokenMap.put("IniciarPartido", sym.IniciarPartido);
        tokenMap.put("InicioPartido", sym.InicioPartido);
        tokenMap.put("FinPartido", sym.FinPartido);
        tokenMap.put("FinJugada", sym.FinJugada);
        tokenMap.put("Local", sym.Local);
        tokenMap.put("Visitante", sym.Visitante);

        // Comandos
        tokenMap.put("Liga", sym.Liga);
        tokenMap.put("Revancha", sym.Revancha);
        tokenMap.put("Copa", sym.Copa);
        tokenMap.put("Entrenamiento", sym.Entrenamiento);
        tokenMap.put("Pretemporada", sym.Pretemporada);
        tokenMap.put("Temporada", sym.Temporada);
        tokenMap.put("Narrar", sym.Narrar);
        tokenMap.put("ResultadoFinal", sym.ResultadoFinal);

        // Tipos y Operadores
        tokenMap.put("Dorsal", sym.Dorsal);
        tokenMap.put("Decimal", sym.Decimal);
        tokenMap.put("JugadaTexto", sym.JugadaTexto);
        tokenMap.put("Ficha", sym.Ficha);
        tokenMap.put("Gana", sym.Gana);
        tokenMap.put("Pierde", sym.Pierde);
        tokenMap.put("Empata", sym.Empata);
        tokenMap.put("GolSuma", sym.GolSuma);
        tokenMap.put("Contraataque", sym.Contraataque);
    }

    private static boolean cargarDesdeArchivo(String nombreArchivo) {
        try (InputStream input = ConfigLenguaje.class.getClassLoader().getResourceAsStream(nombreArchivo)) {
            if (input == null) return false;
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"));
            String linea;
            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) continue;
                int idx = linea.indexOf(':');
                if (idx != -1) {
                    String lexema = linea.substring(0, idx).trim();
                    String nombreTokenCUP = linea.substring(idx + 1).trim();
                    int symId = obtenerSymId(nombreTokenCUP);
                    if (symId != -1) {
                        tokenMap.put(lexema, symId);
                    }
                }
            }
            return true;
        } catch (Exception e) { return false; }
    }

    private static int obtenerSymId(String nombreToken) {
        try {
            return sym.class.getField(nombreToken).getInt(null);
        } catch (Exception e) { return -1; }
    }

    public static String getNombreTokenDesdeSym(int symId) {
        try {
            for (Field field : sym.class.getFields()) {
                if (field.getInt(null) == symId && !field.getName().equals("error") && !field.getName().equals("EOF")) {
                    return field.getName();
                }
            }
        } catch (Exception e) {}
        return "DESCONOCIDO";
    }

    // Métodos de clasificación para que el IDE sepa qué es qué
    private static boolean esOperador(int symId) {
        return symId == sym.Gana || symId == sym.Pierde || symId == sym.Empata ||
                symId == sym.Ficha || symId == sym.GolSuma || symId == sym.Contraataque;
    }

    private static boolean esSimbolo(int symId) {
        return symId == sym.Local || symId == sym.Visitante ||
                symId == sym.InicioPartido || symId == sym.FinPartido ||
                symId == sym.FinJugada || symId == sym.IniciarPartido;
    }
}