import com.sun.xml.internal.ws.api.pipe.NextAction;

/*
 * InstructionList.java
 * 
 * Authors:
 * 
 * Date:
 * 
 * 
 * 
 */


public class InstructionList {

	
	/////////////////////////////////////////////////////
	// Class Fields
	/////////////////////////////////////////////////////
	
	public class InstructionNode{
	
		public String instruction;
		public InstructionNode nextInstruction;
		public int lineNo;
	}
	
	InstructionNode listHead;
	InstructionNode listTail;
	
	
	
	public void addInstruction(String instr, int lineno){
		
		
		
	}
	
	public void removeInstruction(String instr, int lineno){
		
		
		
	}
	
	
}
