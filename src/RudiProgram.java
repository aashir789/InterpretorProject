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
		this.programInstructionList = new InstructionList();
		this.globalVariableTypes = new HashMap<String, String>();
		this.globalVariableValues = new HashMap<String, String>();
		this.localVariableTypes = new HashMap<String, String>();
		this.localVariableValues = new HashMap<String, String>();
		
	}
	
	public String getName(){
		return this.programName;
	}
	
	public void setLocalMaps(HashMap<String,String> localTypeMap, HashMap<String,String> localValueMap){
		
		this.localVariableTypes = localTypeMap;
		this.localVariableValues = localValueMap;
		
	}
	
	public void setGlobalMaps(HashMap<String,String> globalTypeMap, HashMap<String,String> globalValueMap){
		
		this.globalVariableTypes = globalTypeMap;
		this.globalVariableValues = globalValueMap;
		
	}
	
	public void setInstructionList(InstructionList instructions){
		
		this.programInstructionList = instructions;
		
	}
	
	public void addToInstructionList(String instr, int lineno){
		
		this.programInstructionList.addInstruction(instr, lineno);
		
	}
	
	public boolean isUniqueVariable(String var){
		
		if(this.localVariableTypes.get(var)!= null ){
			return false;
		}
		else{
			return true;
		}
	}
	
	
	public void addToTypeMap(String name,String type){
		this.localVariableTypes.put(name, type);
	}
	
	public void executeInstructionList(){
	
		RudiExecutor rd=new RudiExecutor();
		String s=rd.execute(this.localVariableTypes,this.localVariableValues,this.programInstructionList);
		
	}
	
	
	
	
}
