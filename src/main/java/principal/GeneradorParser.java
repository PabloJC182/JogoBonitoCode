package principal;

public class GeneradorParser {
    public static void main(String[] args) {
        try {
            String[] opciones = {
                    "-destdir", "src/main/java/analizador",
                    "-parser", "Parser",
                    "-symbols", "sym",
                    "src/main/java/analizador/Parser.cup"
            };
            java_cup.Main.main(opciones);
            System.out.println("⚽ VAR: ¡Parser generado con éxito!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}