package reportes;

import modelos.TablaGlobal;
import modelos.ErrorCompilador;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class ReporteErrores {

    public static void generar() {
        String ruta = System.getProperty("user.dir") + File.separator + "errores.html";
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"es\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <title>Intervención del VAR - Errores</title>\n");
        html.append("    <link href=\"https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700;900&display=swap\" rel=\"stylesheet\">\n");
        html.append("    <style>\n");
        html.append("        body {\n");
        html.append("            margin: 0; padding: 0;\n");
        html.append("            font-family: 'Montserrat', sans-serif;\n");
        // Fondo con temática de estadio, pero un poco más tenso
        html.append("            background: url('https://images.unsplash.com/photo-1518605368461-1ee125221980?q=80&w=2000&auto=format&fit=crop') no-repeat center center fixed;\n");
        html.append("            background-size: cover;\n");
        html.append("            color: white;\n");
        html.append("        }\n");
        // Capa roja oscura y azul para dar sensación de alerta (VAR)
        html.append("        .overlay {\n");
        html.append("            background: rgba(30, 4, 15, 0.88);\n");
        html.append("            position: fixed; top: 0; left: 0; right: 0; bottom: 0; z-index: -1;\n");
        html.append("        }\n");
        html.append("        .contenedor {\n");
        html.append("            width: 80%; margin: 40px auto;\n");
        html.append("        }\n");
        // Título rojo neón
        html.append("        h1 {\n");
        html.append("            text-align: center; font-size: 3em; font-weight: 900;\n");
        html.append("            color: #ff4d4d; text-transform: uppercase; letter-spacing: 3px;\n");
        html.append("            text-shadow: 0 0 15px rgba(255, 77, 77, 0.8);\n");
        html.append("        }\n");
        // Tabla Glassmorphism
        html.append("        table {\n");
        html.append("            width: 100%; border-collapse: collapse; margin-top: 20px;\n");
        html.append("            background: rgba(255, 255, 255, 0.05);\n");
        html.append("            backdrop-filter: blur(10px);\n");
        html.append("            border-radius: 15px; overflow: hidden;\n");
        html.append("            box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.5);\n");
        html.append("            border: 1px solid rgba(255, 77, 77, 0.3);\n");
        html.append("        }\n");
        html.append("        th {\n");
        // Cabecera roja oscura estilo tarjeta roja
        html.append("            background: linear-gradient(90deg, #660000, #990000);\n");
        html.append("            color: white; padding: 20px; font-size: 1.2em;\n");
        html.append("            text-transform: uppercase; border-bottom: 3px solid #ff4d4d;\n");
        html.append("        }\n");
        html.append("        td {\n");
        html.append("            padding: 15px; text-align: center; font-size: 1.1em;\n");
        html.append("            border-bottom: 1px solid rgba(255, 255, 255, 0.1);\n");
        html.append("        }\n");
        html.append("        tr:hover { background: rgba(255, 77, 77, 0.15); transition: 0.3s; }\n");
        html.append("        .tarjeta-roja { color: #ff4d4d; font-weight: bold; }\n");
        html.append("        .desc { text-align: left; }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <div class=\"overlay\"></div>\n");
        html.append("    <div class=\"contenedor\">\n");
        html.append("        <h1>📺 Revisión Silenciosa del VAR (Faltas) 📺</h1>\n");
        html.append("        <table>\n");
        html.append("            <tr><th>#</th><th>Tipo de Falta</th><th>Motivo (Descripción)</th><th>Minuto (Línea)</th></tr>\n");

        int contador = 1;
        for (ErrorCompilador err : TablaGlobal.errores) {
            html.append("            <tr>\n");
            html.append("                <td>").append(contador).append("</td>\n");
            html.append("                <td class=\"tarjeta-roja\">").append(err.tipo).append("</td>\n");
            html.append("                <td class=\"desc\">").append(err.descripcion).append("</td>\n");
            html.append("                <td>").append(err.linea).append("</td>\n");
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
            System.err.println("Error al generar reporte de errores: " + e.getMessage());
        }
    }
}