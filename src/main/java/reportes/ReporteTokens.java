package reportes;

import modelos.TablaGlobal;
import modelos.Token;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class ReporteTokens {

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

        // 🌟 LA MAGIA AQUÍ: Pegas tu link de Google entre las comillas simples
        html.append("            background: url('https://cloudfront-us-east-1.images.arcpublishing.com/eluniverso/ZKWPGPBUINDCLKJGF2EIHJXOFA.jpg') no-repeat center center fixed;\n");

        html.append("            background-size: cover;\n");
        html.append("            color: white;\n");
        html.append("        }\n");
        html.append("        .overlay {\n");
        html.append("            background: rgba(4, 16, 58, 0.6);\n");
        html.append("            position: fixed; top: 0; left: 0; right: 0; bottom: 0; z-index: -1;\n");
        html.append("        }\n");
        html.append("        .contenedor {\n");
        html.append("            width: 80%; margin: 40px auto;\n");
        html.append("        }\n");
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
        html.append("            color: white; padding: 20px; font-size: 1.2em;\n");
        html.append("            text-transform: uppercase; border-bottom: 3px solid #D4AF37;\n");
        html.append("        }\n");
        html.append("        td {\n");
        html.append("            padding: 15px; text-align: center; font-size: 1.1em;\n");
        html.append("            border-bottom: 1px solid rgba(255, 255, 255, 0.1);\n");
        html.append("        }\n");
        html.append("        tr:hover { background: rgba(255, 255, 255, 0.15); transition: 0.3s; }\n");
        html.append("        .highlight { color: #00f0ff; font-weight: bold; }\n");
        html.append("        .silver { color: #c0c0c0; }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <div class=\"overlay\"></div>\n");
        html.append("    <div class=\"contenedor\">\n");
        html.append("        <h1>🏆 Análisis de Pases (Tokens) 🏆</h1>\n");
        html.append("        <table>\n");
        html.append("            <tr><th>#</th><th>Jugada (Lexema)</th><th>Tipo de Pase (Token)</th><th>Minuto (Línea)</th><th>Posición (Col)</th></tr>\n");

        int contador = 1;
        for (Token t : TablaGlobal.tokens) {
            html.append("            <tr>\n");
            html.append("                <td>").append(contador).append("</td>\n");
            html.append("                <td class=\"highlight\">").append(t.lexema).append("</td>\n");
            html.append("                <td class=\"silver\">").append(t.tipo).append("</td>\n");
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