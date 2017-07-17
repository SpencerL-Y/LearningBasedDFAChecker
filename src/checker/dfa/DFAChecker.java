package checker.dfa;

import dk.brics.automaton.BasicOperations;
import dk.brics.automaton.RegExp;


import cn.ac.ios.automata.Automaton;
import cn.ac.ios.automata.words.Word;
import cn.ac.ios.util.UtilAutomaton;
import cn.ac.ios.learner.Learner;
import cn.ac.ios.query.QuerySimple;
import cn.ac.ios.automata.words.*;
import teacher.dfa.*;


public class DFAChecker {
	RegExp spec;
	dk.brics.automaton.Automaton resultDFA;
	dk.brics.automaton.Automaton intersectDFA;
	dk.brics.automaton.Automaton negSpecDFA;
	
	public DFAChecker(){
		spec         = null;
		resultDFA    = null;
		intersectDFA = null;
		negSpecDFA   = null;
	}
	
	public void getSpecification(String Regex){
		this.spec = new RegExp(Regex);
	}
	
	private void intersection(Automaton learnedAutomaton){
		assert this.spec != null;
		this.resultDFA    = UtilAutomaton.convertToDkAutomaton(learnedAutomaton);
		this.negSpecDFA   = this.spec.toAutomaton().complement();
		this.intersectDFA = BasicOperations.intersection(this.negSpecDFA,
													     this.resultDFA);
	}
	
	//check spec in the system
	public void check(Learner<Automaton, Boolean> lstar, DFATeacher teacher, WordManager contextWord){
		Boolean flag = false;
		while(!flag){
			this.intersection(lstar.getHypothesis());
			if(this.intersectDFA.isEmpty()){
				String refineStr = this.comformanceTest(lstar, teacher, contextWord);
				if(refineStr != null){
					Word refineWord = contextWord.getWordFromString(refineStr);
					lstar.refineHypothesis(new QuerySimple<Boolean>(refineWord, 
																	contextWord.getEmptyWord()));
				}
				else {
					System.out.println("Property satisfied");
					flag = true;
				}
			}
			else {
				String refineStr = violationTest(lstar, teacher);
				if(refineStr != null){
					Word specCE = contextWord.getWordFromString(refineStr);
					lstar.refineHypothesis(new QuerySimple<Boolean>(specCE, contextWord.getEmptyWord()));
				}
				else{
					return;
				}
			}
		}
	}

	//return the counterexample string or null
	private String comformanceTest(Learner<Automaton, Boolean> lstar, DFATeacher teacher, WordManager contextWord){
		this.resultDFA.removeDeadTransitions();
		for(int i = 0; i < DFASampler.getIterateNum(0.1, 0.999, 1); i++){
			if(this.resultDFA.isEmpty()){
				if(this.negSpecDFA.isTotal()){
					break;
				}
				else{
					//TODO: interface will be added later
					String acceptedStr =  teacher.provideAcceptedString();
					return acceptedStr;
				}
			}
			assert !this.resultDFA.isEmpty();
			String equiStr = DFASampler.getPath(this.resultDFA, lstar.getHypothesis().getAlphabet());
			//TODO: interface will be added later
			String membershipResult = teacher.answerMembershipQuery(equiStr);
			if(membershipResult.equals("0")){
				return membershipResult;
			}
			else if(membershipResult.equals("1")){
				;
			}
			else{
				System.out.println("ERROR in DFAChecker");
			}
			
		}
		return null;
	}
	
	private String violationTest(Learner<Automaton, Boolean> lstar, DFATeacher teacher){
		String refineStr = BasicOperations.getShortestExample(this.intersectDFA, true);
		//TODO: interface will be added later
		String membershipResult = teacher.answerMembershipQuery(refineStr);
		if(membershipResult.equals("1")){
			System.out.println("Run " + refineStr + " violates the specification");
		}
		else if(membershipResult.equals("0")){
			return refineStr;
		}
		else{
			System.out.println("ERROR in DFAChecker");
		}
	return null;
	}
}
