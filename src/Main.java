import java.io.FileNotFoundException;

public class Main {

	public static void main(String[] args) {

		try {

			SourceParser sp = new SourceParser(args[0]);

			sp.init();	
			sp.readFromFile();
			sp.runProgram();
	
		} catch (Exception e) {
			
			if(e instanceof SyntaxErrorException){
				((SyntaxErrorException) e).printMessage();
			}
			else if (e instanceof FileNotFoundException){
				System.out.println("Source file not found");
				
			}
			else{
			//	e.getMessage();
			//	e.printStackTrace();
			}
			
		}

	}
}