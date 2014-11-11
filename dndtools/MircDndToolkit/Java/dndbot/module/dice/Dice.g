grammar Dice;

@header {
package dndbot.module.dice;
import java.util.ArrayList;
import java.util.List;
}

@lexer::header {
package dndbot.module.dice;
}

@members {
}

full_expression	returns [ExpressionPart result]
	:	expression {$result=$expression.result;} EOF;

// +, -, or ^ are at lowest level (for simplicity's sake...)
expression	returns [ExpressionPart result]
	:	e1=multiexpr				{ $result = $e1.result; }
		( op=('+'|'-'|'^') WS? e2=multiexpr 	{ $result = ExpressionBuilder.buildBinaryOp($result, $op.text, $e2.result);}	) *
	;
	
// *, /, or % are at 2nd level (for simplicity's sake...)
multiexpr	returns [ExpressionPart result]
	:	e1=diceexpr				{ $result = $e1.result; }
		( op=('*'|'/'|'%') WS? e2=diceexpr 	{ $result = ExpressionBuilder.buildBinaryOp($result, $op.text, $e2.result);}	) *
	;

// Dice expression at 2nd top level
diceexpr	returns [ExpressionPart result]
	:	sign=('+'|'-')?  dice n=note?
					{ $result = $dice.result;
					  if (n != null) $result = ExpressionBuilder.buildNote($result, $n.result);
					  if (sign != null) $result = ExpressionBuilder.buildUnaryOp($sign.text, $result);	
					}
	;

// Plain expression or multiple die, e.g. (2d6)d4
dice	returns [ExpressionPart result]
	:	( funcexpr d1=die?	{ $result = ($d1.result == null) ? $funcexpr.result : new ExpressionPartDice($funcexpr.result, $d1.result) ; }	)
	|	d2=die			{ $result = $d2.result; }
	;

// Single die type, with roll modifier
die	returns [ExpressionPartDice result]
	:	D baseexpr? opt=diceopt?	{ $result = new ExpressionPartDice(null, $baseexpr.result, opt==null?null:$diceopt.result); }
	;

// Dice option: k3, il2, etc., as in 4d6k3
diceopt	returns [ExpressionPartDice.DiceOpt result]
	:	a=word baseexpr?	{ $result = new ExpressionPartDice.DiceOpt($a.result, $baseexpr.result); }
	;
	
word	returns [String result]
	:				{ StringBuilder str = new StringBuilder(); }
		( n = ( ALPHA | D )	{ str.append($n.text); }	) +
					{ $result = str.toString(); }
	;

// Expression note, e.g. 'fire' in '1d6 fire'
note	returns [String result]
	:							{ StringBuilder str = new StringBuilder(); }
		WS ( n = ( OTHER | D | ALPHA | NUMBER | WS )	{ str.append($n.text); }	) *
								{ $result = str.toString().trim(); }
	;

// Function or base expression.  There are cases where function shouldn't be allowed to prevent confusion
funcexpr	returns [ExpressionPart result]
	:	baseexpr			{ $result = $baseexpr.result; }
	|	function			{ $result = $function.result; }
	;

// Smallest expression, either a number or an expression in bracket
baseexpr	returns [ExpressionPart result]
	:	NUMBER				{ $result = new ExpressionPartNumber($NUMBER.text); }
	|	( '(' WS? expression ')'	{ $result = new ExpressionPartGroup($expression.result); }		)
	;
	
function	returns [ExpressionPart result]
	:					{ List<ExpressionPart> params = new ArrayList<ExpressionPart>(); }
	fn=function_name '('
	       WS? p1=expression  		{ params.add($p1.result); }
	 ( ',' WS? p2=expression  		{ params.add($p2.result); }
	 				) * ')' 
						{ $result = new ExpressionPartFunction($fn.text, params); }
	;

// Fucntion name cannot start with D, otherwise may confuse with dice operations
function_name	returns [String result]
	:	  n1=( ALPHA | '_' )				{ StringBuilder str = new StringBuilder($n1.text); }
		( n2=( ALPHA | '_' | D )			{ str.append($n2.text); }	) *
								{ $result = str.toString().trim(); }
	;


// Unicode whitespace
WS	:	('\u0009'..'\u000D'|'\u0020'|'\u0085'|'\u00A0'|'\u1680'|'\u180E'|'\u2000'..'\u200A'|'\u2028'|'\u2029'|'\u202F'|'\u205F'|'\u3000') ;
// *FULL* Number
NUMBER	:	( DIGIT ( '.' DIGIT )? | '.' DIGIT ) ;
fragment
DIGIT	:	('0'..'9')+;

// d or D, for dice
D	:	'd'|'D';
// Any letter that is not D
ALPHA	:	'a'..'z'|'A'..'Z'; 
// Anything else that is not operator and thus may be used in notes.
OTHER	:	( ~('+'|'-'|'%'|'*'|'/'|'^'|'('|')'|','|'0'..'9'|'a'..'z'|'A'..'Z'|'\u0009'..'\u000D'|'\u0020'|'\u0085'|'\u00A0'|'\u1680'|'\u180E'|'\u2000'..'\u200A'|'\u2028'|'\u2029'|'\u202F'|'\u205F'|'\u3000') ) + ;
