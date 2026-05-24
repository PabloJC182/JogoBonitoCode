package reportes;

import modelos.TablaGlobal;
import modelos.Token;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class ReporteTokens {

    // Mapa de equivalencias: token del lenguaje → equivalente real
    private static final Map<String, String> EQUIVALENCIAS = new HashMap<>();

    static {
        // Tipos de dato
        EQUIVALENCIAS.put("Dorsal",       "int");
        EQUIVALENCIAS.put("Decimal",      "double");
        EQUIVALENCIAS.put("JugadaTexto",  "string");

        // Operadores
        EQUIVALENCIAS.put("Ficha",        "= (asignación)");
        EQUIVALENCIAS.put("Gana",         "> (mayor que)");
        EQUIVALENCIAS.put("Pierde",       "< (menor que)");
        EQUIVALENCIAS.put("Empata",       "== (igual)");
        EQUIVALENCIAS.put("GolSuma",      "+ (suma)");
        EQUIVALENCIAS.put("Contraataque", "- (resta)");

        // Estructuras de control
        EQUIVALENCIAS.put("Liga",         "if");
        EQUIVALENCIAS.put("Revancha",     "else if");
        EQUIVALENCIAS.put("Copa",         "else");
        EQUIVALENCIAS.put("Entrenamiento","while");
        EQUIVALENCIAS.put("Pretemporada", "do-while");
        EQUIVALENCIAS.put("Temporada",    "for");

        // I/O y retorno
        EQUIVALENCIAS.put("Narrar",       "cout");
        EQUIVALENCIAS.put("ResultadoFinal","return");

        // Estructura de bloques
        EQUIVALENCIAS.put("IniciarPartido","main()");
        EQUIVALENCIAS.put("InicioPartido", "{ (inicio bloque)");
        EQUIVALENCIAS.put("FinPartido",    "} (fin bloque)");
        EQUIVALENCIAS.put("FinJugada",     "; (fin instrucción)");
        EQUIVALENCIAS.put("Local",         "( (apertura paréntesis)");
        EQUIVALENCIAS.put("Visitante",     ") (cierre paréntesis)");

        // Literales e identificadores
        EQUIVALENCIAS.put("ENTERO",        "Literal entero");
        EQUIVALENCIAS.put("DECIMAL_LITERAL","Literal decimal");
        EQUIVALENCIAS.put("IDENTIFICADOR", "Identificador");
        EQUIVALENCIAS.put("RELATO",        "Delimitador string");
        EQUIVALENCIAS.put("TEXTO_CONTENIDO","Contenido string");
    }

    private static String equivalencia(String tipo) {
        return EQUIVALENCIAS.getOrDefault(tipo, tipo);
    }

    public static void generar() {
        String ruta = System.getProperty("user.dir") + File.separator + "tokens.html";
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"es\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <title>Estadísticas del Partido - Tokens</title>\n");
        html.append("    <link href=\"https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700;900&display=swap\" rel=\"stylesheet\">\n");
        html.append("    <style>\n");
        html.append("        body {\n");
        html.append("            margin: 0; padding: 0;\n");
        html.append("            font-family: 'Montserrat', sans-serif;\n");
        html.append("            background: url('https://cloudfront-us-east-1.images.arcpublishing.com/eluniverso/ZKWPGPBUINDCLKJGF2EIHJXOFA.jpg') no-repeat center center fixed;\n");
        html.append("            background-size: cover;\n");
        html.append("            color: white;\n");
        html.append("        }\n");
        html.append("        .overlay {\n");
        html.append("            background: rgba(4, 16, 58, 0.6);\n");
        html.append("            position: fixed; top: 0; left: 0; right: 0; bottom: 0; z-index: -1;\n");
        html.append("        }\n");
        html.append("        .contenedor { width: 90%; margin: 40px auto; }\n");
        html.append("        h1 {\n");
        html.append("            text-align: center; font-size: 3em; font-weight: 900;\n");
        html.append("            color: #D4AF37; text-transform: uppercase; letter-spacing: 3px;\n");
        html.append("            text-shadow: 0 0 15px rgba(212, 175, 55, 0.6);\n");
        html.append("        }\n");
        html.append("        table {\n");
        html.append("            width: 100%; border-collapse: collapse; margin-top: 20px;\n");
        html.append("            background: rgba(255, 255, 255, 0.05);\n");
        html.append("            backdrop-filter: blur(10px);\n");
        html.append("            border-radius: 15px; overflow: hidden;\n");
        html.append("            box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.37);\n");
        html.append("            border: 1px solid rgba(255, 255, 255, 0.18);\n");
        html.append("        }\n");
        html.append("        th {\n");
        html.append("            background: linear-gradient(90deg, #0e194f, #1a2a6c);\n");
        html.append("            color: white; padding: 16px; font-size: 1em;\n");
        html.append("            text-transform: uppercase; border-bottom: 3px solid #D4AF37;\n");
        html.append("        }\n");
        html.append("        td {\n");
        html.append("            padding: 13px 15px; text-align: center; font-size: 1em;\n");
        html.append("            border-bottom: 1px solid rgba(255, 255, 255, 0.1);\n");
        html.append("        }\n");
        html.append("        tr:hover { background: rgba(255, 255, 255, 0.15); transition: 0.3s; }\n");
        html.append("        .lexema    { color: #00f0ff; font-weight: bold; }\n");
        html.append("        .token     { color: #c0c0c0; }\n");
        html.append("        .equiv     { color: #FFD700; font-weight: bold; }\n");
        html.append("        .badge {\n");
        html.append("            display: inline-block; padding: 3px 10px;\n");
        html.append("            border-radius: 12px; font-size: 0.85em; font-weight: 700;\n");
        html.append("            background: rgba(255,215,0,0.15);\n");
        html.append("            border: 1px solid rgba(255,215,0,0.5);\n");
        html.append("            color: #FFD700;\n");
        html.append("        }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <div class=\"overlay\"></div>\n");
        html.append("    <div class=\"contenedor\">\n");
        html.append("        <h1>🏆 Análisis de Pases (Tokens) 🏆</h1>\n");
        html.append("        <table>\n");
        html.append("            <tr>\n");
        html.append("                <th>#</th>\n");
        html.append("                <th>Jugada (Lexema)</th>\n");
        html.append("                <th>Equivalente Real</th>\n");
        html.append("                <th>Minuto (Línea)</th>\n");
        html.append("                <th>Posición (Col)</th>\n");
        html.append("            </tr>\n");

        int contador = 1;
        for (Token t : TablaGlobal.tokens) {
            String equiv = equivalencia(t.tipo);
            html.append("            <tr>\n");
            html.append("                <td>").append(contador).append("</td>\n");
            html.append("                <td class=\"lexema\">").append(t.lexema).append("</td>\n");
            html.append("                <td><span class=\"badge\">").append(equiv).append("</span></td>\n");
            html.append("                <td>").append(t.linea).append("</td>\n");
            html.append("                <td>").append(t.columna).append("</td>\n");
            html.append("            </tr>\n");
            contador++;
        }

        html.append("        </table>\n");
        html.append("    </div>\n");
        html.append("</body>\n");
        html.append("</html>\n");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ruta))) {
            writer.write(html.toString());
        } catch (Exception e) {
            System.err.println("Error al generar reporte de tokens: " + e.getMessage());
        }
    }
}