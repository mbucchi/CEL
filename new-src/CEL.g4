grammar CEL;

parse
 : ( cel_stmt_list | error )* EOF
 ;

error
 : UNEXPECTED_CHAR 
   { 
     throw new RuntimeException("UNEXPECTED_CHAR=" + $UNEXPECTED_CHAR.text); 
   }
 ;

cel_stmt_list
 : cel_stmt ( ';' cel_stmt )*
 ;

cel_stmt
 : K_SELECT select_strategy? result_values
   ( K_FROM stream_name ( ',' stream_name )* )?
   K_WHERE cel_pattern
   ( K_PARTITION K_BY partition_list )?
   ( K_WITHIN time_window )?
   ( K_CONSUME K_BY consumption_policy )?
 ;

select_strategy
 : K_ALL
 | K_ANY
 | K_MAX
 | K_NEXT
 | K_STRICT
 ;

result_values
 : '*'
 | event_name ( ',' event_name )*
 ;


cel_pattern
 : '(' cel_pattern ')'
 | event_name
 | cel_pattern K_AS any_name
 | cel_pattern '+'
 | cel_pattern ( K_OR | ';' ) cel_pattern
 | cel_pattern K_FILTER bool_expr
 ;

partition_list
 : '[' attribute_list ']' (',' partition_list)?
 ;

attribute_list
 :  IDENTIFIER ( ',' IDENTIFIER )* 
 ;

consumption_policy
 : K_ANY
 | K_PARTITION
 | K_NONE
 ;

bool_expr
 : K_NOT bool_expr                                             # not_expr
 | math_expr ( '<' | '<=' | '>' | '>=' ) math_expr             # inequality_expr
 | math_expr ( '=' | '==' | '!=' | '<>' ) math_expr            # equality_expr
 | bool_expr K_AND bool_expr                                   # and_expr
 | bool_expr K_OR bool_expr                                    # or_expr
 | event_attribute K_LIKE REGEXP                               # regex_expr
 | event_attribute ( K_IN | K_NOT K_IN ) value_list            # containment_expr
 ;

math_expr
 : literal_value
 | event_attribute
 | '(' math_expr ')' 
 | ('+' | '-') math_expr
 | math_expr ( '*' | '/' | '%' ) math_expr
 | math_expr ( '+' | '-' ) math_expr
 | math_expr ( '<' | '<=' | '>' | '>=' ) math_expr
 | math_expr ( '=' | '==' | '!=' | '<>' ) math_expr
 ;

event_attribute
 : event_name '.' attribute_name
 ;

literal_value
 : NUMERIC_LITERAL
 | STRING_LITERAL
 ;

value_list
 : number_list
 | string_list
 ;

number_list
 : '[' NUMERIC_LITERAL (',' NUMERIC_LITERAL)* ']'   # numer_list
 | '[' NUMERIC_LITERAL '..' NUMERIC_LITERAL ']'     # number_range
 | '[' NUMERIC_LITERAL '.. '']'                     # number_range_lower
 | '[' '..' NUMERIC_LITERAL ']'                     # number_range_upper
 ;

string_list
 : '[' STRING_LITERAL (',' STRING_LITERAL)* ']'
 ;

time_window
 : event_span
 | time_span
 ;

event_span
 : INTEGER_LITERAL K_EVENTS
 ;

time_span
 : ( INTEGER_LITERAL K_HOURS )? ( INTEGER_LITERAL K_MINUTES )? ( INTEGER_LITERAL K_SECONDS )?
 ;

named_event
 : event_name ( K_AS any_name )?
 ;

event_name
 : ( stream_name '>' ) ? any_name
 ;

stream_name
 : any_name
 ;

attribute_name
 : any_name
 ;

any_name
 : IDENTIFIER
 ;

keyword
 : K_ALL
 | K_AND
 | K_ANY
 | K_AS
 | K_BY
 | K_CONSUME
 | K_DISTINCT
 | K_EVENTS
 | K_FILTER
 | K_FROM
 | K_HOURS
 | K_IN
 | K_LAST
 | K_LIKE
 | K_MAX
 | K_MINUTES
 | K_NEXT
 | K_NONE
 | K_NOT
 | K_OR
 | K_PARTITION
 | K_SECONDS
 | K_SELECT
 | K_STRICT
 | K_UNLESS
 | K_WHERE
 | K_WITHIN
 ;


K_ALL       : A L L;
K_AND       : A N D;
K_ANY       : A N Y;
K_AS        : A S;
K_BY        : B Y;
K_CONSUME   : C O N S U M E;
K_DISTINCT  : D I S T I N C T;
K_EVENTS    : E V E N T S;
K_FILTER    : F I L T E R;
K_FROM      : F R O M;
K_HOURS     : H O U R S?;
K_IN        : I N;
K_LAST      : L A S T;
K_LIKE      : L I K E;
K_MAX       : M A X;
K_MINUTES   : M I N U T E S?;
K_NEXT      : N E X T;
K_NONE      : N O N E;
K_NOT       : N O T;
K_OR        : O R;
K_PARTITION : P A R T I T I O N;
K_SECONDS   : S E C O N D S?;
K_SELECT    : S E L E C T;
K_STRICT    : S T R I C T;
K_UNLESS    : U N L E S S;
K_WHERE     : W H E R E;
K_WITHIN    : W I T H I N;

IDENTIFIER
 :  '`' (~'`' | '``')* '`'
 | [a-zA-Z_] [a-zA-Z_0-9]* 
 ;

NUMERIC_LITERAL
 : INTEGER_LITERAL
 | INTEGER_LITERAL '.' NUMERICAL_EXPONENT
 | INTEGER_LITERAL? '.' DIGIT+ 
 | INTEGER_LITERAL? '.' DIGIT+ NUMERICAL_EXPONENT
 ;

INTEGER_LITERAL
 : DIGIT+
 ;

NUMERICAL_EXPONENT
 : E '-'? DIGIT+ 
 ;

REGEXP
 : STRING_LITERAL
 ;

STRING_LITERAL
 : '\'' ( ~'\'' | '\'\'' )* '\''
 ;

SINGLE_LINE_COMMENT
 : '--' ~[\r\n]* -> channel(HIDDEN)
 ;

MULTILINE_COMMENT
 : '/*' .*? ( '*/' | EOF ) -> channel(HIDDEN)
 ;

SPACES
 : [ \u000B\t\r\n] -> channel(HIDDEN)
 ;

UNEXPECTED_CHAR
 : .
 ;

fragment DIGIT : [0-9];

fragment A : [aA];
fragment B : [bB];
fragment C : [cC];
fragment D : [dD];
fragment E : [eE];
fragment F : [fF];
fragment G : [gG];
fragment H : [hH];
fragment I : [iI];
fragment J : [jJ];
fragment K : [kK];
fragment L : [lL];
fragment M : [mM];
fragment N : [nN];
fragment O : [oO];
fragment P : [pP];
fragment Q : [qQ];
fragment R : [rR];
fragment S : [sS];
fragment T : [tT];
fragment U : [uU];
fragment V : [vV];
fragment W : [wW];
fragment X : [xX];
fragment Y : [yY];
fragment Z : [zZ];