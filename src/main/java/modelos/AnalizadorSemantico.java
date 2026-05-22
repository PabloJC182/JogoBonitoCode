package modelos;

import modelos.SimboloSemantico.TipoDato;

/**
 * AnalizadorSemantico
 * ===================
 * Métodos estáticos llamados desde las acciones del Parser.cup.
 * Verifica declaraciones, tipos, uso de variables y contexto.
 */
public class AnalizadorSemantico {

    public static final TablaSimbolos tabla = new TablaSimbolos();

    // ── Scopes ───────────────────────────────────────────────────

    public static void abrirScope()  { tabla.abrirScope(); }
    public static void cerrarScope() { tabla.cerrarScope(); }

    // ── Contexto de ciclos ────────────────────────────────────────

    public static void entrarCiclo()  { tabla.entrarCiclo(); }
    public static void salirCiclo()   { tabla.salirCiclo(); }

    // ── Contexto de condicionales ─────────────────────────────────

    public static void entrarCondicional() { tabla.entrarCondicional(); }
    public static void salirCondicional()  { tabla.salirCondicional(); }

    // ── Declaración ───────────────────────────────────────────────

    /**
     * Registra la declaración de una variable con toda la información de contexto.
     */
    public static TipoDato declararVariable(TipoDato tipo, String nombre,
                                            int linea, boolean conValor) {
        SimboloSemantico simbolo = new SimboloSemantico(
                nombre, tipo, linea, conValor,
                tabla.getNivel(),
                tabla.estamosEnCiclo(),
                tabla.estamosEnCondicional()
        );

        boolean ok = tabla.declarar(simbolo);
        if (!ok) {
            error("Semántico",
                    "Variable '" + nombre + "' ya fue declarada en este bloque.",
                    linea);
        }
        return tipo;
    }

    // ── Asignación ────────────────────────────────────────────────

    public static TipoDato verificarAsignacion(String nombre, int linea) {
        SimboloSemantico s = tabla.buscar(nombre);
        if (s == null) {
            error("Semántico", "Variable '" + nombre + "' no fue declarada.", linea);
            return TipoDato.DESCONOCIDO;
        }
        tabla.marcarInicializado(nombre);
        return s.tipo;
    }

    // ── Compatibilidad de tipos ───────────────────────────────────

    public static void verificarCompatibilidad(TipoDato tipoVariable,
                                               TipoDato tipoExpr, int linea) {
        if (tipoVariable == TipoDato.DESCONOCIDO || tipoExpr == TipoDato.DESCONOCIDO) return;

        boolean compatible;
        switch (tipoVariable) {
            case DORSAL:      compatible = (tipoExpr == TipoDato.DORSAL); break;
            case DECIMAL:     compatible = (tipoExpr == TipoDato.DORSAL || tipoExpr == TipoDato.DECIMAL); break;
            case JUGADATEXTO: compatible = (tipoExpr == TipoDato.JUGADATEXTO); break;
            default:          compatible = false;
        }

        if (!compatible) {
            error("Semántico",
                    "Tipo incompatible: no se puede asignar '" + tipoExpr
                            + "' a una variable de tipo '" + tipoVariable + "'.", linea);
        }
    }

    // ── Uso de variable ───────────────────────────────────────────

    public static TipoDato verificarUso(String nombre, int linea) {
        SimboloSemantico s = tabla.buscar(nombre);
        if (s == null) {
            error("Semántico", "Variable '" + nombre + "' no fue declarada.", linea);
            return TipoDato.DESCONOCIDO;
        }
        if (!s.inicializado) {
            error("Semántico",
                    "Variable '" + nombre + "' se usa antes de ser inicializada.", linea);
        }
        return s.tipo;
    }

    // ── Operaciones aritméticas ───────────────────────────────────

    public static TipoDato verificarOperacionAritmetica(TipoDato tipoIzq,
                                                        TipoDato tipoDer,
                                                        String operador,
                                                        int linea) {
        if (!esNumerica(tipoIzq) || !esNumerica(tipoDer)) {
            error("Semántico",
                    "Operación '" + operador + "' requiere operandos numéricos. "
                            + "Se encontró: '" + tipoIzq + "' y '" + tipoDer + "'.", linea);
            return TipoDato.DESCONOCIDO;
        }
        return (tipoIzq == TipoDato.DECIMAL || tipoDer == TipoDato.DECIMAL)
                ? TipoDato.DECIMAL : TipoDato.DORSAL;
    }

    // ── ResultadoFinal ────────────────────────────────────────────

    public static void verificarResultadoFinal(TipoDato tipoExpr, int linea) {
        if (tipoExpr == TipoDato.JUGADATEXTO) {
            error("Semántico",
                    "ResultadoFinal debe ser Dorsal o Decimal, no JugadaTexto.", linea);
        }
    }

    // ── Utilidades ────────────────────────────────────────────────

    private static boolean esNumerica(TipoDato t) {
        return t == TipoDato.DORSAL || t == TipoDato.DECIMAL;
    }

    private static void error(String tipo, String descripcion, int linea) {
        TablaGlobal.errores.add(new ErrorCompilador(tipo, descripcion, linea));
    }

    public static void limpiar() { tabla.limpiar(); }
}