package com.compilador.semantico;

/**
 * ============================================================
 * TYPE SYSTEM
 * ============================================================
 *
 * Esta clase centraliza TODAS las reglas de tipos del lenguaje.
 *
 * El análisis semántico NO decide tipos por sí mismo.
 * Siempre delega en TypeSystem:
 *
 *   compatibilidad de asignaciones
 *   tipos resultantes de operaciones aritméticas
 *   tipos resultantes de comparaciones
 *   tipos resultantes de operadores lógicos
 *   validación de operadores unarios (!, -)
 *
 */

public class TypeSystem {

    private TypeSystem() {}

    // ============================================================
    // TIPOS DEL LENGUAJE
    // ============================================================

    public static final String INT    = "int";
    public static final String DOUBLE = "double";
    public static final String CHAR   = "char";
    public static final String STRING = "string";
    public static final String BOOL   = "bool";
    public static final String VOID   = "void";

    public static final String ERROR  = "ERROR";

    // ============================================================
    // ASIGNACIONES
    // ============================================================

    /**
     * Verifica si es válido asignar un valor de tipo "desde"
     * a una variable de tipo "hacia".
     *
     * Reglas del lenguaje:
     *
     *   int    <- int
     *   double <- double
     *   double <- int (widening: convertir un tipo mas pequeño a uno mas grande)
     *   char   <- char
     *   string <- string
     *   bool   <- bool
     *
     * Cualquier otra combinación es inválida.
     */

    public static boolean esCompatibleAsignacion(String hacia, String desde) {

        if (hacia == null || desde == null) return false;

        // Si ya hubo un error previo, no duplicamos errores
        if (ERROR.equals(hacia) || ERROR.equals(desde)) return true;

        // Tipos idénticos
        if (hacia.equals(desde)) return true;

        // Widening: int - double
        if (DOUBLE.equals(hacia) && INT.equals(desde)) return true;

        return false;
    }

    // ============================================================
    // OPERACIONES ARITMETICAS
    // ============================================================

    /**
     * Determina el tipo resultante de una operación aritmética.
     *
     * Reglas:
     *   int + int       - int
     *   int + double    - double
     *   double + double - double
     *
     * Si alguno no es numérico - ERROR.
     */
    
    public static String inferirAritmetico(String izq, String der) {

        if (ERROR.equals(izq) || ERROR.equals(der) || izq == null || der == null)
            return ERROR;

        if (!esNumerico(izq) || !esNumerico(der))
            return ERROR;

        if (DOUBLE.equals(izq) || DOUBLE.equals(der))
            return DOUBLE;

        return INT;
    }

    // ============================================================
    // OPERADORES RELACIONALES
    // ============================================================

    /**
     * Reglas:
     *   num < num - bool
     *   num > num - bool
     */
    
    public static String inferirRelacional(String izq, String der) {

        if (ERROR.equals(izq) || ERROR.equals(der) || izq == null || der == null)
            return ERROR;

        if (!esNumerico(izq) || !esNumerico(der))
            return ERROR;

        return BOOL;
    }

    // ============================================================
    // IGUALDAD
    // ============================================================

    /**
     * Reglas:
     *   tipos iguales - bool
     *   num == num   - bool
     *   otro caso    - error
     */
    
    public static String inferirIgualdad(String izq, String der) {

        if (ERROR.equals(izq) || ERROR.equals(der) || izq == null || der == null)
            return ERROR;

        if (izq.equals(der)) return BOOL;

        if (esNumerico(izq) && esNumerico(der)) return BOOL;

        return ERROR;
    }

    // ============================================================
    // LOGICOS
    // ============================================================

    /**
     * Reglas:
     *   bool && bool - bool
     *   bool || bool - bool
     */
    
    public static String inferirLogico(String izq, String der) {

        if (ERROR.equals(izq) || ERROR.equals(der) || izq == null || der == null)
            return ERROR;

        if (BOOL.equals(izq) && BOOL.equals(der))
            return BOOL;

        return ERROR;
    }

    // ============================================================
    // NOT
    // ============================================================

    /**
     * Reglas:
     *   !bool - bool
     */
    
    public static String inferirNot(String operando) {

        if (ERROR.equals(operando) || operando == null)
            return ERROR;

        return BOOL.equals(operando) ? BOOL : ERROR;
    }

    // ============================================================
    // NEGATIVO UNARIO
    // ============================================================

    /**
     * Reglas:
     *   -num - num
     */
    
    public static String inferirNegativo(String operando) {

        if (ERROR.equals(operando) || operando == null)
            return ERROR;

        return esNumerico(operando) ? operando : ERROR;
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    /** Retorna true si el tipo es numérico */
    
    public static boolean esNumerico(String tipo) {
        return INT.equals(tipo) || DOUBLE.equals(tipo);
    }

    /** Verifica si un tipo pertenece al lenguaje */
    
    public static boolean esValido(String tipo) {
        return INT.equals(tipo)
                || DOUBLE.equals(tipo)
                || CHAR.equals(tipo)
                || STRING.equals(tipo)
                || BOOL.equals(tipo)
                || VOID.equals(tipo);
    }

    // ============================================================
    // MENSAJES
    // ============================================================

    public static String msgIncompatible(String hacia, String desde) {
        return "no se puede asignar tipo '" + desde + "' a variable de tipo '" + hacia + "'";
    }

    public static String msgOperadorInvalido(String operador, String izq, String der) {
        return "el operador '" + operador + "' no puede aplicarse a tipos '" + izq + "' y '" + der + "'";
    }

    public static String msgUnarioInvalido(String operador, String tipo) {
        return "el operador '" + operador + "' no puede aplicarse al tipo '" + tipo + "'";
    }
}
