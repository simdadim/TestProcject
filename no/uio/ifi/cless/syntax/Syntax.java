package no.uio.ifi.cless.syntax;

/*
 * module Syntax
 */

import no.uio.ifi.cless.cless.CLess;
import no.uio.ifi.cless.code.Code;
import no.uio.ifi.cless.error.Error;
import no.uio.ifi.cless.log.Log;
import no.uio.ifi.cless.scanner.Scanner;
import no.uio.ifi.cless.scanner.Token;

/*
 * Creates a syntax tree by parsing; also prints the tree (if requested).
 */
public class Syntax {
    static DeclList library;
    static Program program;

    public static void init() {
	//-- Must be changed in part 1:
    }
    public static void finish() {
	//-- Must be changed in part 1:
    }

    public static void parseProgram() {
	library = new DeclList(null);
	program = Program.parse(library);
    }

    public static void genCode() {
	program.genCode();
    }

    public static void printProgram() {
	program.printTree();
    }
}


/*
 * Master class for all syntactic units.
 * (This class is not mentioned in the syntax diagrams.)
 */
abstract class SyntaxUnit {
    abstract void genCode();
    abstract void printTree();
}


/*
 * A <program>
 */
class Program extends SyntaxUnit {
    DeclList progDecls;
	
    Program (DeclList library) {
	progDecls = new DeclList(library);
    }
	
    static Program parse(DeclList decls) {
	Log.enterParser("<program>");

	Program p = new Program(decls);
	while (Scanner.curToken != Token.eofToken) {
	    if (Scanner.curToken == Token.intToken && 
		Scanner.nextToken == Token.nameToken && 
		Scanner.nextNextToken == Token.leftParToken) 
	    {
		p.progDecls.addDecl(FuncDecl.parse(p.progDecls));
	    } else if (Scanner.curToken == Token.intToken) {
		//-- Must be changed in part 1:
	    } else {
		Scanner.expected("Declaration");
	    }
	}

	// Check that 'main' has been declared properly:
	//-- Must be changed in part 1:

	Log.leaveParser("</program>");
	return p;
    }
		
    void genCode() {
	progDecls.genCode();
    }

    void printTree() {
	progDecls.printTree();
    }
}


/*
 * A declaration list.
 * (This class is not mentioned in the syntax diagrams.)
 */

class DeclList extends SyntaxUnit {
    Declaration firstDecl;
    DeclList outerScope;

    DeclList (DeclList outer) {
	//-- Must be changed in part 1:
    }

    void addDecl(Declaration d) {
	//-- Must be changed in part 1:
    }
	
    Declaration findDecl(String name) {
	//-- Must be changed in part 1:
	return null;
    }

    void genCode() {
	//-- Must be changed in part 2:
    }

    void printTree() {
	//-- Must be changed in part 1:
    }
}


/*
 * Any kind of declaration.
 * (This class is not mentioned in the syntax diagrams.)
 */
abstract class Declaration extends SyntaxUnit {
    String name, assemblerName;
    Declaration nextDecl = null;

    Declaration(String n, String a) {
	name = n;  assemblerName = a;
    }

    /**
     * Utility method to check whether this Declaration is really an array.
     * When a name is found during parsing, the compiler must check that the
     * name is used properly; for instance, using an array name a in "a()" or
     * in "x=a;" is illegal. This is handled in the following way:
     * <ul>
     * <li> When a name a is found in a setting which implies that should be an
     *      array (i.e., in a construct like "a["), the parser will first 
     *      search for a's declaration d.
     * <li> The parser will call d.checkWhetherArray().
     * <li> Every sub-class of Declaration will implement a checkWhetherArray.
     *      If the declaration is indeed an array, checkWhetherArray will do
     *      nothing, but if it is not, the method will give an error message
     *      and thus stop the parsing.
     * </ul>
     * Examples
     * <dl>
     *  <dt>VarDecl.checkWhetherArray</dt>
     *  <dd>will give an error message if the variable was declared as a simple
     *      variable (i.e., as "a" and not as "a[10]").</dd>
     *  <dt>FuncDecl.checkWhetherArray</dt>
     *  <dd>will always give an error message.</dd>
     * </dl>
     */
    abstract void checkWhetherArray();

    /**
     * Utility method to check whether this Declartion is really a function.
     * 
     * @param nParamsUsed Number of parameters used in the actual call.
     *                    (The method will give an error message if the
     *                    function was used with too many or too few parameters.)
     * @see   checkWhetherArray
     */
    abstract void checkWhetherFunction(int nParamsUsed);

    /**
     * Utility method to check whether this Declaration is really a simple variable.
     *
     * @see   checkWhetherArray
     */
    abstract void checkWhetherSimpleVar();
}


/*
 * A <var decl>
 */
abstract class VarDecl extends Declaration {
    int numElems = -1;

    VarDecl(String n, String a) {
	super(n,a);
    }


    boolean isArray() {
	//-- Must be changed in part 1:
	return false;
    }

    void checkWhetherArray() {
	if (! isArray())
	    Scanner.illegal(name + " is a simple variable and no array!");
    }

    void checkWhetherFunction(int nParamsUsed) {
	Scanner.illegal(name + " is a variable and no function!");
    }
	
    void checkWhetherSimpleVar() {
	if (isArray())
	    Scanner.illegal(name + " is an array and no simple variable!");
    }


    void printTree() {
	//-- Must be changed in part 1:
    }
}


/*
 * A global variable declaration
 */
class GlobalVarDecl extends VarDecl {
    GlobalVarDecl(String n, String a) {
	super(n,a);
    }

    static VarDecl parse(DeclList decls) {
	Log.enterParser("<var decl>");

	//-- Must be changed in part 1:

	Log.leaveParser("</var decl>");
	return null;
    }

    void genCode() {
	//-- Must be changed in part 2:
    }
}


/*
 * A local variable declaration
 */
class LocalVarDecl extends VarDecl {
    LocalVarDecl(String n, String a) {
	super(n,a);
    }

    static VarDecl parse(DeclList decls, FuncDecl curFunc) {
	Log.enterParser("<var decl>");

	//-- Must be changed in part 1:

	Log.leaveParser("</var decl>");
	return null;
    }

    void genCode() {
	//-- Must be changed in part 2:
    }
}


/*
 * A <param decl>
 */
class ParamDecl extends VarDecl {
    ParamDecl(String n, int pNum) {
	super(n,Code.getParamName(pNum));
    }

    static ParamDecl parse(DeclList decls, int paramNum) {
	Log.enterParser("<param decl>");

	//-- Must be changed in part 1:
	Log.leaveParser("</param decl>");
	return null;
    }

    void genCode() {
	//-- Must be changed in part 2:
    }
}


/*
 * A <func decl>
 */
class FuncDecl extends Declaration {
    //-- Must be changed in part 1+2:
	
    FuncDecl(String n, DeclList lib) {
	// Used for user functions:
	super(n,Code.getGlobalName(n));
	//-- Must be changed in part 1:
    }
	
    static FuncDecl parse(DeclList decls) {
	//-- Must be changed in part 1:
	return null;
    }

    void checkWhetherArray() {
	//-- Must be changed in part 1:
    }

    void checkWhetherFunction(int nParamsUsed) {
	//-- Must be changed in part 1:
    }
	
    void checkWhetherSimpleVar() {
	//-- Must be changed in part 1:
    }

    void genCode() {
	//-- Must be changed in part 2:
    }

    void printTree() {
	//-- Must be changed in part 1:
    }
}



/*
 * A <statm list>.
 */
class StatmList extends SyntaxUnit {
    //-- Must be changed in part 1:

    static StatmList parse(DeclList decls, FuncDecl curFunc) {
	//-- Must be changed in part 1:
	return null;
    }

    void genCode() {
	//-- Must be changed in part 2:
    }

    void printTree() {
	//-- Must be changed in part 1:
    }
}


/*
 * A <statement>.
 */
abstract class Statement extends SyntaxUnit {
    Statement nextStatm = null;

    static Statement parse(DeclList decls, FuncDecl curFunc) {
	Log.enterParser("<statement>");

	Statement s = null;
	if (Scanner.curToken==Token.nameToken && 
	    Scanner.nextToken==Token.leftParToken) 
	{
	    //-- Must be changed in part 1:
	} else if (Scanner.curToken == Token.nameToken) {
	    //-- Must be changed in part 1:
	} else if (Scanner.curToken == Token.forToken) {
	    s = ForStatm.parse(decls, curFunc);
	} else if (Scanner.curToken == Token.ifToken) {
	    s = IfStatm.parse(decls, curFunc);
	} else if (Scanner.curToken == Token.returnToken) {
	    //-- Must be changed in part 1:
	} else if (Scanner.curToken == Token.whileToken) {
	    s = WhileStatm.parse(decls, curFunc);
	} else if (Scanner.curToken == Token.semicolonToken) {
	    s = EmptyStatm.parse(decls, curFunc);
	} else {
	    Scanner.expected("Statement");
	}

	Log.leaveParser("</statement>");
	return s;
    }
}


/*
 * An <empty statm>.
 */
class EmptyStatm extends Statement {
    //-- Must be changed in part 1+2:

    static EmptyStatm parse(DeclList decls, FuncDecl curFunc) {
	//-- Must be changed in part 1:
	return null;
    }

    void genCode() {
	//-- Must be changed in part 2:
    }

    void printTree() {
	//-- Must be changed in part 1:
    }
}
	


/*
 * A <for-statm>.
 */
class ForStatm extends Statement {
    //-- Must be changed in part 1+2:

    static ForStatm parse(DeclList decls, FuncDecl curFunc) {
	//-- Must be changed in part 1:
	return null;
    }

    void genCode() {
	//-- Must be changed in part 2:
    }

    void printTree() {
	//-- Must be changed in part 1:
    }
}


/*
 * An <if-statm>.
 */
class IfStatm extends Statement {
    //-- Must be changed in part 1+2:

    static IfStatm parse(DeclList decls, FuncDecl curFunc) {
	//-- Must be changed in part 1:
	return null;
    }

    void genCode() {
	//-- Must be changed in part 2:
    }

    void printTree() {
	//-- Must be changed in part 1:
    }
}


/*
 * A <return-statm>.
 */
class ReturnStatm extends Statement {
    //-- Must be changed in part 1+2:

    static ReturnStatm parse(DeclList decls, FuncDecl curFunc) {
	//-- Must be changed in part 1:
	return null;
    }

    void genCode() {
	//-- Must be changed in part 2:
    }

    void printTree() {
	//-- Must be changed in part 1:
    }
}


/*
 * A <while-statm>.
 */
class WhileStatm extends Statement {
    Expression test;
    StatmList body;

    static WhileStatm parse(DeclList decls, FuncDecl curFunc) {
	Log.enterParser("<while-statm>");

	WhileStatm w = new WhileStatm();
	Scanner.readNext();
	Scanner.skip(Token.leftParToken);
	w.test = Expression.parse(decls, curFunc);
	Scanner.skip(Token.rightParToken);
	Scanner.skip(Token.leftCurlToken);
	w.body = StatmList.parse(decls, curFunc);
	Scanner.skip(Token.rightCurlToken);

	Log.leaveParser("</while-statm>");
	return w;
    }

    void genCode() {
	String testLabel = Code.getLocalLabel(), 
	       endLabel  = Code.getLocalLabel();

	Code.genInstr(testLabel, "", "", "Start while-statement");
	test.genCode();
	Code.genInstr("", "cmpl", "$0,%eax", "");
	Code.genInstr("", "je", endLabel, "");
	body.genCode();
	Code.genInstr("", "jmp", testLabel, "");
	Code.genInstr(endLabel, "", "", "End while-statement");
    }

    void printTree() {
	Log.wTree("while (");
	test.printTree();
	Log.wTreeLn(") {");
	Log.indentTree();
	body.printTree();
	Log.outdentTree();
	Log.wTreeLn("}");
    }
}

//-- Must be changed in part 1+2:

/*
 * An <expression>
 */
class Expression extends Operand {
    Operand firstOp = null;
    Expression nextExpr = null;

    static Expression parse(DeclList decls, FuncDecl curFunc) {
	//-- Must be changed in part 1:
	return null;
    }

    void genCode() {
	//-- Must be changed in part 2:
    }

    void printTree() {
	//-- Must be changed in part 1:
    }
}

/*
 * An <expression list>.
 */

class ExprList extends SyntaxUnit {
    Expression firstExpr = null;

    static ExprList parse(DeclList decls, FuncDecl curFunc) {
	ExprList el = new ExprList();
	Expression last = null;

	Log.enterParser("<expr list>");

	Scanner.readNext();
	if (Scanner.curToken != Token.rightParToken) {
	    do {
		if (Scanner.curToken == Token.commaToken)
		    Scanner.readNext();
		Expression e = Expression.parse(decls, curFunc);
		if (last == null) {
		    el.firstExpr = last = e;
		} else {
		    last.nextExpr = last = e;
		}
		Scanner.check(Token.commaToken, Token.rightParToken);
	    } while (Scanner.curToken == Token.commaToken);
	}
	Scanner.skip(Token.rightParToken);

	Log.leaveParser("</expr list>");
	return el;
    }

    int nExprs() {
	int n = 0;
	//-- Must be changed in part 1:
	return n;
    }

    void genCode() {
	//-- Must be changed in part 2:
    }

    void printTree() {
	Expression ex = firstExpr;
	while (ex != null) {
	    ex.printTree();   ex = ex.nextExpr;
	    if (ex != null) Log.wTree(",");
	}
    }
}


/*
 * An <operand>
 */
abstract class Operand extends SyntaxUnit {
    Operator nextOperator = null;

    static Operand parse(DeclList decls, FuncDecl curFunc) {
	Operand op = null;

	if (Scanner.curToken == Token.numberToken) {
	    op = Number.parse(decls, curFunc);
        } else if (Scanner.curToken==Token.nameToken && 
		   Scanner.nextToken==Token.leftParToken) {
	    op = FunctionCall.parse(decls, curFunc);
	} else if (Scanner.curToken == Token.nameToken) {
	    op = Variable.parse(decls, curFunc);
	} else if (Scanner.curToken == Token.leftParToken) {
	    //-- Must be changed in part 1:
	} else {
	    Scanner.expected("An expression");
	}
	return op;
    }
}


/*
 * An <operator>
 */
abstract class Operator extends SyntaxUnit {
    Operand secondOp;
    Token opToken;

    static Operator parse(DeclList decls, FuncDecl curFunc) {
	Operator opr = null;

	//-- Must be changed in part 1:
	return opr;
    }

    void printTree() {
	//-- Must be changed in part 1:
    }
}

/*
 * An arithmetic operator (+, -, * or /).
 * (This class is not mentioned in the syntax diagrams.)
 */

class ArithOperator extends Operator {
    void genCode() {
	//-- Must be changed in part 2:
    }
}

/*
 * A comparison operator (==, !=, <, <=, > or >=).
 * (This class is not mentioned in the syntax diagrams.)
 */

class CompOperator extends Operator {
    void genCode() {
	Code.genInstr("", "popl", "%ecx", "");
	Code.genInstr("", "cmpl", "%eax,%ecx", "");
	Code.genInstr("", "movl", "$1,%eax", "");
	String trueLab = Code.getLocalLabel();
	//-- Must be changed in part 2:
    }
}


/*
 * A <function call>.
 */
class FunctionCall extends Operand {
    //-- Must be changed in part 1+2:

    static FunctionCall parse(DeclList decls, FuncDecl curFunc) {
	//-- Must be changed in part 1:
	return null;
    }

    void genCode() {
	//-- Must be changed in part 2:
    }

    void genPushParams(Expression px, int n) {
	// Push the parameters onto the stack, but in reverse order. 

	if (px.nextExpr != null) genPushParams(px.nextExpr,n+1);
	px.genCode();
	Code.genInstr("", "pushl", "%eax", "Push parameter #"+n);
    }

    void printTree() {
	//-- Must be changed in part 1:
    }
}


/*
 * A <number>.
 */

class Number extends Operand {
    int numVal;

    Number(long n) {
	if (Math.abs(n) > 10000000000L)
	    Scanner.illegal("Number " + n + " is out of range!");

	numVal = (int)n;
    }

    static Number parse(DeclList decls, FuncDecl curFunc) {
	//-- Must be changed in part 1:
	return null;
    }
	
    void genCode() {
	Code.genInstr("", "movl", "$"+numVal+",%eax", ""+numVal); 
    }

    void printTree() {
	Log.wTree("" + numVal);
    }
}


/*
 * A <variable>.
 */

class Variable extends Operand {
    String varName;
    //-- Must be changed in part 1:
    Variable(String id) {
	varName = id;
    }

    static Variable parse(DeclList decls, FuncDecl curFunc) {
	Log.enterParser("<variable>");
	//-- Must be changed in part 1:
	return null;
    }

    void genCode() {
	//-- Must be changed in part 2:
    }

    void printTree() {
	//-- Must be changed in part 1:
    }
}
