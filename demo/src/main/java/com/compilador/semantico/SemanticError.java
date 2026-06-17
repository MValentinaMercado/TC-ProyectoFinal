package com.compilador.semantico;

/**
 * ============================================================
 * SEMANTIC ERROR
 * ============================================================
 *
 * Esta clase representa un diagnóstico producido durante el
 * análisis semántico. Puede ser:
 *
 *   ERROR        - el programa es incorrecto
 *   ADVERTENCIA  - el programa es válido pero sospechoso
 *
 * El SemanticAnalyzer mantiene dos listas separadas:
 *   - errores
 *   - warnings
 *
 * Pero ambos usan esta misma clase, diferenciados por la
 * enumeración Severidad.
 *
 * Ejemplos:
 *   Error: variable 'x' no declarada (linea 4, columna 10)
 *   Advertencia: variable 'y' declarada pero nunca usada (linea 7)
 *
 * Esta clase NO detiene la compilación: solo almacena el mensaje.
 * La decisión de abortar o continuar la toma App.java.
 */

public class SemanticError {

    /**
     * Severidad del diagnóstico:
     *
     * ERROR:
     *   - Implica que el programa viola las reglas del lenguaje.
     *   - El compilador debe reportarlo pero NO abortar inmediatamente,
     *     para poder mostrar todos los errores en una sola pasada.
     *
     * ADVERTENCIA:
     *   - No impide compilar.
     *   - Indica un posible bug (variable no usada, no inicializada, etc.).
     */

    public enum Severidad { ERROR, ADVERTENCIA }

    /** Línea donde ocurrió el problema */

    private final int linea;

    /** Columna donde ocurrió el problema */

    private final int columna;

    /** Mensaje descriptivo del error o advertencia */

    private final String mensaje;

    /** Severidad del diagnóstico */

    private final Severidad severidad;

    /**
     * Constructor.
     *
     * @param linea     línea del código fuente
     * @param columna   columna del código fuente
     * @param mensaje   descripción del problema
     * @param severidad ERROR o ADVERTENCIA
     */

    public SemanticError(int linea, int columna, String mensaje, Severidad severidad) {
        this.linea     = linea;
        this.columna   = columna;
        this.mensaje   = mensaje;
        this.severidad = severidad;
    }

    public int       getLinea()     { return linea;     }
    public int       getColumna()   { return columna;   }
    public String    getMensaje()   { return mensaje;   }
    public Severidad getSeveridad() { return severidad; }

    /**
     * Formato estándar del mensaje:
     *
     *   Error: mensaje (linea X, columna Y)
     *   Advertencia: mensaje (linea X, columna Y)
     */

    @Override
    public String toString() {
        String tipo = severidad == Severidad.ERROR ? "Error" : "Advertencia";

        return tipo + ": " + mensaje +
                " (linea " + linea + ", columna " + columna + ")";
    }
}
