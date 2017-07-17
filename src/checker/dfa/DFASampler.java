package checker.dfa;
import java.util.Random;

import dk.brics.automaton.*;
import cn.ac.ios.automata.words.Alphabet;

public class DFASampler {
	private static double getNumSamples(double epsilon, double delta, int numEq) {
		double result = (Math.log(1.0/ (1 - delta)) + Math.log(2) * numEq) / epsilon;
		double num = Math.round(result);
		return num;
	}
	
	private static int getNextIndex(int sizeOfAlphabet){
		Random random = new Random();
		return random.nextInt(sizeOfAlphabet);
		
	}
	
	private static String getLetter(Alphabet alphabet){
		 return alphabet.get(getNextIndex(alphabet.size())).getObject();
		 
	}
	
	//get the number of samples 
	public static int getIterateNum(double epsilon, double delta, int numEq){
		int result = (int) Math.ceil(DFASampler.getNumSamples(epsilon,delta,numEq));
		return result;
	}
	
	private static Boolean acceptHasOutDegree(State acceptingState, Alphabet alphabet){
		assert acceptingState.isAccept();
		int alphabetSize = alphabet.size();
		for(int i = 0; i < alphabetSize; i++){
			String letterStr = alphabet.get(i).getObject();
			char[] charArray = letterStr.toCharArray();
			char chara = charArray[0];
			State tempState  = acceptingState.step(chara);
			if(tempState != null){
				return true;
			}
		}
		return false;
	}
	
	public static String getPath(dk.brics.automaton.Automaton automaton, Alphabet alphabet){
		String path = "";
		State currentState = automaton.getInitialState();
		//find a random path to accepting state 
		while(true){
			String letter =  DFASampler.getLetter(alphabet);
			char[] charArray = letter.toCharArray();
			char chara = charArray[0];
			State tempState = currentState.step(chara);
			if(tempState != null){
				if(tempState.isAccept()){
					path = path + chara;
					currentState = tempState;
					break;
				}
				else{
					path = path + chara;
					currentState = tempState;
				}
			}
		}
		//randomly get remnant path after first reaching to the accepting state
		if(DFASampler.acceptHasOutDegree(currentState, alphabet)){
			Random random = new Random();
			if(random.nextBoolean()){
				while(true){
					String letter = DFASampler.getLetter(alphabet);
					char[] charArray = letter.toCharArray();
					char chara = charArray[0];
					State tempState = currentState.step(chara);
					if(tempState != null){
						if(tempState.isAccept()){
							path = path + chara;
							currentState = tempState;
							if(random.nextBoolean()){
								break;
							}
						}
						else{
							path = path + chara;
							currentState = tempState;
						}
					}
				}
			}
		}
		return path;
	}
	
	
}
