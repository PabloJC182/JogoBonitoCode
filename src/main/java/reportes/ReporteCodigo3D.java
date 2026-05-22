package reportes;

import modelos.Generador3Dir;
import modelos.Instruccion3D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReporteCodigo3D {

    public static void generar() {
        List<Instruccion3D> lista = Generador3Dir.instrucciones;
        StringBuilder sb = new StringBuilder();

        sb.append("<!DOCTYPE html><html lang='es'><head><meta charset='UTF-8'>")
                .append("<title>Código de 3 Direcciones</title>")
                .append("<style>")
                .append("*{margin:0;padding:0;box-sizing:border-box;}")
                .append("body{background:#1a0a2e;font-family:'Segoe UI',sans-serif;padding:30px;color:#eee;}")
                .append("h1{text-align:center;color:#ff4444;font-size:2.2em;text-transform:uppercase;")
                .append("letter-spacing:3px;margin-bottom:30px;text-shadow:0 0 20px #ff000088;}")
                .append(".subtitulo{text-align:center;color:#aaa;margin-bottom:25px;font-size:1em;}")
                .append(".badge{display:inline-block;background:#ff4444;color:white;border-radius:12px;")
                .append("padding:2px 10px;font-size:0.8em;margin-left:8px;}")
                .append("table{width:100%;border-collapse:collapse;border-radius:12px;overflow:hidden;")
                .append("box-shadow:0 0 30px #ff000044;}")
                .append("thead tr{background:linear-gradient(135deg,#8b0000,#cc0000);}")
                .append("th{padding:14px 18px;color:white;font-size:1em;letter-spacing:1px;text-align:center;}")
                .append("tbody tr{background:#2a0a1e;border-bottom:1px solid #440022;transition:background 0.2s;}")
                .append("tbody tr:hover{background:#3d0f2f;}")
                .append("td{padding:12px 18px;text-align:center;font-size:0.95em;}")
                .append(".num{color:#ff6666;font-weight:bold;}")
                .append(".inst{font-family:'Courier New',monospace;color:#00ffcc;font-size:1em;text-align:left;}")
                .append(".op1{color:#66ccff;}.operador{color:#ffcc00;font-weight:bold;}")
                .append(".op2{color:#66ccff;}.dest{color:#ff99cc;font-weight:bold;}")
                .append(".linea{color:#aaaaaa;}")
                .append(".empty{text-align:center;padding:40px;color:#666;font-style:italic;}")
                .append("</style></head><body>")
                .append("<h1>⚽ Código de 3 Direcciones</h1>")
                .append("<p class='subtitulo'>Instrucciones generadas durante la compilación")
                .append("<span class='badge'>").append(lista.size()).append(" instrucciones</span></p>");

        if (lista.isEmpty()) {
            sb.append("<table><thead><tr>")
                    .append("<th>#</th><th>INSTRUCCIÓN</th><th>DESTINO</th>")
                    .append("<th>OP1</th><th>OPERADOR</th><th>OP2</th><th>LÍNEA</th>")
                    .append("</tr></thead><tbody>")
                    .append("<tr><td colspan='7' class='empty'>No se generaron instrucciones.</td></tr>")
                    .append("</tbody></table>");
        } else {
            sb.append("<table><thead><tr>")
                    .append("<th>#</th><th>INSTRUCCIÓN</th><th>DESTINO</th>")
                    .append("<th>OP1</th><th>OPERADOR</th><th>OP2</th><th>LÍNEA</th>")
                    .append("</tr></thead><tbody>");

            int i = 1;
            for (Instruccion3D ins : lista) {
                sb.append("<tr>")
                        .append("<td class='num'>").append(i++).append("</td>")
                        .append("<td class='inst'>").append(ins.toString()).append("</td>")
                        .append("<td class='dest'>").append(ins.resultado).append("</td>")
                        .append("<td class='op1'>").append(ins.operando1).append("</td>")
                        .append("<td class='operador'>").append(ins.operador != null ? ins.operador : "=").append("</td>")
                        .append("<td class='op2'>").append(ins.operando2 != null ? ins.operando2 : "-").append("</td>")
                        .append("<td class='linea'>").append(ins.linea).append("</td>")
                        .append("</tr>");
            }
            sb.append("</tbody></table>");
        }

        sb.append("</body></html>");

        try {
            File f = new File(System.getProperty("user.dir") + File.separator + "codigo3d.html");
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            bw.write(sb.toString());
            bw.close();
        } catch (IOException e) {
            System.err.println("Error al generar reporte 3D: " + e.getMessage());
        }
    }
}