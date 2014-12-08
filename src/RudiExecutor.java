import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class RudiExecutor {
	private Map<String, String> localVarTypes;
	private Map<String, String> localVarValues;
	private int bracketStatus = 0;
	private HashMap<String, RudiProgram> allPrograms;
	private String[] keyWords = { "=", "*", "+", "-", ":eq:", ":ne:", ":gt:",
			":ge:", ":le:", ":lt:", "^", "|", "~", "" };

	public RudiExecutor(HashMap<String, RudiProgram> programs) {
		this.allPrograms = programs;
	}

	public String execute(Map<String, String> localVariableTypes,
			Map<String, String> localVariableValues, InstructionList inList)
			throws SyntaxErrorException {

		this.localVarTypes = localVariableTypes;
		this.localVarValues = localVariableValues;

		String vals[], choice;
		boolean bool = false;

		InstructionList.InstructionNode instrList = inList.listHead;

		while (instrList != null) {
			try {
				String s = instrList.instruction;
				choice = getChoice(instrList.instruction);
				String type = "";
				String answer;
				switch (choice) {

				case "assignment":

					String Parts[] = s.split("=");
					if (localVarTypes.containsKey(Parts[0])) {
						type = localVarTypes.get(Parts[0]);
						if (type.equalsIgnoreCase("float")) {
							Parts[1]= PlaceVariableValues(Parts[1]);
							if (checkFloat(Parts[1]))
								localVarValues.put(Parts[0], Parts[1]);
						} else if (type.equalsIgnoreCase("integer")) {
							Parts[1]= PlaceVariableValues(Parts[1]);
							if (checkInt(Parts[1]))
								localVarValues.put(Parts[0], Parts[1]);
						} else if (type.equalsIgnoreCase("String")) {
							Parts[1]=PlaceVariableValues(Parts[1]);
							if (checkString(Parts[1])) {
								String disp = getString(Parts[1]);
								localVarValues.put(Parts[0], disp);
							}
						} else {
							throw new SyntaxErrorException(s, instrList.lineNo);
						}
					}

					break;

				case "subroutine":
					String val[] = s.split("\\(");
					RudiProgram subRoutine = null;

					if (this.allPrograms.containsKey(val[0])) {
						subRoutine = new RudiProgram(allPrograms.get(val[0]).getName(),allPrograms.get(val[0]).getParams());
						subRoutine.setTypeMap(new HashMap<String, String>(allPrograms.get(val[0]).getTypeMap()));
						subRoutine.setValuesMap(new HashMap<String, String>(allPrograms.get(val[0]).getValuesMap()));
						subRoutine.setInstructionList(allPrograms.get(val[0]).getInstructionList());
						subRoutine.setSubroutines(new HashMap<String, RudiProgram>(allPrograms.get(val[0]).getSubroutines()));
						subRoutine.setParams(allPrograms.get(val[0]).getParams());
					}

					// extract params
					String[] params = val[1].replace(")", "").split(",");

					// check if no of params are matching in definition and call
					if (!(subRoutine.getNoOfParams() == params.length)) {
						throw new SyntaxErrorException(instrList.instruction,
								instrList.lineNo);
					}

					// put params in local map of subroutine
					int i =0;
					for (String param : params) {
						subRoutine.addToTypeMap(subRoutine.getParams()[i],
								localVarTypes.get(param));
						subRoutine.addToValueMap(subRoutine.getParams()[i], this.localVarValues.get(param));
						i++;

					}

					// execute subroutine
					subRoutine.executeInstructionList();

					String[] updatedVals = subRoutine.getParams();
					// update subroutine variables
					i =0;
					for (String param : updatedVals) {
						
						this.localVarValues.put(params[i],subRoutine.getValMap(param));
						i++;
					}

					break;

				case "Arith":
					String parts[] = s.split("=");
					float ans = EvaluateExpression(parts[1]);
					AssignAnswer(ans, parts[0]);
					break;

				/*
				 * Comparison :eq:,:ne:,:gt:,:lt:,:ge:,:le:
				 */

				case "Logic":
					s = PlaceVariableValues(s);
					boolean result = EvaluateLogicalExpression(s);
					break;

				/*
				 * input variable
				 */
				case "input":
					s = s.substring(5);
					AcceptUserInput(s);
					break;

				case "print":
					s = s.substring(5);
					if (s.equalsIgnoreCase("cr"))
						System.out.println();
					else if (s.charAt(0) == '\"'
							&& s.charAt(s.length() - 1) == '\"')
						System.out.print(s.substring(1, s.length() - 1));
					else if (this.localVarValues.containsKey(s))
						System.out.print(this.localVarValues.get(s));
					else
						throw new SyntaxErrorException(instrList.instruction,
								instrList.lineNo);
					break;
				case "while":

					InstructionList.InstructionNode endWhileNode = null;

					while (true) {

						InstructionList.InstructionNode currentInstr;

						if (execWhileCondition(s) == true) {
							currentInstr = instrList.nextInstruction;
							// list to store instructions inside if block
							InstructionList whileList = new InstructionList();
							if (!currentInstr.instruction.equalsIgnoreCase("[")) {
								// Syntax error
							}
							currentInstr = currentInstr.nextInstruction;
							int currentBracks = this.bracketStatus;
							while (!currentInstr.instruction
									.equalsIgnoreCase("]")
									|| (this.bracketStatus != currentBracks)) {

								if (currentInstr.instruction.equals("]")) {
									this.bracketStatus--;
								} else if (currentInstr.instruction.equals("[")) {
									this.bracketStatus++;
								}
								whileList.addInstruction(
										currentInstr.instruction,
										currentInstr.lineNo);
								currentInstr = currentInstr.nextInstruction;
							}
							// currentInstr = ]
							endWhileNode = currentInstr;
							execute(localVarTypes, localVarValues, whileList);

						} else {

							break;
						}
					}

					instrList = endWhileNode;
					break;

				case "if":

					InstructionList.InstructionNode iflastNode;

					s = s.replace("then", "");

					if (execIfCondition(s)) {
						InstructionList.InstructionNode currentInstr = instrList.nextInstruction;

						// list to store instructions inside if block
						InstructionList ifList = new InstructionList();
						if (!currentInstr.instruction.equals("[")) {
							// / Syntax error
						}

						// update brackets
						int currentBracks = this.bracketStatus;
						currentInstr = currentInstr.nextInstruction;
						while ((!currentInstr.instruction.equals("]"))
								|| (this.bracketStatus != currentBracks)) {

							if (currentInstr.instruction.equals("]")) {
								this.bracketStatus--;
							} else if (currentInstr.instruction.equals("[")) {
								this.bracketStatus++;
							}

							ifList.addInstruction(currentInstr.instruction,
									currentInstr.lineNo);
							currentInstr = currentInstr.nextInstruction;

						}

						execute(localVarTypes, localVarValues, ifList);

						// skip all else statements
						while (true) {
							iflastNode = currentInstr;
							currentInstr = currentInstr.nextInstruction;
							
							if(currentInstr == null){
								instrList=iflastNode;
								break;
							}
							if (currentInstr.instruction
									.equalsIgnoreCase("else")
									|| currentInstr.instruction
											.equalsIgnoreCase("else if")) {

								currentInstr = currentInstr.nextInstruction;

								if (!currentInstr.instruction.equals("[")) {
									// / Syntax error
								}
								this.bracketStatus++;

								// update brackets
								currentBracks = this.bracketStatus;

								currentInstr = currentInstr.nextInstruction;

								while ((!currentInstr.instruction.equals("]"))
										|| (this.bracketStatus != currentBracks)) {
									if (currentInstr.instruction.equals("]")) {
										this.bracketStatus--;
									} else if (currentInstr.instruction
											.equals("[")) {
										this.bracketStatus++;
									}
									currentInstr = currentInstr.nextInstruction;
								}
								this.bracketStatus--;
							} else {
								instrList = iflastNode;
								break;
							}
						}

						// currentInstr = the one after last ]
					} else {

						InstructionList.InstructionNode currentInstr;

						// if condition is not true, look for else
						InstructionList elseList = new InstructionList();
						currentInstr = instrList.nextInstruction;

						if (!currentInstr.instruction.equals("[")) {
							throw new SyntaxErrorException(
									instrList.instruction, instrList.lineNo);
						}

						this.bracketStatus++;
						int currentBracks = this.bracketStatus;
						currentInstr = currentInstr.nextInstruction;

						while ((!currentInstr.instruction.equals("]"))
								|| this.bracketStatus != currentBracks) {
							if (currentInstr.instruction.equals("]")) {
								this.bracketStatus--;
							} else if (currentInstr.instruction.equals("[")) {
								this.bracketStatus++;
							}
							currentInstr = currentInstr.nextInstruction;
						}

						this.bracketStatus--;

						// if block skipped , currentInsr = "]"
						
						if (currentInstr.nextInstruction.instruction.equalsIgnoreCase("else")) {
							
							currentInstr = currentInstr.nextInstruction;
							// current = else statement
							
							
							currentInstr = currentInstr.nextInstruction;
							// now current should be [
							
							if (!currentInstr.instruction.equals("[")) {
								throw new SyntaxErrorException(
										instrList.instruction, instrList.lineNo);
							}

							currentBracks = this.bracketStatus;
							currentInstr = currentInstr.nextInstruction;
							while ((!currentInstr.instruction.equals("]"))
									|| this.bracketStatus != currentBracks) {
								if (currentInstr.instruction.equals("]")) {
									this.bracketStatus--;
								} else if (currentInstr.instruction.equals("[")) {
									this.bracketStatus++;
								}

								elseList.addInstruction(
										currentInstr.instruction,
										currentInstr.lineNo);
								currentInstr = currentInstr.nextInstruction;
							}

							this.bracketStatus--;

							execute(localVarTypes, localVarValues, elseList);

							// currentInstr = ]
							instrList = currentInstr;
							break;

						} else {
							
							instrList = currentInstr;
							break;
						}

					}
					break;

				default:
					System.out.println("Invalid Instruction");
					throw new Exception();

				}

			} catch (Exception e) {

				e.printStackTrace();
				System.out.println("Error at line no: " + instrList.lineNo
						+ "\n" + instrList.instruction);
				throw new SyntaxErrorException(instrList.instruction,
						instrList.lineNo);

			}

			instrList = instrList.nextInstruction;

		}

		return null;
	}

	private boolean checkString(String string) {

		if (string.charAt(0) == '\"'
				&& string.charAt(string.length() - 1) == '\"')
			return true;
		else
			return false;
	}

	private String getString(String string) throws Exception {

		if (checkString(string))
			return string.substring(1, string.length() - 2);

		throw new Exception();
	}

	private void AcceptUserInput(String string) throws Exception {

		String type = localVarTypes.get(string);
		Scanner in = new Scanner(new InputStreamReader(System.in));
		String val = in.nextLine();

		if (type.equalsIgnoreCase("integer"))
			if (checkInt(val))
				localVarValues.put(string, val);
			else
				throw new Exception();
		if (type.equalsIgnoreCase("float"))
			if (checkFloat(val))
				localVarValues.put(string, val);
			else
				throw new Exception();
		if (type.equalsIgnoreCase("string"))
			localVarValues.put(string, val);
	}

	private String getChoice(String s) throws Exception {

		String paramName = null;

		if (s.contains("(")) {
			String[] ba = s.split("\\(");
			paramName = ba[0];
		}
		;

		if ((this.allPrograms!=null) && this.allPrograms.containsKey(paramName))
			return "subroutine";
		else if (s.startsWith("while"))
			return "while";
		else if (s.startsWith("if"))
			return "if";
		else if (s.contains("+") || s.contains("-") || s.contains("/")
				|| s.contains("*"))
			return "Arith";// All Arithmetic operations grouped in one case:
		else if ((s.contains(":le:")) || (s.contains(":ge:"))
				|| (s.contains(":lt:")) || (s.contains(":gt:"))
				|| (s.contains(":ne:")) || (s.contains(":eq:")))
			return "Logic"; // All Boolean and Logical Operations together
		else if (s.startsWith("print"))
			return "print"; // All Print Operations
		else if (s.startsWith("input"))
			return "input"; // All User Inputs///
		else if (s.contains("="))
			return "assignment";
		else
			System.out.println("Invalid Syntax"); // syntax error
		throw new Exception();
	}

	private void AssignAnswer(float answer, String s) throws Exception {

		if (localVarTypes.get(s) == null) {
			throw new Exception();
		} else if (localVarTypes.get(s).equalsIgnoreCase("float")) {
			localVarValues.put(s, (answer + ""));
		} else if (localVarTypes.get(s).equalsIgnoreCase("integer")) {
			int ans = (int) answer;
			localVarValues.put(s, (ans + ""));
		}
	}

	private boolean EvaluateLogicalExpression(String s) throws Exception {
		float val = 0;
		int index = 0;
		String exp = "";

		s = PlaceVariableValues(s);
		if (s.contains(":eq:") || s.contains(":ne:") || s.contains(":gt:")
				|| s.contains(":lt:") || s.contains(":le:")
				|| s.contains(":ge:")) {
			if (s.contains(":eq:"))
				exp = ":eq:";
			else if (s.contains(":ne:"))
				exp = ":ne:";
			else if (s.contains(":ge:"))
				exp = ":ge:";
			else if (s.contains(":lt:"))
				exp = ":lt:";
			else if (s.contains(":gt:"))
				exp = ":gt:";
			else if (s.contains(":le:"))
				exp = ":le:";
			else
				System.out.println("Invalid Expression");

			String parts[] = s.split(exp);
			if (checkparts(parts, exp)) {
				float a = Float.parseFloat(parts[0]);
				float b = Float.parseFloat(parts[1]);
				switch (exp) {

				case ":eq:":
					if (a == b)
						return true;
					break;
				case ":ne:":
					if (a != b)
						return true;
					break;
				case ":gt:":
					if (a > b)
						return true;
					break;
				case ":lt:":
					if (a < b)
						return true;
					break;
				case ":le:":
					if (a <= b) {
						return true;
					}

					break;
				}
			} else
				throw new Exception();
		}
		return false;
	}

	private boolean checkparts(String[] parts, String exp) {

		if (checkIntegerOrFloat(parts[0]))
			if (checkIntegerOrFloat(parts[1]))
				return true;
		return false;
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

	private float EvaluateExpression(String s) {

		float val = 0;
		int index = 0;

		if (this.localVarTypes.containsKey(s))
			if ((this.localVarTypes.get(s).equalsIgnoreCase("float") || this.localVarTypes
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
		}

		// now finally convert the string (that hopefully consists of number) to
		// a double type.
		try {
			return Float.parseFloat(s);
		} catch (NumberFormatException ex) {
		}
		return val;
	}

	private String PlaceVariableValues(String s) {

		String key,value;
		
		Iterator it = this.localVarValues.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			
			key = (String) pairs.getKey();
			value = (String) pairs.getValue();
			
			s=s.replaceAll(key, value);
			
		}

		return s;
	}

	// checks for the string to be an Integer or not
	private boolean checkInt(String s) {

		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	// checks for the string to be a Float or not.
	private boolean checkFloat(String s) {

		try {
			Float.parseFloat(s);

		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	// Checks if entered string is either integer or float.
	private boolean checkIntegerOrFloat(String s) {
		if (checkFloat(s)) {
			return true;

		} else if (checkInt(s)) {
			return true;
		} else
			return false;
	}

	private boolean execWhileCondition(String s) throws Exception {

		s = s.substring(6); // removes the while
		s = s.substring(0, s.length() - 1);
		return EvaluateLogicalExpression(s);
	}

	private boolean execIfCondition(String s) throws Exception {
		s = s.substring(3); // removes the while
		s = s.substring(0, s.length() - 1);
		return EvaluateLogicalExpression(s);
	}

}
