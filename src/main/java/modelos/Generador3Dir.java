package modelos;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Generador de Código de 3 Direcciones → C++ válido y ejecutable.
 *
 * Produce un programa C++ completo con:
 *   - Declaraciones de variables
 *   - Asignaciones y operaciones con temporales
 *   - Estructuras de control: if/else, while, do-while, for
 *   - cout para Narrar
 */
public class Generador3Dir {

    // Lista de instrucciones en orden de generación
    public static final List<Instruccion3D>  instrucciones = new ArrayList<>();

    // Mapa nombre → tipo C++ (preserva orden para declaraciones)
    public static final Map<String, String>  variables     = new LinkedHashMap<>();

    // Contador de temporales: t1, t2, t3...
    private static int contadorTemp = 0;

    // ── Temporales ───────────────────────────────────────────────

    public static String nuevoTemporal(String tipoCpp) {
        contadorTemp++;
        String nombre = "t" + contadorTemp;
        registrarVariable(nombre, tipoCpp);
        return nombre;
    }

    // ── Registrar variable ────────────────────────────────────────

    public static void registrarVariable(String nombre, String tipoCpp) {
        if (!variables.containsKey(nombre)) {
            variables.put(nombre, tipoCpp);
        }
    }

    // ── Asignación simple: x = valor ─────────────────────────────

    public static String emitirAsignacion(String destino, String valor,
                                          int linea, String tipoCpp) {
        registrarVariable(destino, tipoCpp);
        instrucciones.add(new Instruccion3D(destino, valor, linea, tipoCpp));
        return destino;
    }

    /** Sobrecarga sin tipo — infiere desde variables ya registradas */
    public static String emitirAsignacion(String destino, String valor, int linea) {
        String tipo = variables.getOrDefault(destino, "int");
        return emitirAsignacion(destino, valor, linea, tipo);
    }

    // ── Operación: t1 = a + b ─────────────────────────────────────

    public static String emitirOperacion(String op1, String operador,
                                         String op2, int linea, String tipoCpp) {
        String temp = nuevoTemporal(tipoCpp);
        instrucciones.add(new Instruccion3D(temp, op1, operador, op2, linea, tipoCpp));
        return temp;
    }

    /** Sobrecarga sin tipo — infiere desde op1 */
    public static String emitirOperacion(String op1, String operador,
                                         String op2, int linea) {
        String tipo = variables.getOrDefault(op1, "int");
        return emitirOperacion(op1, operador, op2, linea, tipo);
    }

    // ── Narrar → cout ─────────────────────────────────────────────

    public static void emitirNarrar(String expr, int linea) {
        instrucciones.add(Instruccion3D.narrar(expr, linea));
    }

    // ── Comentario ────────────────────────────────────────────────

    public static void emitirComentario(String texto) {
        instrucciones.add(Instruccion3D.comentario(texto));
    }

    // ── Estructuras de control ────────────────────────────────────

    public static void emitirIfInicio(String condicion) {
        Instruccion3D i = new Instruccion3D(null, condicion, 0, null);
        i.tipoInstruccion = Instruccion3D.TipoInstruccion.IF_INICIO;
        instrucciones.add(i);
    }

    public static void emitirIfFin() {
        Instruccion3D i = Instruccion3D.bloqueFin();
        i.tipoInstruccion = Instruccion3D.TipoInstruccion.IF_FIN;
        instrucciones.add(i);
    }

    public static void emitirElseInicio() {
        Instruccion3D i = new Instruccion3D(null, null, 0, null);
        i.tipoInstruccion = Instruccion3D.TipoInstruccion.ELSE_INICIO;
        instrucciones.add(i);
    }

    public static void emitirElseFin() {
        Instruccion3D i = Instruccion3D.bloqueFin();
        i.tipoInstruccion = Instruccion3D.TipoInstruccion.ELSE_FIN;
        instrucciones.add(i);
    }

    public static void emitirElseIfInicio(String condicion) {
        Instruccion3D i = new Instruccion3D(null, condicion, 0, null);
        i.tipoInstruccion = Instruccion3D.TipoInstruccion.ELSEIF_INICIO;
        instrucciones.add(i);
    }

    public static void emitirWhileInicio(String condicion) {
        Instruccion3D i = new Instruccion3D(null, condicion, 0, null);
        i.tipoInstruccion = Instruccion3D.TipoInstruccion.WHILE_INICIO;
        instrucciones.add(i);
    }

    public static void emitirWhileFin() {
        Instruccion3D i = Instruccion3D.bloqueFin();
        i.tipoInstruccion = Instruccion3D.TipoInstruccion.WHILE_FIN;
        instrucciones.add(i);
    }

    public static void emitirDoWhileInicio() {
        Instruccion3D i = new Instruccion3D(null, null, 0, null);
        i.tipoInstruccion = Instruccion3D.TipoInstruccion.DOWHILE_INICIO;
        instrucciones.add(i);
    }

    public static void emitirDoWhileFin(String condicion) {
        Instruccion3D i = new Instruccion3D(null, condicion, 0, null);
        i.tipoInstruccion = Instruccion3D.TipoInstruccion.DOWHILE_FIN;
        instrucciones.add(i);
    }

    /** For: emite for (init; cond; upd) { — acepta Object para compatibilidad con CUP */
    public static void emitirForInicio(Object ini, String cond, Object upd) {
        String iniStr = ini != null ? ini.toString() : "";
        String updStr = upd != null ? upd.toString() : "";
        Instruccion3D i = new Instruccion3D(null, iniStr, cond, updStr, 0, null);
        i.tipoInstruccion = Instruccion3D.TipoInstruccion.FOR_INICIO;
        instrucciones.add(i);
    }

    public static void emitirForFin() {
        Instruccion3D i = Instruccion3D.bloqueFin();
        i.tipoInstruccion = Instruccion3D.TipoInstruccion.FOR_FIN;
        instrucciones.add(i);
    }

    // ── Generar C++ completo ──────────────────────────────────────

    public static String generarCpp() {
        StringBuilder sb = new StringBuilder();

        sb.append("#include <iostream>\n");
        sb.append("#include <string>\n");
        sb.append("using namespace std;\n\n");
        sb.append("int main() {\n");

        // Declaraciones de variables
        if (!variables.isEmpty()) {
            for (Map.Entry<String, String> e : variables.entrySet()) {
                String tipo = e.getValue() != null ? e.getValue() : "int";
                sb.append("    ").append(tipo).append(" ").append(e.getKey()).append(";\n");
            }
            sb.append("\n");
        }

        // Instrucciones con indentación automática
        int nivel = 1;
        for (Instruccion3D inst : instrucciones) {
            // Reducir nivel antes de cerrar bloques
            switch (inst.tipoInstruccion) {
                case IF_FIN:
                case ELSE_FIN:
                case WHILE_FIN:
                case DOWHILE_FIN:
                case FOR_FIN:
                case BLOQUE_FIN:
                    nivel--;
                    break;
                default:
                    break;
            }

            String indent = "    ".repeat(Math.max(nivel, 0));
            sb.append(indent).append(inst.toString()).append("\n");

            // Aumentar nivel después de abrir bloques
            switch (inst.tipoInstruccion) {
                case IF_INICIO:
                case ELSE_INICIO:
                case ELSEIF_INICIO:
                case WHILE_INICIO:
                case DOWHILE_INICIO:
                case FOR_INICIO:
                case BLOQUE_INICIO:
                    nivel++;
                    break;
                default:
                    break;
            }
        }

        sb.append("\n    return 0;\n");
        sb.append("}\n");

        return sb.toString();
    }

    // ── Conversión de tipo ────────────────────────────────────────

    public static String tipoCpp(SimboloSemantico.TipoDato tipo) {
        switch (tipo) {
            case DORSAL:      return "int";
            case DECIMAL:     return "double";
            case JUGADATEXTO: return "string";
            default:          return "int";
        }
    }

    // ── Limpiar ───────────────────────────────────────────────────

    public static void limpiar() {
        instrucciones.clear();
        variables.clear();
        contadorTemp = 0;
    }
}