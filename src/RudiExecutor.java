
public class RudiExecutor {

	
	
	
	//checks for the string to be an Integer or not
	@SuppressWarnings("unused")
	private boolean checkInt(String s){
		
		try{
			
			Integer.parseInt(s);
			
		}
		catch(NumberFormatException e){
			return false;
		}
		return true;
	}
	
	//checks for the string to be a Float or not.
	@SuppressWarnings("unused")
	private boolean checkFloat(String s){
		
		try{
			
			Float.parseFloat(s);
			
		}
		catch(NumberFormatException e){
			return false;
		}
		return true;
	}
	
	//Checks if entered string is either integer or float. 
	@SuppressWarnings("unused")
	private boolean checkIntegerOrFloat(String s){
		if(checkFloat(s)){
			return true;
			
		}else if(checkInt(s)){
		return true;	
		}	
		else
		return false;
	}
	
	private boolean assignment(String s){
		if(s.contains("=")){
		String parts[]=s.split("=");
		//checkMap()
		
		return true;
		
		}
		else
		return false;
	}
	
	private boolean add(String s){
		if(s.contains("+")){
		String parts[]=s.split("+");
		//
		//checkValues(s[0]);
		return true;
		
		}
		else
		return false;
	}
	
	
	
	
}

	