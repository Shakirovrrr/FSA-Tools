package FSATools.Foundation;

import java.util.Map;

public class FSAState {
	private String name;
	private Map<String, FSAState> transitions;
	private boolean isFinal;

	public FSAState(String name, boolean isFinal, Map<String, FSAState> transitions) {
		this.name = name;
		this.isFinal = isFinal;
		this.transitions = transitions;
	}

	public String getName() {
		return name;
	}

	public Map<String, FSAState> getTransitions() {
		return transitions;
	}

	public boolean isFinal() {
		return isFinal;
	}
}
