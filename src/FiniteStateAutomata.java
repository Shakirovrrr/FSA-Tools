import java.util.Collection;

public class FiniteStateAutomata {
	private Collection<State> states;
	private Collection<String> alphabet;
	private State initialState;

	public FiniteStateAutomata(Collection<State> states, Collection<String> alphabet, State initialState) {
		this.states = states;
		this.alphabet = alphabet;
		this.initialState = initialState;
	}

	public void compile() {
	}
}
