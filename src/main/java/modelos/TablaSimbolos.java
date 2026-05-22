package modelos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Tabla de símbolos con scopes anidados.
 * Rastrea nivel de anidamiento, si estamos dentro de ciclo o condicional.
 */
public class TablaSimbolos {

    // Pila de scopes activos
    private final Stack<Map<String, SimboloSemantico>> scopes = new Stack<>();

    // Historial acumulativo para el reporte (nunca se vacía con cerrarScope)
    public final List<SimboloSemantico> historial = new ArrayList<>();

    // Nivel actual de anidamiento
    private int nivelActual = 0;

    // Contadores de contexto (permiten anidar ciclos y condicionales)
    private int contadorCiclos       = 0;
    private int contadorCondicionales = 0;

    // ── Gestión de scopes ─────────────────────────────────────────

    public void abrirScope() {
        nivelActual++;
        scopes.push(new HashMap<>());
    }

    public void cerrarScope() {
        if (!scopes.isEmpty()) {
            scopes.pop();
            nivelActual--;
        }
    }

    /** Llamar al entrar a un ciclo (Temporada, Entrenamiento, Pretemporada) */
    public void entrarCiclo()  { contadorCiclos++; }
    /** Llamar al salir de un ciclo */
    public void salirCiclo()   { if (contadorCiclos > 0) contadorCiclos--; }

    /** Llamar al entrar a un condicional (Liga, Revancha, Copa) */
    public void entrarCondicional()  { contadorCondicionales++; }
    /** Llamar al salir de un condicional */
    public void salirCondicional()   { if (contadorCondicionales > 0) contadorCondicionales--; }

    public boolean estamosEnCiclo()        { return contadorCiclos > 0; }
    public boolean estamosEnCondicional()  { return contadorCondicionales > 0; }
    public int     getNivel()              { return nivelActual; }

    // ── Declaración ───────────────────────────────────────────────

    /**
     * Declara un símbolo en el scope actual.
     * @return true si se declaró correctamente, false si ya existía (redeclaración).
     */
    public boolean declarar(SimboloSemantico simbolo) {
        if (scopes.isEmpty()) abrirScope();

        Map<String, SimboloSemantico> actual = scopes.peek();
        if (actual.containsKey(simbolo.nombre)) return false;

        actual.put(simbolo.nombre, simbolo);
        historial.add(simbolo);
        return true;
    }

    // ── Búsqueda ──────────────────────────────────────────────────

    public SimboloSemantico buscar(String nombre) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            SimboloSemantico s = scopes.get(i).get(nombre);
            if (s != null) return s;
        }
        return null;
    }

    public void marcarInicializado(String nombre) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            SimboloSemantico s = scopes.get(i).get(nombre);
            if (s != null) { s.inicializado = true; return; }
        }
    }

    // ── Utilidades ────────────────────────────────────────────────

    public void limpiar() {
        scopes.clear();
        historial.clear();
        nivelActual        = 0;
        contadorCiclos     = 0;
        contadorCondicionales = 0;
    }
}