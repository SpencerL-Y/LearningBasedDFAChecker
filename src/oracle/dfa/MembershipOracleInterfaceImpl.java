package oracle.dfa;

import cn.ac.ios.automata.words.Word;
import cn.ac.ios.query.MembershipOracle;
import cn.ac.ios.query.Query;
import teacher.dfa.*;

public class MembershipOracleInterfaceImpl implements MembershipOracle<Boolean>{

		DFATeacher teacher;
		
		public MembershipOracleInterfaceImpl(DFATeacher teacherRef){
			this.teacher = teacherRef;
		}
		
		@Override
		public Boolean answerMembershipQuery(Query<Boolean> query) {
			Word queriedWord = query.getQueriedWord();
			String runStr = queriedWord.toStringWithAlphabet();
			boolean answer = false;
			//TODO: interface will be added later
			String result = this.teacher.answerMembershipQuery(runStr);
			if(result.equals("0")){
				answer = false;
			}
			else if(result.equals("1")){
				answer = true;
			}
			else{
				;
			}
			
			query.answerQuery(answer);
			return answer;
		}


}
