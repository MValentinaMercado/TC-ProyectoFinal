grammar MiLenguaje;

// Regla parser mínima (requerida por ANTLR)

//mvn clean package
//java -jar target/demo-1.0-jar-with-dependencies.jar ejemplo.txt

programa : (token)* EOF ;

expr
    : expr OR term          # OrExpr
    | term                  # JustTerm
    ;

term
    : term AND factor       # AndTerm
    | factor                # JustFactor
    ;

factor
    : NOT factor            # NotFactor
    | PA expr PC            # Parentheses
    | NUM                   # Number
    | TRUE                  # TrueLit
    | FALSE                 # FalseLit
    ;

token : PA | PC | CA | CC | LA | LC | TRUE | FALSE | PYC | COMA | IGUAL | MAYOR | MAYOR_IGUAL
      | MENOR | MENOR_IGUAL | EQL | DISTINTO | SUM | RES | MUL | DIV | MOD
      | OR | AND | NOT | FOR | WHILE | IF | ELSE | INT | CHAR | DOUBLE | VOID
      | RETURN | ID | NUM | INTEGER | DECIMAL | CHARACTER | OTRO
      ;

fragment LETRA : [A-Za-z];
fragment DIGITO : [0-9];

// TOKENS


PA   : '(' ;
PC   : ')' ;
CA   : '[' ;
CC   : ']' ;
LA   : '{' ;
LC   : '}' ;

TRUE  : 'true' ;
FALSE : 'false' ;

PYC  : ';' ;
COMA : ',' ;

IGUAL : '=' ;

MAYOR  : '>' ;
MAYOR_IGUAL: '>=';
MENOR  : '<' ;
MENOR_IGUAL: '<=';
EQL  : '==';
DISTINTO  : '!=';

SUM  : '+' ;
RES  : '-' ;
MUL  : '*' ;
DIV  : '/' ;
MOD  : '%' ;

OR    : 'or' ;
AND   : 'and' ;
NOT   : 'not' ;

FOR  : 'for';
WHILE: 'while';

IF   : 'if' ;
ELSE : 'else' ;

INT     : 'int' ;
CHAR    : 'char' ;
DOUBLE  : 'double' ;
VOID    : 'void' ;

RETURN : 'return';

ID      : (LETRA | '_') (LETRA | DIGITO | '_')* ;
NUM     : DIGITO+ ;

INTEGER : DIGITO+ ;
DECIMAL : INTEGER '.' INTEGER ;
CHARACTER : '\'' (~['\r\n] | '\\' .) '\'' ;


// Comentarios - Se ignoran durante el análisis
//COMENTARIO_LINEA : '//' ~[\r\n]*; sin ocultar
COMENTARIO_LINEA : '//' ~[\r\n]* -> skip;
COMENTARIO_BLOQUE : '/*' .*? '*/' -> skip;

WS : [ \r\n\t] -> skip ;
OTRO : . ;