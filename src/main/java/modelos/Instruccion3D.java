package modelos;

/**
 * Representa una instrucción de código de 3 direcciones
 * orientada a generar C++ válido y ejecutable.
 */
public class Instruccion3D {

    public enum TipoInstruccion {
        ASIGNACION,    // x = valor;
        OPERACION,     // t1 = a + b;
        NARRAR,        // cout << expr << endl;
        COMENTARIO,    // // texto
        IF_INICIO,     // if (cond) {
        IF_FIN,        // }
        ELSE_INICIO,   // else {
        ELSE_FIN,      // }
        ELSEIF_INICIO, // else if (cond) {
        WHILE_INICIO,  // while (cond) {
        WHILE_FIN,     // }
        DOWHILE_INICIO,// do {
        DOWHILE_FIN,   // } while (cond);
        FOR_INICIO,    // for (init; cond; upd) {
        FOR_FIN,       // }
        BLOQUE_INICIO, // {
        BLOQUE_FIN,    // }
        RETORNO        // return expr;
    }

    public TipoInstruccion tipoInstruccion;
    public String resultado;    // Variable destino o temporal
    public String operando1;    // Primer operando o condición
    public String operador;     // Operador (+, -) o condición del for
    public String operando2;    // Segundo operando o actualización del for
    public int    linea;
    public String tipoCpp;      // "int", "double", "string"
    public boolean esTemporary; // true si es t1, t2, etc.

    // ── Constructor: asignación simple  x = valor ────────────────
    public Instruccion3D(String resultado, String operando1, int linea, String tipoCpp) {
        this.tipoInstruccion = TipoInstruccion.ASIGNACION;
        this.resultado   = resultado;
        this.operando1   = operando1;
        this.operador    = null;
        this.operando2   = null;
        this.linea       = linea;
        this.tipoCpp     = tipoCpp;
        this.esTemporary = esTemp(resultado);
    }

    // ── Constructor: operación  t1 = a + b ───────────────────────
    public Instruccion3D(String resultado, String operando1, String operador,
                         String operando2, int linea, String tipoCpp) {
        this.tipoInstruccion = TipoInstruccion.OPERACION;
        this.resultado   = resultado;
        this.operando1   = operando1;
        this.operador    = operador;
        this.operando2   = operando2;
        this.linea       = linea;
        this.tipoCpp     = tipoCpp;
        this.esTemporary = esTemp(resultado);
    }

    // ── Constructor interno para estructuras de control ───────────
    // Usado por Generador3Dir para if, while, for, etc.
    public Instruccion3D(String resultado, String operando1, String operador,
                         String operando2, int linea) {
        this.tipoInstruccion = TipoInstruccion.ASIGNACION;
        this.resultado   = resultado;
        this.operando1   = operando1;
        this.operador    = operador;
        this.operando2   = operando2;
        this.linea       = linea;
        this.tipoCpp     = null;
        this.esTemporary = esTemp(resultado);
    }

    // ── Factory: Narrar → cout << expr << endl ───────────────────
    public static Instruccion3D narrar(String expr, int linea) {
        Instruccion3D i = new Instruccion3D(null, expr, linea, null);
        i.tipoInstruccion = TipoInstruccion.NARRAR;
        i.esTemporary     = false;
        return i;
    }

    // ── Factory: comentario // texto ─────────────────────────────
    public static Instruccion3D comentario(String texto) {
        Instruccion3D i = new Instruccion3D(null, texto, 0, null);
        i.tipoInstruccion = TipoInstruccion.COMENTARIO;
        i.esTemporary     = false;
        return i;
    }

    // ── Factory: cierre de bloque } ──────────────────────────────
    public static Instruccion3D bloqueFin() {
        Instruccion3D i = new Instruccion3D(null, null, 0, null);
        i.tipoInstruccion = TipoInstruccion.BLOQUE_FIN;
        i.esTemporary     = false;
        return i;
    }

    // ── toString → línea C++ ──────────────────────────────────────
    @Override
    public String toString() {
        switch (tipoInstruccion) {
            case ASIGNACION:
                return resultado + " = " + operando1 + ";";
            case OPERACION:
                return resultado + " = " + operando1 + " " + operador + " " + operando2 + ";";
            case NARRAR:
                return "cout << " + operando1 + " << endl;";
            case COMENTARIO:
                return "// " + operando1;
            case IF_INICIO:
                return "if (" + operando1 + ") {";
            case IF_FIN:
                return "}";
            case ELSE_INICIO:
                return "else {";
            case ELSE_FIN:
                return "}";
            case ELSEIF_INICIO:
                return "else if (" + operando1 + ") {";
            case WHILE_INICIO:
                return "while (" + operando1 + ") {";
            case WHILE_FIN:
                return "}";
            case DOWHILE_INICIO:
                return "do {";
            case DOWHILE_FIN:
                return "} while (" + operando1 + ");";
            case FOR_INICIO:
                return "for (" + operando1 + "; " + operador + "; " + operando2 + ") {";
            case FOR_FIN:
                return "}";
            case BLOQUE_INICIO:
                return "{";
            case BLOQUE_FIN:
                return "}";
            case RETORNO:
                return "return " + operando1 + ";";
            default:
                return "";
        }
    }

    private static boolean esTemp(String nombre) {
        return nombre != null && nombre.matches("t\\d+");
    }
}