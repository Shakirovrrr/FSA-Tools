import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FSARegexMaker {
	private List<FSAState> states, finalStates;
	private FSAState initialState;

	public FSARegexMaker(FiniteStateAutomata automata) {
		this.states = new ArrayList<>(automata.getStates());
		this.initialState = automata.getInitialState();
		this.finalStates = new ArrayList<>(automata.getFinalStates());
	}

	public static String makeRegex(FiniteStateAutomata automata) {
		FSARegexMaker maker = new FSARegexMaker(automata);
		return maker.makeRegex();
	}

	public String makeRegex() {
		if (finalStates.isEmpty()) {
			return "{}";
		}

		int k = states.size() - 1;
		int i = states.indexOf(initialState);
		int j = states.indexOf(finalStates.get(0));
		StringBuilder rule = updateRule(k, i, j);

		return rule.toString();
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

		FSAState iState = states.get(i);
		FSAState jState = states.get(j);

		for (Map.Entry<String, FSAState> entry : iState.getTransitions().entrySet()) {
			if (entry.getValue().equals(jState)) {
				builder.append(entry.getKey());
				builder.append('|');
			}
		}
		if (builder.length() > 0) {
			builder.deleteCharAt(builder.length() - 1);
		}

		if (i == j) {
			if (builder.length() > 0) {
				builder.append("|eps");
			} else {
				builder.append("eps");
			}
		}

		if (builder.length() == 0) {
			builder.append("{}");
		}

		return builder;
	}

	private boolean updateRuleAssert(int k, int i, int j) {
		return k >= -1 && k <= states.size() &&
				i >= 0 && i <= states.size() &&
				j >= 0 && j <= states.size();
	}
}
