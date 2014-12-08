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
			else{
				// another error
			}
			
		}

	}
}
