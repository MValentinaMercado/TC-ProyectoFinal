package com.compilador;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.gui.TreeViewer;

import javax.swing.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Uso:");
            System.out.println("java -jar compilador.jar <archivo.cpp>");
            return;
        }

        try {

            String archivo = args[0];

            System.out.println("Iniciando compilacion de: " + archivo);
            System.out.println("============================================================");

            // ============================================================
            // 1. ANÁLISIS LÉXICO
            // ============================================================

            // Leer el archivo en UTF-8 para evitar problemas con tildes/caracteres especiales
            CharStream input = CharStreams.fromFileName(archivo, StandardCharsets.UTF_8);

            MiLenguajeLexer lexer = new MiLenguajeLexer(input);

            List<String> erroresLexicos = new ArrayList<>();

            lexer.removeErrorListeners();

            lexer.addErrorListener(new BaseErrorListener() {
                @Override
                public void syntaxError(
                        Recognizer<?, ?> recognizer,
                        Object offendingSymbol,
                        int line,
                        int charPositionInLine,
                        String msg,
                        RecognitionException e) {

                    erroresLexicos.add(
                            "Linea " + line +
                                    ", columna " + charPositionInLine +
                                    ": " + msg
                    );
                }
            });

            CommonTokenStream tokens = new CommonTokenStream(lexer);
            tokens.fill();

            int cantidadTokens = 0;

            for (Token token : tokens.getTokens()) {
                if (token.getType() != Token.EOF) {
                    cantidadTokens++;
                }
            }

            System.out.println("\n=== 1. ANALISIS LEXICO ===");

            if (!erroresLexicos.isEmpty()) {

                System.out.println("\nERRORES LEXICOS:");

                for (String error : erroresLexicos) {
                    System.out.println("ERROR " + error);
                }

                return;
            }

            System.out.println("Analisis lexico completado sin errores.");
            System.out.println("Tokens procesados: " + cantidadTokens);

            // ============================================================
            // 2. ANÁLISIS SINTÁCTICO
            // ============================================================

            System.out.println("\n=== 2. ANALISIS SINTACTICO ===");

            MiLenguajeParser parser = new MiLenguajeParser(tokens);

            List<String> erroresSintacticos = new ArrayList<>();

            parser.removeErrorListeners();

            parser.addErrorListener(new BaseErrorListener() {
                @Override
                public void syntaxError(
                        Recognizer<?, ?> recognizer,
                        Object offendingSymbol,
                        int line,
                        int charPositionInLine,
                        String msg,
                        RecognitionException e) {

                    erroresSintacticos.add(
                            "Linea " + line +
                                    ", columna " + charPositionInLine +
                                    ": " + msg
                    );
                }
            });

            MiLenguajeParser.ProgramaContext arbol = parser.programa();

            if (!erroresSintacticos.isEmpty()) {

                System.out.println("\nERRORES SINTACTICOS:");

                for (String error : erroresSintacticos) {
                    System.out.println("ERROR " + error);
                }

                return;
            }

            System.out.println("Analisis sintactico completado sin errores.");
            System.out.println("Arbol sintactico generado correctamente");

            // ============================================================
            // 3. VISUALIZACIÓN AST
            // ============================================================

            System.out.println("\n=== 3. VISUALIZACION DEL AST ===");
            System.out.println("Ventana del árbol sintáctico abierta");

            mostrarArbol(arbol, parser);

            System.out.println("\nFase lexica y sintactica completadas correctamente.");

        }
        catch (IOException e) {

            System.err.println("\nError al leer archivo:");
            System.err.println(e.getMessage());

        }
        catch (Exception e) {

            System.err.println("\nError inesperado:");
            e.printStackTrace(System.err);

        }
    }

    private static void mostrarArbol(ParseTree tree, Parser parser) {

        JFrame frame = new JFrame("Arbol Sintactico");

        TreeViewer viewer =
                new TreeViewer(
                        Arrays.asList(parser.getRuleNames()),
                        tree
                );

        viewer.setScale(1.2);

        JScrollPane scrollPane =
                new JScrollPane(viewer);

        frame.add(scrollPane);

        frame.setSize(1200, 800);

        frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
        );

        frame.setVisible(true);
    }
}