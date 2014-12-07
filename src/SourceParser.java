/*
 * SourceParser.java
 * 
 * Authors:
 * 
 * Date:
 * 
 * 
 * 
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Source;

public class SourceParser {

	
	// /////////////////////////////////////////////////////////////////
	// Class fields
	// /////////////////////////////////////////////////////////////////

	/* Name of the sourcefile */
	private String sourceFile;

	private HashMap<String, RudiProgram> subroutines;

	private BufferedReader br;

	private String currentLine;

	private int lineNo;
	
	
	///////////////////////////////////////////////////////////////////
	// Public Methods
	//////////////////////////////////////////////////////////////////

	public SourceParser(String filename) {

		// check if the file name has .rudi extension

		String[] extensions = filename.split("\\.");
		if (extensions[extensions.length - 1].equals("rud")) {
			this.sourceFile = filename;
		}

		else {

			System.out.println("ERROR: Input file not a Rudi file");

		}

	}

	public void init() throws FileNotFoundException {

		this.br = new BufferedReader(new FileReader(this.sourceFile));
		this.subroutines = new HashMap<String, RudiProgram>();

	}

	/*
	 *	 
	 */
	public void readFromFile() throws IOException {

		while ((currentLine = br.readLine()) != null) {

			lineNo++;
			String regex = "\\s+(?=((\\\\[\\\\\"]|[^\\\\\"])*\"(\\\\[\\\\\"]|[^\\\\\"])*\")*(\\\\[\\\\\"]|[^\\\\\"])*$)";
			currentLine = currentLine.replaceAll(regex, "");

			// Remove comments
			currentLine = currentLine.replaceAll("/\\*+\\s*(.*?)\\*+/", "");
			
			// append lines with &

			if (currentLine.equalsIgnoreCase("")) {
				continue;
			} else {
				if (currentLine.equalsIgnoreCase("program")) {

					initProgram("program", null);

				}
				// check if the line is defining a subroutine
				else if (currentLine.toLowerCase().startsWith("subroutine")) {

					currentLine = currentLine.substring(10);

					String[] programParts = currentLine.split("\\(");

					// Error out if programparts is more than length 2

					String[] params = programParts[1].split(",");

					// error out if length is less than 1

					params[params.length - 1] = params[params.length - 1]
							.substring(0,
									params[params.length - 1].length() - 1);

					initProgram(programParts[0], params);

				} else {
					// Syntax error in defining subroutine
				}
			}

			// now the current line has no spaces and string literals are intact

		}

	}

	
	public void runProgram(){
		
		this.subroutines.get("program").executeInstructionList();
		
	}
	
	///////////////////////////////////////////////////////////////////
	// Private Methods
	///////////////////////////////////////////////////////////////////
	
	private void initProgram(String programName, String[] params)
			throws IOException {

		// Local Variables
		RudiProgram prog;

		// check if the program name already occurs in the map
		if (this.subroutines.get(programName) == null) {
			prog = new RudiProgram(programName, params);
			this.subroutines.put(programName, prog);
		} else {
			// Error out due to repetition in names

			return;

		}
		// initialize all program variables and instruction list

		while ((this.currentLine = br.readLine()) != null) {

			lineNo++;

			String regex = "\\s+(?=((\\\\[\\\\\"]|[^\\\\\"])*\"(\\\\[\\\\\"]|[^\\\\\"])*\")*(\\\\[\\\\\"]|[^\\\\\"])*$)";
			currentLine = currentLine.replaceAll(regex, "");
			if (currentLine.equalsIgnoreCase("")) {
				continue;
			}
			
			// Remove comments
			currentLine = currentLine.replaceAll("/\\*+\\s*(.*?)\\*+/", "");
						
			// append lines with &

			

			if (currentLine.equalsIgnoreCase("decs")) {

				// open brackets on new line
				mapVariables(prog);
				continue;

			} else if (currentLine.equalsIgnoreCase("begin")) {

				linkInstructions(prog);
			} else {

				// Error in syntax
				System.out.println("Syntax Error on line " + lineNo);

			}

		}

	}

	private void mapVariables(RudiProgram prog) throws IOException {

		// Local Variables
		boolean decsOpened = false;
		
		while ((currentLine = br.readLine()) != null) {

			lineNo++;

			// Remove white spaces
			String regex = "\\s+(?=((\\\\[\\\\\"]|[^\\\\\"])*\"(\\\\[\\\\\"]|[^\\\\\"])*\")*(\\\\[\\\\\"]|[^\\\\\"])*$)";
			currentLine = currentLine.replaceAll(regex, "");
			
			// Remove comments
			currentLine = currentLine.replaceAll("/\\*+\\s*(.*?)\\*+/", "");
			
			// append lines with &

			if (currentLine.equalsIgnoreCase("")) {
				continue;
			}
			
			
			// check if the first character is [
			
			if(!decsOpened){
				
				if( currentLine.equals("[") ){
					lineNo++;
					decsOpened = true;
					continue;
				}
				else{
					// Syntax error
					System.out.println("Syntax Error in declaring variables");
					return;
				}
			}
			
			
			
			// check if the decs block has ended
			if(currentLine.equals("]")){
				break;
			}
			
			
			// check for the three possible types
			if(currentLine.toLowerCase().startsWith("string")){
				currentLine = currentLine.substring(6);
				
				if(prog.isUniqueVariable(currentLine.toLowerCase())){
					prog.addToTypeMap(currentLine.toLowerCase(),"string");
				}
				else{
					// throw variable already defined error
				}
				
			}
			else if(currentLine.toLowerCase().startsWith("integer")){
				currentLine = currentLine.substring(7);
				
				if(prog.isUniqueVariable(currentLine.toLowerCase())){
					prog.addToTypeMap(currentLine.toLowerCase(),"integer");
				}
				else{
					// throw variable already defined error
				}
			} 
			else if(currentLine.toLowerCase().startsWith("float")){
				currentLine = currentLine.substring(5);
				
				if(prog.isUniqueVariable(currentLine.toLowerCase())){
					prog.addToTypeMap(currentLine.toLowerCase(),"float");
				}
				else{
					// throw variable already defined error
				}
			}
			else{
				// syntax error undefined type
				
			}
			

		}

	}

	private void linkInstructions(RudiProgram prog) throws IOException {

		while ((currentLine = br.readLine()) != null) {

			lineNo++;

			
			String regex = "\\s+(?=((\\\\[\\\\\"]|[^\\\\\"])*\"(\\\\[\\\\\"]|[^\\\\\"])*\")*(\\\\[\\\\\"]|[^\\\\\"])*$)";
			currentLine = currentLine.replaceAll(regex, "");
			
			// Remove comments
			currentLine = currentLine.replaceAll("/\\*+\\s*(.*?)\\*+/", "");
			
			// append lines with &

			
			if (currentLine.equalsIgnoreCase("")) {
				continue;
			}

			
			// check for if-else and while statements and modify them too
			
			// check if the subroutine has ended
			if ((prog.getName().equalsIgnoreCase("program") && currentLine
					.equalsIgnoreCase("end"))
					|| currentLine.equalsIgnoreCase("return")) {
				break;
			} else {

				prog.addToInstructionList(currentLine, lineNo);

			}

		}

	}

	
	///////////////////////////////////////////////////////////////////
	// Main function to test the class
	///////////////////////////////////////////////////////////////////
	
	public static void main(String[] args) throws IOException {

		SourceParser sp = new SourceParser(args[0]);

		sp.init();
		sp.readFromFile();
		sp.runProgram();

	}

}
