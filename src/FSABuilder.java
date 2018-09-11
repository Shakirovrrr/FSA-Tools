import java.util.LinkedList;
import java.util.List;

public class FSABuilder {
	private List<String> states;
	private List<String> alphabet;
	private List<Transition> transitions;
	private List<String> finalStates;
	private String initialState;
	//private List<Exception> errors;

	public FSABuilder() {
		states = new LinkedList<>();
		alphabet = new LinkedList<>();
		transitions = new LinkedList<>();
		finalStates = new LinkedList<>();
	}

	public FSABuilder addState(String name) {
		states.add(name);
		return this;
	}

	public FSABuilder addAlpha(String alpha) {
		alphabet.add(alpha);
		return this;
	}

	public FSABuilder addTransition(String name, String from, String to) {
		transitions.add(new Transition(name, from, to));
		return this;
	}

	public FSABuilder addFinalState(String name) {
		finalStates.add(name);
		return this;
	}

	public FSABuilder setInitialState(String name) {
		initialState = name;
		return this;
	}

	public FSABuildResult build() {
		FiniteStateAutomata automata;
		LinkedList<String> errors = new LinkedList<>();
		boolean success = false;



		// TODO Implement builder
		return null;
	}

	private LinkedList<State> compileStates(){
		LinkedList<State> result = new LinkedList<>();
		for (String state : states) {
			// TODO States compiler
		}

		return result;
	}

	private class Transition {
		String name, from, to;

		Transition(String name, String form, String to) {
			this.name = name;
			this.from = form;
			this.to = to;
		}
	}
}
