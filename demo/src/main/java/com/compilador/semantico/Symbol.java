package com.compilador.semantico;

import java.util.List;
import java.util.ArrayList;

public class Symbol {

    public enum Categoria {
        VARIABLE,
        FUNCION,
        PARAMETRO
    }

    private final String    nombre;
    private final String    tipo;
    private final Categoria categoria;
    private       boolean   inicializado;
    private final int       linea;
    private final int       columna;
    private       boolean   usado;

    // Para arrays
    private final boolean isArray;
    private final int     tamanioArray;

    // Para funciones: lista de tipos de parametros
    private final List<String> paramTypes;

    private Symbol(String nombre, String tipo, Categoria categoria,
                   boolean inicializado, int linea, int columna,
                   boolean isArray, int tamanioArray, List<String> paramTypes) {
        this.nombre       = nombre;
        this.tipo         = tipo;
        this.categoria    = categoria;
        this.inicializado = inicializado;
        this.linea        = linea;
        this.columna      = columna;
        this.isArray      = isArray;
        this.tamanioArray = tamanioArray;
        this.paramTypes   = paramTypes != null ? paramTypes : new ArrayList<>();
        this.usado        = false;
    }

    public static Symbol variable(String nombre, String tipo, boolean inicializado, int linea, int columna) {
        return new Symbol(nombre, tipo, Categoria.VARIABLE, inicializado, linea, columna, false, 0, null);
    }

    public static Symbol array(String nombre, String tipo, int tamanio, int linea, int columna) {
        return new Symbol(nombre, tipo, Categoria.VARIABLE, false, linea, columna, true, tamanio, null);
    }

    public static Symbol funcion(String nombre, String tipoRetorno, int linea, int columna) {
        return new Symbol(nombre, tipoRetorno, Categoria.FUNCION, true, linea, columna, false, 0, new ArrayList<>());
    }

    public static Symbol parametro(String nombre, String tipo, int linea, int columna) {
        return new Symbol(nombre, tipo, Categoria.PARAMETRO, true, linea, columna, false, 0, null);
    }

    /** Agrega un tipo de parametro a la firma de la funcion */
    public void addParamType(String tipo) {
        this.paramTypes.add(tipo);
    }

    /** Retorna la firma de parametros formateada como "[int, int]" */
    public String getParamTypes() {
        return "[" + String.join(", ", paramTypes) + "]";
    }

    public String    getNombre()      { return nombre;       }
    public String    getTipo()        { return tipo;         }
    public Categoria getCategoria()   { return categoria;    }
    public boolean   isInicializado() { return inicializado; }
    public int       getLinea()       { return linea;        }
    public int       getColumna()     { return columna;      }
    public boolean   isArray()        { return isArray;      }
    public int       getTamanioArray(){ return tamanioArray; }
    public boolean   isUsado()        { return usado;        }

    public void setInicializado(boolean inicializado) { this.inicializado = inicializado; }
    public void setUsado(boolean usado)               { this.usado = usado; }

    @Override
    public String toString() {
        return String.format("%-10s %-8s %-12s %s [%d:%d]",
                categoria, tipo, nombre,
                inicializado ? "(inicializado)" : "(sin inicializar)",
                linea, columna);
    }
}
