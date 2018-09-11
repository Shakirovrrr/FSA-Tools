public class FiniteStateAutomata {
	private String[] alphabet;
	private State[] states;
	private State initialState;

	public FiniteStateAutomata(State[] states, String[] alphabet, State initialState) {
		this.states = states;
		this.alphabet = alphabet;
		this.initialState = initialState;
	}

	public boolean isComplete() {
		return true;
	}
}
