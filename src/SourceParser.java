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
	
	 
	/* Class fields */
	
	/*
	 * This holds the list of instructions to be executed as a part of
	 * a subroutine or a control structure (while/if else) 
	 */
	private ArrayList<String> currentStack; 
	
	/*
	 * Map containing name-type pair of local variables 
	 */
	private HashMap<String, String> currentVariables;
	
	/*
	 * Map containing name-type pair of global variables
	 */
	private HashMap<String, String> globalVariables;
 	
	/*
	 * Map containing name-value pair of local variables 
	 */
	private HashMap<String, String> currentVariableValues;
	
	/*
	 * Map containing name-value pair of global variables
	 */
	private HashMap<String, String> globalVariableValues;
	
	/* Name of the sourcefile */
	private String sourceFile;
	
	private BufferedReader br;
	
	private String currentLine;
	
	private boolean saveFlag =false;
	
	
	
	
	
	
	public SourceParser(String filename){
		this.sourceFile = filename;
	}
	
	
	public void init() throws FileNotFoundException{
		
		br = new BufferedReader(new FileReader(this.sourceFile));
		
	}
	
	public void readFromFile() throws IOException{
		
		currentLine = br.readLine();
		
		categorize(currentLine);
		
		
	}
	
	public void categorize(String line){
		
		/*	If its a subroutine, make a program object and
		 *  and add its local variables, stack of instructions
		 *  and finally add it the global variable maps
		 */
		
		
		/*
		 * If its a while statement, save the block in current stack 
		 * and while the while_condition is true, execute the currentstack 
		 * 
		 */
		
		if(currenLine == whileline){
			
			while_condiiton= "";
			
			while(currentline  == "]"){
				
				currentstack.add(cuurentline);
				
			}
			
		}
		
		
		while(execute(while_condition)){
			
			execute(current_stack);
			
		}
		
		
		/*
		 * If its an if statement, execute the if_condition and keep
		 * 
		 */
		
		
		
		
	}
	
}
