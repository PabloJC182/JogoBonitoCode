package modelos;

public class ErrorCompilador {
    public String tipo;
    public String descripcion;
    public int linea;

    public ErrorCompilador(String tipo, String descripcion, int linea) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.linea = linea;
    }
}