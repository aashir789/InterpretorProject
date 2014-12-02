import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Pattern;

public class RudiExecutor {

	public static String execute(Map<String, String> localVariableTypes,
			Map<String, String> localVariableValues,
			ArrayList<String> programInstructionList) {
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

				float answer = EvaluateExpression(s);
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

		}
		return null;
	}

	// 1-2+3*4/6
	
	private float DoCalculations(String s){
		float answer=0;
		String brackexp="";
		
		while(s.contains("(")){
			
			while(brackexp.contains("(")){
				int startIndex=0,brackoccur=0,endbrack=0;
				if(s.contains("("))
				brackoccur=s.indexOf("(",startIndex);
				endbrack=s.indexOf(")",brackoccur);
				brackexp=s.substring(brackoccur,endbrack);
		}
			//brackexp.
		}
		return 1;
	}
	
	

	private static float EvaluateExpression(String s) {
		// TODO Auto-generated method stub
		float val = 0;
		String parts[];		
		while (true) {
			if (s.contains("-")) {
				parts = s.split("-");
				if (parts.length == 2) {
					val = EvaluateExpression(parts[0])
							- EvaluateExpression(parts[1]);
					break;
				}
			} else {
				if (s.contains("+")) {
					parts = s.split(Pattern.quote("+"));
					if (parts.length == 2) {
						val = EvaluateExpression(parts[0])
								+ EvaluateExpression(parts[1]);
						break;
					}
				} else {
					if (s.contains("*")) {
						parts = s.split(Pattern.quote("*"));
						if (parts.length == 2) {
							val = EvaluateExpression(parts[0])
									* EvaluateExpression(parts[1]);
							break;
						}
					} else {
						if (s.contains("/")) {
							parts = s.split("/");
							if (parts.length == 2)
								val = EvaluateExpression(parts[0])
										/ EvaluateExpression(parts[1]);
							break;
						} else {
							
							
							//Add map details here
							
							val = Float.parseFloat(s);
							break;
						}
					}
				}
			}
		}
		// System.out.println("The answer is"+val);
		return val;
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
		String s = "12+12-12/3*6";
		Float result = 0f;
		result = EvaluateExpression(s);
		System.out.println("Answer is: " + result);
	}

}
