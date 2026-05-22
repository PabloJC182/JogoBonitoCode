package modelos;

/**
 * Representa una variable/símbolo registrado en la tabla de símbolos.
 * Incluye tipo, alcance, nivel de anidamiento, contexto y estado.
 */
public class SimboloSemantico {

    // ── Tipo de dato ─────────────────────────────────────────────
    public enum TipoDato {
        DORSAL,        // Entero  (Dorsal)
        DECIMAL,       // Decimal (Decimal)
        JUGADATEXTO,   // Cadena  (JugadaTexto)
        DESCONOCIDO
    }

    // ── Alcance (scope) ──────────────────────────────────────────
    public enum Alcance {
        GLOBAL,        // Declarada fuera de cualquier bloque
        LOCAL,         // Declarada dentro de un bloque normal
        BLOQUE_CICLO   // Declarada dentro de un ciclo (Temporada, Entrenamiento, Pretemporada)
    }

    // ── Contexto ─────────────────────────────────────────────────
    public enum Contexto {
        NINGUNO,
        DENTRO_DE_CICLO,
        DENTRO_DE_CONDICIONAL
    }

    // ── Campos ───────────────────────────────────────────────────
    public String   nombre;
    public TipoDato tipo;
    public int      lineaDeclaracion;
    public boolean  inicializado;

    public Alcance  alcance;          // global / local / bloque_ciclo
    public int      nivelAnidamiento; // 0 = global, 1 = primer bloque, 2 = anidado...
    public Contexto contexto;         // si está dentro de ciclo o condicional
    public boolean  dentroDeCiclo;    // acceso rápido

    // ── Constructor completo ──────────────────────────────────────
    public SimboloSemantico(String nombre, TipoDato tipo, int lineaDeclaracion,
                            boolean inicializado, int nivelAnidamiento,
                            boolean dentroDeCiclo, boolean dentroDeCondicional) {
        this.nombre            = nombre;
        this.tipo              = tipo;
        this.lineaDeclaracion  = lineaDeclaracion;
        this.inicializado      = inicializado;
        this.nivelAnidamiento  = nivelAnidamiento;
        this.dentroDeCiclo     = dentroDeCiclo;

        // Determinar alcance
        if (nivelAnidamiento == 0) {
            this.alcance = Alcance.GLOBAL;
        } else if (dentroDeCiclo) {
            this.alcance = Alcance.BLOQUE_CICLO;
        } else {
            this.alcance = Alcance.LOCAL;
        }

        // Determinar contexto
        if (dentroDeCiclo) {
            this.contexto = Contexto.DENTRO_DE_CICLO;
        } else if (dentroDeCondicional) {
            this.contexto = Contexto.DENTRO_DE_CONDICIONAL;
        } else {
            this.contexto = Contexto.NINGUNO;
        }
    }

    // ── Conversión de lexema a TipoDato ──────────────────────────
    public static TipoDato desdeLexema(String lexema) {
        switch (lexema) {
            case "Dorsal":      return TipoDato.DORSAL;
            case "Decimal":     return TipoDato.DECIMAL;
            case "JugadaTexto": return TipoDato.JUGADATEXTO;
            default:            return TipoDato.DESCONOCIDO;
        }
    }

    @Override
    public String toString() {
        return nombre + " [" + tipo + "] nivel:" + nivelAnidamiento
                + " alcance:" + alcance + (inicializado ? " ✓" : " ✗");
    }
}