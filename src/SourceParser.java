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
	
	/*
	 *	Types of lines when parsed: 
	 * 
	 * 	1. Can be directly executed 
	 * 	2. Is a continuation denoted by & symbol
	 * 	3. Can be a control structure (while or if)
	 * 	4. Program start/end	
	 * 	5. Subroutine start/end
	 * 
	 * 	If it is while, keep parsing executeable lines in a stack/list
	 * 	till the end block is encountered 
	 * 
	 * 
	 */
	
	/////////////////////////////////////////////////////////////////// 
	// Class fields
	///////////////////////////////////////////////////////////////////
	
	/* Name of the sourcefile */
	private String sourceFile;
	
	private HashMap<String,RudiProgram> subroutines;
	
	private BufferedReader br;
	
	private String currentLine;
	
	private int lineNo;
	
	public SourceParser(String filename){
		
		// check if the file name has .rudi extension
		
		String[] extensions = filename.split("\\.");
		if(extensions[extensions.length-1].equals("rudi") ){
			this.sourceFile = filename;
		}
		
		else {
			
			System.out.println("ERROR: Input file not a Rudi file");
			
		}
		
	}
	
	
	public void init() throws FileNotFoundException{
		
		
		br = new BufferedReader(new FileReader(this.sourceFile));
		
	}
	
	/*
	 *	 
	 */
	public void readFromFile() throws IOException{
		
		while((currentLine = br.readLine()) != null){
			
			lineNo++;
			
			
			// remove comments
			
			
			String regex = "\\s+(?=((\\\\[\\\\\"]|[^\\\\\"])*\"(\\\\[\\\\\"]|[^\\\\\"])*\")*(\\\\[\\\\\"]|[^\\\\\"])*$)";
			
			currentLine = currentLine.replaceAll(regex, "");
			
			if(currentLine.equalsIgnoreCase("")){
				continue;
			}
			else{
				if(currentLine.equalsIgnoreCase("program")){
					
					initProgram("program", null);
					
				}
				// check if the line is defining a subroutine
				else if(currentLine.toLowerCase().startsWith("subroutine")){
					
					
					initProgram(subroutineName, params);
					
				}
				else{
					// Syntax error in defining subroutine
				}
				
				
			}
			
			// now the current line has no spaces and string literals are intact
			
			
			
			
			
		}
		
		
		
	}
	
		
	public void initProgram(String programName, String[] params){
	
		// check if the program name already occurs in the map
		
		RudiProgram prog = new RudiProgram(programName, params);
		this.subroutines.put(programName, prog);
		
		
		
		// initilaize all program variables and instruction list
		
		
	}
	
	
	
	public static void main(String[] args) throws IOException{
		
		SourceParser sp = new SourceParser("factorial.rudi");
		
		sp.init();
		sp.readFromFile();
		
	}
	
	
	
	
	
}
