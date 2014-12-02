/*
 * RudyProgram.java
 * 
 * Authors:
 * 
 * Date:
 * 
 * This class contains all information that a generic Rudy
 * function contains which includes local variables and set of 
 * instructions/operations. 
 * 
 * An object of this class can be a Rudy subroutine or even the main 
 * function (program) 
 * 
 */



import java.util.ArrayList;
import java.util.HashMap;



public class RudiProgram {

	
	//////////////////////////////////////////////////////////
	// Private fields
	//////////////////////////////////////////////////////////
	
	/*
	 * Map containing name-type pair of local variables 
	 */
	private HashMap<String, String> localVariableTypes;

	/*
	 * Map containing name-type pair of global variables
	 */
	private HashMap<String, String> globalVariableTypes;
 	
	/*
	 * Map containing name-value pair of local variables 
	 */
	private HashMap<String, String> localVariableValues;
	
	/*
	 * Map containing name-value pair of global variables
	 */
	private HashMap<String, String> globalVariableValues;
	
	private String[] programParameters;
	
	private InstructionList programInstructionList;
	
	private String programName;
	
	////////////////////////////////////////////////////////
	// Public Methods 
	///////////////////////////////////////////////////////
	
	
	/*
	 * Constructor: The constructor of the class takes in an 
	 * array of strings which serve as parameters to the 
	 * function. 
	 * 	
	 */
	public RudiProgram(String name, String[] params){
		this.programParameters = params;
		this.programName = name;
	}
	
	public void setLocalMaps(HashMap<String,String> localTypeMap, HashMap<String,String> localValueMap){
		
		this.localVariableTypes = localTypeMap;
		this.localVariableValues = localValueMap;
		
	}
	
	public void setGlobalMaps(HashMap<String,String> globalTypeMap, HashMap<String,String> globalValueMap){
		
		this.globalVariableTypes = globalTypeMap;
		this.globalVariableValues = globalValueMap;
		
	}
	
	public void setInstructionStack(InstructionList instructions){
		
		this.programInstructionList = instructions;
		
	}
	
	
	public void executeInstructionStack(){
		
		RudiExcecutor.execute(this.localVariableTypes,this.localVariableValues,this.programInstructionList);
		
	}
	
	
	
	
}
