package com.compilador.semantico;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 * TABLA DE SÍMBOLOS
 * ============================================================
 *
 * Esta clase administra la pila de scopes del compilador.
 *
 * Es una PILA de ámbitos (Scope), donde cada ámbito contiene:
 *   - variables
 *   - parámetros
 *   - funciones
 *
 * El análisis semántico:
 *   entra a un scope cuando comienza una función o un bloque
 *   sale del scope al cerrar la llave }
 *   define símbolos en el scope actual
 *   resuelve símbolos buscando recursivamente en los padres
 *
 */

public class SymbolTable {

    /** Scope actual (el tope de la pila) */

    private Scope scopeActual;

    /** Contador para generar nombres únicos de scopes */

    private int contadorScopes;

    /** Historial de todos los scopes creados (para imprimir la tabla completa) */

    private final List<Scope> historial;

    /**
     * Constructor.
     * Crea el scope GLOBAL, que no tiene padre.
     */

    public SymbolTable() {
        Scope global = new Scope("global", null);
        this.scopeActual = global;
        this.contadorScopes = 0;
        this.historial = new ArrayList<>();
        this.historial.add(global);
    }

    /**
     * Entra a un nuevo scope.
     *
     * @param contexto nombre del contexto:
     *                 - "bloque" - bloque interno { }
     *                 - nombre de función - scope de función
     *
     * Reglas:
     *    Los bloques heredan el nombre visible del ámbito padre
     *    Las funciones usan su propio nombre como ámbito visible
     *
     * Ejemplos:
     *   entrarScope("sumar")  - ámbito "sumar"
     *   entrarScope("bloque") - ámbito "sumar" (si estamos dentro de sumar)
     */

    public void entrarScope(String contexto) {
        contadorScopes++;

        String nombreAmbito;

        if ("bloque".equals(contexto)) {
            // Los bloques no cambian el nombre visible del ámbito
            nombreAmbito = scopeActual.getNombreAmbito();
        } else {
            // Las funciones sí
            nombreAmbito = contexto;
        }

        // Nombre interno único: sumar_1, bloque_2, etc.
        Scope nuevo = new Scope(contexto + "_" + contadorScopes, nombreAmbito, scopeActual);

        historial.add(nuevo);
        scopeActual = nuevo;
    }

    /**
     * Sale del scope actual y vuelve al padre.
     * No hace nada si ya estamos en el global.
     */

    public void salirScope() {
        if (scopeActual.getPadre() != null) {
            scopeActual = scopeActual.getPadre();
        }
    }

    /**
     * Define un símbolo en el scope actual.
     * Retorna false si ya existe uno con el mismo nombre.
     */

    public boolean definir(Symbol simbolo) {
        return scopeActual.definir(simbolo);
    }

    /**
     * Busca un símbolo por nombre.
     * La búsqueda es recursiva hacia los scopes padres.
     */

    public Symbol resolver(String nombre) {
        return scopeActual.resolver(nombre);
    }

    /**
     * Verifica si un símbolo está declarado en el scope actual.
     */

    public boolean estaDeclaradoLocalmente(String nombre) {
        return scopeActual.estaDefinidoLocalmente(nombre);
    }

    public String getNombreScopeActual() {
        return scopeActual.getNombre();
    }

    public String getNombreAmbitoActual() {
        return scopeActual.getNombreAmbito();
    }

    public Scope getScopeActual() {
        return scopeActual;
    }

    public List<Scope> getHistorial() {
        return historial;
    }

    /**
     * Imprime la tabla de símbolos completa.
     * Recorre todos los scopes en orden de creación.
     */

    public void imprimirTabla() {
        System.out.println();
        System.out.println("=== TABLA DE SIMBOLOS ===");
        System.out.printf("%-16s%-11s%-16s%-11s%-11s%-16s%s%n",
                "NOMBRE", "TIPO", "CATEGORIA", "LINEA", "COLUMNA", "AMBITO", "DETALLES");
        System.out.println("--------------------------------------------------------------------------------------------");

        for (Scope scope : historial) {
            for (Symbol s : scope.getSimbolos()) {

                String detalles = "";
                if (s.getCategoria() == Symbol.Categoria.VARIABLE) {
                    detalles = s.isArray()
                            ? "[arr:" + s.getTamanioArray() + "] [private]"
                            : "[private]";
                } else if (s.getCategoria() == Symbol.Categoria.FUNCION) {
                    detalles = "[private] " + s.getParamTypes();
                }

                System.out.printf("%-16s%-11s%-16s%-11d%-11d%-16s%s%n",
                        s.getNombre(),
                        s.getTipo(),
                        s.getCategoria().toString().toLowerCase(),
                        s.getLinea(),
                        s.getColumna(),
                        scope.getNombreAmbito(),
                        detalles
                );
            }
        }
    }
}
