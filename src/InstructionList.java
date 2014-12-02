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
	
	/////////////////////////////////////////////////////
	// Public Methods
	/////////////////////////////////////////////////////

	
	public void addInstruction(String instr, int lineno){
		
		if(this.listHead == null){
			this.listHead = new InstructionNode();
			this.listHead.instruction = instr;
			this.listHead.lineNo = lineno;
			
			this.listTail = this.listHead;
			this.listTail.nextInstruction = null;
			return;
		}
		
		this.listTail.nextInstruction = new InstructionNode();
		this.listTail = this.listTail.nextInstruction;
		this.listTail.instruction = instr;
		this.listTail.lineNo = lineno;
		this.listTail.nextInstruction = null;
		
	}
	
	public void removeInstruction(String instr, int lineno){
		
		
		
	}
	
	
}
