import java.util.Collection;
import java.util.HashSet;

public class FiniteStateAutomata {
	private Collection<State> states;
	private Collection<String> alphabet;
	private State initialState;
	private Collection<State> finalStates;

	private boolean finalStatesCollected;

	public FiniteStateAutomata(Collection<State> states, Collection<String> alphabet, State initialState) {
		this.states = states;
		this.alphabet = alphabet;
		this.initialState = initialState;

		finalStatesCollected = false;
	}

	public Collection<State> getStates() {
		return states;
	}

	public Collection<String> getAlphabet() {
		return alphabet;
	}

	public State getInitialState() {
		return initialState;
	}

	public Collection<State> getFinalStates() {
		if (!finalStatesCollected) {
			collectFinalStates();
		}

		return finalStates;
	}

	public void compile() {
	}

	private void collectFinalStates() {
		finalStates = new HashSet<>();
		for (State state : states) {
			if (state.isFinal()) {
				finalStates.add(state);
			}
		}
		finalStatesCollected = true;
	}
}
