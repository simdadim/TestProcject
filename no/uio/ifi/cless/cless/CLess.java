package no.uio.ifi.cless.cless;

/*
 * module CLess
 *
 * (c) 2010 DFL/Ifi/UiO 
 */

import java.io.*;
import no.uio.ifi.cless.chargenerator.CharGenerator;
import no.uio.ifi.cless.code.Code;
import no.uio.ifi.cless.error.Error;
import no.uio.ifi.cless.log.Log;
import no.uio.ifi.cless.scanner.Scanner;
import no.uio.ifi.cless.scanner.Token;
import no.uio.ifi.cless.syntax.Syntax;

/*
 * The main program of the C< compiler.
 */
public class CLess {
    public static final String version = "2010-08-24";

    public static String sourceName = null,  // Source file name
	sourceBaseName = null;               // Source file name without extension
    public static boolean noLink = false;    // Should we drop linking?

    /**
     * The actual main program of the C< compiler.
     * It will initialize the various modules and start the
     * compilation (or module testing, if requested); finally,
     * it will terminate the modules.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
	boolean testParser = false, testScanner = false;
		
	for (int opt_no = 0;  opt_no < args.length;  ++opt_no) {
	    String opt = args[opt_no];

	    if (opt.equals("-c")) {
		noLink = true;
	    } else if (opt.equals("-logP")) {
		Log.doLogParser = true;
	    } else if (opt.equals("-logT")) {
		Log.doLogTree = true;
	    } else if (opt.equals("-logS")) {
		Log.doLogScanner = true;
	    } else if (opt.equals("-testparser")) {
		testParser = true;
		Log.doLogParser = Log.doLogTree = true;
	    } else if (opt.equals("-testscanner")) {
		testScanner = true;  
		Log.doLogScanner = true;
	    } else if (opt.equals("-version")) {
		System.err.println("This is C< (version "+version+")");
	    } else if (opt.startsWith("-")) {
		Error.giveUsage();
	    } else {
		if (sourceName != null) Error.giveUsage();
		sourceName = opt;
		if (opt.endsWith(".c<")) 
		    sourceBaseName = opt.substring(0,opt.length()-3);
		else if (opt.endsWith(".cless")) 
		    sourceBaseName = opt.substring(0,opt.length()-6);
		else 
		    sourceBaseName = opt;
	    }
	}
	if (sourceName == null) Error.giveUsage();
		
	Error.init();  Log.init();  Code.init();
	CharGenerator.init();  Scanner.init();  Syntax.init();
		
	if (testScanner) {
	    while (Scanner.nextNextToken != Token.eofToken) 
		Scanner.readNext();
	} else {
	    Syntax.parseProgram();
	    if (Log.doLogTree) Syntax.printProgram();
	    if (! testParser) Syntax.genCode();  
	}
		
	Syntax.finish();  Scanner.finish();  CharGenerator.finish();  
	Code.finish();  Log.finish();  Error.finish();

	if (! testScanner && ! testParser) assembleCode();
    }

    private static void assembleCode() {
	String sName = sourceBaseName + ".s";

	String gccCmd = "gcc -m32";
	if (noLink) {
	    gccCmd += " -c ";
	} else {
	    gccCmd += " -o " + sourceBaseName;
	}
	gccCmd += " " + sName + " /local/share/inf2100/ificlib.c";
	System.out.println("Running " + gccCmd + " ...");

	// Run gcc and print any error messages:
	try {
	    Process p = Runtime.getRuntime().exec(gccCmd);
	    BufferedReader error =
		new BufferedReader(new InputStreamReader(p.getErrorStream()));

	    String line;
	    while ((line = error.readLine()) != null) {
		System.err.println(line);
	    }
	    error.close();

	    int status = p.waitFor();
	    if (status != 0) Error.error("Running gcc produced errors!");
	} catch (Exception e) {
	    Error.error("Cannot run gcc!");
	}
    }
}
