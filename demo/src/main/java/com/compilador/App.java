package com.compilador;

import com.compilador.codigoIntermedio.CodigoTresDir;
import com.compilador.optimizacion.Optimizador;
import com.compilador.semantico.SemanticAnalyzer;
import com.compilador.semantico.SemanticError;
import com.compilador.semantico.Scope;

import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Uso: java -jar compilador.jar <archivo.cpp>");
            return;
        }

        try {

            String archivo = args[0];

            System.out.println("Iniciando compilacion de: " + archivo);
            System.out.println("============================================================");

            // =========================
            // 1. LEXICO
            // =========================

            CharStream input = CharStreams.fromFileName(archivo, StandardCharsets.UTF_8);
            MiLenguajeLexer lexer = new MiLenguajeLexer(input);

            List<String> erroresLexicos = new ArrayList<>();

            lexer.removeErrorListeners();
            lexer.addErrorListener(new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer,
                                        Object offendingSymbol,
                                        int line,
                                        int charPositionInLine,
                                        String msg,
                                        RecognitionException e) {
                    erroresLexicos.add("Linea " + line + ", columna " + charPositionInLine + ": " + msg);
                }
            });

            CommonTokenStream tokens = new CommonTokenStream(lexer);
            tokens.fill();

            int cantidadTokens = 0;
            for (Token t : tokens.getTokens()) {
                if (t.getType() != Token.EOF) cantidadTokens++;
            }

            System.out.println("\n=== 1. ANALISIS LEXICO ===");

            if (!erroresLexicos.isEmpty()) {
                System.out.println("ERRORES LEXICOS:");
                erroresLexicos.forEach(e -> System.out.println("  " + e));
                return;
            }

            System.out.println(" Analisis lexico completado sin errores.");
            System.out.println("   Tokens procesados: " + cantidadTokens);

            // =========================
            // 2. SINTACTICO
            // =========================

            System.out.println("\n=== 2. ANALISIS SINTACTICO ===");

            MiLenguajeParser parser = new MiLenguajeParser(tokens);

            List<String> erroresSintacticos = new ArrayList<>();

            parser.removeErrorListeners();
            parser.addErrorListener(new BaseErrorListener() {
                @Override
                public void syntaxError(Recognizer<?, ?> recognizer,
                                        Object offendingSymbol,
                                        int line,
                                        int charPositionInLine,
                                        String msg,
                                        RecognitionException e) {
                    erroresSintacticos.add("Linea " + line + ", columna " + charPositionInLine + ": " + msg);
                }
            });

            MiLenguajeParser.ProgramaContext arbol = parser.programa();

            if (!erroresSintacticos.isEmpty()) {
                System.out.println("ERRORES SINTACTICOS:");
                erroresSintacticos.forEach(e -> System.out.println("  " + e));
                return;
            }

            System.out.println(" Analisis sintactico completado sin errores.");
            System.out.println("   Arbol sintactico generado correctamente");

            // =========================
            // 3. AST
            // =========================

            System.out.println("\n=== 3. VISUALIZACION DEL AST ===");
            mostrarArbol(arbol, parser);
            System.out.println("   Ventana del arbol sintactico abierta");

            // =========================
            // 4. SEMANTICO
            // =========================

            System.out.println("\n=== 4. ANALISIS SEMANTICO ===");

            SemanticAnalyzer analyzer = new SemanticAnalyzer();
            analyzer.visit(arbol);

            System.out.println("   Tabla de simbolos construida:");
            analyzer.getTablaSimbolos().imprimirTabla();

            // Errores semanticos
            if (analyzer.hayErrores()) {
                System.out.println("ERRORES SEMANTICOS:");
                for (SemanticError e : analyzer.getErrores()) {
                    System.out.println("   Error: " + e.getMensaje());
                }

                if (analyzer.hayAdvertencias()) {
                    System.out.println("WARNINGS SEMANTICOS:");
                    for (SemanticError w : analyzer.getAdvertencias()) {
                        System.out.println("   Warning: " + w.getMensaje());
                    }
                    System.out.println("   El codigo tiene warnings, pero se puede continuar.");
                }

                System.out.println("Compilacion detenida debido a errores semanticos.");
                return;
            }

            // Solo warnings (sin errores)
            if (analyzer.hayAdvertencias()) {
                System.out.println("WARNINGS SEMANTICOS:");
                for (SemanticError w : analyzer.getAdvertencias()) {
                    System.out.println("   Warning: " + w.getMensaje());
                }
                System.out.println("   El codigo tiene warnings, pero se puede continuar.");
            }

            System.out.println(" Analisis semantico completado sin errores.");

            // =========================
            // 5. CODIGO INTERMEDIO
            // =========================

            System.out.println("\n=== 5. GENERACION DE CODIGO INTERMEDIO ===");
            System.out.println(" Iniciando recorrido del AST con CodigoTresDir...");

            CodigoTresDir generador = new CodigoTresDir();
            generador.visit(arbol);

            generador.imprimir();

            // Calcular nombre base (sin extension) para todos los archivos de salida
            String nombreBase = Paths.get(archivo).getFileName().toString();
            if (nombreBase.contains(".")) {
                nombreBase = nombreBase.substring(0, nombreBase.lastIndexOf('.'));
            }

            String archivoSalida = nombreBase + "_codigo_intermedio.txt";

            try (PrintWriter pw = new PrintWriter(archivoSalida, StandardCharsets.UTF_8)) {
                pw.println("// Codigo de tres direcciones generado");
                for (String linea : generador.getLineas()) {
                    pw.println(linea);
                }
            }

            System.out.println("  Codigo intermedio guardado en: " + archivoSalida);

            // =========================
            // 6. OPTIMIZACIÓN
            // =========================

            System.out.println("\n=== 6. OPTIMIZACION DE CODIGO ===");
            System.out.println("   Aplicando optimizaciones al codigo intermedio...");

            Optimizador optimizador = new Optimizador(generador);
            // Pasan nombreBase para que guarde un archivo por cada pasada:
            //   {nombreBase}_optimizacion_pasada_1.txt, _pasada_2.txt, etc.
            optimizador.optimizar(nombreBase);

            System.out.println("Optimizacion completada:");
            System.out.printf("   Instrucciones originales:  %d%n", optimizador.getCantidadOriginal());
            System.out.printf("   Instrucciones optimizadas: %d%n", optimizador.getCantidadOptimizada());
            System.out.printf("   Instrucciones eliminadas:  %d%n", optimizador.getCantidadEliminadas());
            System.out.printf("   Reduccion de codigo: %.2f%%%n",   optimizador.getPorcentajeReduccion());

            System.out.println();
            optimizador.imprimir();

            String archivoOptimizado = nombreBase + "_codigo_optimizado.txt";

            try (PrintWriter pwOpt = new PrintWriter(archivoOptimizado, StandardCharsets.UTF_8)) {
                pwOpt.println("// Codigo optimizado generado");
                for (String linea : optimizador.getLineas()) {
                    pwOpt.println(linea);
                }
            }

            System.out.println("  Codigo optimizado guardado en: " + archivoOptimizado);

            // =========================
            // 7. RESUMEN (solo si compilacion exitosa)
            // =========================

            System.out.println("\n=== 7. RESUMEN DE COMPILACION ===");
            System.out.println("   Archivo procesado:         " + archivo);
            System.out.println("   Tokens analizados:         " + cantidadTokens);
            System.out.println("   Simbolos en tabla:         " + contarSimbolos(analyzer));
            System.out.println("   Instrucciones generadas:   " + optimizador.getCantidadOriginal());
            System.out.println("   Instrucciones optimizadas: " + optimizador.getCantidadOptimizada());
            System.out.println("   Archivo codigo intermedio: " + archivoSalida);
            System.out.println("   Archivo codigo optimizado: " + archivoOptimizado);
            System.out.println("    COMPILACION Y OPTIMIZACION EXITOSA!");

        } catch (IOException e) {
            System.out.println("Error al leer archivo: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado:");
            e.printStackTrace(System.out);
        }
    }

    private static int contarSimbolos(SemanticAnalyzer analyzer) {
        int count = 0;
        for (Scope scope : analyzer.getTablaSimbolos().getHistorial()) {
            count += scope.getSimbolos().size();
        }
        return count;
    }

    private static void mostrarArbol(ParseTree tree, Parser parser) {
        JFrame frame = new JFrame("Arbol Sintactico");
        TreeViewer viewer = new TreeViewer(Arrays.asList(parser.getRuleNames()), tree);
        viewer.setScale(1.2);
        frame.add(new JScrollPane(viewer));
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}