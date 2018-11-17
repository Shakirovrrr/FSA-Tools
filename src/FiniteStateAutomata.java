import java.util.Collection;
import java.util.HashSet;

public class FiniteStateAutomata {
	private Collection<FSAState> states;
	private Collection<String> alphabet;
	private FSAState initialState;
	private Collection<FSAState> finalStates;

	private boolean finalStatesCollected;

	public FiniteStateAutomata(Collection<FSAState> states, Collection<String> alphabet, FSAState initialState) {
		this.states = states;
		this.alphabet = alphabet;
		this.initialState = initialState;

		finalStatesCollected = false;
	}

	public Collection<FSAState> getStates() {
		return states;
	}

	public Collection<String> getAlphabet() {
		return alphabet;
	}

	public FSAState getInitialState() {
		return initialState;
	}

	public Collection<FSAState> getFinalStates() {
		if (!finalStatesCollected) {
			collectFinalStates();
		}

		return finalStates;
	}

	public void compile() {
	}

	private void collectFinalStates() {
		finalStates = new HashSet<>();
		for (FSAState state : states) {
			if (state.isFinal()) {
				finalStates.add(state);
			}
		}
		finalStatesCollected = true;
	}
}
