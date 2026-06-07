package com.compilador.semantico;

import java.util.ArrayList;
import java.util.List;

public class SymbolTable {

    private Scope scopeActual;
    private int contadorScopes;
    private final List<Scope> historial;

    public SymbolTable() {
        Scope global = new Scope("global", null);
        this.scopeActual = global;
        this.contadorScopes = 0;
        this.historial = new ArrayList<>();
        this.historial.add(global);
    }

    /**
     * Entra a un scope de FUNCION: el nombreAmbito es el nombre de la funcion.
     * Entra a un scope de BLOQUE: hereda el nombreAmbito de la funcion contenedora.
     */
    public void entrarScope(String contexto) {
        contadorScopes++;
        String nombreAmbito;
        if ("bloque".equals(contexto)) {
            // Los bloques muestran el mismo ambito que la funcion que los contiene
            nombreAmbito = scopeActual.getNombreAmbito();
        } else {
            // Funciones usan su propio nombre
            nombreAmbito = contexto;
        }
        Scope nuevo = new Scope(contexto + "_" + contadorScopes, nombreAmbito, scopeActual);
        historial.add(nuevo);
        scopeActual = nuevo;
    }

    public void salirScope() {
        if (scopeActual.getPadre() != null) {
            scopeActual = scopeActual.getPadre();
        }
    }

    public boolean definir(Symbol simbolo) {
        return scopeActual.definir(simbolo);
    }

    public Symbol resolver(String nombre) {
        return scopeActual.resolver(nombre);
    }

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
