package com.compilador.semantico;

/**
 * Mensaje diagnostico producido durante el analisis semantico.
 *
 * SemanticAnalyzer mantiene dos listas separadas: una para ERRORs y otra
 * para ADVERTENCIAs.  Usar un unico tipo con un campo de severidad permite
 * que las dos listas compartan el mismo tipo y que toString() elija
 * automaticamente la etiqueta correcta al imprimir.
 *
 * Salida de toString():
 *   [Linea 13:4] Error semantico: no se puede asignar tipo 'string' …
 *   [Linea  7:8] Advertencia semantica: variable 'x' usada sin inicializar
 */
public class SemanticError {

    /**
     * ERROR      — problema que impide que el programa sea correcto.
     *              El compilador reporta el error y sigue analizando (no aborta)
     *              para poder mostrar todos los errores de una sola pasada.
     * ADVERTENCIA — condicion sospechosa pero no invalida (variable sin inicializar).
     *              El programa puede compilar y ejecutar, pero podria tener bugs.
     */
    public enum Severidad { ERROR, ADVERTENCIA }

    private final int       linea;
    private final int       columna;
    private final String    mensaje;
    private final Severidad severidad;

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

    @Override
    public String toString() {
        String tipo = severidad == Severidad.ERROR ? "Error" : "Advertencia";

        return tipo + ": " + mensaje +
                " (linea " + linea + ", columna " + columna + ")";
    }
}
