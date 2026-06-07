package com.compilador.codigoIntermedio;

public class Instruccion {

    public enum Tipo {
        COMENTARIO,          // // texto
        ETIQUETA,            // LABEL:
        DECLARE,             // DECLARE nombre tipo
        DECLARE_ARRAY,       // DECLARE nombre[size] tipo
        PARAM_DEF,           // PARAM nombre tipo  (cabecera de funcion)
        ASIGNAR,             // dest = src
        ASIGNAR_ARRAY_W,     // arr[idx] = val
        ASIGNAR_ARRAY_R,     // dest = arr[idx]
        BINARIA,             // dest = a op b
        UNARIA,              // dest = op a
        SALTO_INCOND,        // goto LABEL
        SALTO_COND_VERDAD,   // if cond goto LABEL
        CALL,                // CALL func, arg1, arg2, ...
        RETURN_VALUE_ASSIGN, // dest = RETURN_VALUE
        RETORNO,             // return val  o  return
    }

    private final Tipo   tipo;
    private final String resultado;  // destino, nombre etiqueta/funcion, nombre en DECLARE/PARAM
    private final String arg1;       // fuente/operando-izq/condicion/tipo en DECLARE/lista-args en CALL
    private final String operador;   // +, -, *, /, %, ==, !=, <, >, <=, >=, &&, ||, !, -
    private final String arg2;       // operando-der, indice-array, size en DECLARE_ARRAY

    private Instruccion(Tipo tipo, String resultado, String arg1, String operador, String arg2) {
        this.tipo      = tipo;
        this.resultado = resultado;
        this.arg1      = arg1;
        this.operador  = operador;
        this.arg2      = arg2;
    }

    public Tipo getTipo() { return tipo; }

    // =========================================================================
    // FABRICAS
    // =========================================================================

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
     * Se usa para saltar cuando la condicion es VERDADERA (patron del ejemplo_correcto:
     *   t10 = estado > 0
     *   if t10 goto THEN_1
     *   goto END_IF_1
     *   THEN_1:
     *   ...cuerpo...
     *   END_IF_1:
     */
    public static Instruccion saltoCondVerdad(String cond, String label) {
        return new Instruccion(Tipo.SALTO_COND_VERDAD, label, cond, null, null);
    }

    /**
     * CALL func, arg1, arg2, ...
     * argsStr: argumentos separados por ", ", puede ser "" si no hay args.
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

    // =========================================================================
    // toString
    // =========================================================================

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
                // resultado[arg1] = arg2
                return resultado + "[" + arg1 + "] = " + arg2;
            case ASIGNAR_ARRAY_R:
                // resultado = arg1[arg2]
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
                if (arg1 != null && !arg1.isEmpty()) {
                    return "CALL " + resultado + ", " + arg1;
                } else {
                    return "CALL " + resultado;
                }
            case RETURN_VALUE_ASSIGN:
                return resultado + " = RETURN_VALUE";
            case RETORNO:
                if (arg1 != null) return "return " + arg1;
                else              return "return";
            default:
                return "???";
        }
    }
}