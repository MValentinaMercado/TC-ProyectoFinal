package com.compilador.semantico;

import java.util.List;
import java.util.ArrayList;

/**
 * ============================================================
 * SYMBOL
 * ============================================================
 *
 * Un Symbol representa cualquier identificador declarado:
 *
 *   variable
 *   parámetro
 *   función
 *   array
 *
 * Cada símbolo almacena:
 *   - nombre
 *   - tipo
 *   - categoría (variable, función, parámetro)
 *   - si está inicializado
 *   - posición en el código (línea, columna)
 *   - si fue usado (para warnings)
 *   - información adicional (arrays, parámetros de función)
 *
 */

public class Symbol {

    /** Categorías posibles de un símbolo */

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

    // Información para arrays

    private final boolean isArray;
    private final int     tamanioArray;

    // Para funciones: lista de tipos de parámetros

    private final List<String> paramTypes;

    /**
     * Constructor privado.
     * Se usa a través de los métodos estáticos factory.
     */

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

    // ============================================================
    // FACTORY METHODS (creación de símbolos)
    // ============================================================

    /** Crea una variable simple */

    public static Symbol variable(String nombre, String tipo, boolean inicializado, int linea, int columna) {
        return new Symbol(nombre, tipo, Categoria.VARIABLE, inicializado, linea, columna, false, 0, null);
    }

    /** Crea un array */

    public static Symbol array(String nombre, String tipo, int tamanio, int linea, int columna) {
        return new Symbol(nombre, tipo, Categoria.VARIABLE, false, linea, columna, true, tamanio, null);
    }

    /** Crea una función */

    public static Symbol funcion(String nombre, String tipoRetorno, int linea, int columna) {
        return new Symbol(nombre, tipoRetorno, Categoria.FUNCION, true, linea, columna, false, 0, new ArrayList<>());
    }

    /** Crea un parámetro */

    public static Symbol parametro(String nombre, String tipo, int linea, int columna) {
        return new Symbol(nombre, tipo, Categoria.PARAMETRO, true, linea, columna, false, 0, null);
    }

    // ============================================================
    // FUNCIONES PARA MANEJO DE FUNCIONES
    // ============================================================

    /** Agrega un tipo de parámetro a la firma de la función */

    public void addParamType(String tipo) {
        this.paramTypes.add(tipo);
    }

    /** Retorna la firma de parámetros formateada como "[int, int]" */

    public String getParamTypes() {
        return "[" + String.join(", ", paramTypes) + "]";
    }

    // ============================================================
    // GETTERS
    // ============================================================

    public String    getNombre()      { return nombre;       }
    public String    getTipo()        { return tipo;         }
    public Categoria getCategoria()   { return categoria;    }
    public boolean   isInicializado() { return inicializado; }
    public int       getLinea()       { return linea;        }
    public int       getColumna()     { return columna;      }
    public boolean   isArray()        { return isArray;      }
    public int       getTamanioArray(){ return tamanioArray; }
    public boolean   isUsado()        { return usado;        }

    // ============================================================
    // SETTERS
    // ============================================================

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
