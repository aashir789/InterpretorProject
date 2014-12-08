
public class SyntaxErrorException extends Exception {
	
	private String instrcution;
	private int lineNo;
	
	public SyntaxErrorException(String s, int no){
		
		this.instrcution = s;
		this.lineNo = no;
		
	}
	
	public void printMessage(){
		System.out.println("Error at line no: "+this.lineNo+
					"\n"+this.instrcution);
		
	}
	
	
	
}
