package checker.dfa;

import checker.dfa.DFAChecker;
import cn.ac.ios.automata.Automaton;
import cn.ac.ios.automata.words.Alphabet;
import cn.ac.ios.automata.words.*;
import cn.ac.ios.learner.Learner;
import cn.ac.ios.learner.lstar.LearnerLStar;
import cn.ac.ios.query.MembershipOracle;
import cn.ac.ios.value.Type;
import cn.ac.ios.value.Value;
import cn.ac.ios.value.*;
import oracle.dfa.MembershipOracleInterfaceImpl;
import teacher.dfa.DFATeacher;

public class DFACheckerTest {
	public static void main(String[] args) {

		ValueManager contextValue = new ValueManager();
		WordManager contextWord = new WordManager(contextValue);
		Type typeObject = contextValue.newTypeObject(String.class);
		contextValue.newTypeLetter(String.class);
		Value valueLetter = typeObject.newValue();
		Alphabet alphabet = contextWord.getAlphabet();

		valueLetter.set("a");
		alphabet.add(valueLetter.clone());
		valueLetter.set("b");
		alphabet.add(valueLetter.clone());
		alphabet.setImmutable();
		valueLetter.set("");
		contextWord.setLetterSplitter(valueLetter);
		DFATeacher teacher = new DFATeacher();
		teacher.getSystemDesc("abb*");
		MembershipOracle<Boolean> membershipOracle = new MembershipOracleInterfaceImpl(teacher);
		Learner<Automaton, Boolean> lstar = new LearnerLStar(contextWord, membershipOracle);
		System.out.println("starting learning");
		lstar.startLearning();
		
		DFAChecker checker = new DFAChecker();
		
		checker.getSpecification("bb*");
		checker.check(lstar, teacher, contextWord);
		/*
		 * //Regular expression of property RegExp spec = new RegExp("ab(aa)*");
		 * Boolean flag = false; while(!flag){ Automaton temp =
		 * lstar.getHypothesis(); //DFA for specification
		 * dk.brics.automaton.Automaton specDFA = spec.toAutomaton();
		 * dk.brics.automaton.Automaton negSpecDFA = specDFA.complement();
		 * dk.brics.automaton.Automaton resultDFA =
		 * UtilAutomaton.convertToDkAutomaton(temp);
		 * dk.brics.automaton.Automaton intersectDFA =
		 * BasicOperations.intersection(negSpecDFA, resultDFA);
		 * 
		 * if(intersectDFA.isEmpty() ){
		 * System.out.println("Conformance Test: "); //conformance test
		 * resultDFA.removeDeadTransitions();
		 * 
		 * Boolean noCE = true; Boolean meet = false; for(int i = 0; i <
		 * DFASampler.getIterateNum(1.0, 0.999, 1) && noCE; i++){
		 * if(resultDFA.isEmpty()){ if(negSpecDFA.isTotal()){ meet = true;
		 * break; } else{
		 * System.out.println("Enter a word in the unknown language");
		 * BufferedReader reader = new BufferedReader(new
		 * InputStreamReader(System.in)); String refineStr; try { refineStr =
		 * reader.readLine(); Word refineWord =
		 * contextWord.getWordFromString(refineStr); lstar.refineHypothesis(new
		 * QuerySimple<Boolean>(refineWord, contextWord.getEmptyWord())); break;
		 * } catch (IOException e) { e.printStackTrace(); } meet = false;
		 * 
		 * } } String equiStr = DFASampler.getPath(resultDFA, alphabet);
		 * System.out.println("Interface: Is " + equiStr +
		 * " in the unknown language"); BufferedReader reader = new
		 * BufferedReader(new InputStreamReader(System.in)); String input; try {
		 * Boolean finish = false; while(!finish){ input = reader.readLine();
		 * 
		 * if(input.equals("1")){ finish = true; meet = true; } else
		 * if(input.equals("0")){ finish = true; noCE = false; Word EquiCE =
		 * contextWord.getWordFromString(equiStr);
		 * 
		 * lstar.refineHypothesis(new QuerySimple<Boolean>(EquiCE,
		 * contextWord.getEmptyWord())); meet = false;
		 * 
		 * } else{ System.out.println("Illegal input try again"); } }
		 * 
		 * } catch (IOException e) { e.printStackTrace(); }
		 * 
		 * } if(meet){ System.out.println("Specification meets"); flag = true; }
		 * 
		 * } else { //find a counterexample from intersection automaton String
		 * possibleCE = BasicOperations.getShortestExample(intersectDFA, true);
		 * System.out.println("Interface: Is " + possibleCE +
		 * " in the unknown language: 1/0 "); BufferedReader reader = new
		 * BufferedReader(new InputStreamReader(System.in)); String input; try {
		 * Boolean finish = false; while(!finish){ input = reader.readLine();
		 * if(input.equals("1")){ finish = true; System.out.println("Run \"" +
		 * possibleCE + "\" violates the specification."); flag = true;
		 * 
		 * } else if(input.equals("0")){ finish = true; Word specCE =
		 * contextWord.getWordFromString(possibleCE); lstar.refineHypothesis(new
		 * QuerySimple<Boolean>(specCE, contextWord.getEmptyWord())); } else{
		 * System.out.println("Illegal input try again"); } } } catch
		 * (IOException e) { e.printStackTrace(); } } }
		 */
	}
}
