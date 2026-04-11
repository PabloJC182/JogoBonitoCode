package principal;

public class GeneradorLexer {
    public static void main(String[] args) {
        try {
            String[] opciones = {
                    "src/main/java/analizador/Lexer.flex",
                    "-d", "src/main/java/analizador"
            };
            jflex.Main.main(opciones);
            System.out.println("⚽ VAR: ¡Lexer generado con éxito!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}