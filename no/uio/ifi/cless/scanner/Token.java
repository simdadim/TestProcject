package no.uio.ifi.cless.scanner;

/*
 * class Token
 */

/*
 * The different kinds of tokens read by Scanner.
 */
public enum Token { addToken, assignToken, commaToken, divideToken, elseToken,
    eofToken, equalToken, forToken, greaterEqualToken, greaterToken, 
    ifToken, intToken, leftBracketToken, leftCurlToken, leftParToken, 
    lessEqualToken, lessToken, multiplyToken, nameToken, notEqualToken,
    numberToken, rightBracketToken, rightCurlToken, rightParToken,
    returnToken, semicolonToken, subtractToken, whileToken;

    public static boolean isOperator(Token t) {
	//-- Must be changed in part 0:
	return false;
    }
}
