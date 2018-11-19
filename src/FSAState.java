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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(name);
		builder.append(": ");
		builder.append(isFinal ? "FINAL" : "NOT FINAL");
		builder.append(", TRANSITIONS: [");
		for (Map.Entry<String, FSAState> entry : transitions.entrySet()) {
			builder.append(entry.getKey());
			builder.append("->");
			builder.append(entry.getValue().getName());
			builder.append(", ");
		}
		builder.deleteCharAt(builder.length() - 1);
		builder.deleteCharAt(builder.length() - 1);
		builder.append(']');

		return builder.toString();
	}
}
