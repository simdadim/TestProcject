package no.uio.ifi.cless.scanner;

/*
 * module Scanner
 */

import no.uio.ifi.cless.chargenerator.CharGenerator;
import no.uio.ifi.cless.error.Error;
import no.uio.ifi.cless.log.Log;

/*
 * Module for forming characters into tokens.
 */
public class Scanner {
    public static Token curToken, nextToken, nextNextToken;
    public static String curName, nextName, nextNextName;
    public static long curNum, nextNum, nextNextNum;
	
    public static void init() {
	//-- Must be changed in part 0:
    }
	
    public static void finish() {
	//-- Must be changed in part 0:
    }
	
    public static void readNext() {
	curToken = nextToken;  nextToken = nextNextToken;
	curName = nextName;  nextName = nextNextName;
	curNum = nextNum;  nextNum = nextNextNum;

	nextNextToken = null;
	while (nextNextToken == null) {
	    //-- Must be changed in part 0:
	}
	Log.noteToken();
    }
	
    private static boolean isLetterAZ(char c) {
	//-- Must be changed in part 0:
	return false;
    }

    // Various error reporting methods
    // (They are placed in this package because most of them include 
    // information found here.)
	
    public static void illegal(String message) {
	Error.error(CharGenerator.curLineNum(), message);
    }

    public static void expected(String exp) {
	illegal(exp + " expected, but found a " + curToken + "!");;
    }
	
    public static void check(Token t) {
	if (curToken != t)
	    expected("A " + t);
    }
	
    public static void check(Token t1, Token t2) {
	if (curToken != t1 && curToken != t2)
	    expected("A " + t1 + " or a " + t2);
    }
	
    public static void skip(Token t) {
	check(t);  readNext();
    }
	
    public static void skip(Token t1, Token t2) {
	check(t1,t2);  readNext();
    }
}
