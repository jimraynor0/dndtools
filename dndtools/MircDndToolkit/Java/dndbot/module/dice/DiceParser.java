// $ANTLR 3.3 Nov 30, 2010 12:45:30 D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g 2011-05-15 20:40:15

package dndbot.module.dice;
import java.util.ArrayList;
import java.util.List;


import org.antlr.runtime.*;

public class DiceParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "WS", "D", "ALPHA", "OTHER", "NUMBER", "DIGIT", "'+'", "'-'", "'^'", "'*'", "'/'", "'%'", "'('", "')'", "','", "'_'"
    };
    public static final int EOF=-1;
    public static final int T__10=10;
    public static final int T__11=11;
    public static final int T__12=12;
    public static final int T__13=13;
    public static final int T__14=14;
    public static final int T__15=15;
    public static final int T__16=16;
    public static final int T__17=17;
    public static final int T__18=18;
    public static final int T__19=19;
    public static final int WS=4;
    public static final int D=5;
    public static final int ALPHA=6;
    public static final int OTHER=7;
    public static final int NUMBER=8;
    public static final int DIGIT=9;

    // delegates
    // delegators


        public DiceParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public DiceParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return DiceParser.tokenNames; }
    public String getGrammarFileName() { return "D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g"; }





    // $ANTLR start "full_expression"
    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:16:1: full_expression returns [ExpressionPart filteredResult] : expression EOF ;
    public final ExpressionPart full_expression() throws RecognitionException {
        ExpressionPart result = null;

        ExpressionPart expression1 = null;


        try {
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:17:2: ( expression EOF )
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:17:4: expression EOF
            {
            pushFollow(FOLLOW_expression_in_full_expression36);
            expression1=expression();

            state._fsp--;

            result =expression1;
            match(input,EOF,FOLLOW_EOF_in_full_expression40); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "full_expression"


    // $ANTLR start "expression"
    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:20:1: expression returns [ExpressionPart filteredResult] : e1= multiexpr (op= ( '+' | '-' | '^' ) ( WS )? e2= multiexpr )* ;
    public final ExpressionPart expression() throws RecognitionException {
        ExpressionPart result = null;

        Token op=null;
        ExpressionPart e1 = null;

        ExpressionPart e2 = null;


        try {
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:21:2: (e1= multiexpr (op= ( '+' | '-' | '^' ) ( WS )? e2= multiexpr )* )
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:21:4: e1= multiexpr (op= ( '+' | '-' | '^' ) ( WS )? e2= multiexpr )*
            {
            pushFollow(FOLLOW_multiexpr_in_expression56);
            e1=multiexpr();

            state._fsp--;

             result = e1; 
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:22:3: (op= ( '+' | '-' | '^' ) ( WS )? e2= multiexpr )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>=10 && LA2_0<=12)) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:22:5: op= ( '+' | '-' | '^' ) ( WS )? e2= multiexpr
            	    {
            	    op=(Token)input.LT(1);
            	    if ( (input.LA(1)>=10 && input.LA(1)<=12) ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:22:22: ( WS )?
            	    int alt1=2;
            	    int LA1_0 = input.LA(1);

            	    if ( (LA1_0==WS) ) {
            	        alt1=1;
            	    }
            	    switch (alt1) {
            	        case 1 :
            	            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:22:22: WS
            	            {
            	            match(input,WS,FOLLOW_WS_in_expression77); 

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_multiexpr_in_expression82);
            	    e2=multiexpr();

            	    state._fsp--;

            	     result = ExpressionBuilder.buildBinaryOp(result, (op!=null?op.getText():null), e2);

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "expression"


    // $ANTLR start "multiexpr"
    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:26:1: multiexpr returns [ExpressionPart filteredResult] : e1= diceexpr (op= ( '*' | '/' | '%' ) ( WS )? e2= diceexpr )* ;
    public final ExpressionPart multiexpr() throws RecognitionException {
        ExpressionPart result = null;

        Token op=null;
        ExpressionPart e1 = null;

        ExpressionPart e2 = null;


        try {
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:27:2: (e1= diceexpr (op= ( '*' | '/' | '%' ) ( WS )? e2= diceexpr )* )
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:27:4: e1= diceexpr (op= ( '*' | '/' | '%' ) ( WS )? e2= diceexpr )*
            {
            pushFollow(FOLLOW_diceexpr_in_multiexpr108);
            e1=diceexpr();

            state._fsp--;

             result = e1; 
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:28:3: (op= ( '*' | '/' | '%' ) ( WS )? e2= diceexpr )*
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>=13 && LA4_0<=15)) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:28:5: op= ( '*' | '/' | '%' ) ( WS )? e2= diceexpr
            	    {
            	    op=(Token)input.LT(1);
            	    if ( (input.LA(1)>=13 && input.LA(1)<=15) ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:28:22: ( WS )?
            	    int alt3=2;
            	    int LA3_0 = input.LA(1);

            	    if ( (LA3_0==WS) ) {
            	        alt3=1;
            	    }
            	    switch (alt3) {
            	        case 1 :
            	            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:28:22: WS
            	            {
            	            match(input,WS,FOLLOW_WS_in_multiexpr129); 

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_diceexpr_in_multiexpr134);
            	    e2=diceexpr();

            	    state._fsp--;

            	     result = ExpressionBuilder.buildBinaryOp(result, (op!=null?op.getText():null), e2);

            	    }
            	    break;

            	default :
            	    break loop4;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "multiexpr"


    // $ANTLR start "diceexpr"
    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:32:1: diceexpr returns [ExpressionPart filteredResult] : (sign= ( '+' | '-' ) )? dice (n= note )? ;
    public final ExpressionPart diceexpr() throws RecognitionException {
        ExpressionPart result = null;

        Token sign=null;
        String n = null;

        ExpressionPart dice2 = null;


        try {
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:33:2: ( (sign= ( '+' | '-' ) )? dice (n= note )? )
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:33:4: (sign= ( '+' | '-' ) )? dice (n= note )?
            {
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:33:8: (sign= ( '+' | '-' ) )?
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( ((LA5_0>=10 && LA5_0<=11)) ) {
                alt5=1;
            }
            switch (alt5) {
                case 1 :
                    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:33:8: sign= ( '+' | '-' )
                    {
                    sign=(Token)input.LT(1);
                    if ( (input.LA(1)>=10 && input.LA(1)<=11) ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }


                    }
                    break;

            }

            pushFollow(FOLLOW_dice_in_diceexpr167);
            dice2=dice();

            state._fsp--;

            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:33:27: (n= note )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==WS) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:33:27: n= note
                    {
                    pushFollow(FOLLOW_note_in_diceexpr171);
                    n=note();

                    state._fsp--;


                    }
                    break;

            }

             result = dice2;
            					  if (n != null) result = ExpressionBuilder.buildNote(result, n);
            					  if (sign != null) result = ExpressionBuilder.buildUnaryOp((sign!=null?sign.getText():null), result);	
            					

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "diceexpr"


    // $ANTLR start "dice"
    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:41:1: dice returns [ExpressionPart filteredResult] : ( ( funcexpr (d1= die )? ) | d2= die );
    public final ExpressionPart dice() throws RecognitionException {
        ExpressionPart result = null;

        ExpressionPartDice d1 = null;

        ExpressionPartDice d2 = null;

        ExpressionPart funcexpr3 = null;


        try {
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:42:2: ( ( funcexpr (d1= die )? ) | d2= die )
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==ALPHA||LA8_0==NUMBER||LA8_0==16||LA8_0==19) ) {
                alt8=1;
            }
            else if ( (LA8_0==D) ) {
                alt8=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 0, input);

                throw nvae;
            }
            switch (alt8) {
                case 1 :
                    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:42:4: ( funcexpr (d1= die )? )
                    {
                    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:42:4: ( funcexpr (d1= die )? )
                    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:42:6: funcexpr (d1= die )?
                    {
                    pushFollow(FOLLOW_funcexpr_in_dice197);
                    funcexpr3=funcexpr();

                    state._fsp--;

                    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:42:17: (d1= die )?
                    int alt7=2;
                    int LA7_0 = input.LA(1);

                    if ( (LA7_0==D) ) {
                        alt7=1;
                    }
                    switch (alt7) {
                        case 1 :
                            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:42:17: d1= die
                            {
                            pushFollow(FOLLOW_die_in_dice201);
                            d1=die();

                            state._fsp--;


                            }
                            break;

                    }

                     result = (d1 == null) ? funcexpr3 : new ExpressionPartDice(funcexpr3, d1) ; 

                    }


                    }
                    break;
                case 2 :
                    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:43:4: d2= die
                    {
                    pushFollow(FOLLOW_die_in_dice213);
                    d2=die();

                    state._fsp--;

                     result = d2; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "dice"


    // $ANTLR start "die"
    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:47:1: die returns [ExpressionPartDice filteredResult] : D ( baseexpr )? (opt= diceopt )? ;
    public final ExpressionPartDice die() throws RecognitionException {
        ExpressionPartDice result = null;

        ExpressionPartDice.DiceOpt opt = null;

        ExpressionPart baseexpr4 = null;


        try {
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:48:2: ( D ( baseexpr )? (opt= diceopt )? )
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:48:4: D ( baseexpr )? (opt= diceopt )?
            {
            match(input,D,FOLLOW_D_in_die233); 
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:48:6: ( baseexpr )?
            int alt9=2;
            int LA9_0 = input.LA(1);

            if ( (LA9_0==NUMBER||LA9_0==16) ) {
                alt9=1;
            }
            switch (alt9) {
                case 1 :
                    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:48:6: baseexpr
                    {
                    pushFollow(FOLLOW_baseexpr_in_die235);
                    baseexpr4=baseexpr();

                    state._fsp--;


                    }
                    break;

            }

            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:48:19: (opt= diceopt )?
            int alt10=2;
            int LA10_0 = input.LA(1);

            if ( ((LA10_0>=D && LA10_0<=ALPHA)) ) {
                alt10=1;
            }
            switch (alt10) {
                case 1 :
                    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:48:19: opt= diceopt
                    {
                    pushFollow(FOLLOW_diceopt_in_die240);
                    opt=diceopt();

                    state._fsp--;


                    }
                    break;

            }

             result = new ExpressionPartDice(null, baseexpr4, opt==null?null:opt); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "die"


    // $ANTLR start "diceopt"
    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:52:1: diceopt returns [ExpressionPartDice.DiceOpt filteredResult] : a= word ( baseexpr )? ;
    public final ExpressionPartDice.DiceOpt diceopt() throws RecognitionException {
        ExpressionPartDice.DiceOpt result = null;

        String a = null;

        ExpressionPart baseexpr5 = null;


        try {
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:53:2: (a= word ( baseexpr )? )
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:53:4: a= word ( baseexpr )?
            {
            pushFollow(FOLLOW_word_in_diceopt261);
            a=word();

            state._fsp--;

            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:53:11: ( baseexpr )?
            int alt11=2;
            int LA11_0 = input.LA(1);

            if ( (LA11_0==NUMBER||LA11_0==16) ) {
                alt11=1;
            }
            switch (alt11) {
                case 1 :
                    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:53:11: baseexpr
                    {
                    pushFollow(FOLLOW_baseexpr_in_diceopt263);
                    baseexpr5=baseexpr();

                    state._fsp--;


                    }
                    break;

            }

             result = new ExpressionPartDice.DiceOpt(a, baseexpr5); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "diceopt"


    // $ANTLR start "word"
    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:56:1: word returns [String filteredResult] : (n= ( ALPHA | D ) )+ ;
    public final String word() throws RecognitionException {
        String result = null;

        Token n=null;

        try {
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:57:2: ( (n= ( ALPHA | D ) )+ )
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:57:7: (n= ( ALPHA | D ) )+
            {
             StringBuilder str = new StringBuilder(); 
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:58:3: (n= ( ALPHA | D ) )+
            int cnt12=0;
            loop12:
            do {
                int alt12=2;
                int LA12_0 = input.LA(1);

                if ( ((LA12_0>=D && LA12_0<=ALPHA)) ) {
                    alt12=1;
                }


                switch (alt12) {
            	case 1 :
            	    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:58:5: n= ( ALPHA | D )
            	    {
            	    n=(Token)input.LT(1);
            	    if ( (input.LA(1)>=D && input.LA(1)<=ALPHA) ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	     str.append((n!=null?n.getText():null)); 

            	    }
            	    break;

            	default :
            	    if ( cnt12 >= 1 ) break loop12;
                        EarlyExitException eee =
                            new EarlyExitException(12, input);
                        throw eee;
                }
                cnt12++;
            } while (true);

             result = str.toString(); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "word"


    // $ANTLR start "note"
    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:63:1: note returns [String filteredResult] : WS (n= ( OTHER | D | ALPHA | NUMBER | WS ) )* ;
    public final String note() throws RecognitionException {
        String result = null;

        Token n=null;

        try {
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:64:2: ( WS (n= ( OTHER | D | ALPHA | NUMBER | WS ) )* )
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:64:10: WS (n= ( OTHER | D | ALPHA | NUMBER | WS ) )*
            {
             StringBuilder str = new StringBuilder(); 
            match(input,WS,FOLLOW_WS_in_note342); 
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:65:6: (n= ( OTHER | D | ALPHA | NUMBER | WS ) )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( ((LA13_0>=WS && LA13_0<=NUMBER)) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:65:8: n= ( OTHER | D | ALPHA | NUMBER | WS )
            	    {
            	    n=(Token)input.LT(1);
            	    if ( (input.LA(1)>=WS && input.LA(1)<=NUMBER) ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	     str.append((n!=null?n.getText():null)); 

            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);

             result = str.toString().trim(); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "note"


    // $ANTLR start "funcexpr"
    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:70:1: funcexpr returns [ExpressionPart filteredResult] : ( baseexpr | function );
    public final ExpressionPart funcexpr() throws RecognitionException {
        ExpressionPart result = null;

        ExpressionPart baseexpr6 = null;

        ExpressionPart function7 = null;


        try {
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:71:2: ( baseexpr | function )
            int alt14=2;
            int LA14_0 = input.LA(1);

            if ( (LA14_0==NUMBER||LA14_0==16) ) {
                alt14=1;
            }
            else if ( (LA14_0==ALPHA||LA14_0==19) ) {
                alt14=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 14, 0, input);

                throw nvae;
            }
            switch (alt14) {
                case 1 :
                    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:71:4: baseexpr
                    {
                    pushFollow(FOLLOW_baseexpr_in_funcexpr402);
                    baseexpr6=baseexpr();

                    state._fsp--;

                     result = baseexpr6; 

                    }
                    break;
                case 2 :
                    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:72:4: function
                    {
                    pushFollow(FOLLOW_function_in_funcexpr411);
                    function7=function();

                    state._fsp--;

                     result = function7; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "funcexpr"


    // $ANTLR start "baseexpr"
    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:76:1: baseexpr returns [ExpressionPart filteredResult] : ( NUMBER | ( '(' ( WS )? expression ')' ) );
    public final ExpressionPart baseexpr() throws RecognitionException {
        ExpressionPart result = null;

        Token NUMBER8=null;
        ExpressionPart expression9 = null;


        try {
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:77:2: ( NUMBER | ( '(' ( WS )? expression ')' ) )
            int alt16=2;
            int LA16_0 = input.LA(1);

            if ( (LA16_0==NUMBER) ) {
                alt16=1;
            }
            else if ( (LA16_0==16) ) {
                alt16=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 16, 0, input);

                throw nvae;
            }
            switch (alt16) {
                case 1 :
                    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:77:4: NUMBER
                    {
                    NUMBER8=(Token)match(input,NUMBER,FOLLOW_NUMBER_in_baseexpr431); 
                     result = new ExpressionPartNumber((NUMBER8!=null?NUMBER8.getText():null)); 

                    }
                    break;
                case 2 :
                    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:78:4: ( '(' ( WS )? expression ')' )
                    {
                    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:78:4: ( '(' ( WS )? expression ')' )
                    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:78:6: '(' ( WS )? expression ')'
                    {
                    match(input,16,FOLLOW_16_in_baseexpr443); 
                    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:78:10: ( WS )?
                    int alt15=2;
                    int LA15_0 = input.LA(1);

                    if ( (LA15_0==WS) ) {
                        alt15=1;
                    }
                    switch (alt15) {
                        case 1 :
                            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:78:10: WS
                            {
                            match(input,WS,FOLLOW_WS_in_baseexpr445); 

                            }
                            break;

                    }

                    pushFollow(FOLLOW_expression_in_baseexpr448);
                    expression9=expression();

                    state._fsp--;

                    match(input,17,FOLLOW_17_in_baseexpr450); 
                     result = new ExpressionPartGroup(expression9); 

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "baseexpr"


    // $ANTLR start "function"
    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:81:1: function returns [ExpressionPart filteredResult] : fn= function_name '(' ( WS )? p1= expression ( ',' ( WS )? p2= expression )* ')' ;
    public final ExpressionPart function() throws RecognitionException {
        ExpressionPart result = null;

        DiceParser.function_name_return fn = null;

        ExpressionPart p1 = null;

        ExpressionPart p2 = null;


        try {
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:82:2: (fn= function_name '(' ( WS )? p1= expression ( ',' ( WS )? p2= expression )* ')' )
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:82:8: fn= function_name '(' ( WS )? p1= expression ( ',' ( WS )? p2= expression )* ')'
            {
             List<ExpressionPart> params = new ArrayList<ExpressionPart>(); 
            pushFollow(FOLLOW_function_name_in_function480);
            fn=function_name();

            state._fsp--;

            match(input,16,FOLLOW_16_in_function482); 
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:84:9: ( WS )?
            int alt17=2;
            int LA17_0 = input.LA(1);

            if ( (LA17_0==WS) ) {
                alt17=1;
            }
            switch (alt17) {
                case 1 :
                    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:84:9: WS
                    {
                    match(input,WS,FOLLOW_WS_in_function492); 

                    }
                    break;

            }

            pushFollow(FOLLOW_expression_in_function497);
            p1=expression();

            state._fsp--;

             params.add(p1); 
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:85:3: ( ',' ( WS )? p2= expression )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( (LA19_0==18) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:85:5: ',' ( WS )? p2= expression
            	    {
            	    match(input,18,FOLLOW_18_in_function508); 
            	    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:85:9: ( WS )?
            	    int alt18=2;
            	    int LA18_0 = input.LA(1);

            	    if ( (LA18_0==WS) ) {
            	        alt18=1;
            	    }
            	    switch (alt18) {
            	        case 1 :
            	            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:85:9: WS
            	            {
            	            match(input,WS,FOLLOW_WS_in_function510); 

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_expression_in_function515);
            	    p2=expression();

            	    state._fsp--;

            	     params.add(p2); 

            	    }
            	    break;

            	default :
            	    break loop19;
                }
            } while (true);

            match(input,17,FOLLOW_17_in_function532); 
             result = new ExpressionPartFunction((fn!=null?input.toString(fn.start,fn.stop):null), params); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return result;
    }
    // $ANTLR end "function"

    public static class function_name_return extends ParserRuleReturnScope {
        public String result;
    };

    // $ANTLR start "function_name"
    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:91:1: function_name returns [String filteredResult] : n1= ( ALPHA | '_' ) (n2= ( ALPHA | '_' | D ) )* ;
    public final DiceParser.function_name_return function_name() throws RecognitionException {
        DiceParser.function_name_return retval = new DiceParser.function_name_return();
        retval.start = input.LT(1);

        Token n1=null;
        Token n2=null;

        try {
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:92:2: (n1= ( ALPHA | '_' ) (n2= ( ALPHA | '_' | D ) )* )
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:92:6: n1= ( ALPHA | '_' ) (n2= ( ALPHA | '_' | D ) )*
            {
            n1=(Token)input.LT(1);
            if ( input.LA(1)==ALPHA||input.LA(1)==19 ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }

             StringBuilder str = new StringBuilder((n1!=null?n1.getText():null)); 
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:93:3: (n2= ( ALPHA | '_' | D ) )*
            loop20:
            do {
                int alt20=2;
                int LA20_0 = input.LA(1);

                if ( ((LA20_0>=D && LA20_0<=ALPHA)||LA20_0==19) ) {
                    alt20=1;
                }


                switch (alt20) {
            	case 1 :
            	    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:93:5: n2= ( ALPHA | '_' | D )
            	    {
            	    n2=(Token)input.LT(1);
            	    if ( (input.LA(1)>=D && input.LA(1)<=ALPHA)||input.LA(1)==19 ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	     str.append((n2!=null?n2.getText():null)); 

            	    }
            	    break;

            	default :
            	    break loop20;
                }
            } while (true);

             retval.result = str.toString().trim(); 

            }

            retval.stop = input.LT(-1);

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return retval;
    }
    // $ANTLR end "function_name"

    // Delegated rules


 

    public static final BitSet FOLLOW_expression_in_full_expression36 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_full_expression40 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_multiexpr_in_expression56 = new BitSet(new long[]{0x0000000000001C02L});
    public static final BitSet FOLLOW_set_in_expression69 = new BitSet(new long[]{0x0000000000090D70L});
    public static final BitSet FOLLOW_WS_in_expression77 = new BitSet(new long[]{0x0000000000090D70L});
    public static final BitSet FOLLOW_multiexpr_in_expression82 = new BitSet(new long[]{0x0000000000001C02L});
    public static final BitSet FOLLOW_diceexpr_in_multiexpr108 = new BitSet(new long[]{0x000000000000E002L});
    public static final BitSet FOLLOW_set_in_multiexpr121 = new BitSet(new long[]{0x0000000000090D70L});
    public static final BitSet FOLLOW_WS_in_multiexpr129 = new BitSet(new long[]{0x0000000000090D70L});
    public static final BitSet FOLLOW_diceexpr_in_multiexpr134 = new BitSet(new long[]{0x000000000000E002L});
    public static final BitSet FOLLOW_set_in_diceexpr159 = new BitSet(new long[]{0x0000000000090D70L});
    public static final BitSet FOLLOW_dice_in_diceexpr167 = new BitSet(new long[]{0x0000000000000012L});
    public static final BitSet FOLLOW_note_in_diceexpr171 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_funcexpr_in_dice197 = new BitSet(new long[]{0x0000000000090D72L});
    public static final BitSet FOLLOW_die_in_dice201 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_die_in_dice213 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_D_in_die233 = new BitSet(new long[]{0x0000000000010162L});
    public static final BitSet FOLLOW_baseexpr_in_die235 = new BitSet(new long[]{0x0000000000000062L});
    public static final BitSet FOLLOW_diceopt_in_die240 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_word_in_diceopt261 = new BitSet(new long[]{0x0000000000010102L});
    public static final BitSet FOLLOW_baseexpr_in_diceopt263 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_word295 = new BitSet(new long[]{0x0000000000000062L});
    public static final BitSet FOLLOW_WS_in_note342 = new BitSet(new long[]{0x00000000000001F2L});
    public static final BitSet FOLLOW_set_in_note350 = new BitSet(new long[]{0x00000000000001F2L});
    public static final BitSet FOLLOW_baseexpr_in_funcexpr402 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_in_funcexpr411 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_NUMBER_in_baseexpr431 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_16_in_baseexpr443 = new BitSet(new long[]{0x0000000000090D70L});
    public static final BitSet FOLLOW_WS_in_baseexpr445 = new BitSet(new long[]{0x0000000000090D70L});
    public static final BitSet FOLLOW_expression_in_baseexpr448 = new BitSet(new long[]{0x0000000000020000L});
    public static final BitSet FOLLOW_17_in_baseexpr450 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_function_name_in_function480 = new BitSet(new long[]{0x0000000000010000L});
    public static final BitSet FOLLOW_16_in_function482 = new BitSet(new long[]{0x0000000000090D70L});
    public static final BitSet FOLLOW_WS_in_function492 = new BitSet(new long[]{0x0000000000090D70L});
    public static final BitSet FOLLOW_expression_in_function497 = new BitSet(new long[]{0x0000000000060000L});
    public static final BitSet FOLLOW_18_in_function508 = new BitSet(new long[]{0x0000000000090D70L});
    public static final BitSet FOLLOW_WS_in_function510 = new BitSet(new long[]{0x0000000000090D70L});
    public static final BitSet FOLLOW_expression_in_function515 = new BitSet(new long[]{0x0000000000060000L});
    public static final BitSet FOLLOW_17_in_function532 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_function_name561 = new BitSet(new long[]{0x0000000000080062L});
    public static final BitSet FOLLOW_set_in_function_name582 = new BitSet(new long[]{0x0000000000080062L});

}