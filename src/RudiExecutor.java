import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

public class RudiExecutor {

	public String execute(Map<String, String> localVariableTypes,
			Map<String, String> localVariableValues,
			ArrayList<String> programInstructionList) {
		String vals[];
		boolean bool=false;
		for (String s : programInstructionList) {
		
			switch (s) {
			/*
			 * keywords
			 * 
			 * program/end/decs/begin/stop
			 * 
			 * control
			 * 
			 * if(boolean)then[...]
			 * 
			 * if(boolean)then[...]else[...]
			 * 
			 * while(Boolean)[...]
			 */
			/*
			 * Arithmetic =,+,-,/,*
			 */
			
			case "-":
			case "+":
			case "*":
			case "/":
				s=PlaceVariableValues(s);
				float answer = EvaluateExpression(s);
				break;	
		  
			case ":eq:":
			case ":ne:":
			case ":gt:":
			case ":lt:":
			case ":ge:":
			case ":le:":
				s=PlaceVariableValues(s);
				boolean result= EvaluateLogicalExpression(s);
				break;
			}

			/*
			 * Comparison :eq:,:ne:,:gt:,:lt:,:ge:,:le:
			 * 
			 * Boolean ^,|,~
			 * 
			 * Comments /
			 * 
			 * print" "
			 * 
			 * print cr
			 * 
			 * print variable
			 * 
			 * input variable
			 */
			//case "input ":
				
					
		}
		return null;
	}

	private boolean EvaluateLogicalExpression(String s) {
		return false;
		// TODO Auto-generated method stub
		
		
	}

	// 1-2+3*4/6
/*	
	*//**
	* Returns the last position of c outside of all brackets in the string s.
	* @param s the string in which is searched for
	* @param c the character to find
	* @return the index of the first occurrence of the character
	*/
	private static int findIndex(String s, char c)
	{
	  int count = 0;
	  // searching the string from back to front
	  for (int i = s.length() - 1; i >= 0; i--)
	  {
	    if (s.charAt(i) == '(') count++; // count openening brackets
	    if (s.charAt(i) == ')') count--; // count closing brackets
	    // count = 0 means we are outside all brackets
	    if (s.charAt(i) == c && count == 0)
	      // found the rightmost occurence outside all brackets
	      return i;
	  }
	  return -1;
	}	
	
private static float EvaluateExpression(String s) {
	
	// TODO Auto-generated method stub
		s=PlaceVariableValues(s);
		float val = 0;
		int index=0;
		//String parts[];	
		
			if ((index = (findIndex(s, '-'))) >= 0) {
					return (EvaluateExpression(s.substring(0,index))
							- EvaluateExpression(s.substring(index+1, s.length())));
			} else
				if ((index = (findIndex(s, '+'))) >= 0) {
					return (EvaluateExpression(s.substring(0,index))
							+ EvaluateExpression(s.substring(index+1, s.length())));
				} else
					if ((index = (findIndex(s, '*'))) >= 0) {
						return (EvaluateExpression(s.substring(0,index))
								* EvaluateExpression(s.substring(index+1, s.length())));
					} else
						if ((index = (findIndex(s, '/'))) >= 0) {
							return (EvaluateExpression(s.substring(0,index))
									/ EvaluateExpression(s.substring(index+1, s.length())));
						} /*
						
						else {
			
							//Add map details here
							return Float.parseFloat(s);
						}
				//}
		//	}
	//	}
			
*/			
			if (s.charAt(0) == '(')
			  {
			    if (s.charAt(s.length()-1) == ')')
			      return (EvaluateExpression(s.substring(1, s.length()-1)));
			    //else
			      //throw new ParserException("Invalid brackets: " + s);
			  }

			  // now finally convert the string (that hopefully consists of number) to a double type.
			  try {
			    return Float.parseFloat(s);
			  } catch (NumberFormatException ex) {
			    //throw new ParserException("String to number parsing exception: " + s);
			  }	
		return val;
	}

	
	
	
	
	private static String PlaceVariableValues(String s) {
	// TODO Auto-generated method stub
	String valSplit[]=s.split(" ");
	for(String ch:valSplit){
						}		
return "";
	}

	// checks for the string to be an Integer or not
	@SuppressWarnings("unused")
	private boolean checkInt(String s) {

		try {

			Integer.parseInt(s);

		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	// checks for the string to be a Float or not.
	@SuppressWarnings("unused")
	private boolean checkFloat(String s) {

		try {

			Float.parseFloat(s);

		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	// Checks if entered string is either integer or float.
	@SuppressWarnings("unused")
	private boolean checkIntegerOrFloat(String s) {
		if (checkFloat(s)) {
			return true;

		} else if (checkInt(s)) {
			return true;
		} else
			return false;
	}

	private boolean assignment(String s) {
		if (s.contains("=")) {
			String parts[] = s.split("=");
			// checkMap()

			return true;

		} else
			return false;
	}

	private boolean add(String s) {
		if (s.contains("+")) {
			String parts[] = s.split("+");
			//
			// checkValues(s[0]);
			return true;

		} else
			return false;
	}

	public static void main(String args[]) {
		String s = "(1-2)+3*(4/8)";
		Float result = 0f;
		result = EvaluateExpression(s);
		System.out.println("Answer is: " + result);
	}

}
