package no.uio.ifi.cless.code;

/*
 * module Code
 */

import java.io.*;
import no.uio.ifi.cless.cless.CLess;
import no.uio.ifi.cless.error.Error;
import no.uio.ifi.cless.log.Log;

/*
 * Code generation for the x86 processor.
 */
public class Code {
    private static PrintWriter codeFile;
    private static boolean generatingData = false;

    public static void init() {
	String codeFileName;
	
	if (CLess.sourceBaseName == null) return;
	codeFileName = CLess.sourceBaseName + ".s";

	try {
	    codeFile = new PrintWriter(codeFileName);
	} catch (FileNotFoundException e) {
	    Error.error("Cannot create code file " + codeFileName + "!");
	}
    }

    public static void finish() {
	codeFile.close();
    }


    private static int numLabels = 0;

    public static String getLocalLabel() {
	return String.format(".L%04d", ++numLabels);
    }

    public static String getGlobalName(String vName) {
	return vName;
    }

    public static String getLocalName(String fName, String vName) {
	return fName + "$" + vName;
    }

    public static String getParamName(int n) {
	return "" + (4*(n+1)) + "(%ebp)";
    }

    public static String getExitName(String fName) {
	return ".exit$" + fName;
    }


    public static void genInstr(String lab, String instr, 
				String arg, String comment) {
	if (generatingData) {
	    codeFile.println("        .text");
	    generatingData = false;
	}

	if (lab.length() > 6) {
	    codeFile.println(lab + ":");
	    codeFile.print("        ");
	} else if (lab.length() > 0) {
	    codeFile.printf("%-8s", lab+":");
	} else {
	    codeFile.print("        ");
	}

	codeFile.printf("%-7s %-23s ", instr, arg);

	if (comment.length() > 0) {
	    codeFile.print("# " + comment);
	}
	codeFile.println();
    }

    public static void genVar(String name, boolean global,
			      int nBytes, String comment) {
	if (! generatingData) {
	    codeFile.println("        .data");
	    generatingData = true;
	}

	if (global)
	    codeFile.println("        .globl  " + name);

	if (name.length() > 6) {
	    codeFile.println(name + ":");
	    codeFile.print("        ");
	} else if (name.length() > 0) {
	    codeFile.printf("%-8s", name+":");
	} else {
	    codeFile.print("        ");
	}

	codeFile.printf(".fill   %-24d", nBytes);

	if (comment.length() > 0) {
	    codeFile.print("# " + comment);
	}
	codeFile.println();
    }
}
