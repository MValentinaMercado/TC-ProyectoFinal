grammar MiLenguaje;

// ============================================================
// PROGRAMA
// ============================================================

programa
    : declaracion* EOF
    ;

// ============================================================
// DECLARACIONES
// ============================================================

declaracion
    : declaracionVariable
    | declaracionFuncion
    ;

declaracionVariable
    : tipo ID (IGUAL expresion)? PYC
    | tipo ID CA NUM CC PYC
    ;

tipo
    : INT
    | DOUBLE
    | CHAR
    | STRING
    | BOOL
    | VOID
    ;

// ============================================================
// FUNCIONES
// ============================================================

declaracionFuncion
    : tipo ID PA parametros? PC bloque
    ;

parametros
    : parametro (COMA parametro)*
    ;

parametro
    : tipo ID
    | tipo ID CA CC
    ;

// ============================================================
// BLOQUES Y SENTENCIAS
// ============================================================

bloque
    : LA sentencia* LC
    ;

sentencia
    : declaracionVariable
    | asignacion
    | incremento PYC
    | llamadaFuncion PYC
    | sentenciaIf
    | sentenciaWhile
    | sentenciaFor
    | sentenciaBreak
    | sentenciaContinue
    | retorno
    | bloque
    ;

// ============================================================
// CONTROL DE FLUJO
// ============================================================

sentenciaIf
    : IF PA expresion PC bloque (ELSE bloque)?
    ;

sentenciaWhile
    : WHILE PA expresion PC bloque
    ;

sentenciaFor
    : FOR PA inicializacionFor? PYC expresion? PYC actualizacionFor? PC bloque
    ;

inicializacionFor
    : declaracionFor
    | asignacionFor
    ;

declaracionFor
    : tipo ID IGUAL expresion
    ;

asignacionFor
    : ID IGUAL expresion
    ;

actualizacionFor
    : ID INC
    | ID DEC
    | ID IGUAL expresion
    ;

sentenciaBreak
    : BREAK PYC
    ;

sentenciaContinue
    : CONTINUE PYC
    ;

retorno
    : RETURN expresion? PYC
    ;

// ============================================================
// ASIGNACIONES / LLAMADAS
// ============================================================

asignacion
    : ID IGUAL expresion PYC
    | ID CA expresion CC IGUAL expresion PYC
    ;

llamadaFuncion
    : ID PA argumentos? PC
    ;

argumentos
    : expresion (COMA expresion)*
    ;

incremento
    : ID INC
    | ID DEC
    ;

// ============================================================
// EXPRESIONES (IMPORTANTE: LIMPIAS Y SIN AMBIGÜEDAD)
// ============================================================

expresion
    : expresion OR expresion                    #ExprOr
    | expresion AND expresion                   #ExprAnd

    | expresion (EQL | DISTINTO) expresion      #ExprIgualdad
    | expresion (MAYOR | MENOR | MAYOR_IGUAL | MENOR_IGUAL) expresion #ExprRelacional

    | expresion (SUM | RES) expresion           #ExprAditiva
    | expresion (MUL | DIV | MOD) expresion     #ExprMultiplicativa

    | NOT expresion                             #ExprNot
    | RES expresion                             #ExprNegativo

    | PA expresion PC                           #ExprAgrupada

    | llamadaFuncion                            #ExprLlamada
    | ID CA expresion CC                        #ExprArray
    | ID                                        #ExprIdentificador

    | NUM                                       #ExprEntero
    | DECIMAL                                   #ExprDecimal
    | CHARACTER                                 #ExprCaracter
    | CADENA                                    #ExprCadena
    | TRUE                                      #ExprTrue
    | FALSE                                     #ExprFalse
    ;

// ============================================================
// LEXER
// ============================================================

fragment LETRA : [A-Za-z] ;
fragment DIGITO : [0-9] ;

PA : '(' ;
PC : ')' ;
CA : '[' ;
CC : ']' ;
LA : '{' ;
LC : '}' ;

PYC : ';' ;
COMA : ',' ;

IGUAL : '=' ;

EQL : '==' ;
DISTINTO : '!=' ;

MAYOR_IGUAL : '>=' ;
MENOR_IGUAL : '<=' ;

MAYOR : '>' ;
MENOR : '<' ;

INC : '++' ;
DEC : '--' ;

SUM : '+' ;
RES : '-' ;
MUL : '*' ;
DIV : '/' ;
MOD : '%' ;

OR : '||' ;
AND : '&&' ;
NOT : '!' ;

IF : 'if' ;
ELSE : 'else' ;

WHILE : 'while' ;
FOR : 'for' ;

BREAK : 'break' ;
CONTINUE : 'continue' ;

RETURN : 'return' ;

INT : 'int' ;
DOUBLE : 'double' ;
CHAR : 'char' ;
STRING : 'string' ;
BOOL : 'bool' ;
VOID : 'void' ;

TRUE : 'true' ;
FALSE : 'false' ;

ID
    : (LETRA | '_') (LETRA | DIGITO | '_')*
    ;

DECIMAL
    : DIGITO+ '.' DIGITO+
    ;

NUM
    : DIGITO+
    ;

CHARACTER
    : '\'' (~['\r\n] | '\\' .) '\''
    ;

CADENA
    : '"' (~["\\\r\n] | '\\' .)* '"'
    ;

COMENTARIO_LINEA
    : '//' ~[\r\n]* -> skip
    ;

COMENTARIO_BLOQUE
    : '/*' .*? '*/' -> skip
    ;

WS
    : [ \t\r\n]+ -> skip
    ;

OTRO
    : .
    ;