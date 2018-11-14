import java.util.ArrayList;
import java.util.List;

public class FSARegexMaker {
	private FiniteStateAutomata automata;
	private List<State> states;

	public FSARegexMaker(FiniteStateAutomata automata) {
		this.automata = automata;
		this.states = new ArrayList<>(automata.getStates());
	}

	public String makeRegex() {
		int k = states.size();
		int i = 0, j = states.size();
		StringBuilder rule = updateRule(k, i, j);

		return rule.toString();
	}

	public static String makeRegex(FiniteStateAutomata automata) {
		FSARegexMaker maker = new FSARegexMaker(automata);
		return maker.makeRegex();
	}

	private StringBuilder updateRule(int k, int i, int j) {
		assert updateRuleAssert(k, i, j);

		if (k == -1) {
			return initialUpdateRule(i, j);
		}

		StringBuilder builder = new StringBuilder();
		builder.append('(').append(updateRule(k - 1, i, k)).append(')')
				.append('(').append(updateRule(k - 1, k, k)).append(")*")
				.append('(').append(updateRule(k - 1, k, j)).append(")|")
				.append('(').append(updateRule(k - 1, i, j)).append(')');

		return builder;
	}

	private StringBuilder initialUpdateRule(int i, int j) {
		StringBuilder builder = new StringBuilder();
		if (i == j) {
			
		}
	}

	private boolean updateRuleAssert(int k, int i, int j) {
		return k >= -1 && k <= states.size() &&
				i >= 0 && i <= states.size() &&
				j >= 0 && j <= states.size();
	}
}
