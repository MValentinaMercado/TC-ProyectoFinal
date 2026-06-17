package com.compilador.codigoIntermedio;

/**
 * ============================================================
 * Instruccion
 * ============================================================
 * Representa una instrucción de Código de Tres Direcciones.
 *
 * Cada instrucción es inmutable y tiene un tipo (enum Tipo) que determina
 * qué campos son relevantes. Los campos resultado, arg1, operador y arg2
 * se reutilizan según el tipo de instrucción.
 *
 */

public class Instruccion {

    /**
     * Tipos de instrucciones TAC soportadas por el compilador.
     *
     * COMENTARIO            - línea informativa, no ejecutable
     * ETIQUETA              - punto de salto (LABEL:)
     * DECLARE               - declaración de variable
     * DECLARE_ARRAY         - declaración de arreglo
     * PARAM_DEF             - definición de parámetro formal de función
     * ASIGNAR               - asignación simple
     * ASIGNAR_ARRAY_W       - escritura en arreglo: arr[idx] = val
     * ASIGNAR_ARRAY_R       - lectura de arreglo: dest = arr[idx]
     * BINARIA               - operación binaria: dest = a op b
     * UNARIA                - operación unaria: dest = op a
     * SALTO_INCOND          - goto LABEL
     * SALTO_COND_VERDAD     - if cond goto LABEL
     * CALL                  - llamada a función
     * RETURN_VALUE_ASSIGN   - dest = RETURN_VALUE
     * RETORNO               - return val
     */
    
    public enum Tipo {
        COMENTARIO,
        ETIQUETA,
        DECLARE,
        DECLARE_ARRAY,
        PARAM_DEF,
        ASIGNAR,
        ASIGNAR_ARRAY_W,
        ASIGNAR_ARRAY_R,
        BINARIA,
        UNARIA,
        SALTO_INCOND,
        SALTO_COND_VERDAD,
        CALL,
        RETURN_VALUE_ASSIGN,
        RETORNO,
    }

    // -------------------------------------------------------------------------
    // CAMPOS INTERNOS
    // -------------------------------------------------------------------------

    private final Tipo   tipo;       // Tipo de instrucción TAC
    private final String resultado;  // destino o etiqueta según el tipo
    private final String arg1;       // operando izquierdo, condición, tipo, args...
    private final String operador;   // operador binario/unario
    private final String arg2;       // operando derecho, índice, tamaño array

    /**
     * Constructor privado: todas las instrucciones se crean mediante
     * métodos fábrica estáticos para evitar inconsistencias.
     */
    
    private Instruccion(Tipo tipo, String resultado, String arg1, String operador, String arg2) {
        this.tipo      = tipo;
        this.resultado = resultado;
        this.arg1      = arg1;
        this.operador  = operador;
        this.arg2      = arg2;
    }

    public Tipo getTipo() { return tipo; }

    // -------------------------------------------------------------------------
    // MÉTODOS FÁBRICA — crean instrucciones TAC específicas
    // -------------------------------------------------------------------------

    /** // comentario */
    
    public static Instruccion comentario(String texto) {
        return new Instruccion(Tipo.COMENTARIO, texto, null, null, null);
    }

    /** LABEL: */
    
    public static Instruccion etiqueta(String label) {
        return new Instruccion(Tipo.ETIQUETA, label, null, null, null);
    }

    /** DECLARE nombre tipo */
    
    public static Instruccion declare(String nombre, String tipoDato) {
        return new Instruccion(Tipo.DECLARE, nombre, tipoDato, null, null);
    }

    /** DECLARE nombre[size] tipo */
    
    public static Instruccion declareArray(String nombre, String size, String tipoDato) {
        return new Instruccion(Tipo.DECLARE_ARRAY, nombre, tipoDato, null, size);
    }

    /** PARAM nombre tipo */
    
    public static Instruccion paramDef(String nombre, String tipoDato) {
        return new Instruccion(Tipo.PARAM_DEF, nombre, tipoDato, null, null);
    }

    /** dest = src */
    
    public static Instruccion asignar(String dest, String src) {
        return new Instruccion(Tipo.ASIGNAR, dest, src, null, null);
    }

    /** arr[idx] = val */
    
    public static Instruccion asignarArrayW(String arr, String idx, String val) {
        return new Instruccion(Tipo.ASIGNAR_ARRAY_W, arr, idx, null, val);
    }

    /** dest = arr[idx] */
    
    public static Instruccion asignarArrayR(String dest, String arr, String idx) {
        return new Instruccion(Tipo.ASIGNAR_ARRAY_R, dest, arr, null, idx);
    }

    /** dest = a op b */
    
    public static Instruccion binaria(String dest, String a, String op, String b) {
        return new Instruccion(Tipo.BINARIA, dest, a, op, b);
    }

    /** dest = op a */
    
    public static Instruccion unaria(String dest, String op, String a) {
        return new Instruccion(Tipo.UNARIA, dest, a, op, null);
    }

    /** goto LABEL */
    
    public static Instruccion saltoIncond(String label) {
        return new Instruccion(Tipo.SALTO_INCOND, label, null, null, null);
    }

    /**
     * if cond goto LABEL
     * Salto condicional cuando la condición es verdadera.
     */
    
    public static Instruccion saltoCondVerdad(String cond, String label) {
        return new Instruccion(Tipo.SALTO_COND_VERDAD, label, cond, null, null);
    }

    /**
     * CALL func, arg1, arg2, ...
     * arg1 contiene la lista de argumentos como string.
     */
    
    public static Instruccion call(String func, String argsStr) {
        return new Instruccion(Tipo.CALL, func, argsStr, null, null);
    }

    /** dest = RETURN_VALUE */
    
    public static Instruccion returnValueAssign(String dest) {
        return new Instruccion(Tipo.RETURN_VALUE_ASSIGN, dest, null, null, null);
    }

    /** return valor  o  return (si valor es null) */
    
    public static Instruccion retorno(String valor) {
        return new Instruccion(Tipo.RETORNO, null, valor, null, null);
    }
    
    @Override
    public String toString() {
        switch (tipo) {
            case COMENTARIO:
                return "// " + resultado;

            case ETIQUETA:
                return resultado + ":";

            case DECLARE:
                return "DECLARE " + resultado + " " + arg1;

            case DECLARE_ARRAY:
                return "DECLARE " + resultado + "[" + arg2 + "] " + arg1;

            case PARAM_DEF:
                return "PARAM " + resultado + " " + arg1;

            case ASIGNAR:
                return resultado + " = " + arg1;

            case ASIGNAR_ARRAY_W:
                return resultado + "[" + arg1 + "] = " + arg2;

            case ASIGNAR_ARRAY_R:
                return resultado + " = " + arg1 + "[" + arg2 + "]";

            case BINARIA:
                return resultado + " = " + arg1 + " " + operador + " " + arg2;

            case UNARIA:
                return resultado + " = " + operador + arg1;

            case SALTO_INCOND:
                return "goto " + resultado;

            case SALTO_COND_VERDAD:
                return "if " + arg1 + " goto " + resultado;

            case CALL:
                return (arg1 != null && !arg1.isEmpty())
                        ? "CALL " + resultado + ", " + arg1
                        : "CALL " + resultado;

            case RETURN_VALUE_ASSIGN:
                return resultado + " = RETURN_VALUE";

            case RETORNO:
                return (arg1 != null)
                        ? "return " + arg1
                        : "return";

            default:
                return "???";
        }
    }
}
