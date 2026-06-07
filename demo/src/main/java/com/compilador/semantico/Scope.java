package com.compilador.semantico;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Scope {

    private final String              nombre;       // nombre interno: "sumar_1", "bloque_2"
    private final String              nombreAmbito; // nombre para mostrar: "sumar", "main", "global"
    private final Scope               padre;
    private final Map<String, Symbol> simbolos;

    public Scope(String nombre, String nombreAmbito, Scope padre) {
        this.nombre       = nombre;
        this.nombreAmbito = nombreAmbito;
        this.padre        = padre;
        this.simbolos     = new LinkedHashMap<>();
    }

    /** Constructor para el scope global */
    public Scope(String nombre, Scope padre) {
        this(nombre, nombre, padre);
    }

    public boolean definir(Symbol simbolo) {
        if (simbolos.containsKey(simbolo.getNombre())) return false;
        simbolos.put(simbolo.getNombre(), simbolo);
        return true;
    }

    public Symbol resolver(String nombre) {
        Symbol s = simbolos.get(nombre);
        if (s != null) return s;
        if (padre != null) return padre.resolver(nombre);
        return null;
    }

    public boolean estaDefinidoLocalmente(String nombre) {
        return simbolos.containsKey(nombre);
    }

    public String             getNombre()       { return nombre;       }
    public String             getNombreAmbito() { return nombreAmbito; }
    public Scope              getPadre()        { return padre;        }
    public Collection<Symbol> getSimbolos()     { return simbolos.values(); }
    public boolean            esGlobal()        { return padre == null; }

    @Override
    public String toString() {
        return "Scope[" + nombre + "]" + (esGlobal() ? " (global)" : "");
    }
}
