import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class FSABuilder {
	private List<String> states, alphabet;
	private List<Transition> transitions;
	private List<String> finalStates;
	private String initialState;

	private boolean malformedInput;

	private List<FSABuildResult.FSAError> warnings;
	private HashMap<String, FSAState> stateCache;

	public FSABuilder() {
		states = new LinkedList<>();
		alphabet = new LinkedList<>();
		transitions = new LinkedList<>();
		finalStates = new LinkedList<>();
		malformedInput = false;
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

	public FSABuilder invalidateInputFile() {
		malformedInput = true;
		return this;
	}

	public FSABuildResult build() {
		if (malformedInput) {
			return new FSABuildResult(FSABuildResult.FSAError.E5);
		}

		warnings = new LinkedList<>();

		// Check for E4
		if (initialState == null) {
			return new FSABuildResult(FSABuildResult.FSAError.E4);
		}

		// Check for W1
		if (finalStates.isEmpty()) {
			warnings.add(FSABuildResult.FSAError.W1);
		}

		// Compile and check for E1 and E3
		LinkedList<FSAState> compiled;
		try {
			compiled = compileTransitions();
		} catch (FSAException e) {
			return new FSABuildResult(e.reason, e.notRepresented);
		}

		// Check for E2 and W2
		boolean statesConnected = checkInitialSpan();
		if (!statesConnected) {
			return new FSABuildResult(FSABuildResult.FSAError.E2);
		}

		// Check completeness
		boolean complete = checkCompleteness();

		FiniteStateAutomata automata =
				new FiniteStateAutomata(compiled, alphabet, stateCache.get(initialState));
		return new FSABuildResult(true, warnings, automata, complete);
	}

	private LinkedList<FSAState> compileTransitions() throws FSAException {
		stateCache = new HashMap<>();

		for (Transition transition : transitions) {
			// Check for E1
			if (!states.contains(transition.from)) {
				throw new FSAException(FSABuildResult.FSAError.E1, transition.from);
			} else if (!states.contains(transition.to)) {
				throw new FSAException(FSABuildResult.FSAError.E1, transition.to);
			}

			// Check for E3
			if (!alphabet.contains(transition.alpha)) {
				throw new FSAException(FSABuildResult.FSAError.E3, transition.alpha);
			}

			FSAState from = pickState(transition.from);
			FSAState to = pickState(transition.to);

			// Check for W3
			if (from.getTransitions().containsKey(transition.alpha) &&
					!from.getTransitions().get(transition.alpha).equals(to)) {
				warnings.add(FSABuildResult.FSAError.W3);
				addRedundantTransition(from, transition.alpha, to);
			} else {
				from.getTransitions().put(transition.alpha, to);
			}
		}

		return new LinkedList<>(stateCache.values());
	}

	private FSAState pickState(String name) {
		FSAState state;

		if (stateCache.containsKey(name)) {
			state = stateCache.get(name);
		} else {
			boolean isFinal = finalStates.contains(name);
			state = new FSAState(name, isFinal, new HashMap<>());

			stateCache.put(name, state);
		}

		return state;
	}

	private void addRedundantTransition(FSAState state, String transition, FSAState to) {
		if (state.getTransitions().containsKey(transition)) {
			addRedundantTransition(state, transition + '.', to);
		} else {
			state.getTransitions().put(transition, to);
		}
	}

	private boolean checkInitialSpan() {
		HashSet<FSAState> spanSet = buildSpanSet();

		if (spanSet.size() == states.size()) {
			return true;
		}

		warnings.add(FSABuildResult.FSAError.W2);

		HashSet<FSAState> outsiders = findOutsiders(spanSet);

		rescueOutsiders(outsiders, spanSet);
		return outsiders.size() <= 0;
	}

	private HashSet<FSAState> buildSpanSet() {
		HashSet<FSAState> spanSet = new HashSet<>();
		HashSet<FSAState> buffer = new HashSet<>();
		LinkedList<FSAState> bufferIn = new LinkedList<>();

		buffer.add(stateCache.get(initialState));
		while (buffer.size() > 0) {
			for (FSAState state : buffer) {
				for (FSAState value : state.getTransitions().values()) {
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

	private HashSet<FSAState> findOutsiders(HashSet<FSAState> spanSet) {
		HashSet<FSAState> outsiders = new HashSet<>();
		for (FSAState state : stateCache.values()) {
			if (!spanSet.contains(state)) {
				outsiders.add(state);
			}
		}

		return outsiders;
	}

	private void rescueOutsiders(HashSet<FSAState> outsiders, HashSet<FSAState> spanned) {
		LinkedList<FSAState> picked = new LinkedList<>();
		for (FSAState outsider : outsiders) {
			if (hasTransitionToAny(outsider, spanned)) {
				picked.add(outsider);
			}
		}
		outsiders.removeAll(picked);

		for (FSAState state : picked) {
			rescueFollowingOutsiders(state, outsiders);
		}
	}

	private void rescueFollowingOutsiders(FSAState rescued, HashSet<FSAState> outsiders) {
		LinkedList<FSAState> picked = new LinkedList<>();
		for (FSAState outsider : outsiders) {
			if (outsider.getTransitions().containsValue(rescued)) {
				picked.add(outsider);
			}
		}
		outsiders.removeAll(picked);

		for (FSAState state : picked) {
			rescueFollowingOutsiders(state, outsiders);
		}
	}

	private boolean hasTransitionToAny(FSAState who, HashSet<FSAState> where) {
		for (FSAState state : who.getTransitions().values()) {
			if (where.contains(state)) {
				return true;
			}
		}

		return false;
	}

	private boolean checkCompleteness() {
		for (FSAState state : stateCache.values()) {
			for (String alpha : alphabet) {
				if (!state.getTransitions().containsKey(alpha)) {
					return false;
				}
			}
		}

		return true;
	}

	private class Transition {
		String alpha, from, to;

		Transition(String alpha, String form, String to) {
			this.alpha = alpha;
			this.from = form;
			this.to = to;
		}
	}

	private class FSAException extends Exception {
		FSABuildResult.FSAError reason;
		String notRepresented;

		FSAException(FSABuildResult.FSAError reason) {
			this.reason = reason;
		}

		FSAException(FSABuildResult.FSAError reason, String notRepresented) {
			this.reason = reason;
			this.notRepresented = notRepresented;
		}
	}
}
