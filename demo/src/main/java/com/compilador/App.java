package com.compilador;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.gui.TreeViewer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java -jar demo-1.0-jar-with-dependencies.jar <archivo.txt>");
            System.exit(1);
        }

        try {
            System.out.println("Analizando archivo: " + args[0]);

            CharStream inputLexico = CharStreams.fromFileName(args[0]);
            realizarAnalisisLexico(inputLexico);

            CharStream inputSintactico = CharStreams.fromFileName(args[0]);
            realizarAnalisisSintactico(inputSintactico);

        } catch (IOException e) {
            System.err.println("❌ Error al leer el archivo: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void realizarAnalisisLexico(CharStream input) {
        MiLenguajeLexer lexer = new MiLenguajeLexer(input);

        List<String> errores = new ArrayList<>();
        lexer.removeErrorListeners();
        lexer.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                    int line, int charPositionInLine, String msg, RecognitionException e) {
                String errorMsg = "ERROR LÉXICO en línea " + line + ":" + charPositionInLine + " - " + msg;
                errores.add(errorMsg);
                throw new ParseCancellationException(errorMsg);
            }
        });

        try {
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            tokens.fill();

            System.out.println("\n=== ANÁLISIS LÉXICO ===");
            System.out.printf("%-20s %-30s %-10s %-10s\n", "TIPO", "LEXEMA", "LÍNEA", "COLUMNA");
            System.out.println("-------------------------------------------------------------------");

            for (Token token : tokens.getTokens()) {
                if (token.getType() != Token.EOF) {
                    String tokenName = MiLenguajeLexer.VOCABULARY.getSymbolicName(token.getType());
                    System.out.printf("%-20s %-30s %-10d %-10d\n",
                            tokenName, token.getText(), token.getLine(), token.getCharPositionInLine());
                }
            }

            System.out.println("\n✅ Análisis léxico completado sin errores.");

        } catch (ParseCancellationException e) {
            System.out.println("\n❌ " + e.getMessage());
        }
    }

    private static void realizarAnalisisSintactico(CharStream input) {
        MiLenguajeLexer lexer = new MiLenguajeLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        MiLenguajeParser parser = new MiLenguajeParser(tokens);

        parser.removeErrorListeners();
        parser.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                    int line, int charPositionInLine, String msg, RecognitionException e) {
                String errorMsg = "ERROR SINTÁCTICO en línea " + line + ":" + charPositionInLine + " - " + msg;
                throw new ParseCancellationException(errorMsg);
            }
        });

        try {
            System.out.println("\n=== ANÁLISIS SINTÁCTICO DE EXPRESIONES ===");

            ParseTree tree = parser.expr();

            System.out.println("\n✅ Análisis sintáctico completado sin errores.");
            System.out.println("Árbol sintáctico (texto):");
            System.out.println(tree.toStringTree(parser));

            System.out.println("\nEstructura de la expresión:");
            ExprVisitor visitor = new ExprVisitor();
            visitor.visit(tree);

            mostrarArbolGrafico(tree, parser);

        } catch (ParseCancellationException e) {
            System.out.println("\n❌ " + e.getMessage());
            System.out.println("Intentando analizar como programa general...");

            try {
                tokens.seek(0);
                parser.reset();

                ParseTree tree = parser.programa();
                System.out.println("✅ Análisis como programa general completado sin errores.");

            } catch (Exception ex) {
                System.out.println("❌ ERROR: El archivo no cumple con la gramática definida.");
            }
        }
    }

    private static void mostrarArbolGrafico(ParseTree tree, MiLenguajeParser parser) {
        JFrame frame = new JFrame("Árbol Sintáctico");
        JPanel panel = new JPanel();

        TreeViewer viewer = new TreeViewer(Arrays.asList(parser.getRuleNames()), tree);
        viewer.setScale(1.5);

        panel.add(viewer);

        JScrollPane scrollPane = new JScrollPane(panel);
        frame.add(scrollPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }
}

class ExprVisitor extends MiLenguajeBaseVisitor<Void> {

    private int indentLevel = 0;

    private void indent() {
        for (int i = 0; i < indentLevel; i++) System.out.print("  ");
    }

    @Override
    public Void visitChildren(RuleNode node) {
        indent();
        String name = node.getClass().getSimpleName().replace("Context", "");
        System.out.println(name);
        indentLevel++;
        super.visitChildren(node);
        indentLevel--;
        return null;
    }

    @Override
    public Void visitTerminal(TerminalNode node) {
        indent();
        System.out.println("TOKEN: " + node.getText());
        return null;
    }
}
