package teacher.dfa;

import java.io.*;

import dk.brics.automaton.*;

public class DFATeacher {
	RegExp systemRegex;
	dk.brics.automaton.Automaton systemDFA;
	public DFATeacher(){
		systemRegex = null;
		systemDFA   = null;
	}
	
	public void getSystemDesc(String Regex){
		this.systemRegex = new RegExp(Regex);
		this.systemDFA   = this.systemRegex.toAutomaton();
		this.systemDFA.removeDeadTransitions();
	}
	
	public String answerMembershipQuery(String runStr){
		assert this.systemRegex != null;
		Boolean accepted = this.systemDFA.run(runStr);
		if(accepted){
			return "1";
		}
		else{
			return "0";
		}
	}
	public String answerMembershipQueryManul(String runStr){
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Is run " + runStr + " in the unknown language: 1/0");
		String result = null;
		try {
			while(true){
				result = reader.readLine();
				if(result.equals("0") || result.equals("1")){
					break;
				}
				else{
					System.out.println("Illegal output try again");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public String provideAcceptedString(){
		assert this.systemRegex != null;
		String accRun = this.systemDFA.getShortestExample(true);
		return accRun;
	}
	
	public String provideAcceptedStringManul(){
		String accRun = null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter a word in the unknown language: ");
		try {
			accRun = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return accRun;
	}

}
