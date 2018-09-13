import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class FSABuilder {
	private List<String> states, alphabet;
	private List<Transition> transitions;
	private List<String> finalStates;
	private String initialState;

	private List<FSABuildResult.FSAError> errors;
	private HashMap<String, State> stateCache;

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
		errors = new LinkedList<>();
		boolean success = false;

		// Check for E4
		if (initialState == null) {
			errors.add(FSABuildResult.FSAError.E4);
			return new FSABuildResult(false, errors, null);
		}

		// Check for W1
		if (finalStates.size() == 0) {
			errors.add(FSABuildResult.FSAError.W1);
		}


		// TODO Implement builder

		return null;
	}

	private LinkedList<State> compileTransitions() throws FSAException {
		stateCache = new HashMap<>();

		for (Transition transition : transitions) {
			// Check for E1
			if (!states.contains(transition.from) ||
					!states.contains(transition.to)) {
				throw new FSAException(FSABuildResult.FSAError.E1);
			}

			// Check for E3
			if (!alphabet.contains(transition.name)) {
				throw new FSAException(FSABuildResult.FSAError.E3);
			}

			State from = pickState(transition.from);
			State to = pickState(transition.to);

			// Check for W3
			if (from.getTransitions().containsKey(transition.name)){
				// FIXME Is it error?
				throw new FSAException(FSABuildResult.FSAError.W3);
			}
			from.getTransitions().put(transition.name, to);
		}

		return new LinkedList<>(stateCache.values());
	}

	private State pickState(String name) {
		State state;

		if (stateCache.containsKey(name)) {
			state = stateCache.get(name);
		} else {
			boolean isFinal = finalStates.contains(name);
			state = new State(name, isFinal, new HashMap<>());

			stateCache.put(name, state);
		}

		return state;
	}

	private class Transition {
		String name, from, to;

		Transition(String name, String form, String to) {
			this.name = name;
			this.from = form;
			this.to = to;
		}
	}

	private class FSAException extends Exception {
		FSABuildResult.FSAError reason;

		FSAException(FSABuildResult.FSAError reason) {
			this.reason = reason;
		}
	}
}
