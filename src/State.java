public class State {
	private String name;
	private State[] transitions;

	public State(String name, State[] transitions) {
		this.name = name;
		this.transitions = transitions;
	}

	public String getName() {
		return name;
	}

	public State[] getTransitions() {
		return transitions;
	}
}
