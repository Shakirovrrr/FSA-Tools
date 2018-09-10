import java.util.LinkedList;
import java.util.List;

public class FSABuilder {
	private List<String> states;
	private List<String> alphabet;
	private List<Transition> transitions;
	//private List<Exception> errors;

	public FSABuilder() {
		states = new LinkedList<>();
		alphabet = new LinkedList<>();
		transitions = new LinkedList<>();
	}

	public FSABuilder addState(String name) {
		states.add(name);
		return this;
	}

	public FSABuilder addAlpha(String alpha) {
		alphabet.add(alpha);
		return this;
	}

	public FSABuilder addTransition(String from, String to) {
		transitions.add(new Transition(from, to));
		return this;
	}

	private class Transition {
		String from, to;

		Transition(String form, String to) {
			this.from = form;
			this.to = to;
		}
	}
}
