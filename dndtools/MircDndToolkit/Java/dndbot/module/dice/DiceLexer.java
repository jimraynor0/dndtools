// $ANTLR 3.3 Nov 30, 2010 12:45:30 D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g 2011-05-15 20:40:15

package dndbot.module.dice;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class DiceLexer extends Lexer {
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

    public DiceLexer() {;} 
    public DiceLexer(CharStream input) {
        this(input, new RecognizerSharedState());
    }
    public DiceLexer(CharStream input, RecognizerSharedState state) {
        super(input,state);

    }
    public String getGrammarFileName() { return "D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g"; }

    // $ANTLR start "T__10"
    public final void mT__10() throws RecognitionException {
        try {
            int _type = T__10;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:7:7: ( '+' )
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:7:9: '+'
            {
            match('+'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__10"

    // $ANTLR start "T__11"
    public final void mT__11() throws RecognitionException {
        try {
            int _type = T__11;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:8:7: ( '-' )
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:8:9: '-'
            {
            match('-'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__11"

    // $ANTLR start "T__12"
    public final void mT__12() throws RecognitionException {
        try {
            int _type = T__12;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:9:7: ( '^' )
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:9:9: '^'
            {
            match('^'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__12"

    // $ANTLR start "T__13"
    public final void mT__13() throws RecognitionException {
        try {
            int _type = T__13;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:10:7: ( '*' )
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:10:9: '*'
            {
            match('*'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__13"

    // $ANTLR start "T__14"
    public final void mT__14() throws RecognitionException {
        try {
            int _type = T__14;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:11:7: ( '/' )
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:11:9: '/'
            {
            match('/'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__14"

    // $ANTLR start "T__15"
    public final void mT__15() throws RecognitionException {
        try {
            int _type = T__15;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:12:7: ( '%' )
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:12:9: '%'
            {
            match('%'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__15"

    // $ANTLR start "T__16"
    public final void mT__16() throws RecognitionException {
        try {
            int _type = T__16;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:13:7: ( '(' )
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:13:9: '('
            {
            match('('); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__16"

    // $ANTLR start "T__17"
    public final void mT__17() throws RecognitionException {
        try {
            int _type = T__17;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:14:7: ( ')' )
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:14:9: ')'
            {
            match(')'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__17"

    // $ANTLR start "T__18"
    public final void mT__18() throws RecognitionException {
        try {
            int _type = T__18;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:15:7: ( ',' )
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:15:9: ','
            {
            match(','); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__18"

    // $ANTLR start "T__19"
    public final void mT__19() throws RecognitionException {
        try {
            int _type = T__19;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:16:7: ( '_' )
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:16:9: '_'
            {
            match('_'); 

            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "T__19"

    // $ANTLR start "WS"
    public final void mWS() throws RecognitionException {
        try {
            int _type = WS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:99:4: ( ( '\\u0009' .. '\\u000D' | '\\u0020' | '\\u0085' | '\\u00A0' | '\\u1680' | '\\u180E' | '\\u2000' .. '\\u200A' | '\\u2028' | '\\u2029' | '\\u202F' | '\\u205F' | '\\u3000' ) )
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:99:6: ( '\\u0009' .. '\\u000D' | '\\u0020' | '\\u0085' | '\\u00A0' | '\\u1680' | '\\u180E' | '\\u2000' .. '\\u200A' | '\\u2028' | '\\u2029' | '\\u202F' | '\\u205F' | '\\u3000' )
            {
            if ( (input.LA(1)>='\t' && input.LA(1)<='\r')||input.LA(1)==' '||input.LA(1)=='\u0085'||input.LA(1)=='\u00A0'||input.LA(1)=='\u1680'||input.LA(1)=='\u180E'||(input.LA(1)>='\u2000' && input.LA(1)<='\u200A')||(input.LA(1)>='\u2028' && input.LA(1)<='\u2029')||input.LA(1)=='\u202F'||input.LA(1)=='\u205F'||input.LA(1)=='\u3000' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "WS"

    // $ANTLR start "NUMBER"
    public final void mNUMBER() throws RecognitionException {
        try {
            int _type = NUMBER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:101:8: ( ( DIGIT ( '.' DIGIT )? | '.' DIGIT ) )
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:101:10: ( DIGIT ( '.' DIGIT )? | '.' DIGIT )
            {
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:101:10: ( DIGIT ( '.' DIGIT )? | '.' DIGIT )
            int alt2=2;
            int LA2_0 = input.LA(1);

            if ( ((LA2_0>='0' && LA2_0<='9')) ) {
                alt2=1;
            }
            else if ( (LA2_0=='.') ) {
                alt2=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 2, 0, input);

                throw nvae;
            }
            switch (alt2) {
                case 1 :
                    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:101:12: DIGIT ( '.' DIGIT )?
                    {
                    mDIGIT(); 
                    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:101:18: ( '.' DIGIT )?
                    int alt1=2;
                    int LA1_0 = input.LA(1);

                    if ( (LA1_0=='.') ) {
                        alt1=1;
                    }
                    switch (alt1) {
                        case 1 :
                            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:101:20: '.' DIGIT
                            {
                            match('.'); 
                            mDIGIT(); 

                            }
                            break;

                    }


                    }
                    break;
                case 2 :
                    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:101:35: '.' DIGIT
                    {
                    match('.'); 
                    mDIGIT(); 

                    }
                    break;

            }


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "NUMBER"

    // $ANTLR start "DIGIT"
    public final void mDIGIT() throws RecognitionException {
        try {
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:103:7: ( ( '0' .. '9' )+ )
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:103:9: ( '0' .. '9' )+
            {
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:103:9: ( '0' .. '9' )+
            int cnt3=0;
            loop3:
            do {
                int alt3=2;
                int LA3_0 = input.LA(1);

                if ( ((LA3_0>='0' && LA3_0<='9')) ) {
                    alt3=1;
                }


                switch (alt3) {
            	case 1 :
            	    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:103:10: '0' .. '9'
            	    {
            	    matchRange('0','9'); 

            	    }
            	    break;

            	default :
            	    if ( cnt3 >= 1 ) break loop3;
                        EarlyExitException eee =
                            new EarlyExitException(3, input);
                        throw eee;
                }
                cnt3++;
            } while (true);


            }

        }
        finally {
        }
    }
    // $ANTLR end "DIGIT"

    // $ANTLR start "D"
    public final void mD() throws RecognitionException {
        try {
            int _type = D;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:106:3: ( 'd' | 'D' )
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:
            {
            if ( input.LA(1)=='D'||input.LA(1)=='d' ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "D"

    // $ANTLR start "ALPHA"
    public final void mALPHA() throws RecognitionException {
        try {
            int _type = ALPHA;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:108:7: ( 'a' .. 'z' | 'A' .. 'Z' )
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:
            {
            if ( (input.LA(1)>='A' && input.LA(1)<='Z')||(input.LA(1)>='a' && input.LA(1)<='z') ) {
                input.consume();

            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;}


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "ALPHA"

    // $ANTLR start "OTHER"
    public final void mOTHER() throws RecognitionException {
        try {
            int _type = OTHER;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:110:7: ( (~ ( '+' | '-' | '%' | '*' | '/' | '^' | '(' | ')' | ',' | '0' .. '9' | 'a' .. 'z' | 'A' .. 'Z' | '\\u0009' .. '\\u000D' | '\\u0020' | '\\u0085' | '\\u00A0' | '\\u1680' | '\\u180E' | '\\u2000' .. '\\u200A' | '\\u2028' | '\\u2029' | '\\u202F' | '\\u205F' | '\\u3000' ) )+ )
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:110:9: (~ ( '+' | '-' | '%' | '*' | '/' | '^' | '(' | ')' | ',' | '0' .. '9' | 'a' .. 'z' | 'A' .. 'Z' | '\\u0009' .. '\\u000D' | '\\u0020' | '\\u0085' | '\\u00A0' | '\\u1680' | '\\u180E' | '\\u2000' .. '\\u200A' | '\\u2028' | '\\u2029' | '\\u202F' | '\\u205F' | '\\u3000' ) )+
            {
            // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:110:9: (~ ( '+' | '-' | '%' | '*' | '/' | '^' | '(' | ')' | ',' | '0' .. '9' | 'a' .. 'z' | 'A' .. 'Z' | '\\u0009' .. '\\u000D' | '\\u0020' | '\\u0085' | '\\u00A0' | '\\u1680' | '\\u180E' | '\\u2000' .. '\\u200A' | '\\u2028' | '\\u2029' | '\\u202F' | '\\u205F' | '\\u3000' ) )+
            int cnt4=0;
            loop4:
            do {
                int alt4=2;
                int LA4_0 = input.LA(1);

                if ( ((LA4_0>='\u0000' && LA4_0<='\b')||(LA4_0>='\u000E' && LA4_0<='\u001F')||(LA4_0>='!' && LA4_0<='$')||(LA4_0>='&' && LA4_0<='\'')||LA4_0=='.'||(LA4_0>=':' && LA4_0<='@')||(LA4_0>='[' && LA4_0<=']')||(LA4_0>='_' && LA4_0<='`')||(LA4_0>='{' && LA4_0<='\u0084')||(LA4_0>='\u0086' && LA4_0<='\u009F')||(LA4_0>='\u00A1' && LA4_0<='\u167F')||(LA4_0>='\u1681' && LA4_0<='\u180D')||(LA4_0>='\u180F' && LA4_0<='\u1FFF')||(LA4_0>='\u200B' && LA4_0<='\u2027')||(LA4_0>='\u202A' && LA4_0<='\u202E')||(LA4_0>='\u2030' && LA4_0<='\u205E')||(LA4_0>='\u2060' && LA4_0<='\u2FFF')||(LA4_0>='\u3001' && LA4_0<='\uFFFF')) ) {
                    alt4=1;
                }


                switch (alt4) {
            	case 1 :
            	    // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:110:11: ~ ( '+' | '-' | '%' | '*' | '/' | '^' | '(' | ')' | ',' | '0' .. '9' | 'a' .. 'z' | 'A' .. 'Z' | '\\u0009' .. '\\u000D' | '\\u0020' | '\\u0085' | '\\u00A0' | '\\u1680' | '\\u180E' | '\\u2000' .. '\\u200A' | '\\u2028' | '\\u2029' | '\\u202F' | '\\u205F' | '\\u3000' )
            	    {
            	    if ( (input.LA(1)>='\u0000' && input.LA(1)<='\b')||(input.LA(1)>='\u000E' && input.LA(1)<='\u001F')||(input.LA(1)>='!' && input.LA(1)<='$')||(input.LA(1)>='&' && input.LA(1)<='\'')||input.LA(1)=='.'||(input.LA(1)>=':' && input.LA(1)<='@')||(input.LA(1)>='[' && input.LA(1)<=']')||(input.LA(1)>='_' && input.LA(1)<='`')||(input.LA(1)>='{' && input.LA(1)<='\u0084')||(input.LA(1)>='\u0086' && input.LA(1)<='\u009F')||(input.LA(1)>='\u00A1' && input.LA(1)<='\u167F')||(input.LA(1)>='\u1681' && input.LA(1)<='\u180D')||(input.LA(1)>='\u180F' && input.LA(1)<='\u1FFF')||(input.LA(1)>='\u200B' && input.LA(1)<='\u2027')||(input.LA(1)>='\u202A' && input.LA(1)<='\u202E')||(input.LA(1)>='\u2030' && input.LA(1)<='\u205E')||(input.LA(1)>='\u2060' && input.LA(1)<='\u2FFF')||(input.LA(1)>='\u3001' && input.LA(1)<='\uFFFF') ) {
            	        input.consume();

            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        recover(mse);
            	        throw mse;}


            	    }
            	    break;

            	default :
            	    if ( cnt4 >= 1 ) break loop4;
                        EarlyExitException eee =
                            new EarlyExitException(4, input);
                        throw eee;
                }
                cnt4++;
            } while (true);


            }

            state.type = _type;
            state.channel = _channel;
        }
        finally {
        }
    }
    // $ANTLR end "OTHER"

    public void mTokens() throws RecognitionException {
        // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:1:8: ( T__10 | T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | WS | NUMBER | D | ALPHA | OTHER )
        int alt5=15;
        alt5 = dfa5.predict(input);
        switch (alt5) {
            case 1 :
                // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:1:10: T__10
                {
                mT__10(); 

                }
                break;
            case 2 :
                // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:1:16: T__11
                {
                mT__11(); 

                }
                break;
            case 3 :
                // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:1:22: T__12
                {
                mT__12(); 

                }
                break;
            case 4 :
                // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:1:28: T__13
                {
                mT__13(); 

                }
                break;
            case 5 :
                // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:1:34: T__14
                {
                mT__14(); 

                }
                break;
            case 6 :
                // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:1:40: T__15
                {
                mT__15(); 

                }
                break;
            case 7 :
                // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:1:46: T__16
                {
                mT__16(); 

                }
                break;
            case 8 :
                // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:1:52: T__17
                {
                mT__17(); 

                }
                break;
            case 9 :
                // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:1:58: T__18
                {
                mT__18(); 

                }
                break;
            case 10 :
                // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:1:64: T__19
                {
                mT__19(); 

                }
                break;
            case 11 :
                // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:1:70: WS
                {
                mWS(); 

                }
                break;
            case 12 :
                // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:1:73: NUMBER
                {
                mNUMBER(); 

                }
                break;
            case 13 :
                // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:1:80: D
                {
                mD(); 

                }
                break;
            case 14 :
                // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:1:82: ALPHA
                {
                mALPHA(); 

                }
                break;
            case 15 :
                // D:\\Java\\DnDBot\\src\\Java\\dndbot\\module\\dice\\Dice.g:1:88: OTHER
                {
                mOTHER(); 

                }
                break;

        }

    }


    protected DFA5 dfa5 = new DFA5(this);
    static final String DFA5_eotS =
        "\12\uffff\1\21\2\uffff\1\20\5\uffff";
    static final String DFA5_eofS =
        "\23\uffff";
    static final String DFA5_minS =
        "\1\0\11\uffff\1\0\2\uffff\1\60\5\uffff";
    static final String DFA5_maxS =
        "\1\uffff\11\uffff\1\uffff\2\uffff\1\71\5\uffff";
    static final String DFA5_acceptS =
        "\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\11\1\uffff\1\13\1\14"+
        "\1\uffff\1\15\1\16\1\17\1\12\1\15";
    static final String DFA5_specialS =
        "\1\0\11\uffff\1\1\10\uffff}>";
    static final String[] DFA5_transitionS = {
            "\11\20\5\13\22\20\1\13\4\20\1\6\2\20\1\7\1\10\1\4\1\1\1\11"+
            "\1\2\1\15\1\5\12\14\7\20\3\17\1\16\26\17\3\20\1\3\1\12\1\20"+
            "\3\17\1\16\26\17\12\20\1\13\32\20\1\13\u15df\20\1\13\u018d\20"+
            "\1\13\u07f1\20\13\13\35\20\2\13\5\20\1\13\57\20\1\13\u0fa0\20"+
            "\1\13\ucfff\20",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\11\20\5\uffff\22\20\1\uffff\4\20\1\uffff\2\20\6\uffff\1\20"+
            "\13\uffff\7\20\32\uffff\3\20\1\uffff\2\20\32\uffff\12\20\1\uffff"+
            "\32\20\1\uffff\u15df\20\1\uffff\u018d\20\1\uffff\u07f1\20\13"+
            "\uffff\35\20\2\uffff\5\20\1\uffff\57\20\1\uffff\u0fa0\20\1\uffff"+
            "\ucfff\20",
            "",
            "",
            "\12\14",
            "",
            "",
            "",
            "",
            ""
    };

    static final short[] DFA5_eot = DFA.unpackEncodedString(DFA5_eotS);
    static final short[] DFA5_eof = DFA.unpackEncodedString(DFA5_eofS);
    static final char[] DFA5_min = DFA.unpackEncodedStringToUnsignedChars(DFA5_minS);
    static final char[] DFA5_max = DFA.unpackEncodedStringToUnsignedChars(DFA5_maxS);
    static final short[] DFA5_accept = DFA.unpackEncodedString(DFA5_acceptS);
    static final short[] DFA5_special = DFA.unpackEncodedString(DFA5_specialS);
    static final short[][] DFA5_transition;

    static {
        int numStates = DFA5_transitionS.length;
        DFA5_transition = new short[numStates][];
        for (int i=0; i<numStates; i++) {
            DFA5_transition[i] = DFA.unpackEncodedString(DFA5_transitionS[i]);
        }
    }

    class DFA5 extends DFA {

        public DFA5(BaseRecognizer recognizer) {
            this.recognizer = recognizer;
            this.decisionNumber = 5;
            this.eot = DFA5_eot;
            this.eof = DFA5_eof;
            this.min = DFA5_min;
            this.max = DFA5_max;
            this.accept = DFA5_accept;
            this.special = DFA5_special;
            this.transition = DFA5_transition;
        }
        public String getDescription() {
            return "1:1: Tokens : ( T__10 | T__11 | T__12 | T__13 | T__14 | T__15 | T__16 | T__17 | T__18 | T__19 | WS | NUMBER | D | ALPHA | OTHER );";
        }
        public int specialStateTransition(int s, IntStream _input) throws NoViableAltException {
            IntStream input = _input;
        	int _s = s;
            switch ( s ) {
                    case 0 : 
                        int LA5_0 = input.LA(1);

                        s = -1;
                        if ( (LA5_0=='+') ) {s = 1;}

                        else if ( (LA5_0=='-') ) {s = 2;}

                        else if ( (LA5_0=='^') ) {s = 3;}

                        else if ( (LA5_0=='*') ) {s = 4;}

                        else if ( (LA5_0=='/') ) {s = 5;}

                        else if ( (LA5_0=='%') ) {s = 6;}

                        else if ( (LA5_0=='(') ) {s = 7;}

                        else if ( (LA5_0==')') ) {s = 8;}

                        else if ( (LA5_0==',') ) {s = 9;}

                        else if ( (LA5_0=='_') ) {s = 10;}

                        else if ( ((LA5_0>='\t' && LA5_0<='\r')||LA5_0==' '||LA5_0=='\u0085'||LA5_0=='\u00A0'||LA5_0=='\u1680'||LA5_0=='\u180E'||(LA5_0>='\u2000' && LA5_0<='\u200A')||(LA5_0>='\u2028' && LA5_0<='\u2029')||LA5_0=='\u202F'||LA5_0=='\u205F'||LA5_0=='\u3000') ) {s = 11;}

                        else if ( ((LA5_0>='0' && LA5_0<='9')) ) {s = 12;}

                        else if ( (LA5_0=='.') ) {s = 13;}

                        else if ( (LA5_0=='D'||LA5_0=='d') ) {s = 14;}

                        else if ( ((LA5_0>='A' && LA5_0<='C')||(LA5_0>='E' && LA5_0<='Z')||(LA5_0>='a' && LA5_0<='c')||(LA5_0>='e' && LA5_0<='z')) ) {s = 15;}

                        else if ( ((LA5_0>='\u0000' && LA5_0<='\b')||(LA5_0>='\u000E' && LA5_0<='\u001F')||(LA5_0>='!' && LA5_0<='$')||(LA5_0>='&' && LA5_0<='\'')||(LA5_0>=':' && LA5_0<='@')||(LA5_0>='[' && LA5_0<=']')||LA5_0=='`'||(LA5_0>='{' && LA5_0<='\u0084')||(LA5_0>='\u0086' && LA5_0<='\u009F')||(LA5_0>='\u00A1' && LA5_0<='\u167F')||(LA5_0>='\u1681' && LA5_0<='\u180D')||(LA5_0>='\u180F' && LA5_0<='\u1FFF')||(LA5_0>='\u200B' && LA5_0<='\u2027')||(LA5_0>='\u202A' && LA5_0<='\u202E')||(LA5_0>='\u2030' && LA5_0<='\u205E')||(LA5_0>='\u2060' && LA5_0<='\u2FFF')||(LA5_0>='\u3001' && LA5_0<='\uFFFF')) ) {s = 16;}

                        if ( s>=0 ) return s;
                        break;
                    case 1 : 
                        int LA5_10 = input.LA(1);

                        s = -1;
                        if ( ((LA5_10>='\u0000' && LA5_10<='\b')||(LA5_10>='\u000E' && LA5_10<='\u001F')||(LA5_10>='!' && LA5_10<='$')||(LA5_10>='&' && LA5_10<='\'')||LA5_10=='.'||(LA5_10>=':' && LA5_10<='@')||(LA5_10>='[' && LA5_10<=']')||(LA5_10>='_' && LA5_10<='`')||(LA5_10>='{' && LA5_10<='\u0084')||(LA5_10>='\u0086' && LA5_10<='\u009F')||(LA5_10>='\u00A1' && LA5_10<='\u167F')||(LA5_10>='\u1681' && LA5_10<='\u180D')||(LA5_10>='\u180F' && LA5_10<='\u1FFF')||(LA5_10>='\u200B' && LA5_10<='\u2027')||(LA5_10>='\u202A' && LA5_10<='\u202E')||(LA5_10>='\u2030' && LA5_10<='\u205E')||(LA5_10>='\u2060' && LA5_10<='\u2FFF')||(LA5_10>='\u3001' && LA5_10<='\uFFFF')) ) {s = 16;}

                        else s = 17;

                        if ( s>=0 ) return s;
                        break;
            }
            NoViableAltException nvae =
                new NoViableAltException(getDescription(), 5, _s, input);
            error(nvae);
            throw nvae;
        }
    }
 

}