package modelos;

import modelos.SimboloSemantico.TipoDato;

/**
 * Resultado de una expresión en el Parser.
 * Propaga el tipo semántico y el valor/nombre C++ hacia arriba en el árbol.
 *
 * Ejemplos:
 *   ENTERO 5        → ExprResult(DORSAL, "5")
 *   IDENTIFICADOR x → ExprResult(DORSAL, "x")
 *   x GolSuma 3     → ExprResult(DORSAL, "t1")  ← t1 es el temporal generado
 */
public class ExprResult {

    public TipoDato tipo;   // Tipo semántico del resultado
    public String   valor;  // Nombre de la variable, literal o temporal C++

    public ExprResult(TipoDato tipo, String valor) {
        this.tipo  = tipo;
        this.valor = valor;
    }

    @Override
    public String toString() {
        return "ExprResult{tipo=" + tipo + ", valor='" + valor + "'}";
    }
}