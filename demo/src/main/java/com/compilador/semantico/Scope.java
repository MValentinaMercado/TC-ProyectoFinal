package com.compilador.semantico;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ============================================================
 * SCOPE
 * ============================================================
 *
 * Un Scope representa un ÁMBITO del lenguaje:
 *
 *   - global
 *   - el cuerpo de una función
 *   - un bloque interno { ... }
 *
 * La tabla de símbolos se implementa como una PILA de scopes.
 * Cada scope contiene:
 *   un nombre interno único (sumar_1, bloque_3)
 *   un nombre visible para el usuario (sumar, main, global)
 *   un puntero al scope padre
 *   un mapa de símbolos declarados en ese ámbito
 *
 */

public class Scope {

    /** Nombre interno único del scope (para depuración) */

    private final String nombre;

    /** Nombre visible del ámbito (para mensajes de error) */

    private final String nombreAmbito;

    /** Scope padre (null si es global) */

    private final Scope padre;

    /** Símbolos declarados en este ámbito */

    private final Map<String, Symbol> simbolos;

    /**
     * Constructor general.
     *
     * @param nombre        nombre interno único
     * @param nombreAmbito  nombre visible del ámbito
     * @param padre         scope padre (null si es global)
     */

    public Scope(String nombre, String nombreAmbito, Scope padre) {
        this.nombre       = nombre;
        this.nombreAmbito = nombreAmbito;
        this.padre        = padre;
        this.simbolos     = new LinkedHashMap<>();
    }

    /**
     * Constructor para el scope global.
     * El nombre interno y visible coinciden.
     */

    public Scope(String nombre, Scope padre) {
        this(nombre, nombre, padre);
    }

    /**
     * Define un símbolo en el scope actual.
     * Retorna false si ya existe uno con el mismo nombre.
     */

    public boolean definir(Symbol simbolo) {
        if (simbolos.containsKey(simbolo.getNombre())) return false;
        simbolos.put(simbolo.getNombre(), simbolo);
        return true;
    }

    /**
     * Busca un símbolo por nombre.
     * Si no está en el scope actual, busca recursivamente en los padres.
     *
     * Esto implementa el comportamiento clásico de los lenguajes:
     *   - primero busca local
     *   - luego en el ámbito superior
     *   - y así hasta global
     */

    public Symbol resolver(String nombre) {
        Symbol s = simbolos.get(nombre);
        if (s != null) return s;
        if (padre != null) return padre.resolver(nombre);
        return null;
    }

    /**
     * Verifica si un símbolo está definido en este scope (no en los padres).
     */

    public boolean estaDefinidoLocalmente(String nombre) {
        return simbolos.containsKey(nombre);
    }

    public String             getNombre()       { return nombre;       }
    public String             getNombreAmbito() { return nombreAmbito; }
    public Scope              getPadre()        { return padre;        }
    public Collection<Symbol> getSimbolos()     { return simbolos.values(); }

    /** Retorna true si este es el scope global */

    public boolean esGlobal() { return padre == null; }

    @Override
    public String toString() {

        return "Scope[" + nombre + "]" + (esGlobal() ? " (global)" : "");
    }
}
