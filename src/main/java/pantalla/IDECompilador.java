package pantalla;

import analizador.Lexer;
import analizador.Parser;
import config.ConfigLenguaje;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Pattern;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import modelos.TablaGlobal;
import reportes.ReporteErrores;
import reportes.ReporteTokens;

public class IDECompilador extends JFrame {

    private boolean compilado = false;
    private JTextArea txtCodigo;
    private JTextArea txtNumerosLinea;
    private JButton btnCargar, btnGuardarComo, btnCompilar, btnTokens, btnErrores, btnNuevo;
    private JLabel lblStatus;
    private Pattern p;

    private File archivoActual = null;

    public IDECompilador() {
        initComponents();
        configurarEventosTeclado();
        this.setLocationRelativeTo(null);
    }

    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Estadio JogoBonito - IDE Compilador");
        setPreferredSize(new Dimension(1050, 700));
        getContentPane().setLayout(new BorderLayout(10, 10));

        getContentPane().setBackground(new Color(4, 16, 58));

        txtCodigo = new JTextArea();
        txtCodigo.setFont(new Font("Consolas", Font.PLAIN, 16));
        txtCodigo.setTabSize(4);
        txtCodigo.setBackground(new Color(10, 20, 40));
        txtCodigo.setForeground(new Color(0, 255, 164));
        txtCodigo.setCaretColor(Color.YELLOW);

        txtNumerosLinea = new JTextArea("1");
        txtNumerosLinea.setFont(new Font("Consolas", Font.PLAIN, 16));
        txtNumerosLinea.setBackground(new Color(5, 12, 35));
        txtNumerosLinea.setForeground(new Color(100, 150, 200));
        txtNumerosLinea.setEditable(false);
        txtNumerosLinea.setFocusable(false);
        txtNumerosLinea.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        txtNumerosLinea.setHighlighter(null);

        txtCodigo.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void actualizarLineas() {
                int lineas = txtCodigo.getLineCount();
                StringBuilder textoNumeros = new StringBuilder();
                for (int i = 1; i <= lineas; i++) {
                    textoNumeros.append(i).append(System.lineSeparator());
                }
                txtNumerosLinea.setText(textoNumeros.toString());
            }
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { actualizarLineas(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { actualizarLineas(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { actualizarLineas(); }
        });

        JScrollPane scrollCodigo = new JScrollPane(txtCodigo);
        scrollCodigo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 15, 5, 15),
                BorderFactory.createLineBorder(new Color(0, 240, 255), 2)
        ));

        txtCodigo.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10)); // Margen interior del código
        scrollCodigo.setRowHeaderView(txtNumerosLinea);

        scrollCodigo.setBackground(new Color(4, 16, 58));
        scrollCodigo.getViewport().setBackground(new Color(10, 20, 40));

        // --- VESTUARIOS (Panel de Botones) ---
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        panelBotones.setBackground(new Color(4, 16, 58));

        btnCargar = crearBotonEstilizado(" Abrir", new Color(41, 182, 246));
        btnGuardarComo = crearBotonEstilizado(" Guardar Como", new Color(126, 87, 194));
        btnCompilar = crearBotonEstilizado(" Pitar Inicio", new Color(46, 125, 50));
        btnTokens = crearBotonEstilizado(" Tokens", new Color(25, 118, 210));
        btnErrores = crearBotonEstilizado(" Faltas", new Color(211, 47, 47));
        btnNuevo = crearBotonEstilizado(" Limpiar", new Color(255, 160, 0));

        panelBotones.add(btnCargar);
        panelBotones.add(btnGuardarComo);
        panelBotones.add(btnCompilar);
        panelBotones.add(btnTokens);
        panelBotones.add(btnErrores);
        panelBotones.add(btnNuevo);

        // --- MARCADOR (Barra de estado inferior) ---
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        panelInferior.setBackground(new Color(4, 16, 58));

        lblStatus = new JLabel("Minuto 1 | Posición 1 | Táctica: Sin guardar");
        lblStatus.setFont(new Font("Arial", Font.BOLD, 12));
        lblStatus.setForeground(new Color(212, 175, 55));
        panelInferior.add(lblStatus);

        // Ensamblar todo
        getContentPane().add(panelBotones, BorderLayout.NORTH);
        getContentPane().add(scrollCodigo, BorderLayout.CENTER);
        getContentPane().add(panelInferior, BorderLayout.SOUTH);

        // Asignar Eventos a los botones
        btnCargar.addActionListener(evt -> cargarArchivo());
        btnGuardarComo.addActionListener(evt -> ejecutarGuardadoComo());
        btnCompilar.addActionListener(evt -> btnCompilarActionPerformed(evt));
        btnTokens.addActionListener(evt -> abrirReporte("tokens.html"));
        btnErrores.addActionListener(evt -> abrirReporte("errores.html"));
        btnNuevo.addActionListener(evt -> btnNuevoActionPerformed(evt));

        pack();
    }

    private JButton crearBotonEstilizado(String texto, Color colorFondo) {
        JButton boton = new JButton(texto);

        boton.setFont(new Font("Arial", Font.BOLD, 15));
        boton.setForeground(Color.WHITE); // Texto blanco brillante
        boton.setBackground(colorFondo); // Color sólido
        boton.setFocusPainted(false); // Sin cuadros feos al hacer clic
        boton.setOpaque(true); // Obliga a pintar el fondo sólido

        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorFondo.darker(), 2),
                BorderFactory.createEmptyBorder(10, 18, 10, 18)
        ));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setBackground(colorFondo.brighter());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBackground(colorFondo);
            }
        });

        return boton;
    }

    private void configurarEventosTeclado() {
        txtCodigo.addCaretListener(evt -> {
            try {
                int caretPos = txtCodigo.getCaretPosition();
                int linea = txtCodigo.getLineOfOffset(caretPos);
                int columna = caretPos - txtCodigo.getLineStartOffset(linea);

                String nombreArchivo = (archivoActual != null) ? archivoActual.getName() : "Sin guardar";
                lblStatus.setText("Línea: " + (linea + 1) + " | Columna: " + (columna + 1) + " | Táctica: " + nombreArchivo);

                limpiarResaltado();
                iluminarSintaxis();
            } catch (Exception ex) {
            }
        });
    }

    private void cargarArchivo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Selecciona la táctica (.txt)");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de Texto (*.txt)", "txt"));

        int seleccion = fileChooser.showOpenDialog(this);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            archivoActual = fileChooser.getSelectedFile();
            try (BufferedReader lector = new BufferedReader(new FileReader(archivoActual))) {
                txtCodigo.setText("");
                String linea;
                while ((linea = lector.readLine()) != null) {
                    txtCodigo.append(linea + "\n");
                }
                txtCodigo.setCaretPosition(0);
                lblStatus.setText("Línea: 1 | Columna: 1 | Táctica cargada: " + archivoActual.getName());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al leer el archivo táctico.", "VAR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void ejecutarGuardadoComo() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Táctica Como...");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de Texto (*.txt)", "txt"));

        if (archivoActual != null) {
            fileChooser.setSelectedFile(archivoActual);
        } else {
            fileChooser.setSelectedFile(new File("mi_tactica.txt"));
        }

        int seleccion = fileChooser.showSaveDialog(this);
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();

            if (!archivoSeleccionado.getName().toLowerCase().endsWith(".txt")) {
                archivoActual = new File(archivoSeleccionado.getParentFile(), archivoSeleccionado.getName() + ".txt");
            } else {
                archivoActual = archivoSeleccionado;
            }

            try (BufferedWriter escritor = new BufferedWriter(new FileWriter(archivoActual))) {
                escritor.write(txtCodigo.getText());
                lblStatus.setText("Línea: 1 | Columna: 1 | Táctica: " + archivoActual.getName());

                JOptionPane.showMessageDialog(this,
                        "¡Táctica guardada con éxito! \n\nRuta: " + archivoActual.getAbsolutePath(),
                        "Guardado Exitoso",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error al guardar la táctica: " + e.getMessage(), "VAR - Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void btnCompilarActionPerformed(java.awt.event.ActionEvent evt) {
        String codigo = txtCodigo.getText();
        limpiarResaltado();

        if (codigo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "¡La cancha está vacía! Ingresa código para jugar.", "VAR - Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            ConfigLenguaje.cargarConfiguracion();
            TablaGlobal.tokens.clear();
            TablaGlobal.errores.clear();

            Lexer lexer = new Lexer(new StringReader(codigo));
            Parser parser = new Parser(lexer);

            try {
                parser.parse();
            } catch (Exception e) {
                TablaGlobal.errores.add(new modelos.ErrorCompilador("Sintáctico Crítico", "Error fatal en la estructura: " + e.getMessage(), 0));
            }

            compilado = true;
            ReporteTokens.generar();
            ReporteErrores.generar();

            if (TablaGlobal.errores.isEmpty()) {
                iluminarSintaxis();
                JOptionPane.showMessageDialog(this, " ¡Juego Limpio! Compilación exitosa.\nTokens generados: " + TablaGlobal.tokens.size(), "Resultado del VAR", JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (modelos.ErrorCompilador err : TablaGlobal.errores) {
                    if (err.linea > 0) {
                        resaltarError(err.linea);
                    }
                }
                JOptionPane.showMessageDialog(this, " ¡Falta! Se detectaron " + TablaGlobal.errores.size() + " errores.\nRevisa el reporte de errores para más info.", "Resultado del VAR", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Throwable t) {
            JOptionPane.showMessageDialog(this, "Error grave en el sistema: " + t.getMessage(), "VAR - Error Crítico", JOptionPane.ERROR_MESSAGE);
            t.printStackTrace();
        }
    }

    private void abrirReporte(String nombreArchivo) {
        if (!compilado) {
            JOptionPane.showMessageDialog(this, "Primero debes compilar antes de ver los reportes", "VAR", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            File archivo = new File(System.getProperty("user.dir") + File.separator + nombreArchivo);
            if (archivo.exists()) {
                Desktop.getDesktop().browse(archivo.toURI());
            } else {
                JOptionPane.showMessageDialog(this, "No se ha generado el archivo aún.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al abrir: " + e.getMessage());
        }
    }

    private void iluminarSintaxis() {
        try {
            Highlighter highlighter = txtCodigo.getHighlighter();
            Highlighter.HighlightPainter pintorRelato = new DefaultHighlighter.DefaultHighlightPainter(new Color(144, 238, 144, 100));
            String texto = txtCodigo.getText();

            p = Pattern.compile("Relato[\\s\\S]*?Relato");
            java.util.regex.Matcher mRelato = p.matcher(texto);

            Pattern pCiclos = Pattern.compile("Mientras|Entrenamiento|Temporada|Pretemporada");
            java.util.regex.Matcher mCiclos = pCiclos.matcher(texto);

            while (mCiclos.find()) {
                highlighter.addHighlight(mCiclos.start(), mCiclos.end(), new DefaultHighlighter.DefaultHighlightPainter(new Color(173, 216, 230, 100)));
            }

            while (mRelato.find()) {
                highlighter.addHighlight(mRelato.start(), mRelato.end(), pintorRelato);
            }

            java.util.regex.Matcher mPase = Pattern.compile("'[^']'").matcher(texto);
            while (mPase.find()) {
                highlighter.addHighlight(mPase.start(), mPase.end(), new DefaultHighlighter.DefaultHighlightPainter(new Color(255, 200, 0, 100)));
            }
        } catch (Exception e) {}
    }

    private void limpiarResaltado() {
        txtCodigo.getHighlighter().removeAllHighlights();
    }

    private void resaltarError(int linea) {
        if (linea <= 0) return;
        try {
            Highlighter highlighter = txtCodigo.getHighlighter();
            Highlighter.HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(new Color(255, 51, 51, 150));
            int inicio = txtCodigo.getLineStartOffset(linea - 1);
            int fin = txtCodigo.getLineEndOffset(linea - 1);
            highlighter.addHighlight(inicio, fin, painter);
        } catch (Exception e) {
            System.err.println("No se pudo resaltar: " + e.getMessage());
        }
    }

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {
        int opcion = JOptionPane.showConfirmDialog(this, "¿Seguro que quieres limpiar la cancha? Si no guardaste, perderás los cambios.", "Nuevo Archivo", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            txtCodigo.setText("");
            archivoActual = null;
            lblStatus.setText("Línea: 1 | Columna: 1 | Táctica: Sin guardar");
            txtCodigo.requestFocus();
        }
    }

    public static void main(String args[]) {
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        java.awt.EventQueue.invokeLater(() -> new IDECompilador().setVisible(true));
    }
}