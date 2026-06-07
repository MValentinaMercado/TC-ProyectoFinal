package com.compilador.semantico;

/**
 * Centraliza todas las reglas de tipos del lenguaje.
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
     * Reglas:
     *
     * int    <- int
     * double <- double
     * double <- int
     * char   <- char
     * string <- string
     * bool   <- bool
     */
    public static boolean esCompatibleAsignacion(
            String hacia,
            String desde) {

        if (hacia == null || desde == null) {
            return false;
        }

        if (ERROR.equals(hacia) || ERROR.equals(desde)) {
            return true;
        }

        if (hacia.equals(desde)) {
            return true;
        }

        // widening permitido
        if (DOUBLE.equals(hacia) && INT.equals(desde)) {
            return true;
        }

        return false;
    }

    // ============================================================
    // OPERACIONES ARITMETICAS
    // ============================================================

    public static String inferirAritmetico(
            String izq,
            String der) {

        if (ERROR.equals(izq)
                || ERROR.equals(der)
                || izq == null
                || der == null) {

            return ERROR;
        }

        if (!esNumerico(izq) || !esNumerico(der)) {
            return ERROR;
        }

        if (DOUBLE.equals(izq) || DOUBLE.equals(der)) {
            return DOUBLE;
        }

        return INT;
    }

    // ============================================================
    // OPERADORES RELACIONALES
    // ============================================================

    public static String inferirRelacional(
            String izq,
            String der) {

        if (ERROR.equals(izq)
                || ERROR.equals(der)
                || izq == null
                || der == null) {

            return ERROR;
        }

        if (!esNumerico(izq) || !esNumerico(der)) {
            return ERROR;
        }

        return BOOL;
    }

    // ============================================================
    // IGUALDAD
    // ============================================================

    public static String inferirIgualdad(
            String izq,
            String der) {

        if (ERROR.equals(izq)
                || ERROR.equals(der)
                || izq == null
                || der == null) {

            return ERROR;
        }

        if (izq.equals(der)) {
            return BOOL;
        }

        if (esNumerico(izq) && esNumerico(der)) {
            return BOOL;
        }

        return ERROR;
    }

    // ============================================================
    // LOGICOS
    // ============================================================

    public static String inferirLogico(
            String izq,
            String der) {

        if (ERROR.equals(izq)
                || ERROR.equals(der)
                || izq == null
                || der == null) {

            return ERROR;
        }

        if (BOOL.equals(izq) && BOOL.equals(der)) {
            return BOOL;
        }

        return ERROR;
    }

    // ============================================================
    // NOT
    // ============================================================

    public static String inferirNot(String operando) {

        if (ERROR.equals(operando)
                || operando == null) {

            return ERROR;
        }

        return BOOL.equals(operando)
                ? BOOL
                : ERROR;
    }

    // ============================================================
    // NEGATIVO UNARIO
    // ============================================================

    public static String inferirNegativo(String operando) {

        if (ERROR.equals(operando)
                || operando == null) {

            return ERROR;
        }

        return esNumerico(operando)
                ? operando
                : ERROR;
    }

    // ============================================================
    // AUXILIARES
    // ============================================================

    public static boolean esNumerico(String tipo) {

        return INT.equals(tipo)
                || DOUBLE.equals(tipo);
    }

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

    public static String msgIncompatible(
            String hacia,
            String desde) {

        return "no se puede asignar tipo '"
                + desde
                + "' a variable de tipo '"
                + hacia
                + "'";
    }

    public static String msgOperadorInvalido(
            String operador,
            String izq,
            String der) {

        return "el operador '"
                + operador
                + "' no puede aplicarse a tipos '"
                + izq
                + "' y '"
                + der
                + "'";
    }

    public static String msgUnarioInvalido(
            String operador,
            String tipo) {

        return "el operador '"
                + operador
                + "' no puede aplicarse al tipo '"
                + tipo
                + "'";
    }
}