import java.io.InputStreamReader;
import java.util.Map;
import java.util.Scanner;

public class RudiExecutor {
	private static Map<String, String> localVarTypes;
	private static Map<String, String> localVarValues;

	public RudiExecutor() {
		// TODO Auto-generated constructor stub
	}

	public String execute(Map<String, String> localVariableTypes,
			Map<String, String> localVariableValues, InstructionList inList) {
		localVarTypes = localVariableTypes;
		localVarValues = localVariableValues;

		String vals[], choice;
		boolean bool = false;

		InstructionList.InstructionNode instrList = inList.listHead;
		while (instrList != null) {

			String s = instrList.instruction;
			choice = getChoice(instrList.instruction);
			String type = "";
			String answer;
			switch (choice) {

			case "assignment":

				String Parts[] = s.split("=");
				if (localVariableTypes.containsKey(Parts[0]))
					type = localVariableTypes.get(Parts[0]);
				if (type.equalsIgnoreCase("float"))
					if (checkFloat(Parts[1]))
						localVariableValues.put(Parts[0], Parts[1]);
					else if (type.equalsIgnoreCase("integer"))
						if (checkInt(Parts[1]))
							localVariableValues.put(Parts[0], Parts[1]);
						else if (type.equalsIgnoreCase("String"))
							if (checkString(Parts[1])) {
								String disp = getString(Parts[1]);
								localVariableValues.put(Parts[0], disp);
							}
				/*
				 * keywords
				 * 
				 * program/end/decs/begin/stop
				 * 
				 * control
				 * 
				 * 
				 * 
				 * while(Boolean)[...]
				 */

				// Boolean ^,|,~

				/*
				 * Arithmetic =,+,-,/,*
				 */

				/*
				 * if(boolean)then[...]
				 * 
				 * if(boolean)then[...]else[...]
				 */

			case "Arith":
				String parts[] = s.split("=");
				// parts[1]=PlaceVariableValues(parts[1], localVariableTypes,
				// localVariableValues);
				float ans = EvaluateExpression(parts[1]);
				AssignAnswer(ans, localVariableTypes, localVariableValues,
						parts[0]);
				break;

			/*
			 * Comparison :eq:,:ne:,:gt:,:lt:,:ge:,:le:
			 */

			case "Logic":
				s = PlaceVariableValues(s, localVariableTypes,
						localVariableValues);
				String result = EvaluateLogicalExpression(s);
				break;

			/*
			 * input variable
			 */
			case "input":
				s = s.substring(5);
				AcceptUserInput(s, localVariableTypes, localVariableValues);
				break;

			case "print":
				s = s.substring(5);
				if (s.equalsIgnoreCase("cr"))
					System.out.println("\n");
				else if (s.charAt(0) == '\"'
						&& s.charAt(s.length() - 1) == '\"')
					System.out.println(s.substring(1, s.length() - 2));
				else if (localVariableValues.containsKey(s))
					System.out.println(localVariableValues.get(s));
				else
					System.out.println("Error in parsing string at "
							+ instrList.lineNo);
				break;

			}
			instrList = instrList.nextInstruction;
		}
		return null;
	}

	private boolean checkString(String string) {
		// TODO Auto-generated method stub
		if (string.charAt(0) == '\"'
				&& string.charAt(string.length() - 1) == '\"')
			return true;
		else
			return false;
	}

	private String getString(String string) {
		// TODO Auto-generated method stub
		if (checkString(string))
			return string.substring(1, string.length() - 2);
		else
			return "Error invalid String";
	}

	private void AcceptUserInput(String string,
			Map<String, String> localVariableTypes,
			Map<String, String> localVariableValues) {
		// TODO Auto-generated method stub
		String type = localVariableTypes.get(string);
		Scanner in = new Scanner(new InputStreamReader(System.in));
		String val = in.nextLine();

		if (type.equalsIgnoreCase("integer"))
			if (checkInt(val))
				localVariableValues.put(string, val);
			else
				System.out.println("Error Data cannot be cast to operand type");
		if (type.equalsIgnoreCase("float"))
			if (checkFloat(val))
				localVariableValues.put(string, val);
			else
				System.out.println("Error Data cannot be cast to operand type");
		if (type.equalsIgnoreCase("string"))
			localVariableValues.put(string, val);
	}

	private String getChoice(String s) {
		// TODO Auto-generated method stub
		if (s.contains("+") || s.contains("-") || s.contains("/")
				|| s.contains("*"))
			return "Arith";// All Arithmetic operations grouped in one case:
		if ((s.contains(":le:")) || (s.contains(":ge:"))
				|| (s.contains(":lt:")) || (s.contains(":gt:"))
				|| (s.contains(":ne:")) || (s.contains(":eq:")))
			return "Logic"; // All Boolean and Logical Operations together
		if (s.contains("print"))
			return "print"; // All Print Operations
		if (s.contains("input"))
			return "input"; // All User Inputs///
		if (s.contains("="))
			return "assignment";
		return null;
	}

	private void AssignAnswer(float answer,
			Map<String, String> localVariableTypes,
			Map<String, String> localVariableValues, String s) {
		// TODO Auto-generated method stub
		
		if (localVariableTypes.get(s) == null)
			System.out.println("Data Assigned to invalid operand");

		if (localVariableTypes.get(s).equalsIgnoreCase("float"))
			localVariableValues.put(s, (answer + ""));
		
		if (localVariableTypes.get(s).equalsIgnoreCase("integer")) {
			int ans = (int) answer;
			localVariableValues.put(s, (ans + ""));
		}
	}

	private String EvaluateLogicalExpression(String s) {
		float val = 0;
		int index = 0;

		return "false";

	}

	// 1-2+3*4/6
	/*	
	*//**
	 * Returns the last position of c outside of all brackets in the string
	 * s.
	 * 
	 * @param s
	 *            the string in which is searched for
	 * @param c
	 *            the character to find
	 * @return the index of the first occurrence of the character
	 */
	private static int findIndex(String s, char c) {
		int count = 0;
		// searching the string from back to front
		for (int i = s.length() - 1; i >= 0; i--) {
			if (s.charAt(i) == '(')
				count++; // count openening brackets
			if (s.charAt(i) == ')')
				count--; // count closing brackets
			// count = 0 means we are outside all brackets
			if (s.charAt(i) == c && count == 0)
				// found the rightmost occurence outside all brackets
				return i;
		}
		return -1;
	}

	private static float EvaluateExpression(String s) {

		// TODO Auto-generated method stub
		float val = 0;
		int index = 0;

		if (RudiExecutor.localVarTypes.containsKey(s))
			if ((RudiExecutor.localVarTypes.get(s).equalsIgnoreCase("float") || RudiExecutor.localVarTypes
					.get(s).equalsIgnoreCase("integer")))
				return Float.parseFloat(localVarValues.get(s));

		if ((index = (findIndex(s, '-'))) >= 0) {
			return (EvaluateExpression(s.substring(0, index)) - EvaluateExpression(s
					.substring(index + 1, s.length())));
		} else if ((index = (findIndex(s, '+'))) >= 0) {
			return (EvaluateExpression(s.substring(0, index)) + EvaluateExpression(s
					.substring(index + 1, s.length())));
		} else if ((index = (findIndex(s, '*'))) >= 0) {
			return (EvaluateExpression(s.substring(0, index)) * EvaluateExpression(s
					.substring(index + 1, s.length())));
		} else if ((index = (findIndex(s, '/'))) >= 0
				&& EvaluateExpression(s.substring(index + 1, s.length())) > 0f) {
			return (EvaluateExpression(s.substring(0, index)) / EvaluateExpression(s
					.substring(index + 1, s.length())));
		}

		if (s.charAt(0) == '(') {
			if (s.charAt(s.length() - 1) == ')')
				return (EvaluateExpression(s.substring(1, s.length() - 1)));
			// else
			// throw new ParserException("Invalid brackets: " + s);
		}

		// now finally convert the string (that hopefully consists of number) to
		// a double type.
		try {
			return Float.parseFloat(s);
		} catch (NumberFormatException ex) {
			// throw new ParserException("String to number parsing exception: "
			// + s);
		}
		return val;
	}

	private static String PlaceVariableValues(String s,
			Map<String, String> localVariableTypes,
			Map<String, String> localVariableValues) {
		// TODO Auto-generated method stub

		String valSplit[] = s.split("");
		String type, value;

		for (String ch : valSplit) {
			if (localVariableTypes.containsKey(ch)) {
				type = localVariableTypes.get(ch);
				value = localVariableValues.get(ch);
				s.replace(ch, value);
			}
		}

		return s;
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

	public static void main(String args[]) {
		String s = "(1-2)+3*(4/8)";
		Float result = 0f;
		result = EvaluateExpression(s);
		System.out.println("Answer is: " + result);
	}

}
