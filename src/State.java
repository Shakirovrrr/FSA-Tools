import java.util.Map;

public class State {
	private String name;
	private Map<String, State> transitions;
	private boolean isFinal;

	public State(String name, boolean isFinal, Map<String, State> transitions) {
		this.name = name;
		this.isFinal = isFinal;
		this.transitions = transitions;
	}

	public String getName() {
		return name;
	}

	public Map<String, State> getTransitions() {
		return transitions;
	}

	public boolean isFinal() {
		return isFinal;
	}
}
