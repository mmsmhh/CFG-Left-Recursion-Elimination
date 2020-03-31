import java.util.ArrayList;

public class CFG_Left_Recursion_Elimination {


	static class Rule {

		static String dash = "'";

		String head;
		String[] tail;
		String[] tailDash;

		public Rule(String rule) {

			String[] parts = rule.split(",");

			this.head = parts[0];

			this.tail = new String[parts.length - 1];

			for (int i = 0; i < tail.length; i++) {
				tail[i] = parts[i + 1];
			}
		}

		public void substitute(Rule rule) {

			ArrayList<String> newTail = new ArrayList<String>();

			for (int i = 0; i < tail.length; i++) {

				String currentRuleTail = tail[i];

				if (Character.isUpperCase(currentRuleTail.charAt(0))
						&& rule.head.equals(String.valueOf(currentRuleTail.charAt(0)))) {

					String[] otherRuleTail = rule.tail;

					String withoutFirstCharacter = currentRuleTail.substring(1);

					for (int j = 0; j < otherRuleTail.length; j++) {
						newTail.add(otherRuleTail[j] + withoutFirstCharacter);
					}

				} else {
					newTail.add(tail[i]);
				}

			}

			tail = new String[newTail.size()];

			for (int i = 0; i < tail.length; i++) {
				tail[i] = newTail.get(i);
			}

		}

		public void eliminateLeftRecursion() {

			ArrayList<String> alfa = new ArrayList<String>();
			ArrayList<String> beta = new ArrayList<String>();

			boolean isLeft = false;

			for (int i = 0; i < tail.length; i++) {
				if (head.contentEquals(String.valueOf(tail[i].charAt(0)))) {
					alfa.add(tail[i].substring(1));
					isLeft = true;
				} else {
					beta.add(tail[i]);
				}
			}

			if (!isLeft)
				return;

			ArrayList<String> newTail = new ArrayList<String>();

			ArrayList<String> newTailDash = new ArrayList<String>();

			for (int i = 0; i < beta.size(); i++) {
				newTail.add(beta.get(i) + head + dash);
			}

			for (int i = 0; i < alfa.size(); i++) {
				newTailDash.add(alfa.get(i) + head + dash);
			}

			newTailDash.add("");

			tail = new String[newTail.size()];

			for (int i = 0; i < tail.length; i++) {
				tail[i] = newTail.get(i);
			}

			tailDash = new String[newTailDash.size()];

			for (int i = 0; i < tailDash.length; i++) {
				tailDash[i] = newTailDash.get(i);
			}

		}
	}

	private static String LRE(String input) {

		String[] allRules = input.split(";");

		Rule[] rules = new Rule[allRules.length];

		for (int i = 0; i < rules.length; i++) {
			rules[i] = new Rule(allRules[i]);
		}

		for (int i = 0; i < rules.length; i++) {
			for (int j = 0; j < i; j++) {
				rules[i].substitute(rules[j]);
			}

			rules[i].eliminateLeftRecursion();
		}

		String result = "";

		for (int i = 0; i < rules.length; i++) {
			Rule r = rules[i];
			result += r.head;
			for (int j = 0; j < r.tail.length; j++) {
				result += "," + r.tail[j];
			}

			result += ";";

			if (r.tailDash != null) {
				result += r.head + r.dash;
				for (int j = 0; j < r.tailDash.length; j++) {
					result += "," + r.tailDash[j];
				}

				result += ";";

			}

		}

		return result.substring(0, result.length() - 1);
	}

	public static void main(String[] args) {
		String inputString = "S,SuS,SS,Ss,lSr,";

		String[] problem = { "S,ScT,T;T,aSb,iaLb,i;L,SdL,S", "S,Sa,b", "S,Sab,cd", "S,SuS,SS,Ss,lSr,a",
				"S,SuT,T;T,TF,F;F,Fs,P;P,a,b", "S,z,To;T,o,Sz", "S,lLr,a;L,LbS,S", "S,BC,C;B,Bb,b;C,SC,a" };

		String[] answer = { "S,TS';S',cTS',;T,aSb,iaLb,i;L,aSbS'dL,iaLbS'dL,iS'dL,aSbS',iaLbS',iS'", "S,bS';S',aS',",
				"S,cdS';S',abS',", "S,lSrS',aS';S',uSS',SS',sS',", "S,TS';S',uTS',;T,FT';T',FT',;F,PF';F',sF',;P,a,b",
				"S,z,To;T,oT',zzT';T',ozT',", "S,lLr,a;L,lLrL',aL';L',bSL',",
				"S,BC,C;B,bB';B',bB',;C,bB'CCC',aC';C',CC'," };

		for (int i = 0; i < answer.length; i++) {

			String output = LRE(problem[i]);
			System.out.println(answer[i].equals(output));

		}

	}

}
