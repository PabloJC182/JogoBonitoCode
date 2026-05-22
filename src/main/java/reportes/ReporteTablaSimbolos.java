package reportes;

import modelos.AnalizadorSemantico;
import modelos.SimboloSemantico;
import modelos.SimboloSemantico.TipoDato;
import modelos.SimboloSemantico.Alcance;
import modelos.SimboloSemantico.Contexto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class ReporteTablaSimbolos {

    public static void generar() {
        String ruta = System.getProperty("user.dir") + File.separator + "tabla_simbolos.html";
        List<SimboloSemantico> historial = AnalizadorSemantico.tabla.historial;

        long totalDorsal  = historial.stream().filter(s -> s.tipo == TipoDato.DORSAL).count();
        long totalDecimal = historial.stream().filter(s -> s.tipo == TipoDato.DECIMAL).count();
        long totalTexto   = historial.stream().filter(s -> s.tipo == TipoDato.JUGADATEXTO).count();
        long totalInit    = historial.stream().filter(s -> s.inicializado).count();
        long enCiclo      = historial.stream().filter(s -> s.dentroDeCiclo).count();

        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html><html lang='es'><head><meta charset='UTF-8'>")
                .append("<title>Plantilla del Equipo - Tabla de Símbolos</title>")
                .append("<link href='https://fonts.googleapis.com/css2?family=Montserrat:wght@400;700;900&display=swap' rel='stylesheet'>")
                .append("<style>")
                .append("*{margin:0;padding:0;box-sizing:border-box;}")
                .append("body{font-family:'Montserrat',sans-serif;background:url('https://images.unsplash.com/photo-1508098682722-e99c43a406b2?q=80&w=2000&auto=format&fit=crop') no-repeat center center fixed;background-size:cover;color:#eee;min-height:100vh;padding:30px 40px;}")
                .append(".overlay{background:rgba(0,10,40,0.92);position:fixed;top:0;left:0;right:0;bottom:0;z-index:-1;}")
                .append("h1{text-align:center;font-size:2.6em;font-weight:900;color:#FFD700;text-transform:uppercase;letter-spacing:3px;text-shadow:0 0 20px rgba(255,215,0,0.7);margin-bottom:6px;}")
                .append(".subtitulo{text-align:center;font-size:0.9em;color:#777;margin-bottom:28px;}")

                // Cards
                .append(".cards{display:flex;justify-content:center;gap:18px;margin-bottom:32px;flex-wrap:wrap;}")
                .append(".card{background:rgba(255,255,255,0.06);border:1px solid rgba(255,215,0,0.25);border-radius:14px;padding:14px 24px;text-align:center;}")
                .append(".card .num{font-size:2em;font-weight:900;color:#FFD700;}")
                .append(".card .lbl{font-size:0.7em;color:#888;text-transform:uppercase;margin-top:3px;}")

                // Tabla
                .append("table{width:100%;border-collapse:collapse;background:rgba(255,255,255,0.04);backdrop-filter:blur(12px);border-radius:15px;overflow:hidden;box-shadow:0 8px 32px rgba(0,0,0,0.5);border:1px solid rgba(255,215,0,0.2);}")
                .append("th{background:linear-gradient(90deg,#1a3a00,#2e6b00);color:#FFD700;padding:16px 12px;font-size:0.82em;text-transform:uppercase;border-bottom:3px solid #FFD700;letter-spacing:1px;}")
                .append("td{padding:13px 12px;text-align:center;font-size:0.88em;border-bottom:1px solid rgba(255,255,255,0.06);}")
                .append("tr:hover{background:rgba(255,215,0,0.07);transition:0.2s;}")

                // Badges tipo
                .append(".badge{display:inline-block;padding:3px 12px;border-radius:20px;font-weight:700;font-size:0.78em;}")
                .append(".t-dorsal{background:rgba(0,180,255,0.15);color:#00b4ff;border:1px solid #00b4ff;}")
                .append(".t-decimal{background:rgba(255,160,0,0.15);color:#FFA000;border:1px solid #FFA000;}")
                .append(".t-texto{background:rgba(180,0,255,0.15);color:#c87dff;border:1px solid #c87dff;}")
                .append(".t-desc{background:rgba(255,255,255,0.1);color:#aaa;border:1px solid #aaa;}")

                // Badges alcance
                .append(".a-global{background:rgba(0,255,100,0.12);color:#00ff88;border:1px solid #00ff88;}")
                .append(".a-local{background:rgba(100,180,255,0.12);color:#64b4ff;border:1px solid #64b4ff;}")
                .append(".a-ciclo{background:rgba(255,100,0,0.12);color:#ff6400;border:1px solid #ff6400;}")

                // Badges contexto
                .append(".c-ciclo{background:rgba(255,80,0,0.12);color:#ff7030;border:1px solid #ff7030;}")
                .append(".c-cond{background:rgba(200,120,255,0.12);color:#c878ff;border:1px solid #c878ff;}")
                .append(".c-ninguno{color:#555;font-size:0.85em;}")

                // Init
                .append(".si{color:#00e676;font-weight:700;}")
                .append(".no{color:#ff5252;font-weight:700;}")
                .append(".nivel-badge{display:inline-block;width:28px;height:28px;border-radius:50%;background:linear-gradient(135deg,#1a3a00,#2e6b00);color:#FFD700;font-weight:900;font-size:0.85em;line-height:28px;}")
                .append(".vacio{text-align:center;padding:60px;color:#444;font-size:1.1em;}")
                .append("</style></head><body>")
                .append("<div class='overlay'></div>")
                .append("<h1>⚽ Plantilla del Equipo ⚽</h1>")
                .append("<p class='subtitulo'>Tabla de Símbolos — Variables registradas durante la compilación</p>");

        // ── Cards ────────────────────────────────────────────────────
        html.append("<div class='cards'>")
                .append(card(String.valueOf(historial.size()), "Total Variables"))
                .append(card(String.valueOf(totalDorsal),  "Dorsal"))
                .append(card(String.valueOf(totalDecimal), "Decimal"))
                .append(card(String.valueOf(totalTexto),   "JugadaTexto"))
                .append(card(totalInit + "/" + historial.size(), "Inicializadas"))
                .append(card(String.valueOf(enCiclo),      "En ciclo"))
                .append("</div>");

        // ── Tabla ────────────────────────────────────────────────────
        html.append("<table><thead><tr>")
                .append("<th>#</th>")
                .append("<th>Nombre (ID)</th>")
                .append("<th>Tipo de Dato</th>")
                .append("<th>Alcance</th>")
                .append("<th>Nivel Anidamiento</th>")
                .append("<th>Contexto</th>")
                .append("<th>Dentro de Ciclo</th>")
                .append("<th>Línea Declaración</th>")
                .append("<th>Inicializada</th>")
                .append("</tr></thead><tbody>");

        if (historial.isEmpty()) {
            html.append("<tr><td colspan='9' class='vacio'>🏟️ No se declararon variables en esta compilación.</td></tr>");
        } else {
            int i = 1;
            for (SimboloSemantico s : historial) {
                html.append("<tr>")
                        .append("<td>").append(i++).append("</td>")
                        .append("<td><strong>").append(s.nombre).append("</strong></td>")
                        .append("<td>").append(badgeTipo(s.tipo)).append("</td>")
                        .append("<td>").append(badgeAlcance(s.alcance)).append("</td>")
                        .append("<td><span class='nivel-badge'>").append(s.nivelAnidamiento).append("</span></td>")
                        .append("<td>").append(badgeContexto(s.contexto)).append("</td>")
                        .append("<td>").append(s.dentroDeCiclo
                                ? "<span class='si'>✔ Sí</span>"
                                : "<span class='c-ninguno'>—</span>").append("</td>")
                        .append("<td>").append(s.lineaDeclaracion).append("</td>")
                        .append("<td>").append(s.inicializado
                                ? "<span class='si'>✔ Sí</span>"
                                : "<span class='no'>✘ No</span>").append("</td>")
                        .append("</tr>");
            }
        }

        html.append("</tbody></table></body></html>");

        try (BufferedWriter w = new BufferedWriter(new FileWriter(ruta))) {
            w.write(html.toString());
        } catch (Exception e) {
            System.err.println("Error al generar tabla de símbolos: " + e.getMessage());
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────

    private static String card(String numero, String label) {
        return "<div class='card'><div class='num'>" + numero + "</div>"
                + "<div class='lbl'>" + label + "</div></div>";
    }

    private static String badgeTipo(TipoDato tipo) {
        switch (tipo) {
            case DORSAL:      return "<span class='badge t-dorsal'>Dorsal</span>";
            case DECIMAL:     return "<span class='badge t-decimal'>Decimal</span>";
            case JUGADATEXTO: return "<span class='badge t-texto'>JugadaTexto</span>";
            default:          return "<span class='badge t-desc'>Desconocido</span>";
        }
    }

    private static String badgeAlcance(Alcance alcance) {
        switch (alcance) {
            case GLOBAL:       return "<span class='badge a-global'>Global</span>";
            case LOCAL:        return "<span class='badge a-local'>Local</span>";
            case BLOQUE_CICLO: return "<span class='badge a-ciclo'>Bloque Ciclo</span>";
            default:           return "<span class='badge t-desc'>—</span>";
        }
    }

    private static String badgeContexto(Contexto ctx) {
        switch (ctx) {
            case DENTRO_DE_CICLO:        return "<span class='badge c-ciclo'>Dentro de ciclo</span>";
            case DENTRO_DE_CONDICIONAL:  return "<span class='badge c-cond'>Dentro de condicional</span>";
            default:                     return "<span class='c-ninguno'>—</span>";
        }
    }
}