import java.util.HashMap;
import java.util.HashSet;
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
		errors = new LinkedList<>();

		// Check for E4
		if (initialState == null) {
			return failedBuild(FSABuildResult.FSAError.E4);
		}

		// Check for W1
		if (finalStates.isEmpty()) {
			errors.add(FSABuildResult.FSAError.W1);
		}

		FiniteStateAutomata automata = null;
		LinkedList<State> compiled;
		try {
			compiled = compileTransitions();
			automata = new FiniteStateAutomata(compiled, alphabet, stateCache.get(initialState));
		} catch (FSAException e) {
			switch (e.reason) {
				case E1:
				case E3:
					return failedBuild(e.reason);
				case W3:
					// FIXME W3 as error
					return failedBuild(FSABuildResult.FSAError.W3);
			}
		}

		// Check for E2 and W2
		try {
			checkInitialSpan();
		} catch (FSAException e) {
			return failedBuild(e.reason);
		}

		return new FSABuildResult(true, errors, automata);
	}

	private FSABuildResult failedBuild(FSABuildResult.FSAError error) {
		errors.add(error);
		return new FSABuildResult(false, errors, null);
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
			if (from.getTransitions().containsKey(transition.name)) {
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

	private void checkInitialSpan() throws FSAException {
		HashSet<State> spanSet = buildSpanSet();

		if (spanSet.size() == states.size()) {
			return;
		}

		errors.add(FSABuildResult.FSAError.W2);

		HashSet<State> outsiders = findOutsiders(spanSet);

		rescueOutsiders(outsiders, spanSet);
		if (outsiders.size() > 0) {
			throw new FSAException(FSABuildResult.FSAError.E2);
		}
	}

	private HashSet<State> buildSpanSet() {
		HashSet<State> spanSet = new HashSet<>();
		HashSet<State> buffer = new HashSet<>();
		LinkedList<State> bufferIn = new LinkedList<>();

		buffer.add(stateCache.get(initialState));
		while (buffer.size() > 0) {
			for (State state : buffer) {
				for (State value : state.getTransitions().values()) {
					if (!buffer.contains(value) && !spanSet.contains(value)) {
						bufferIn.add(value);
					}
				}
				spanSet.add(state);
			}
			buffer.clear();
			buffer.addAll(bufferIn);
			bufferIn.clear();
		}

		return spanSet;
	}

	private HashSet<State> findOutsiders(HashSet<State> spanSet) {
		HashSet<State> outsiders = new HashSet<>();
		for (State state : stateCache.values()) {
			if (!spanSet.contains(state)) {
				outsiders.add(state);
			}
		}

		return outsiders;
	}

	private void rescueOutsiders(HashSet<State> outsiders, HashSet<State> spanned) {
		LinkedList<State> picked = new LinkedList<>();
		for (State outsider : outsiders) {
			if (hasTransitionToAny(outsider, spanned)) {
				picked.add(outsider);
			}
		}
		outsiders.removeAll(picked);

		for (State state : picked) {
			rescueFollowingOutsiders(state, outsiders);
		}
	}

	private void rescueFollowingOutsiders(State rescued, HashSet<State> outsiders) {
		LinkedList<State> picked = new LinkedList<>();
		for (State outsider : outsiders) {
			if (outsider.getTransitions().containsValue(rescued)) {
				picked.add(outsider);
			}
		}
		outsiders.removeAll(picked);

		for (State state : picked) {
			rescueFollowingOutsiders(state, outsiders);
		}
	}

	private boolean hasTransitionToAny(State who, HashSet<State> where) {
		for (State state : who.getTransitions().values()) {
			if (where.contains(state)) {
				return true;
			}
		}

		return false;
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
